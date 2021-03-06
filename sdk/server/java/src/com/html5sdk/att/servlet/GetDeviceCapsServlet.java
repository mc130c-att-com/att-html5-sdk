package com.html5sdk.att.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.att.api.dc.service.DCService;
import com.att.api.oauth.OAuthToken;
import com.att.api.rest.RESTException;
import com.html5sdk.att.AttConstants;
import com.html5sdk.att.provider.ApiRequestException;

public class GetDeviceCapsServlet extends ServiceServletBase {
    private static final long serialVersionUID = 1L;

    public GetDeviceCapsServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        executeMatchingAction(request, response,
                new Action[] { new GetCapsAction() });
    }

    class GetCapsAction implements Action {

        public boolean match(HttpServletRequest request) {
            return true; // matches all paths for this servlet
        }

        public void handleException(Exception e, HttpServletResponse response) {
            submitJsonResponseFromException(e, response);
        }

        public void execute(HttpServletRequest request,
                HttpServletResponse response) throws ApiRequestException,
                RESTException, IOException {

            OAuthToken token = SessionUtils.getTokenForScope(request.getSession(),
                    "DC");

            if (token == null) {
                response.sendError(401, "{\"error\": \"app not authorized by user\"");
                return;
            }

            DCService svc = new DCService(AttConstants.HOST, token);
            String jsonResult = svc.getDeviceCapabilitiesAndReturnRawJson();
            submitJsonResponseFromJsonResult(jsonResult, response);
        }
    }
}
