package cn.unis.gmweb.service;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//https://blog.csdn.net/zbw18297786698/article/details/53930415
@Service
public class HttpClientServiceImpl implements HttpClientService {


    @Autowired(required = false)
    private CloseableHttpClient httpClient;

    @Autowired(required = false)
    private RequestConfig requestConfig;


    /**
     * 执行POST请求
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String doPost(String url, String xml) {


        CloseableHttpResponse response = null;
        try {
            // 创建http POST请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new StringEntity(xml, "text/xml", "utf-8"));

            // 执行请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public  void download(String path, String name) {
        CloseableHttpResponse response=null;
        // 文件已存在，跳过下载
        File imageFile = new File("C:\\Users\\lgf\\Desktop\\pict.jpg");
        if (imageFile.exists()) {
            return;
        }

        try {
            HttpGet get =new HttpGet("http://211.155.165.1:12357/picture/2018-12-12/1598/BIPC_23_20181212_09_16_35_33351328_1.jpg");
            response = httpClient.execute(get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        try {
            InputStream instream = entity.getContent();
            byte[] data = readInputStream(instream);

            //创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();
        } catch (TruncatedChunkException te) {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public  byte[] readInputStream (InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }



}
