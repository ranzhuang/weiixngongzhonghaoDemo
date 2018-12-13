package pojo.message;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class MusicMesage extends BaseMessage{
    @XStreamAlias("Music")
    private Music music;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public MusicMesage(Map<String, String> inputStreamMap, Music music) {
        super(inputStreamMap);
        this.setMsgType(MSG_SEND_TYPE_MUSIC);
        this.music = music;
    }

}
