package pojo.message;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class VoiceMessage extends BaseMessage {
    /**
     * 声音id
     */
    @XStreamAlias("MediaId")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public VoiceMessage(Map<String,String> inputStreamMap, String mediaId) {
        super(inputStreamMap);
        this.setMsgType(MSG_SEND_TYPE_VOICE);
        this.mediaId = mediaId;
    }
}
