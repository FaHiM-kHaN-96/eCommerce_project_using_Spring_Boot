package com.example.ecomarce.dec_test;

public class HeaderDecorator extends MessageDecorator  {
    public HeaderDecorator(Message message) {
        super(message);
    }

    @Override
    public String getContent() {

        return "Header: Confidential\n" + super.getContent();
    }
}
