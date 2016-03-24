package com.ctg.ctvideo.services;

import org.json.JSONObject;

public class CtVideoService {
    private static String loginUrl = "http://192.168.6.116:8080/CtVideoServer/login.do";
    private static String vodListUrl = "http://192.168.6.116:8080/CtVideoServer/firstController/getVodList.do";

    private static String sessionId;

    public static JSONObject login(String username, String password) {
        String url = loginUrl + "?username=" + username + "&password=" + password;
        JSONObject result = NetworkService.getJson(url, sessionId);
        sessionId = result.optString("session");
        return result;
    }

    public static JSONObject getVodList() {
        return NetworkService.getJson(vodListUrl, sessionId);
    }
}
