package com.icesoft.tumblrget.models;

public class TypedJson
{
    public int type;
    public String json;

    public TypedJson(int type, String json) {
        this.type = type;
        this.json = json;
    }
}
