package tp1;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class MessageSender implements Runnable {
    private DataOutputStream out;
    private Scanner scanner;

    public MessageSender(Socket socket) throws IOException {
        this.out = new DataOutputStream(socket.getOutputStream());
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.print("Digite a mensagem: ");
                String message = scanner.nextLine();
                out.writeUTF(message); // Envia mensagem ao outro lado
                if (message.equalsIgnoreCase("sair")) {
                    break; // Encerra a conexão ao digitar "sair"
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }
}
