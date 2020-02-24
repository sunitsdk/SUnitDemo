package com.shareit.sunit;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * create by huanting on 2018/9/5 下午7:58
 */
public class HttpHelper {
    private static final String TAG = "HttpHelper";

    public static HttpResponse post(String urlStr, Map<String, String> headers, Map<String, String> params, int connectTimeout, int readTimeout) throws IOException {
        Writer writer = null;
        HttpResponse response = null;

        Log.d(TAG, "post url -> " + urlStr + " headers=" + (headers != null ? headers.toString() : null)
                + " params=" + (params != null ? params.toString() : null));
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        try {
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setInstanceFollowRedirects(true);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (params != null && params.size() > 0) {
                StringBuilder builder = new StringBuilder();
                writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                boolean isfirst = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (isfirst) {
                        isfirst = false;
                    } else {
                        writer.write("&");
                        builder.append("&");
                    }

                    writer.append(entry.getKey()).append("=").append(urlEncode(entry.getValue()));
                    builder.append(entry.getKey()).append("=").append(urlEncode(entry.getValue()));
                }
                writer.flush();
                Log.v(TAG, "post params: " + builder);
            }

            response = new HttpResponse(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {}
            }
            conn.disconnect();
        }

        Log.d(TAG, "response=" + response.getContent());
        return response;
    }

    // url encode a string with UTF-8 encoding
    public static String urlEncode(String src) {
        try {
            return URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
