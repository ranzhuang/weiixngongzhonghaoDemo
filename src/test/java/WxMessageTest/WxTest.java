package WxMessageTest;


import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;
import pojo.button.ClickButton;
import pojo.button.SubButton;
import pojo.button.ViewButton;
import pojo.button.WxButton;
import pojo.message.*;
import service.WxService;
import util.NetManagerUtil;

import java.util.HashMap;
import java.util.Map;

public class WxTest {

    @Test
    public void getMedia() {
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN".replace("ACCESS_TOKEN", WxService.getAccessToken());

        Map<String,String> data = new HashMap<>();
        data.put("type","news");
        data.put("offset","0");
        data.put("count","20");

        System.out.println(NetManagerUtil.post(url,JSONObject.toJSONString(data)));
    }

    /**
     * 创建按钮json字符串
     */
    @Test
    public void addButton() {
        WxButton wxButton = new WxButton();
        ClickButton clickButton = new ClickButton("第一个菜单","1");
        ViewButton viewButton = new ViewButton("第二个菜单","www.baidu.com");

        SubButton subButton = new SubButton("第三个菜单");
        ClickButton clickButton31 = new ClickButton("二级菜单","31");
        ViewButton viewButton32 = new ViewButton("第二个菜单","www.baidu.com");
        subButton.getSub_button().add(clickButton31);
        subButton.getSub_button().add(viewButton32);
        wxButton.getButton().add(clickButton);
        wxButton.getButton().add(viewButton);
        wxButton.getButton().add(subButton);
        System.out.println(JSONObject.toJSONString(wxButton));
    }



    /**
     * 获取token
     */
    @Test
    public void getToken() {
        System.out.println(WxService.getAccessToken());
        System.out.println(WxService.getAccessToken());
    }

    /**
     * 回复文字消息
     */
    @Test
    public void textMsg() {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("ToUserName","to");
        inputMap.put("FromUserName", "from");
        inputMap.put("MsgType","type");
        TextMessage tm = new TextMessage(inputMap,"你好");
        this.changeToXmlStr(tm);
    }


    /**
     * 转xml
     * @param model
     * @return
     */
    private String changeToXmlStr(BaseMessage model) {
        // 转xml
        XStream stream = new XStream();
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMesage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        stream.processAnnotations(VideoMessage.class);
        String xmlStr = stream.toXML(model);
        System.out.println(xmlStr);
        return xmlStr;
    }

}
