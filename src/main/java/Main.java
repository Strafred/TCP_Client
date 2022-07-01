import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        if (args.length != Client.ARGS_NUMBER_REQUIRED) {
            System.err.println("Incorrect arguments number!");
            return;
        }

        File file = new File(args[0]);
        if (!file.isFile()) {
            System.err.println("Incorrect file!");
            return;
        }

        try {
            Socket socket = new Socket(InetAddress.getByName(args[1]), Integer.parseInt(args[2]));
            Client.uploadFile(socket, file);

        } catch (IOException e) {
            System.err.println("No connection...");
        }
    }
}