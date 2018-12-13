package pojo.button;


public class ClickButton extends BaseButton {
    /**
     * 菜单类型
     */
    private String type = "click";
    /**
     * 唯一键
     */
    private String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ClickButton(String name, String key) {
        super(name);
        this.key = key;
    }
}
