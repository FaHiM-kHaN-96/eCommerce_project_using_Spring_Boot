package com.example.ecomarce.dec_test;

import java.io.Serializable;

public class PlainMessage implements Message {
    private String text;

    public PlainMessage(String text) {
        this.text = text;
    }

    @Override
    public String getContent() {
        return text;
    }
}
