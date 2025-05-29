import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        System.out.println("[SERVER STARTED ON PORT " + serverSocket.getLocalPort() + "]");
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                Thread t = new Thread(new ClientHandler(socket));
                t.start();
            }
        } catch (IOException e) {
            // fall through to finally
        } finally {
            closeServerSocket();
        }
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) throws IOException {
        new Server(new ServerSocket(1234)).startServer();
    }
}
