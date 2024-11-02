/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class TP1 {

    static InetAddress clientAddress;
    static int clientPort;
    static String mensagemRecebida = "";
    static String mensagemParaEnviar = "";

//    private Socket socket;
//    private DataOutputStream out;
//    private DataInputStream in;
    public static void criaComunicacaoUDP() {
        //tenta servidor ===============================================
        try {
            DatagramSocket socket = new DatagramSocket(4872); // Porta do servidor
            byte[] buffer = new byte[1000];
            System.out.println("Serei servidor.");
            System.out.println("Servidor iniciado. Aguardando mensagens...");

            // Thread para receber mensagens do cliente
            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                        socket.receive(request); // Recebe mensagens do cliente

                        String newMessage = new String(request.getData(), 0, request.getLength());
                        if (newMessage != "") {
                            mensagemRecebida = newMessage;
                            System.out.println("Consegui:" + newMessage);
                        }
                        // Armazena o endereço e a porta do cliente
                        clientAddress = request.getAddress();
                        clientPort = request.getPort();
                    }
                } catch (IOException e) {
                    System.out.println("Erro de E/S: " + e.getMessage());
                }
            });
            // Inicializa a thread para receber mensagens
            receiveThread.start();

            // Permite que o servidor também envie mensagens ao cliente
            Scanner scanner = new Scanner(System.in);
            while (true) {
                while (mensagemParaEnviar == "");
                String message = mensagemParaEnviar;
                mensagemParaEnviar = "";
                // Verifica se o cliente foi definido antes de enviar                // Verifica se o cliente foi definido antes de enviar                // Verifica se o cliente foi definido antes de enviar                // Verifica se o cliente foi definido antes de enviar
                if (clientAddress != null && clientPort != 0) {
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
                              clientAddress, clientPort);
                    socket.send(packet);
                } else {
                    System.out.println("Aguardando um cliente para enviar mensagens...");
                }
            }
        } catch (SocketException e) {
            System.out.println("Serei cliente: ");
        } catch (IOException e) {
            System.out.println("Erro de E/S: " + e.getMessage());
        } finally {
            //tenta cliente ===============================================
            try {
                final DatagramSocket socket = new DatagramSocket(); // Cria o socket para enviar mensagens
                InetAddress serverAddress = InetAddress.getByName("127.0.0.1"); // Usando localhost
                int serverPort = 4872; // Porta do servidor
                // Thread para receber mensagens do servidor
                Thread receiveThread = new Thread(() -> {
                    try {
                        byte[] buffer = new byte[1000];
                        while (true) {
                            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                            socket.receive(reply); // Recebe a mensagem do servidor
                            String newMessage = new String(reply.getData(), 0, reply.getLength());
                            if (newMessage != "") {
                                mensagemRecebida = newMessage;
                                System.out.println("Consegui:" + newMessage);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Erro de E/S: " + e.getMessage());
                    }
                });
                // Inicia a thread de recepção
                receiveThread.start();
                // Permite que o cliente também envie mensagens ao servidor
                Scanner scanner = new Scanner(System.in);
                String message = "Comunicação establecida.";
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort);
                socket.send(packet);
                System.out.println("Comunicação estabelecida.");
                while (true) {
                    while (mensagemParaEnviar == "");
                    message = mensagemParaEnviar;
                    mensagemParaEnviar = "";
                    packet = new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort);
                    socket.send(packet);
                }
            } catch (SocketException e) {
                System.out.println("Erro no Socket: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Erro de E/S: " + e.getMessage());
            }
        }
    }

//    public static void criaComunicacaoTCP() {
//        Scanner scanner = new Scanner(System.in);
//        String serverIP = "127.0.0.1";
//        int port = 4872;
//        boolean souCliente = false;
//        try {
//            // tenta servidor ===============================================
//            TCPServer server = new TCPServer();
//            server.startServer();
//            new Thread(() -> {
//                while (true) {
//                    String newMessage = server.getMessage();
//                    if (newMessage != "") {
//                        System.out.println("Consegui:" + newMessage);
//                        mensagemRecebida = newMessage;
//                    }
//                }
//            }).start();
//            String newMessage = "";
//            while (true) {
//                while (newMessage == "") {
//                    newMessage = mensagemParaEnviar;
//                }
//                mensagemParaEnviar = "";
//                server.enviarMensagem(newMessage);
//                if (newMessage.equalsIgnoreCase("sair")) {
//                    server.close();
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            // tenta cliente ===============================================
//            System.out.println("Serei cliente.");
//            souCliente = true;
//        }
//
//        if (souCliente) {
//
//            try {
//                final TCPClient client;
//                client = new TCPClient(serverIP, port);
//                client.connect();
//                new Thread(() -> {
//                    while (true) {
//                        String newMessage = client.getMessage();
//                        if (newMessage != "") {
//                            mensagemRecebida = newMessage;
//                            System.out.println("Consegui:" + newMessage);
//                        }
//                    }
//                }).start();
//                String message = "Comunicação estabelecida.";
//                client.enviarMensagem(message);
//                message = "";
//                while (true) {
//                    while (message == "") {
//                        message = mensagemParaEnviar;
//                    }
//                    mensagemParaEnviar = "";
//                    client.enviarMensagem(message);
//
//                    if (message.equalsIgnoreCase("sair")) {
//                        try {
//                            client.close();
//                        } catch (IOException ex) {
//                            Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        break;
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        scanner.close();
//    }

    public static void main(String[] args) throws IOException {
//        Scanner sc = new Scanner(System.in);

//        System.out.print("Digite 1 pra servidor e 2 para cliente: ");
//        String op = sc.next();
//        if (op == "1") {
//            UDPServer server = new UDPServer(socket);
//        } else if (op == "2") {
//
//        }

//        String ip = "127.0.0.1";
//        try {
//            DatagramSocket socket = new DatagramSocket(4872); // Porta do servidor
//            System.out.println("Serei Servidor");
//            UDPServer server = new UDPServer(socket);
//            server.start();
//            new Thread(() -> {
//                do {
//                    String msg = server.receberMensagem();
//                    System.out.println(msg);
//                } while (true);
//            }).start();
//            do {
//                System.out.print("msg: ");
//                String msg = sc.next();
//                server.enviarMensagem(msg);
//            } while (true);
//        } catch (SocketException ex1) {
//            DatagramSocket socket;
//            socket = new DatagramSocket(); // Cria o socket para enviar mensagens
//            System.out.println("Serei cliente: ");
//            UDPClient client = new UDPClient(ip, socket);
//            client.start();
//            client.enviarMensagem("Conexão concluída.");
//            new Thread(() -> {
//                do {
//                    String msg = client.receberMensagem();
//                    System.out.println(msg);
//                } while (true);
//            }).start();
//            do {
//                System.out.print("msg: ");
//                String msg = sc.next();
//                client.enviarMensagem(msg);
//            } while (true);
//        }
        
        Interface intf;
        
        intf = new Interface();
        intf.setVisible(true);
    }

//        Scanner sc = new Scanner(System.in);
//        System.out.print("Digite 1 para Servidor e 2 para Cliente: ");
//        int op = sc.nextInt();
//        
//        if(op == 1){
//            UDPServer udpserver = new UDPServer(); // Exemplo de porta dinâmica
//            TCPServer tcpserver = new TCPServer(); // Exemplo de porta dinâmica
//            intf = Interface.criaInterface(udpserver, tcpserver, Interface.Comunicacao.UDPserver);
//            intf.setVisible(true);
//        }else if(op == 2){
//            UDPClient udpclient = new UDPClient("127.0.0.1");
//            TCPClient tcpclient = new TCPClient("127.0.0.1");
//            intf = Interface.criaInterface(udpclient, tcpclient, Interface.Comunicacao.UDPclient);
//            intf.setVisible(true);
//        }
//
//        TP1 chatApp = new TP1();
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Digite 'S' para iniciar como servidor ou 'C' como cliente:");
//        String role = scanner.nextLine();
//
//        if (role.equalsIgnoreCase("S")) {
//            System.out.println("Iniciando servidor...");
//            chatApp.startTCPServer(7896);
//
//            while (true) {
//                System.out.println("Digite uma mensagem para enviar ou 'sair' para finalizar:");
//                String message = scanner.nextLine();
//                if (message.equalsIgnoreCase("sair")) {
//                    break;
//                }
//                chatApp.sendMessageAsServer(message);
//                System.out.println("Mensagem recebida: " + chatApp.tcpserver.receiveMessage());
//            }
//        } else if (role.equalsIgnoreCase("C")) {
//            System.out.println("Digite o endereço IP do servidor:");
//            String serverAddress = scanner.nextLine();
//            System.out.println("Conectando ao servidor...");
//            chatApp.startTCPClient(serverAddress);
//
//            while (true) {
//                System.out.println("Digite uma mensagem para enviar ou 'sair' para finalizar:");
//                String message = scanner.nextLine();
//                if (message.equalsIgnoreCase("sair")) {
//                    break;
//                }
//                chatApp.sendMessageAsClient(message);
//                System.out.println("Mensagem recebida: " + chatApp.tcpclient.receiveMessage());
//            }
//        }
//
//        chatApp.closeConnections();
//        scanner.close();
//    }
//    private static void UDP() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Escolha uma opção: (1) Iniciar como Servidor, (2) Iniciar como Cliente");
//        int escolha = scanner.nextInt();
//        scanner.nextLine(); // Consume the newline
//
//        if (escolha == 1) {
//            iniciarServidorUDP();
//        } else if (escolha == 2) {
//            iniciarClienteUDP();
//        } else {
//            System.out.println("Opção inválida. Encerrando...");
//        }
//    }
//    private static void iniciarServidorUDP() {
//        try {
//            udpserver = new UDPServer();
//        } catch (Exception e) {
//            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
//        }
//    }
//    private static void iniciarClienteUDP() {
//        try {
//            udpclient = new UDPClient();
//        } catch (Exception e) {
//            System.out.println("Erro ao iniciar o cliente: " + e.getMessage());
//        }
//    }
//    private static void TCP() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Digite '1' para iniciar como Servidor ou '2' para iniciar como Cliente:");
//        int choice = scanner.nextInt();
//        scanner.nextLine(); // Consumir nova linha
//
//        if (choice == 1) {
//            int port = 4872;
//
//            try {
//                TCPServer server = new TCPServer(port);
//                server.startServer();
//
//                while (true) {
//                    System.out.print("Digite mensagem para o cliente (ou 'sair' para encerrar): ");
//                    String message = scanner.nextLine();
//                    server.enviarMensagem(message);
//                    if (message.equalsIgnoreCase("sair")) {
//                        server.close();
//                        break;
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println("Erro no servidor: " + e.getMessage());
//            }
//
//        } else if (choice == 2) {
//            String serverIP = "10.0.0.156";
//            int port = 4872;
//            scanner.nextLine(); // Consumir nova linha
//            TCPClient client = null;
//            try {
//                client = new TCPClient(serverIP, port);
//
//            } catch (IOException ex) {
//                Logger.getLogger(TP1.class
//                          .getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                client.connect();
//
//            } catch (IOException ex) {
//                Logger.getLogger(TP1.class
//                          .getName()).log(Level.SEVERE, null, ex);
//            }
//            while (true) {
//                System.out.print("Digite mensagem para o servidor (ou 'sair' para encerrar): ");
//                String message = scanner.nextLine();
//                try {
//                    client.enviarMensagem(message);
//
//                } catch (IOException ex) {
//                    Logger.getLogger(TP1.class
//                              .getName()).log(Level.SEVERE, null, ex);
//                }
//                if (message.equalsIgnoreCase("sair")) {
//                    try {
//                        client.close();
//
//                    } catch (IOException ex) {
//                        Logger.getLogger(TP1.class
//                                  .getName()).log(Level.SEVERE, null, ex);
//                    }
//                    break;
//                }
//            }
//
//        } else {
//            System.out.println("Escolha inválida.");
//        }
//
//        scanner.close();
//    }
}
