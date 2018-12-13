package pojo.message;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;

@XStreamAlias("xml")
public class TextMessage extends BaseMessage{
    /**
     * 回复内容
     */
    @XStreamAlias("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextMessage(Map<String, String> inputStreamMap, String content) {
        super(inputStreamMap);
        this.setMsgType(MSG_SEND_TYPE_TEXT);
        this.content = content;
    }

}
