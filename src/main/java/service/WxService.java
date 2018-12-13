package service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pojo.accessToken.AccessToken;
import pojo.message.*;
import pojo.robot.ChatRobot;
import util.CreatButtonUtil;
import util.NetManagerUtil;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.*;

public class WxService {
    //获取ACCESS_TOKEN的URL
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //appID
    private static final String APPID = "wx503297a5bc732744";
    //appsecret
    private static final String APPSECRET = "ab7c6be47ffe47ef4b44d13502afc60a";
    // 微信公众号配置的TOKEN
    private static final String TOKEN = "hjwx";
    // 所接受消息的类型
    private static final String MSG_RECEIVE_TYPE_TEXT = "text";
    private static final String MSG_RECEIVE_TYPE_IMAGE = "image";
    private static final String MSG_RECEIVE_TYPE_VOICE= "voice";
    private static final String MSG_RECEIVE_TYPE_VIDEO = "video";
    private static final String MSG_RECEIVE_TYPE_SHORTVIDEO = "shortvideo";
    private static final String MSG_RECEIVE_TYPE_LOCATION = "location";
    private static final String MSG_RECEIVE_TYPE_LINK = "link";
    private static final String MSG_RECEIVE_TYPE_EVENT = "event";
    private static AccessToken cuttentAccessToken;
    /**
     * 微信接入验证
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param signature 微信加密签名
     * @return
     */
    public static boolean check(String timestamp, String nonce, String signature) {
        /**
         * 1）将token、timestamp、nonce三个参数进行字典序排序
         * 2）将三个参数字符串拼接成一个字符串进行sha1加密
         * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         */
        String[] strs = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(strs);
        String str = strs[0] + strs[1] + strs[2];
        String mySignature = sha1(str);
        return mySignature.equalsIgnoreCase(signature);
    }

    /**
     * 进行sha1加密
     * @param str
     * @return
     */
    private static String sha1(String str) {

        try {
            //获取加密对象
            MessageDigest messageD = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = messageD.digest(str.getBytes());
            //处理加密结果
            char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b:digest) {
                stringBuilder.append(chars[(b>>4) & 15]);
                stringBuilder.append(chars[b & 15]);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析xml数据包
     * @param inputStream 输入流
     * @return
     */
    public static Map<String, String> dealRequest(InputStream inputStream) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            //读书输入流，获取文档对象
            Document document = reader.read(inputStream);
            //根据文档对象获取根节点
            Element root = document.getRootElement();
            //获取所有的子节点
            List<Element> elements = root.elements();
            for (Element e:elements) {
                map.put(e.getName(),e.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 处理所有消息事件
     * @param inputStreamMap 输入流的键值对
     * @return xml字符串(包含各种格式)
     */
    public static String replyMessageAndEvent(Map<String, String> inputStreamMap) {
        BaseMessage baseMessage = new BaseMessage(inputStreamMap);
        String msgType = inputStreamMap.get("MsgType");
        switch (msgType) {

            case MSG_RECEIVE_TYPE_TEXT:
                baseMessage = dealText(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_IMAGE:
                baseMessage = dealImage(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_VOICE:
                baseMessage = dealVoice(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_VIDEO:
                baseMessage = dealVideo(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_SHORTVIDEO:
                baseMessage = dealShortVideo(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_LOCATION:
                baseMessage = dealLocation(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_LINK:
                baseMessage = dealLink(inputStreamMap);
                break;
            case MSG_RECEIVE_TYPE_EVENT:
                baseMessage = dealEvent(inputStreamMap);
                break;
            default:
                break;
        }
        if (baseMessage != null) {
            //将对象转换为xml字符串
            String xmlStr = changeXmlFromModel(baseMessage);
            System.out.println(xmlStr);
            return xmlStr;
        } else  {
            return "success";
        }

    }

    /**
     * 将回复消息的模型转换为xml字符串
     * @param baseMessage 回复消息的模型
     * @return xml形式的字符串
     */
    private static String changeXmlFromModel(BaseMessage baseMessage) {
        // 转xml
        XStream stream = new XStream();
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMesage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        stream.processAnnotations(VideoMessage.class);
        String xmlStr = stream.toXML(baseMessage);
        return xmlStr;
    }



    /**
     * 处理文本消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealText(Map<String, String> inputStreamMap) {
        BaseMessage baseMessage = new BaseMessage(inputStreamMap);
        String contentStr = inputStreamMap.get("Content");

        //和机器人对话，传入文字
        ChatRobot robot = ChatRobot.giveChatMessage(contentStr);
        //回复文本消息
        String message = new String();
        if (robot.getText() == null) {
            message = "success";
        } else if (robot.getUrl() != null) {
            message = robot.getText() + robot.getUrl();
        } else {
            message =robot.getText();
        }

        baseMessage = sendTextMessage(inputStreamMap,message);

        return baseMessage;
    }

    /**
     * 处理图片消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealImage(Map<String, String> inputStreamMap) {
        return null;
    }

    /**
     * 处理声音消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealVoice(Map<String, String> inputStreamMap) {
        return null;
    }
    /**
     * 处理视频消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealVideo(Map<String, String> inputStreamMap) {
        return null;
    }
    /**
     * 处理小视频消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealShortVideo(Map<String, String> inputStreamMap) {
        return null;
    }
    /**
     * 处理地理位置消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealLocation(Map<String, String> inputStreamMap) {
        return null;
    }
    /**
     * 处理链接消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealLink(Map<String, String> inputStreamMap) {
        return null;
    }
    /**
     * 处理事件推送消息
     * @param inputStreamMap 输入流的键值对
     * @return 各种消息类型
     */
    private static BaseMessage dealEvent(Map<String, String> inputStreamMap) {
        String event = inputStreamMap.get("Event");
        BaseMessage baseMessage = null;
        switch (event) {
            case "CLICK":
                //自定义菜单事件
                baseMessage = dealMenuClick(inputStreamMap);
                break;
            default:
                break;
        }
        return baseMessage;
    }

    /**
     * 处理菜单点击事件
     * @param inputStreamMap 输入流的键值对
     */
    private static BaseMessage dealMenuClick(Map<String, String> inputStreamMap) {
        if (inputStreamMap.get("EventKey").equalsIgnoreCase(CreatButtonUtil.ONE_MENU_TITLEKEY)) {
            List<Articles> articles = new ArrayList<>();

            Articles article = new Articles("这是一个测试数据标题","测试数据描述",
                    "https://upload-images.jianshu.io/upload_images/1990028-044e441b2a2a8d6f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
                    "https://www.jianshu.com/p/77632e7a0e8f");
            articles.add(article);
            return sendNewsMessage(inputStreamMap,articles);
        }
        return null;
    }

    /**
     * 回复文本消息
     * @param inputStreamMap 输入流的键值对
     * @param message 回复的消息
     * @return 文本消息模型
     */
    private static BaseMessage sendTextMessage(Map<String, String> inputStreamMap, String message) {
        TextMessage textMessage = new TextMessage(inputStreamMap,message);
        return textMessage;
    }
    /**
     * 回复图片消息
     * @param inputStreamMap 输入流的键值对
     * @param mediaId 上传数据的ID
     * @return 图片消息模型
     */
    private static BaseMessage sendImageMessage(Map<String, String> inputStreamMap, String mediaId) {
        ImageMessage imageMessage = new ImageMessage(inputStreamMap,mediaId);
        return imageMessage;
    }
    /**
     * 回复音乐消息
     * @param inputStreamMap 输入流的键值对
     * @param music music对象
     * @return 音乐消息模型
     */
    private static BaseMessage sendMusicMessage(Map<String, String> inputStreamMap, Music music) {
        MusicMesage musicMesage = new MusicMesage(inputStreamMap,music);
        return musicMesage;
    }
    /**
     * 回复视频消息
     * @param inputStreamMap 输入流的键值对
     * @param mediaId 上传数据ID
     * @param title 视频的标题
     * @param description 视频消息的描述
     * @return 视频消息模型
     */
    private static BaseMessage sendVideoMessage(Map<String,String> inputStreamMap, String mediaId, String title, String description) {
        VideoMessage videoMessage = new VideoMessage(inputStreamMap,mediaId,title,description);
        return videoMessage;
    }
    /**
     * 回复声音消息
     * @param inputStreamMap 输入流的键值对
     * @param mediaId 上传数据ID
     * @return 文本消息模型
     */
    private static BaseMessage sendVoiceMessage(Map<String, String> inputStreamMap, String mediaId) {
        VoiceMessage voiceMessage = new VoiceMessage(inputStreamMap,mediaId);
        return voiceMessage;
    }
    /**
     * 回复图文消息
     * @param inputStreamMap 输入流的键值对
     * @param articles   Articles数组
     * @return 新闻消息模型
     */
    private static BaseMessage sendNewsMessage(Map<String, String> inputStreamMap,List<Articles> articles) {
        NewsMessage newsMessage = new NewsMessage(inputStreamMap,articles);
        return newsMessage;
    }

    /**
     * 获取AccessToken
     */
    private static void getCurrentAccessToken() {
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET",APPSECRET);
        try {
            //调用GET方法获取数据
            String jsonStr = NetManagerUtil.net(url,null,"GET");
            //将json字符串转为对象
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            AccessToken accessToken = new AccessToken(jsonObject.getString("access_token"), jsonObject.getString("expires_in"));
            cuttentAccessToken = accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getAccessToken() {
        //如果token不存在或者token过期就去获取token
        if (cuttentAccessToken == null || cuttentAccessToken.isExpires()) {
            getCurrentAccessToken();
        }
        return cuttentAccessToken.getToken();
    }


}
