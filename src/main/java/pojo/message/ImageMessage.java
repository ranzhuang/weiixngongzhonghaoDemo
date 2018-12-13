package pojo.message;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class ImageMessage extends BaseMessage {
    /**
     * 图片id
     */
    @XStreamAlias("MediaId")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public ImageMessage(Map<String,String> inputStreamMap, String mediaId) {
        super(inputStreamMap);
        this.setMsgType(MSG_SEND_TYPE_IMAGE);
        this.mediaId = mediaId;
    }
}
