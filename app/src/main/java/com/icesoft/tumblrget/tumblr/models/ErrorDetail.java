package com.icesoft.tumblrget.tumblr.models;

public class ErrorDetail implements ITumblrDetail {
    public static final int TYPE = 0;
    private String message;
    @Override
    public int getType() {
        return TYPE;
    }

    public ErrorDetail(String message) {
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
