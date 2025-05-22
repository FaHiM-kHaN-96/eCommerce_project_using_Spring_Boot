package com.example.ecomarce.dec_test;

public class Main {
    public static void main(String[] args) {
        Message message = new PlainMessage("This is a plain message.");
        message = new HeaderDecorator(message);


        System.out.println("Final Message:\n" + message.getContent());
    }
}
