package org.example;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class SomeClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String nickname;

    public SomeClient(String addr, int port) {
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException ignored) {
            System.out.println("Connection to socket failed.");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.nickname = "user" + Math.random() * (10000 - 2 + 1) + 2;
            new ReadFromServer().start();
            new WriteToServer().start();
        } catch (IOException ignored) {
            SomeClient.this.downService();
        }
    }

    private void downService() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException ignored) {
        }
    }

    private class ReadFromServer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    String str = in.readLine();
                    if (str.contains("-exit")) {
                        SomeClient.this.downService();
                        break;
                    } else System.out.println(str);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private class WriteToServer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    out.write("(" + LocalTime.now() + ") " + nickname + ": " + inputUser.readLine() + "\n");
                    out.flush();
                } catch (IOException ignored) {
                }
            }
        }
    }
}

