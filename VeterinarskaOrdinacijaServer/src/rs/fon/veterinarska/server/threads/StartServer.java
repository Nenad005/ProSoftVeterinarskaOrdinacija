/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.fon.veterinarska.server.threads;
import java.io.*;
//import java.net.Socket;
import java.net.*;
import java.util.List;
import java.util.ArrayList;


/**
 *
 * @author nenad
 */
public class StartServer extends Thread {

    private final ServerSocket serverSocket;
    private final List<ClientRequestHandler> clients;

    public StartServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                ClientRequestHandler client =
                        new ClientRequestHandler(socket);

                clients.add(client);
                client.start();
            }
        } catch (IOException ex) {
            if (!serverSocket.isClosed()) {
                ex.printStackTrace();
            }
        } finally {
            for (ClientRequestHandler client : clients) {
                client.closeConnection();
            }

            stopServer();
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
