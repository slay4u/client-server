package org.example;

public class Client {
    public static String ipAddr = "localhost";
    public static int port = 8080;

    public static void main(String[] args) {
        new SomeClient(ipAddr, port);
    }
}