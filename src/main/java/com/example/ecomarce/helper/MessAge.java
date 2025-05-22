package com.example.ecomarce.helper;

public class MessAge {
    private String content;
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessAge(String content, String type) {
        super();
        this.content = content;
        this.type = type;
    }
}
