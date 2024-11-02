package tp1;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer extends Thread {

    private DatagramSocket socket;
    private InetAddress clientAddress;
    private int clientPort;
    byte[] buffer;

    public UDPServer() throws SocketException {
        socket = new DatagramSocket(4872); // Porta do servidor
        buffer = new byte[1000];
        System.out.println("Servidor iniciado. Aguardando mensagens...");
    }

    public void enviarMensagem(String message) {
        if (clientAddress == null && clientPort == 0) {
            System.out.println("Aguardando um cliente para enviar a mensagem: "+message);
            while(clientAddress == null && clientPort == 0);
        }
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
                  clientAddress, clientPort);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Mensagem enviada ao cliente: " + message);
    }
    
    public String receberMensagem() {
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        
        try {
            socket.receive(request); // Bloqueia até receber uma mensagem do cliente
            String message = new String(request.getData(), 0, request.getLength());
            // Armazena o endereço e a porta do cliente se for a primeira mensagem
            if (clientAddress == null && clientPort == 0) {
                clientAddress = request.getAddress();
                clientPort = request.getPort();
            }
            
            System.out.println("Mensagem recebida do cliente: " + message);
            return message;
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void run() {
//        try {
//            socket = new DatagramSocket(4872); // Porta do servidor
//        buffer = new byte[1000];

        // Thread para receber mensagens do cliente
//            Thread receiveThread = new Thread(() -> {
//                try {
//                    while (true) {
//                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
//                        socket.receive(request); // Recebe mensagens do cliente
//                        String message = new String(request.getData(), 0, request.getLength());
//                        System.out.println("\n" + message);
//
//                        // Armazena o endereço e a porta do cliente
//                        clientAddress = request.getAddress();
//                        clientPort = request.getPort();
//
//                        // Envia resposta de confirmação ao cliente
//                        String replyMessage = "Mensagem recebida: " + message;
//                        DatagramPacket reply = new DatagramPacket(replyMessage.getBytes(), replyMessage.length(),
//                                  clientAddress, clientPort);
//                        socket.send(reply);
//                    }
//                } catch (IOException e) {
//                    System.out.println("Erro de E/S: " + e.getMessage());
//                }
//            });
//
//            // Inicializa a thread para receber mensagens
//            receiveThread.start();
        // Permite que o servidor também envie mensagens ao cliente
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                System.out.print("\n");
//                String message = scanner.nextLine();
//
//                // Verifica se o cliente foi definido antes de enviar
//                if (clientAddress != null && clientPort != 0) {
//                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
//                              clientAddress, clientPort);
//                    socket.send(packet);
//                    System.out.println("Mensagem enviada ao cliente: " + message);
//                } else {
//                    System.out.println("Aguardando um cliente para enviar mensagens...");
//                }
//            }
//        } catch (SocketException e) {
//            System.out.println("Erro no Socket: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Erro de E/S: " + e.getMessage());
//        } finally {
//            System.out.println("JÁ TEM SERVIDOR");
//        }
    }
}
