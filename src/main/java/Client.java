import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private void start() {
        try {
            clientSocket = new Socket("localhost", 8080);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ignored) {
        }
    }

    private String send(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (IOException ignored) {
            return null;
        }
    }

    private void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
        String inputLine;
        while ((inputLine = new Scanner(System.in).nextLine()) != null) {
            String msg = client.send(inputLine);
            System.out.println("From server: " + msg);
            if (Objects.equals(msg, "-")) break;
        }
        client.stop();
    }
}
