package com.patchmng.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sujianfeng on 2015/3/18.
 */
public class HttpUtils {

    private static final String HTTP_PROTOCOL_URL = "http://";
    private static final String HTTPS_PROTOCOL_URL = "https://";
    private static final String HTTPS_PROTOCOL = "https";
    private static final String HTTPS_PROTOCOL_HEADER = "X-Forwarded-Proto";

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 从request对象中获取客户端真实的ip地址
     * @param request request对象
     * @return 客户端的IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isJsonRequest(HttpServletRequest request){
        if (request.getHeader("accept") != null &&
                request.getHeader("accept").indexOf("application/json") > -1){
            return true;
        }
        if (request.getHeader("X-Requested-With")!= null
                && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1){
            return true;
        }
        return false;
    }

    public static String sendHttpRequest(String urlPath, String encoding, String method) throws Exception {
        if ("GET".equals(method)) {
            return sendHttpRequestGet(urlPath, encoding, method);
        } else {
            return sendHttpRequestPOST(urlPath, encoding, method);
        }
    }

    public static String sendHttpRequestGet(String urlPath, String encoding, String method) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5 * 1000);
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] data = readStream(inStream);
            return new String(data, encoding);
        }
        return "";
    }

    public static byte[] sendHttpRequestGet(String urlPath, String method) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5 * 1000);
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] data = readStream(inStream);
            return data;
        }
        return null;
    }

    public static String sendHttpRequestPOST(String urlPath, String encoding, String method) throws Exception {
        String tempparr[] = urlPath.split("\\?");
        URL url = new URL(tempparr[0]);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod(method);
        conn.setUseCaches(false);
        conn.setConnectTimeout(5 * 1000);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(tempparr[1]);
        out.flush();
        out.close();
        InputStream inStream = conn.getInputStream();
        byte[] data = readStream(inStream);
        return new String(data, encoding);
    }

    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static String getParam(String paramName, String paramValue) {
        return paramName + "=" + paramValue;
    }

    public static String removeHtml(String str) {
        int start = str.indexOf("<");
        int end = str.indexOf(">");
        while (start >= 0 && end > 0 && end > start) {
            str = str.substring(0, start) + str.substring(end + 1);
            start = str.indexOf("<");
            end = str.indexOf(">");
        }
        return str;
    }

    /**
     * 根据map拼装URI部分
     *
     * @param map map对象键值为<String, String>
     * @return
     */
    public static String stringMapToURI(Map<String, Object> map) {
        StringBuffer body = new StringBuffer();
        if (map != null) {
            Iterator it = map.keySet().iterator();
            while (true) {
                do {
                    if (!(it.hasNext())) {
                        return body.toString();
                    }
                    String key = String.valueOf(it.next());
                    try {
                        String value = String.valueOf(map.get(key));
                        if (!(StringUtilsEx.isEmpty(value) || "null".equals(value)))
                            body.append(key + "=" + URLEncoder.encode(value, "utf-8"));
                        else
                            body.append(key + "=");
                    } catch (UnsupportedEncodingException e) {
                        log.error("url encode failure,{}", e.getMessage());
                    }
                }
                while (!(it.hasNext()));
                body.append("&");
            }
        }
        return body.toString();
    }

    public static String getFullUri(HttpServletRequest request){
        String uri = request.getRequestURI();
        StringBuffer sb = new StringBuffer(uri);
        if (request.getQueryString() != null) {
            sb.append('?');
            sb.append(request.getQueryString());
        }
        return sb.toString();
    }

    public static String getFullUrl(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        StringBuffer sb = new StringBuffer(url);
        if (request.getQueryString() != null) {
            sb.append('?');
            sb.append(request.getQueryString());
        }
        return sb.toString();
    }
    public static String getFullServerPath(HttpServletRequest request) {
        String httpProtocol = HTTP_PROTOCOL_URL;
        String protocol = request.getHeader(HTTPS_PROTOCOL_HEADER);
        if (StringUtils.hasText(protocol) && protocol.equals(HTTPS_PROTOCOL)) {
            httpProtocol = HTTPS_PROTOCOL_URL;
        }
        int serverPort = ConvertUtils.cInt(request.getServerPort());
        boolean isNormalPort = ConvertUtils.oneOfInt(serverPort, 80, 443);
        String port = isNormalPort ? "" : ":" + request.getServerPort();
        return httpProtocol + request.getServerName() + port + request.getContextPath();
    }

    public static void main(String[] args) {

    }

}
