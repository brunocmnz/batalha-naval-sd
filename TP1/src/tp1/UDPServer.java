package tp1;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class UDPServer {
    private DatagramSocket socket;
    private InetAddress clientAddress;
    private int clientPort;
    private final int serverPort = 4872;
    String mensagem;

    public UDPServer() {
        try {
            socket = new DatagramSocket(serverPort);
            System.out.println("Servidor iniciado. Aguardando mensagens...");
            executarRecepcao();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        }
    }

    private void executarRecepcao() {
        // Thread para receber mensagens
        Thread receiveThread = new Thread(() -> {
            byte[] buffer = new byte[1000];
            try {
                while (true) {
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                    socket.receive(request); // Recebe mensagens do cliente
                    mensagem = new String(request.getData(), 0, request.getLength());
                    System.out.println("\nMensagem recebida do cliente: " + mensagem);

                    // Armazena o endere�o e porta do cliente na primeira conex�o
                    if (clientAddress == null || clientPort == 0) {
                        clientAddress = request.getAddress();
                        clientPort = request.getPort();
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro de E/S na recep��o: " + e.getMessage());
            }
        });
        receiveThread.start();
    }

    // M�todo p�blico para enviar mensagens ao cliente
    public void sendMessage(String message) {
        if (clientAddress != null && clientPort != 0) {
            try {
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), clientAddress, clientPort);
                socket.send(packet);
                System.out.println("Mensagem enviada ao cliente: " + message);
            } catch (IOException e) {
                System.out.println("Erro ao enviar mensagem: " + e.getMessage());
            }
        } else {
            System.out.println("Cliente n�o conectado. N�o � poss�vel enviar mensagem.");
        }
    }

    // Fecha o socket quando o servidor for encerrado
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
