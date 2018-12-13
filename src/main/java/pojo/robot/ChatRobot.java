package pojo.robot;


import com.alibaba.fastjson.JSON;
import util.NetManagerUtil;
import java.util.HashMap;
import java.net.URLEncoder;

public class ChatRobot {

    private static final String robotUrlAPI = "http://www.tuling123.com/openapi/api?key=";
    private static final String apiKey = "5cf57dd2ad5f4d139771f6281e4720cf";
    /**
     * 状态码
     */
    private String code;
    /**
     * 文字
     */
    private String text;
    /**
     * 链接
     */
    private String url;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取机器人返回的参数对象
     * @param contentStr 用户发送的聊天文字消息
     * @return ChatRobot对象
     */
    public static ChatRobot giveChatMessage(String contentStr) {

        //获取json数据
        String jsonStr = ChatRobot.getRobotMessage(contentStr);
        if (jsonStr.equals(null)) {
            return null;
        } else {
            return JSON.parseObject(jsonStr,ChatRobot.class);
        }
    }

    /**
     * 获取机器人对话json数据
     * @param contentStr 用户发送的聊天文字消息
     * @return json字符串
     */
    private static String getRobotMessage(String contentStr) {
        try {
            String INFO = URLEncoder.encode(contentStr,"utf-8");
            String getURL = robotUrlAPI + apiKey + "&info=" + INFO;

            String s = NetManagerUtil.net(getURL,new HashMap(),"POST");
            return s;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
