package com.sencha.att.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.att.api.oauth.OAuthService;
import com.att.api.oauth.OAuthToken;
import com.att.api.rest.RESTException;
import com.sencha.att.AttConstants;
import com.sencha.att.provider.ServiceProviderConstants;

/**
 * 
 * Once the user has logged-in with their credentials, they get re-directed to
 * this URL with a 'code' parameter. This is exchanged for an access token which
 * can be used in any future calls to the AT&T APIs
 * 
 * @class com.sencha.att.servlet.AttAuthCallbackServlet
 */
public class AttAuthCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger log = Logger
            .getLogger(ServiceProviderConstants.SERVICEPROVIDERLOGGER);

    private final int refreshTokenExpireMilis;

    /*
     * @see HttpServlet#HttpServlet()
     */
    public AttAuthCallbackServlet() {
        super();

        this.refreshTokenExpireMilis = (AttConstants.REFRESH_TOKEN_EXPIRE_HOURS * 60 * 60 * 1000);

    }

    /**
     * Calls doPost
     * 
     * @method doGet
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @method doPost
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String code = request.getParameter(AttConstants.CODE);
        String scope = request.getParameter(AttConstants.SCOPE);
        String returnUrl = request.getParameter(AttConstants.RETURN_URL);

        if (handleCodeMissing(code, returnUrl, request, response)) {
            return;
        }

        OAuthService svc = new OAuthService(AttConstants.HOST,
                AttConstants.CLIENTIDSTRING, AttConstants.CLIENTSECRETSTRING);

        OAuthToken token;
        try {
            token = svc.getTokenUsingCode(code);
        } catch (RESTException e) {
            log.severe(e.getMessage());
            e.printStackTrace();
            redirectWithError(returnUrl, response,
                    "could not convert code to token");
            return;
        }

        String accessToken = token.getAccessToken();
        SessionUtils.setTokenForScope(request.getSession(), scope, accessToken);
        response.sendRedirect(returnUrl);
    }

    private boolean handleCodeMissing(String code, String returnUrl,
            HttpServletRequest request, HttpServletResponse response) {
        if (code != null) {
            return false;
        }
        String msg = "no code and no error message returned from the user authentication";
        String error = request.getParameter("error");
        if (error != null) {
            msg = error;
            String desc = request.getParameter("error_description");
            if (desc != null) {
                msg = msg + " - " + desc;
            }
        }
        redirectWithError(returnUrl, response, msg);
        return true;
    }

    private void redirectWithError(String url, HttpServletResponse response,
            String msg) {
        String delimiter = "?";
        if (url.contains("?")) {
            delimiter = "&";
        }
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warning(e.getMessage());
            e.printStackTrace();
        }
        url += delimiter + "error=" + msg;
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    private String getClientID() {
        return AttConstants.CLIENTIDSTRING;
    }

    private String getClientSecret() {
        return AttConstants.CLIENTSECRETSTRING;
    }

    private String getHost(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort();
    }
}
