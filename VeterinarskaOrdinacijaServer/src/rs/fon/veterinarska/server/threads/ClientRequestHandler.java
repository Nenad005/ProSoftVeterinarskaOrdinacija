/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.fon.veterinarska.server.threads;
import java.io.*;
import java.net.Socket;
import rs.fon.veterinarska.common.communication.*;
import rs.fon.veterinarska.server.controller.ServerController;
/**
 *
 * @author nenad
 */
public class ClientRequestHandler extends Thread {

    private final Socket socket;

    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream output =
                    new ObjectOutputStream(socket.getOutputStream());

            output.flush();

            ObjectInputStream input =
                    new ObjectInputStream(socket.getInputStream());

            while (!socket.isClosed()) {
                ClientRequest request =
                        (ClientRequest) input.readObject();

                ServerResponse response = handleRequest(request);

                output.reset();
                output.writeObject(response);
                output.flush();
            }
        } catch (EOFException ex) {
            // Klijent je zatvorio vezu.
        } catch (IOException | ClassNotFoundException ex) {
            if (!socket.isClosed()) {
                ex.printStackTrace();
            }
        } finally {
            closeConnection();
        }
    }

    private ServerResponse handleRequest(ClientRequest request) {
//        ServerResponse response = ServerController.odgovoriKlijentu(request);
        // Ovde cemo kasnije pozvati ServerController.
        return new ServerResponse(
                null,
                "Operacija jos nije implementirana.",
                ResponseStatus.ERROR
        );

//        return response;
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
