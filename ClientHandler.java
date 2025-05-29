import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {

    // thread-safe list
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final String name;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.name = in.readLine();                    // the first line is the username
        clients.add(this);

        broadcast("[Server]: " + name + " joined.");
        System.out.println("[Client connected: " + name + "]");
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {

                /* -------- command handling -------- */
                if (line.equalsIgnoreCase("exit")) {
                    break;                                    // leave the loop ➜ finally
                }
                if (line.startsWith("/")) {
                    handleCommand(line.trim());
                    continue;
                }

                /* -------- normal chat message -------- */
                broadcast("[" + name + "]: " + line);
            }
        } catch (IOException ignored) {
            // fall through to finally
        } finally {
            cleanup();
        }
    }

    /* -------------------------------------------------- */

    private void handleCommand(String cmd) throws IOException {
        switch (cmd) {
            case "/all":
                sendPrivate("[Server]: " + getRoster());
                break;

            case "/help":
                sendPrivate(
                        "[Server]: Available commands:\n" +
                                " /all  -> Show a list of all connected users\n" +
                                " /help -> Show this help menu\n" +
                                " exit  -> Disconnect from the server"
                );
                break;

            default:
                sendPrivate("[Server]: Unknown command – type /help");
        }
    }


    private String getRoster() {
        return clients.stream().map(c -> c.name).reduce((a, b) -> a + " " + b).orElse("");
    }

    private void broadcast(String msg) {
        for (ClientHandler c : clients) c.sendPrivate(msg);
    }

    private void sendPrivate(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException ignored) { }
    }

    private void cleanup() {
        clients.remove(this);
        broadcast("[Server]: " + name + " left.");
        System.out.println("[Client disconnected: " + name + "]");
        try { socket.close(); } catch (IOException ignored) { }
    }
}
