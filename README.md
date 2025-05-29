# multiple-chat
# Java Socket Chat Application

A lightweight, console‑based chat system implemented with plain Java sockets. It consists of a multithreaded server and a simple terminal client, allowing multiple users to exchange messages in real time.

---

## Features

* **Concurrent Connections** – Each client is handled in its own `Thread`, ensuring non‑blocking interactions.
* **Broadcast Messaging** – Messages from any client are broadcast to all connected users.
* **Built‑in Commands**

  * `/all` – List all active users
  * `/help` – Display available commands
  * `exit` – Disconnect from the server
* **Thread‑Safe Roster** – Uses `CopyOnWriteArrayList` to maintain the list of online clients safely across threads.
* **Graceful Shutdown** – Both server and client close sockets cleanly on exit or error.

---

## Project Structure

| File                 | Responsibility                                                                                                           |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| `Server.java`        | Listens on a TCP port (default **1234**), accepts incoming sockets, and starts a `ClientHandler` thread for each client. |
| `ClientHandler.java` | Implements `Runnable`; manages one connected client: reads lines, handles commands, and broadcasts messages.             |
| `Client.java`        | Console client that connects to the server, sends the user’s name followed by chat input, and prints anything received.  |

---

## Prerequisites

* Java Development Kit (JDK) **11** or newer

---

## Building

```bash
# Compile everything from the project root
javac Server.java ClientHandler.java Client.java
```

---

## Running

### Start the Server

```bash
# Uses default port 1234
java Server
```

To use a custom port, edit the `main` method in `Server.java` or add your own constructor parameter.

### Launch a Client

```bash
java Client
```

The client will prompt for a **username** and then connect to `localhost:1234`. Provide a different host/port by modifying the `Client` constructor in `Client.java` or adding command‑line arguments.

---

## Using the Chat

* Type messages and press **Enter** to send.
* Use the commands listed under **Features** at any time.
* Enter `exit` to leave the chat.

---

## Extending the Project

* **Private Messaging** – Add a `/msg <user> <text>` command.
* **GUI Client** – Build a Swing/JavaFX or web front‑end.
* **Persistence** – Log chat history to disk or a database.
* **Security** – Wrap sockets with TLS (SSLSocket) for encryption.

---

## License

This project is released under the MIT License. Feel free to use and modify it for your own purposes.
