package pojo.button;


public class ViewButton extends BaseButton {
    /**
     * 菜单类型
     */
    private String type = "view";
    /**
     * 跳转地址
     */
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ViewButton(String name, String url) {
        super(name);
        this.url = url;
    }
}
