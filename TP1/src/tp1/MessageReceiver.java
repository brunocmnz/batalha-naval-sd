
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

class MessageReceiver implements Runnable {
    private DataInputStream in;

    public MessageReceiver(Socket socket) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readUTF(); // Recebe mensagem do outro lado
                if (message.equalsIgnoreCase("sair")) {
                    System.out.println("Conexão encerrada pelo outro usuário.");
                    break;
                }
                System.out.println("Mensagem recebida: " + message);
            }
        } catch (IOException e) {
            System.out.println("Erro ao receber mensagem: " + e.getMessage());
        }
    }
}
