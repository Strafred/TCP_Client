import java.io.*;
import java.net.Socket;

public class Client {
    public static final int ARGS_NUMBER_REQUIRED = 3;
    private static final int SOCKET_BUFFER_SIZE = 65536;

    public static void uploadFile(Socket socket, File file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        FileInputStream fileInputStream = new FileInputStream(file);

        dataOutputStream.writeUTF(file.getName());
        dataOutputStream.writeLong(file.length());

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
}