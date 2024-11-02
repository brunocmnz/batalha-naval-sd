package tp1;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Thread{
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String mensagem = "";

    public String getMessage() {
        String mensagem = this.mensagem;
        this.mensagem = "";
        return mensagem;
    }
    
    public boolean newMessage(){
        return mensagem!= "";
    }
    
    public TCPClient(String serverIP) throws IOException {
        socket = new Socket(serverIP, 4872);
        connect();
    }

    public void connect() throws IOException {
        System.out.println("Conectado ao servidor TCP!");
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

//        new Thread(this).start(); // Inicia a thread para ouvir mensagens recebidas
    }

    public void enviarMensagem(String message){
        if (out != null) {
            try {
                out.writeUTF(message);
            } catch (IOException ex) {
                Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public void close() throws IOException {
        if (socket != null) socket.close();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String newmessage = in.readUTF();
                if(newmessage != ""){
                    mensagem = newmessage;
                    System.out.println("IntPrintTCP: "+mensagem);
                }
                if (mensagem.equalsIgnoreCase("sair")) {
                    System.out.println("Servidor desconectou.");
                    break;
                }
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
