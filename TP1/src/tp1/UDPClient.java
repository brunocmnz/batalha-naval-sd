package tp1;


import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient extends Thread {

    DatagramSocket socket; // Cria o socket para enviar mensagens
    InetAddress serverAddress; // Usando localhost
    int serverPort = 4872; // Porta do servidor
    byte[] buffer;
    
    UDPClient(String ip) throws SocketException {
        socket = new DatagramSocket(); // Cliente com nova porta
        buffer = new byte[1000];
        try {
            serverAddress = InetAddress.getByName(ip); // Usando localhost
            this.socket = socket;
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensagem(String message) {
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receberMensagem(){
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(reply); // Recebe a mensagem do servidor
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        String message = new String(reply.getData(), 0, reply.getLength());
        System.out.println("Recebi: "+message);
        return message;
    }

    @Override
    public void run() {
//        try {
//            final DatagramSocket socket = new DatagramSocket(); // Cria o socket para enviar mensagens
//            InetAddress serverAddress = InetAddress.getByName("127.0.0.1"); // Usando localhost
//            int serverPort = 4872; // Porta do servidor
//
//            // Thread para receber mensagens do servidor
//            Thread receiveThread = new Thread(() -> {
//                try {
//                    buffer = new byte[1000];
//                    while (true) {
//                        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//                        socket.receive(reply); // Recebe a mensagem do servidor
//                        String message = new String(reply.getData(), 0, reply.getLength());
//                        System.out.println("\n" + message);
//                    }
//                } catch (IOException e) {
//                    System.out.println("Erro de E/S: " + e.getMessage());
//                }
//            });
//
//            // Inicia a thread de recepção
//            receiveThread.start();
//
//            // Permite que o cliente também envie mensagens ao servidor
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                System.out.print("Digite sua mensagem: ");
//                String message = scanner.nextLine();
//                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort);
//                socket.send(packet);
//            }
//        } catch (SocketException e) {
//            System.out.println("Erro no Socket: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Erro de E/S: " + e.getMessage());
//        }
    }

}
