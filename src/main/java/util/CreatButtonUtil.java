package util;


import com.alibaba.fastjson.JSONObject;
import pojo.button.ClickButton;
import pojo.button.SubButton;
import pojo.button.ViewButton;
import pojo.button.WxButton;
import service.WxService;

public class CreatButtonUtil {

    private static final String CREAT_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    private static final String ONE_MENU_TITLE = "查找路线";
    public static final String ONE_MENU_TITLEKEY = "查找路线KEY";

    private static final String TWO_MENU_TITLE = "产品服务";
    private static final String TWO_MENU_SUBTITLE_ONE = "服务范围";
    private static final String TWO_MENU_SUBTITLE_ONEURL = "http://www.epochyoosure.com/fangan/";
    private static final String TWO_MENU_SUBOTITLE_TWO = "服务项目";
    private static final String TWO_MENU_SUBTITLE_TWOURL = "http://www.epochyoosure.com/a/youxuechanpin/";
    private static final String TWO_MENU_SUBTITLE_THREE = "新闻资讯";
    private static final String TWO_MENU_SUBTITLE_THREEURL = "http://www.epochyoosure.com/zixun/";

    private static final String THREE_MENU_TITLE = "艾派格";
    private static final String THREE_SUBTITLE_ONE = "私人订制";
    private static final String THREE_SUBTITLE_ONEKEY = "私人订制KEY";
    private static final String THREE_SUBTITLE_TWO = "关于我们";
    private static final String THREE_MENU_SUBTITLE_THREEURL = "http://www.epochyoosure.com/index.html";

    public static void main(String[] args) {
        creatWxButton();
    }

    private static void creatWxButton() {
        //第一个菜单
        ClickButton oneButton = new ClickButton(ONE_MENU_TITLE,ONE_MENU_TITLEKEY);
        //第二个菜单
        SubButton twoButton = new SubButton(TWO_MENU_TITLE);
        //第二个菜单的子菜单
        ViewButton oneSubButton = new ViewButton(TWO_MENU_SUBTITLE_ONE,TWO_MENU_SUBTITLE_ONEURL);
        ViewButton twoSubButton = new ViewButton(TWO_MENU_SUBOTITLE_TWO,TWO_MENU_SUBTITLE_TWOURL);
        ViewButton threeSubButton = new ViewButton(TWO_MENU_SUBTITLE_THREE,TWO_MENU_SUBTITLE_THREEURL);
        twoButton.getSub_button().add(threeSubButton);
        twoButton.getSub_button().add(twoSubButton);
        twoButton.getSub_button().add(oneSubButton);
        //第三个菜单
        SubButton threeButton = new SubButton(THREE_MENU_TITLE);
        //第三个菜单的子菜单
        ClickButton subButtonOne = new ClickButton(THREE_SUBTITLE_ONE,THREE_SUBTITLE_ONEKEY);
        ViewButton subButtonTwo = new ViewButton(THREE_SUBTITLE_TWO,THREE_MENU_SUBTITLE_THREEURL);
        threeButton.getSub_button().add(subButtonOne);
        threeButton.getSub_button().add(subButtonTwo);
        //添加到button对象中
        WxButton wxButton = new WxButton();
        wxButton.getButton().add(oneButton);
        wxButton.getButton().add(twoButton);
        wxButton.getButton().add(threeButton);
        //转json
        String jsonStr = JSONObject.toJSONString(wxButton);
        //替换token
        String url = CREAT_MENU_URL.replace("ACCESS_TOKEN", WxService.getAccessToken());
        //调用网络请求
        NetManagerUtil.post(url,jsonStr);
    }
}
