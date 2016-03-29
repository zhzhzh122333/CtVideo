package com.ctg.ctvideo.services;

import org.json.JSONObject;

import java.util.Date;

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

    /**
     * 手机甩屏链接
     * @param code
     * @return
     */
    public static String throwVideo(String code) {
        Long currentTime = new Date().getTime();
        String url =  "http://14.29.1.161:8080/examine/createMessage?cmd=SendMsg&msgtype=1&timestamp=" + currentTime + "&sid=1&appid=100&expire=86400&epg=smchd&restype=1&starttime=" + currentTime + "&receivers=088820140113006&resource=2201&resName=ZJ&content={%22senderid%22:%22%22,%22sender%22:%22SYSTEM%22,%22head%22:%22%22,%22subject%22:%22test%20subject%22,%22text%22:%22" + code + "%22}&sig=2c7f8e55f1825237ba3ad0d810f0df25&url=http://172.41.8.93:8080/push/mgr.do";
        return NetworkService.getString(url);
    }
}
