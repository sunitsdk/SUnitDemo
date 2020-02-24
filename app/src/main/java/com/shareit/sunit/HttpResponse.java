package com.shareit.sunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * create by huanting on 2018/9/5 下午8:01
 */
public class HttpResponse {

    private Map<String, List<String>> headers;

    private String content;
    private int statusCode;
    private String statusMessage;

    HttpResponse(HttpURLConnection conn) throws IOException {
        headers = conn.getHeaderFields();
        statusCode = conn.getResponseCode();
        statusMessage = conn.getResponseMessage();

        InputStream input = null;
        try {
            try {
                input = conn.getInputStream();
            } catch (IOException e) {
                input = conn.getErrorStream();
            }
            if (input != null)
                content = inputStreamToString(input, true);
        } finally {
            if(input != null) {
                try {
                    input.close();
                }
                catch (Exception e) {

                }
            }
        }
    }

    // read everything in an input stream and return as string (trim-ed, and may apply optional utf8 conversion)
    public static String inputStreamToString(final InputStream is, final boolean sourceIsUTF8) throws IOException {
        InputStreamReader isr = sourceIsUTF8 ? new InputStreamReader(is, Charset.forName("UTF-8")) : new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();
        return sb.toString().trim();
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
