package pojo.button;


public abstract class BaseButton {
    /**
     * 菜单名字
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseButton(String name) {
        this.name = name;
    }
}
