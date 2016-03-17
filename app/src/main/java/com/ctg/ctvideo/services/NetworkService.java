package com.ctg.ctvideo.services;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkService {

    /**
     * 请求链接，返回String结果
     * @param url 链接地址
     * @return
     */
    public static String getStringByUrl(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int temp;
            while ((temp = in.read()) != -1) {
                out.write(temp);
            }
            in.close();

            return out.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    /**
     * 请求链接，返回json结果
     * @param url 链接地址
     * @return
     */
    public static JSONObject getJsonByUrl(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int temp;
            while ((temp = in.read()) != -1) {
                out.write(temp);
            }
            in.close();

            return new JSONObject(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
