
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final int ARGS_NUMBER_REQUIRED = 3;
    private static final int SOCKET_BUFFER_SIZE = 65536;

    private static void uploadFile(Socket socket, File file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        FileInputStream fileInputStream = new FileInputStream(file);

        dataOutputStream.writeUTF(file.getName());
        dataOutputStream.writeLong(file.length());

        String name = file.toString();
        String extension = null;
        int index = name.lastIndexOf('.');
        if (index > 0) {
            extension = name.substring(index + 1);
        }

        assert extension != null;
        dataOutputStream.writeUTF(extension);

        byte[] sendBuffer = new byte[socket.getSendBufferSize()];
        int count;

        do {
            count = fileInputStream.read(sendBuffer);
            dataOutputStream.write(sendBuffer, 0, count);
        } while (count >= SOCKET_BUFFER_SIZE);

        String status = dataInputStream.readUTF();
        System.out.println(status);

        socket.shutdownInput();
        socket.shutdownOutput();
        dataInputStream.close();
        dataOutputStream.close();
        fileInputStream.close();
    }

    public static void main(String[] args) {
        if (args.length != ARGS_NUMBER_REQUIRED) {
            System.err.println("Incorrect arguments number!");
            return;
        }

        File file = new File(args[0]);
        if (!file.isFile()) {
            System.err.println("Incorrect file!");
            return;
        }

        try {
            // Socket socket = new Socket(InetAddress.getByName(args[1]), Integer.parseInt(args[2]));
            Socket socket = new Socket(InetAddress.getLocalHost(), Integer.parseInt(args[2]));
            uploadFile(socket, file);
        } catch (IOException e) {
            System.err.println("No connection...");
        }
    }
}