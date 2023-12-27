package com.example.chatgpt;

public class Message {


    String mText;
    boolean isSendbyUser;

    public Message(String mText, boolean isSendbyUser) {
        this.mText = mText;
        this.isSendbyUser = isSendbyUser;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public boolean isSendbyUser() {
        return isSendbyUser;
    }

    public void setSendbyUser(boolean sendbyUser) {
        isSendbyUser = sendbyUser;
    }

}
