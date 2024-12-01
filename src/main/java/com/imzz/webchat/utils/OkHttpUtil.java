package com.imzz.webchat.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    /**
     * 声明日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    /**
     * <p>get请求参数拼接，返回拼接后的请求参数</p>
     * <p>@param url</p>
     * <p>@param queries</p>
     * <p>@return</p>
     */
    private static StringBuffer getStringBuffer(String url,Map<String, String> queries){
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        logger.info("[参数拼接后的结果]{}","["+sb+"]");
        return sb;
    }

    /**
     * <p>执行请求，并返回请求信息</p>
     * <p>@param request</p>
     * <p>@return</p>
     */
    private static String executionRequest(Request request){
        logger.info("[请求路径]{}","["+request.url()+"]");
        //申明返回信息
        Response response = null;
        try {
            //建立请求对象,并设置超时时间
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60,TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).build();
            //执行请求并放回执行结果
            response = okHttpClient.newCall(request).execute();
            logger.info("[请求状态]{}","["+response.code()+"]");
            //判断执行结果是否成功
            if (response.isSuccessful()) {
                //成功，返回结果
                return response.body().string();
            }
        } catch (IOException e) {
            if(response == null){
                logger.error("[请求错误信息]{"+e.getClass()+"："+e.getMessage()+"}");
            }else{
                logger.error("[请求状态]["+ response.code()+"]-","[请求错误信息]{"+e.getMessage()+"}");
            }
            return e.getMessage();
        } finally {
            //判断返回的信息是否为空
            if (response != null) {
                //不为空，关闭流
                response.close();
            }
        }
        return null;
    }

    /**
     * <p>get 请求</p>
     * <p>@param url     请求的url</p>
     * <p>@param queries 请求的参数，在浏览器？后面的数据，没有可以传null</p>
     * <p>@return</p>
     */
    public static String get(String url, Map<String, String> queries) {

        //获取路径参数
        StringBuffer sb = getStringBuffer(url,queries);

        //建立请求信息
        Request request = new Request.Builder().url(sb.toString()).build();

        //执行请求并返回请求结果
        return executionRequest(request);
    }

    /**
     * <p>post 请求</p>
     * <p>@param url    请求的url</p>
     * <p>@param params post form 提交的参数</p>
     * <p>@return </p>
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if(params.get(key) != null){
                    builder.add(key, params.get(key));
                }
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        return executionRequest(request);
    }

    /**
     * <p>get</p>
     * <p>@param url     请求的url</p>
     * <p>@param queries 请求的参数，在浏览器？后面的数据，没有可以传null</p>
     * <p>@return</p>
     */
    public static String getForHeader(String url, Map<String, String> queries) {

        //得到请求参数
        StringBuffer sb = getStringBuffer(url,queries);

        //建立请求信息
        Request request = new Request.Builder().addHeader("key", "value").url(sb.toString()).build();

        //执行请求并返回请求结果
        return executionRequest(request);
    }

    /**
     * <p>Post请求，发送JSON数据</p>
     * <p>@param url 请求路径</p>
     * <p>@param jsonParams 请求参数</p>
     * <p>@return</p>
     */
    public static String postJsonParams(String url, String jsonParams) {

        //设置请求内容类型
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);

        //建立请求信息
        Request request = new Request.Builder().url(url).post(requestBody).build();

        //执行请求并返回请求结果
        return executionRequest(request);
    }

    /**
     * <p>Post请求，发送xml数据</p>
     * <p>@param url  请求路径</p>
     * <p>@param xml  请求参数</p>
     * <p>@return</p>
     */
    public static String postXmlParams(String url, String xml) {

        //设置请求内容类型
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);

        //建立请求信息
        Request request = new Request.Builder().url(url).post(requestBody).build();

        //执行请求并返回请求结果
        return executionRequest(request);
    }

    /**
     * <p>URL编码（utf-8）</p>
     * <p>加码</p>
     * <p>@param source</p>
     * <p>@return</p>
     */
    public static String urlEncodeUTF8(String source) throws UnsupportedEncodingException {

        return java.net.URLEncoder.encode(source, "utf-8");
    }

    /**
     * <p>URL编码（utf-8）</p>
     * <p>解码</p>
     * <p>@param source</p>
     * <p>@return</p>
     */
    public static String urlDecodeUTF8(String source) throws UnsupportedEncodingException {

        return URLDecoder.decode(source, "utf-8");
    }
}