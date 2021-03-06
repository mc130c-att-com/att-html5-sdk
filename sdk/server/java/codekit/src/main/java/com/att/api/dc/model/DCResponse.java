/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4 */

/*
 * ====================================================================
 * LICENSE: Licensed by AT&T under the 'Software Development Kit Tools
 * Agreement.' 2013.
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTIONS:
 * http://developer.att.com/sdk_agreement/
 *
 * Copyright 2013 AT&T Intellectual Property. All rights reserved.
 * For more information contact developer.support@att.com
 * ====================================================================
 */
package com.att.api.dc.model;


import org.json.JSONObject;

/**
 * Immutable class that holds DC API response information.
 *
 * @author pk9069
 * @version 1.0
 * @since 1.0
 */
public final class DCResponse {
    private String typeAllocationCode;

    private String name;
    private String vendor;
    private String model;
    private String firmwareVersion;
    private String uaProf;
    private boolean mmsCapable;
    private boolean assistedGps;
    private String locationTechnology;
    private String deviceBrowser;
    private boolean wapPushCapable;

    /**
     * Disallow default constructor.
     */
    private DCResponse() {
    }

    /**
     * Factory method for creating a DC response by parsing a JSON object.
     *
     * @param jobj JSON object to parse
     * @return DC response
     */
    public static DCResponse valueOf(JSONObject jobj) {
        DCResponse response = new DCResponse();

        // parse json
        JSONObject deviceInfo = jobj.getJSONObject("DeviceInfo");
        JSONObject deviceId = deviceInfo.getJSONObject("DeviceId");
        JSONObject capabilities = deviceInfo.getJSONObject("Capabilities");
        response.typeAllocationCode = deviceId.getString("TypeAllocationCode");

        response.name = capabilities.getString("Name");
        response.vendor = capabilities.getString( "Vendor");
        response.model = capabilities.getString("Model");
        response.firmwareVersion = capabilities.getString("FirmwareVersion");
        response.uaProf = capabilities.getString("UaProf");

        final String mmsCapableStr = capabilities.getString("MmsCapable");
        response.mmsCapable = mmsCapableStr.equals("Y") ? true : false;

        final String assistedGpsStr = capabilities.getString("AssistedGps");
        response.assistedGps = assistedGpsStr.equals("Y") ? true : false;

        response.locationTechnology 
            = capabilities.getString("LocationTechnology");
        response.deviceBrowser = capabilities.getString("DeviceBrowser");

        final String wapPushStr = capabilities.getString("WapPushCapable"); 
        response.wapPushCapable = wapPushStr.equals("Y") ? true : false;

        return response;
    }

    /**
     * Gets the type allocation code.
     *
     * @return type allocation code 
     */
    public String getTypeAllocationCode() {
        return typeAllocationCode;
    }

    /**
     * Gets name.
     *
     * @return name 
     */
    public String getName() {
        return name;
    }

    /**
     * Gets vendor.
     *
     * @return vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Gets model.
     *
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the firmware version.
     *
     * @return firmwareVersion
     */
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    /**
     * Gets the user agent profile.
     *
     * @return user agent profile
     */
    public String getUaProf() {
        return uaProf;
    }

    /**
     * Gets whether device is mms capable.
     *
     * @return mms capable string
     */
    public boolean isMmsCapable() {
        return mmsCapable;
    }

    /**
     * Gets whether device is assisted gps capable.
     *
     * @return assisted gps string
     */
    public boolean isAssistedGpsCapable() {
        return assistedGps;
    }

    /**
     * Gets location technology.
     *
     * @return location technology
     */
    public String getLocationTechnology() {
        return locationTechnology;
    }

    /**
     * Gets device browser.
     *
     * @return device browser
     */
    public String getDeviceBrowser() {
        return deviceBrowser;
    }

    /**
     * Gets whether the device is wap push capable.
     *
     * @return wap push capable string
     */
    public boolean isWapPushCapable() {
        return wapPushCapable;
    }
}
