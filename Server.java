import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Port number can be adjusted

            System.out.println("Server started. Waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String messagereceive = new String(buffer, 0, bytesRead);
                System.out.println("Received from client: " + messagereceive);

                // Echo the message back to the client
                // outputStream.write(buffer, 0, bytesRead);
                // outputStream.flush();
                boolean running=true;
                while (running) {
                    System.out.print("Enter message (type 'exit' to quit): ");
                Scanner scanner = new Scanner(System.in);
                    String messagetosend = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(messagetosend)) {
                        break;
                    }
                    outputStream.write(messagetosend.getBytes());
                    outputStream.flush();
                    running=false;
                }
            }
         

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
