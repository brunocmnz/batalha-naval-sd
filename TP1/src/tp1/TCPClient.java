package tp1;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient implements Runnable {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    String mensagem;
    int port = 4872;

    public TCPClient(String serverIP) {
        try {
            socket = new Socket(serverIP, port);
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() throws IOException {
        System.out.println("Conectado ao servidor!");
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        new Thread(this).start(); // Inicia a thread para ouvir mensagens recebidas
    }

    public void sendMessage(String message) throws IOException {
        if (out != null) {
            out.writeUTF(message);
        }
    }
    
    public String receiveMessage() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Amigo: " + message);
            }
            return message;
        } catch (IOException e) {
            System.out.println("Erro ao receber mensagens: " + e.getMessage());
        }
        return null;
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readUTF();
                if (message.equalsIgnoreCase("sair")) {
                    System.out.println("Servidor desconectou.");
                    break;
                }
                System.out.println("Servidor: " + message);
            }
        } catch (IOException e) {
            System.out.println("Erro ao receber mensagem: " + e.getMessage());
        } finally {
            try {
                close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar cliente: " + e.getMessage());
            }
        }
    }
}
