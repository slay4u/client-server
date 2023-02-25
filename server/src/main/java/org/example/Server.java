package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Server {
    public static final int PORT = 8080;
    public static LinkedList<SomeServer> activeConnection = new LinkedList<>();
    public static int count = 0;

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started at " + LocalDateTime.now());
            while (true) {
                Socket socket = server.accept();
                SomeServer connection = new SomeServer(socket);
                activeConnection.add(connection);
                count++;
                System.out.println("User 'client-" + count + "' connected successfully at " + LocalDateTime.now());
                for (SomeServer conn : Server.activeConnection) {
                    conn.sendMessage("User 'client-" + count + "' connected successfully at " + LocalDateTime.now());
                }
            }
        }
    }
}
