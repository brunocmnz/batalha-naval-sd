package tp1;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private String mensagem = "";


    public TCPServer() throws IOException {
        serverSocket = new ServerSocket(4872);
        startServer();
    }

    public String receberMensagem() {
        try {
            while (true) {
                String newmessage = in.readUTF();
                if (newmessage != "") {
                    mensagem = newmessage;
                    System.out.println("IntPrintTCP: " + mensagem);
                }
                if (mensagem.equalsIgnoreCase("sair")) {
                    System.out.println("Cliente desconectou.");
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void startServer() throws IOException {
        System.out.println("Servidor TCP aguardando conexão...");
        clientSocket = serverSocket.accept();
        System.out.println("Cliente conectado!");

        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());

//        new Thread(this).start(); // Inicia a thread para ouvir mensagens recebidas
    }

    public void enviarMensagem(String message) {
        if (out != null) {
            try {
                out.writeUTF(message);
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void close() throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    @Override
    public void run() {
        try {
            startServer();
            try {
                while (true) {
                    String newmessage = in.readUTF();
                    if (newmessage != "") {
                        mensagem = newmessage;
                        System.out.println("IntPrintTCP: " + mensagem);
                    }
                    if (mensagem.equalsIgnoreCase("sair")) {
                        System.out.println("Cliente desconectou.");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao receber mensagem: " + e.getMessage());
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar servidor: " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
