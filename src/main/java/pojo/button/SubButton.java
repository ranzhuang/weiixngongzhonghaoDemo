package pojo.button;


import java.util.ArrayList;
import java.util.List;

public class SubButton extends BaseButton {

    private List<BaseButton> sub_button = new ArrayList<>();

    public List<BaseButton> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<BaseButton> sub_button) {
        this.sub_button = sub_button;
    }

    public SubButton(String name) {
        super(name);
    }
}
