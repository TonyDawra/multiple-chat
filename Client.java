import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final String name;

    public Client(String host, int port, String name) throws IOException {
        this.socket = new Socket(host, port);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.name = name;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {

            /* send our name first */
            out.write(name);
            out.newLine();
            out.flush();

            System.out.println("[Connected to server at " + socket.getRemoteSocketAddress() + "]");

            /* ---------- thread that prints everything coming from the server ---------- */
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException ignored) { }
            }).start();

            /* ---------- main loop that reads user input ---------- */
            while (true) {
                String msg = scanner.nextLine().trim();
                if (msg.isEmpty()) continue;

                if (msg.equalsIgnoreCase("exit")) {
                    out.write("exit");
                    out.newLine();
                    out.flush();
                    break;
                }

                out.write(msg);                // raw line; server will prepend [name]:
                out.newLine();
                out.flush();
            }

        } catch (IOException ignored) {
        } finally {
            closeQuietly();
            System.out.println("[Disconnected from server]");
        }
    }

    private void closeQuietly() {
        try { socket.close(); } catch (IOException ignored) { }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        new Client("localhost", 1234, sc.nextLine().trim()).start();
    }
}
