package siplaundry.entity;

import com.google.gson.annotations.SerializedName;

public class WhatsappMessage {
    @SerializedName("session")
    private String session = "sip_app";

    @SerializedName("target")
    private String target;

    @SerializedName("text")
    private String text;

    public WhatsappMessage(String target, String text) {
        this.target = target;
        this.text = text;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
