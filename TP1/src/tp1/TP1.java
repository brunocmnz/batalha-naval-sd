/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.IOException;
import tp1.Interface;

/**
 *
 * @author bruno
 */
public class TP1 {

    private static TCPServer tcpserver;
    private static TCPClient tcpclient;
    private static UDPServer udpserver;
    private static UDPClient udpclient;

    public void startTCPServer(int port) throws IOException {
        tcpserver = new TCPServer();
        System.out.println("Servidor pronto para receber mensagens.");
    }

    public void startTCPClient(String serverAddress) throws IOException {
        tcpclient = new TCPClient(serverAddress);
        System.out.println("Cliente conectado ao servidor.");
    }

    public void sendMessageAsServer(String message) throws IOException {
        if (tcpserver != null) {
            tcpserver.sendMessage(message);
        }
    }

    public void sendMessageAsClient(String message) throws IOException {
        if (tcpclient != null) {
            tcpclient.sendMessage(message);
        }
    }

    public void closeConnections() throws IOException {
        if (tcpserver != null) {
            tcpserver.close();
        }
        if (tcpclient != null) {
            tcpclient.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Interface intf = Interface.criaInterface(udpserver);
        intf.setVisible(true);
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
    }

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

    private static void iniciarServidorUDP() {
        try {
            udpserver = new UDPServer();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

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
//                    server.sendMessage(message);
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
//                    client.sendMessage(message);
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
