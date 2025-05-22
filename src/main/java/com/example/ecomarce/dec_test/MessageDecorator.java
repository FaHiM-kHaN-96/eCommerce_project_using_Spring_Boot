package com.example.ecomarce.dec_test;

public class MessageDecorator implements Message{
    protected Message message;

    public MessageDecorator(Message message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return message.getContent();
    }
}
