package me.zhengjie.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: yshopmall_hd
 * @Package: com.jiamu
 * @ClassName: HttpUtils
 * @Author: fengwen
 * @Description: http请求工具类包含post与get等serverlet服务方式
 * @Date: 2022/7/11 12:41
 * @Version: 1.0.0
 */
@Slf4j
public class HttpUtils {
    /**
     * post方式获取json报文返回请求数据
     * @param url 请求URL
     * @param xmlInfo xml内容
     * @return 返回组装信息
     */
    public static String httpPostXml(String url, String xmlInfo) {
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept","text/html");
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent","MSIE");
        String charset="UTF-8";
//            String xmlInfo= URLEncoder.encode();
        StringEntity entity= new StringEntity(xmlInfo,charset);
        System.out.println("entity:" + entity);
        httpPost.setEntity(entity);
        CloseableHttpResponse response=null;
        try{
            response=httpClient.execute(httpPost);
            System.out.println(response);
            StatusLine statusLine=response.getStatusLine();
            int status= statusLine.getStatusCode();
            if(status == HttpStatus.SC_OK){
                HttpEntity responseEntity=response.getEntity();
                String jsonStr= EntityUtils.toString(responseEntity,"utf-8");
                log.info("========={}",jsonStr);
                return jsonStr;
            }else{
                System.out.println("请求返回："+status+"("+url+")");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(response !=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * post请求后，响应返回xml格式报文
     */
    public static String doXmlPost(String requestMethod, String url, String data) throws IOException {
        HttpURLConnection connection=null;
        try {
            URL reqUrl=new URL(url);
            connection = (HttpURLConnection) reqUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/xml");
            connection.connect();

            // 写入发送的数据
            OutputStream outputStream=connection.getOutputStream();
            outputStream.write(data.getBytes(StandardCharsets.UTF_8));

            // 获取返回值
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuilder stringBuilder=new StringBuilder();
            while ((lines = bufferedReader.readLine()) !=null){
                lines = new String(lines.getBytes(StandardCharsets.UTF_8));
                stringBuilder.append(lines);
            }
            log.info("接收到返回的xml报文信息：{}", stringBuilder);
            return stringBuilder.toString();

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if(connection !=null){
                connection.disconnect();
            }
        }
    }
}
