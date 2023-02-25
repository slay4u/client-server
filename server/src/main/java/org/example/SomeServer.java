package org.example;

import java.io.*;
import java.net.Socket;

public class SomeServer extends Thread{
    private BufferedReader in;
    private BufferedWriter out;

    public SomeServer(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            start();
        } catch (IOException ignored) {
            System.out.println("Something went wrong...");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String word = in.readLine();
                if (word.contains("-file")) {
                    File file = new File("E:/rest project/client-server/server/src/main/resources/files", "client-" + 1 + ".txt");
                    try {
                        boolean created = file.createNewFile();
                        if (created) {
                            System.out.println(word + "\nFile created successfully.");
                            for (SomeServer connection : Server.activeConnection) {
                                connection.sendMessage(word + "\nFile created successfully.");
                            }
                        } else {
                            System.out.println(word + "\nFile was not created. Please, try again.");
                            for (SomeServer connection : Server.activeConnection) {
                                connection.sendMessage(word + "\nFile was not created. Please, try again.");
                            }
                        }
                    } catch (IOException ignored) {
                    }
                } else if (word.contains("-exit")) {
                    System.out.println("User 'client-" + 1 + "' disconnected from server.");
                    for (SomeServer connection : Server.activeConnection) {
                        connection.sendMessage("-exit");
                        connection.join();
                    }
                } else {
                    System.out.println("Echoing: " + word);
                    for (SomeServer connection : Server.activeConnection) {
                        connection.sendMessage(word);
                    }
                }
            } catch (NullPointerException | IOException | InterruptedException ignored) {
            }
        }
    }

    void sendMessage(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }
}
