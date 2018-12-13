package pojo.button;


import java.util.ArrayList;
import java.util.List;

public class WxButton {

    private List<BaseButton> button = new ArrayList<>();

    public List<BaseButton> getButton() {
        return button;
    }

    public void setButton(List<BaseButton> button) {
        this.button = button;
    }
}
