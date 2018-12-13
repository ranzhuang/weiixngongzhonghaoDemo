package util;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;


public class NetManagerUtil {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;

    /**
     * pust 请求
     * @param url  请求的url
     * @param data 请求的data
     * @return 返回json字符串
     */
    public static String post(String url,String data){
        try {
            URL urlObj = new URL(url);
            //开链接
            URLConnection connection = urlObj.openConnection();
            //要发送数据出去，需要设置为可发送数据状态
            connection.setDoOutput(true);
            //是否缓存
            connection.setUseCaches(false);
            //过期时间
            connection.setConnectTimeout(DEF_CONN_TIMEOUT);
            connection.setReadTimeout(DEF_READ_TIMEOUT);
            //获取输出流
            OutputStream outputStream = connection.getOutputStream();
            //写出数据
            if (data != null) {
                outputStream.write(data.getBytes());
            }
            outputStream.close();
            //获取输入流
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            StringBuffer sb = new StringBuffer();

            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            //GET方法链接设置
            if(method==null || method.equals("GET")){
                if (params!= null) {
                    strUrl = strUrl+"?"+urlencode(params);
                }
            }
            URL url = new URL(strUrl);
            //开连接
            conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            //是否缓存
            conn.setUseCaches(false);
            //过期时间
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            //是否重定向
            conn.setInstanceFollowRedirects(false);
            //连接
            conn.connect();
            //POST方法设置
            if (params!= null && method.equals("POST")) {
                try {
                    //输出流
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            //读取输入流
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            System.out.println(sb.toString());
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
