package pojo.message;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;

public class BaseMessage {
    // 所接受消息的类型
    public static final String MSG_SEND_TYPE_TEXT  = "text";
    public static final String MSG_SEND_TYPE_IMAGE = "image";
    public static final String MSG_SEND_TYPE_VOICE = "voice";
    public static final String MSG_SEND_TYPE_VIDEO = "video";
    public static final String MSG_SEND_TYPE_MUSIC = "music";
    public static final String MSG_SEND_TYPE_NEWS  = "news";
    /**
     * 接收者名称
     */
    @XStreamAlias("ToUserName")
    private String toUserName;
    /**
     * 发送者名称
     */
    @XStreamAlias("FromUserName")
    private String fromUserName;
    /**
     * 创建时间
     */
    @XStreamAlias("CreateTime")
    private String createTime;
    /**
     * 消息类型
     */
    @XStreamAlias("MsgType")
    private String msgType;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public BaseMessage(Map<String,String> inputStreamMap) {
        this.toUserName = inputStreamMap.get("FromUserName");
        this.fromUserName = inputStreamMap.get("ToUserName");
        this.createTime = System.currentTimeMillis()/1000+"";
    }
}
