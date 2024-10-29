package tp1;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class UDPClient {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort = 4872; // Porta do servidor
    String mensagem;

    public UDPClient(String ip) {
        try {
            // Inicializa o socket e define o endereço do servidor
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip); // Altere para o IP do servidor, se necessário
            iniciarRecepcao();
        } catch (SocketException e) {
            System.out.println("Erro no Socket: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("Erro de host desconhecido: " + e.getMessage());
        }
    }

    // Thread para receber mensagens do servidor
    private void iniciarRecepcao() {
        Thread receiveThread = new Thread(() -> {
            byte[] buffer = new byte[1000];
            try {
                while (true) {
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    socket.receive(reply); // Recebe a mensagem do servidor
                    mensagem = new String(reply.getData(), 0, reply.getLength());
                    System.out.println("\nMensagem recebida do servidor: " + mensagem);
                }
            } catch (IOException e) {
                System.out.println("Erro de E/S na recepção: " + e.getMessage());
            }
        });
        receiveThread.start();
    }

    // Método para enviar uma mensagem ao servidor
    public void sendMessage(String message) {
        try {
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort);
            socket.send(packet);
            System.out.println("Mensagem enviada ao servidor: " + message);
        } catch (IOException e) {
            System.out.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    // Fecha o socket quando o cliente for encerrado
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
