/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFormattedTextField;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author bruno
 */
public class Interface extends javax.swing.JFrame {

    private static TCPServer tcpserver;
    private static TCPClient tcpclient;
    private static UDPServer udpserver;
    private static UDPClient udpclient;

    private void sendMenssageUDPserver(String str) {
        udpserver.sendMessage(str);
    }

    private void sendMenssageUDPclient(String str) {
        udpclient.sendMessage(str);
    }

    private String recebeMensagemUDPclient() {
        String mensagem = null;
        do {
            mensagem = udpclient.mensagem;
        } while (mensagem == null);
        udpserver.mensagem = null;
        return mensagem;
    }

    private String recebeMensagemUDPserver() {
        String mensagem = null;
        do {
            mensagem = udpserver.mensagem;
        } while (mensagem == null);
        udpserver.mensagem = null;
        return mensagem;
    }

    private String recebeMensagemTCPclient() {
        String mensagem = null;
        do {
            mensagem = tcpclient.mensagem;
        } while (mensagem == null);
        tcpclient.mensagem = null;
        return mensagem;
    }

    private String recebeMensagemTCPserver() {
        String mensagem = null;
        do {
            mensagem = tcpserver.mensagem;
        } while (mensagem == null);
        tcpserver.mensagem = null;
        return mensagem;
    }

    private String recebeMensagem() {
        switch (tipoComunicacao) {
            case UDPcliente:
                return recebeMensagemUDPclient();
            case UDPserver:
                return recebeMensagemUDPserver();
            case TCPclient:
                return recebeMensagemTCPclient();
            case TCPserver:
                return recebeMensagemTCPserver();
        }
        return "";
    }

    private enum Comunicacao {
        TCPserver, TCPclient, UDPserver, UDPcliente;

        public Object get() {
            switch (this) {
                case TCPclient:
                    return tcpclient;
                case TCPserver:
                    return tcpserver;
                case UDPcliente:
                    return udpclient;
                case UDPserver:
                    return udpserver;
            }
            return null;
        }
    }

    private void reproduzirAudio(String caminho) {
        try {
            // Carrega o arquivo de ï¿½udio
            File arquivoAudio = new File(caminho);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivoAudio);

            // Configura o formato de ï¿½udio e o mixer
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Abre o clip e inicia a reproduï¿½ï¿½o
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();

            // Fecha o ï¿½udio apï¿½s a reproduï¿½ï¿½o completa
            audioClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    audioClip.close();
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
        }
    }

    private void imprimeTextoCentralizado(Graphics g, String texto, int x, int y) {
        if (g == null) {
            return;
        }

        // Calcula as dimensï¿½es do texto
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int larguraTexto = metrics.stringWidth(texto);
        int alturaTexto = metrics.getHeight();

        // Calcula a posiï¿½ï¿½o para centralizar o texto no ponto (x, y)
        int posX = x - (larguraTexto / 2);
        int posY = y + (alturaTexto / 4);

        // Desenha o texto
        g.drawString(texto, posX, posY);
        g.dispose(); // Libera o contexto grï¿½fico apï¿½s o uso
    }

    String[] minhasPosicoes;

    String[] posicoesA = {
        "2L60", // 2Q
        "2C53", // 2Q

        "3L00", // 3Q
        "3L32", // 3Q
        "3C17", // 3Q
        "3C79", // 3Q

        "5L91" // 5Q
    };

    String[] posicoesB = {
        "2L00", //2Q
        "2L08", //2Q

        "3L64", //3Q
        "3L04", //3Q
        "3C70", //3Q
        "3C78", //3Q

        "5L23" //5Q
    };

    //--------------  Opcoes de cores
    int VAZIO = 0;
    int NAVIO = 1;
    int ERRO = 2;
    int ACERTO = 3;
    int espaco = 250;
    //-----------------
    //110 225
    Quadrado[][] meuCampo;
    Quadrado[][] campoTiro;
    static int h = 10;  //Altura
    static int w = 10; //Largura
    int ladoQuad = 20;
    Color[] cor = {Color.WHITE, Color.gray, Color.BLUE, Color.red};
    String[] vetLetras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    Boolean acerto;
    int xMsgAcerto = 230, yMsgAcerto = 520;
    Comunicacao tipoComunicacao = Comunicacao.UDPserver; //LEANDER MUDE AQUI PRA UDPclient
    long enderecoIPlong;
    String enderecoIPString;
    int altura, largura;
    boolean transmissaoEscolhida = false;
    boolean meuTurno;

    private void inicializaNavios(Quadrado[][] campo, String[] vetPos) {
        minhasPosicoes = vetPos;
        for (int navioAt = 0; navioAt < vetPos.length; navioAt++) {
            String posicao = vetPos[navioAt];
            int quantQuads = posicao.charAt(0) - '0';
            int coordLin = posicao.charAt(2) - '0';
            int coordCol = posicao.charAt(3) - '0';
            if (posicao.charAt(1) == 'L') { //eh linha
                int limite = coordCol + quantQuads;
                for (int j = coordCol; j < limite; j++) {
                    campo[coordLin][j].cor = NAVIO;
                }
            } else {      //eh coluna
                int limite = coordLin + quantQuads;
                for (int i = coordLin; i < limite; i++) {
                    campo[i][coordCol].cor = NAVIO;
                }
            }
        }
    }

    private int[] verificaPosicaoTiro(String input) {
        int coluna = Integer.valueOf(input.substring(1, input.length()));  //pega o numero da coluna
        String letraLinha = "" + input.charAt(0);   //pega o char da linha
        int linha = -1;
        for (int i = 0; i < vetLetras.length; i++) {
            if (vetLetras[i].toLowerCase().equals(letraLinha.toLowerCase())) {
                linha = i;
            }
        }
        int[] ret = {linha, coluna};
        return ret;
    }

    private boolean verificaAcertoDoTiro(Quadrado[][] campo, int[] posTiro) {
        int linhaTiro = posTiro[0];
        int colunaTiro = posTiro[1];
        if (linhaTiro < 0 || linhaTiro > 9 || colunaTiro < 0 || colunaTiro > 9) {
            return false;
        }
        return verificaSeHaNavio(campo[linhaTiro][colunaTiro]);
    }

    private Quadrado verificaAcertoAdversario(String texto) {
        int[] vetPos = verificaPosicaoTiro(texto);
        acerto = verificaAcertoDoTiro(meuCampo, vetPos);
        int linha = vetPos[0], coluna = vetPos[1];
        if (linha >= 0 && linha < 10 && coluna >= 0 && coluna < 10) {
            return meuCampo[vetPos[0]][vetPos[1]];
        }
        return null;
    }

    private Quadrado verificaMeuAcerto(String texto) {
        sendMenssageUDPserver(texto);
        String mensagem = recebeMensagem();
        if (mensagem.equals("1")) {
            acerto = true;
        } else {
            acerto = false;
        }
        int[] vetPos = verificaPosicaoTiro(texto);
//        acerto = verificaAcertoDoTiro(campoTiro, vetPos);
        int linha = vetPos[0], coluna = vetPos[1];
        if (linha >= 0 && linha < 10 && coluna >= 0 && coluna < 10) {
            return campoTiro[vetPos[0]][vetPos[1]];
        }
        return null;
    }

    private boolean verificaSeHaNavio(Quadrado quad) {
        if (quad.cor == NAVIO) {  //se a cor eh cinza, eh navio (pode ser que de problema, por comparar cor)
            return true;
        }
        return false;
    }

    private boolean verificaAcertoPorCodigo(String input) {
        for (int i = 0; i < minhasPosicoes.length; i++) {
            String codPos = minhasPosicoes[i];
            int coordLinAT = codPos.charAt(2) - '0';
            int coordColAT = codPos.charAt(3) - '0';
            int[] pos = verificaPosicaoTiro(input);
            int coordLinIN = pos[0];
            int coordColIN = pos[1];
            if (coordLinIN == coordLinAT && coordColIN == coordColAT) {
                return true;    //linha e coluna iguais
            } else if (coordLinIN == coordLinAT) {     //linha igual
                int limite = coordColAT + (codPos.charAt(0) - '0');
                if (coordColIN >= coordColAT && coordColIN < limite) {
                    return true;
                }
            } else if (coordColIN == coordColAT) {     //coluna igual
                int limite = coordLinAT + (codPos.charAt(0) - '0');
                if (coordLinIN >= coordLinAT && coordLinIN < limite) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Interface criaInterface(UDPClient udpclient) {
        return new Interface(Comunicacao.UDPcliente, false);
    }

    public static Interface criaInterface(UDPServer udpserver) {
        return new Interface(Comunicacao.UDPserver, true);
    }

    public static Interface criaInterface(TCPClient tcpclient) {
        return new Interface(Comunicacao.TCPclient, false);
    }

    public static Interface criaInterface(TCPServer tcpserver) {
        return new Interface(Comunicacao.TCPserver, true);
    }

    //########################################
    //########################################
    //########################################
    /**
     * Creates new form Interface
     */
    private Interface(Comunicacao tipoComunicacao, boolean meuTurno) {
        
        this.meuTurno = meuTurno;
        this.tipoComunicacao = tipoComunicacao;
        altura = getHeight();
        largura = getWidth();
        initComponents();
        jLabel2.setText("Tipo de Conexão");
        jLabel1.setText("IP para conexão");
        jLabel3.setText("Batalha Naval Com Conexão TCP");
        // Centraliza a janela na tela
        this.setLocationRelativeTo(null);

        meuCampo = new Quadrado[h][w];
        campoTiro = new Quadrado[h][w];
        int xini = this.getWidth() - 450;
        xini /= 2;
        for (int i = 0; i < meuCampo.length; i++) {
            for (int j = 0; j < meuCampo[0].length; j++) {
                int xq = xini + (j) * ladoQuad;
                int yq = 280 + (i) * ladoQuad;
                meuCampo[i][j] = new Quadrado(xq, yq, 0);
            }
        }
        xini += 250;
        for (int i = 0; i < campoTiro.length; i++) {
            for (int j = 0; j < campoTiro[0].length; j++) {
                int xq = xini + (j) * ladoQuad;
                int yq = 280 + (i) * ladoQuad;
                campoTiro[i][j] = new Quadrado(xq, yq, 0);
            }
        }

        switch (this.tipoComunicacao) {
            case TCPclient:
            case UDPcliente:
                inicializaNavios(meuCampo, posicoesA);
                break;
            case TCPserver:
            case UDPserver:
                inicializaNavios(meuCampo, posicoesB);
                break;
        }

        this.setTitle("Batalha Naval");

        ActionListener acao = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        };
        
        ActionListener acao2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        };

// Adiciona o listener ao botï¿½o e ao campo de texto
        jFormattedTextField1.addActionListener(acao); // Ao pressionar Enter no campo de texto
        jFormattedTextField1.addActionListener(acao2); // Ao pressionar Enter no campo de texto

        // Adiciona ActionListener para os RadioButtons
        jRadioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jRadioButton2ActionPerformed(e);  // Chama a funï¿½ï¿½o correspondente
            }
        });
        jRadioButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jRadioButton3ActionPerformed(e);  // Chama a funï¿½ï¿½o correspondente
            }
        });

        try {
            jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###.###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

// Adiciona o ActionListener
        jFormattedTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField2ActionPerformed(evt);
            }
        });

// Adiciona o KeyListener para validaï¿½ï¿½o
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String texto = jFormattedTextField2.getText();
                // Remove espaï¿½os e formata os octetos
                texto = texto.replace(" ", "");
                String[] octets = texto.split("\\.");

                // Verifica cada octeto
                for (int i = 0; i < octets.length; i++) {
                    if (!octets[i].isEmpty()) {
                        try {
                            int value = Integer.parseInt(octets[i]);
                            if (value < 0 || value > 255) {
                                // Se o octeto for invï¿½lido, exibe uma mensagem
                                JOptionPane.showMessageDialog(null, "Cada octeto deve ser entre 0 e 255.");
                                // Reinicia o campo ou reverte a ï¿½ltima entrada vï¿½lida
                                int numDig = texto.length();
                                System.out.println(texto);
                                System.out.println("Tam: " + numDig);
                                numDig -= 3;
                                System.out.println("tamD: " + numDig);
                                int retira = 3;
                                String novoTexto = texto;
                                System.out.println("Ret: " + retira);
                                do {
                                    if (novoTexto.charAt(novoTexto.length() - 1) != '.') {
                                        retira--;
                                    }
                                    novoTexto = novoTexto.substring(0, novoTexto.length() - 1);
                                } while (retira > 0);
                                jFormattedTextField2.setText(novoTexto); // Limpa o campo ou pode colocar um valor anterior vï¿½lido;
                                break;
                            }
                        } catch (NumberFormatException e) {
                            // Se ocorrer um erro de formato, nï¿½o faz nada
                            break;
                        }
                    }
                }
            }
        });

        setResizable(false);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        DesenhaQuadrados(g);
        g.setFont(new Font("Arial", Font.BOLD, 18)); // Define a fonte como Arial, negrito, tamanho 24
        if (acerto == null) {
            return;
        }
        if (acerto) {
//            g.drawString("Acertou, atire novamente!", xMsgAcerto - 50, yMsgAcerto);
            imprimeTextoCentralizado(g, "Acertou, atire novamente!", this.getWidth() / 2, 515);
        } else if (acerto == false) {
//            g.drawString("Errou, perder a vez!", xMsgAcerto - 40, yMsgAcerto);
            imprimeTextoCentralizado(g, "Errou, perdeu a vez!", this.getWidth() / 2, 515);
        }
    }

    private void DesenhaQuadrados(Graphics g) {
        Random rdm = new Random();

        g.drawString("Meu Campo", meuCampo[9][0].x, meuCampo[9][0].y + 35);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Quadrado quad = meuCampo[i][j];
                if (j == 0) {
                    g.drawString(vetLetras[i], quad.x - 15, quad.y + 15);
                }
                if (i == 0) {
                    g.drawString(Integer.toString(j), quad.x + 5, quad.y - 5);
                }
                g.setColor(cor[quad.cor]);
                int vx[] = {
                    quad.x,
                    quad.x,
                    quad.x + ladoQuad,
                    quad.x + ladoQuad};
                int vy[] = {
                    quad.y,
                    quad.y + ladoQuad,
                    quad.y + ladoQuad,
                    quad.y};
                g.fillPolygon(vx, vy, 4);
                g.setColor(Color.black);
                g.drawPolygon(vx, vy, 4);
            }
        }
        g.drawString("Campo Inimigo", campoTiro[9][0].x, campoTiro[9][0].y + 35);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Quadrado quad = campoTiro[i][j];
                if (j == 0) {
                    g.drawString(vetLetras[i], quad.x - 15, quad.y + 15);
                }
                if (i == 0) {
                    g.drawString(Integer.toString(j), quad.x + 5, quad.y - 5);
                }
                g.setColor(cor[quad.cor]);
                int vx[] = {
                    quad.x,
                    quad.x,
                    quad.x + ladoQuad,
                    quad.x + ladoQuad};
                int vy[] = {
                    quad.y,
                    quad.y + ladoQuad,
                    quad.y + ladoQuad,
                    quad.y};
                g.fillPolygon(vx, vy, 4);
                g.setColor(Color.black);
                g.drawPolygon(vx, vy, 4);
            }
        }
        Scanner sc = new Scanner(System.in);
        String[][] m = new String[h][w];
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long ipToLong(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        if (octets.length != 4) {
            throw new IllegalArgumentException("Endereï¿½o IP invï¿½lido: " + ipAddress);
        }
        long result = 0;
        for (int i = 0; i < 4; i++) {
            int octet = Integer.parseInt(octets[i]);
            if (octet < 0 || octet > 255) {
                System.out.println("Octeto fora do intervalo: " + octet);
                return -1;
            }
            result = (result << 8) | octet; // Desloca ï¿½ esquerda e adiciona o octeto
        }
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("TCP");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("UDP");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jLabel2.setText("Tipo de Conexï¿½o");

        jLabel1.setText("IP para conexï¿½o");

        jLabel3.setText("Batalha Naval Com Conexï¿½o TCP");

        jLabel4.setText("Coordenada (Ex: A5)");

        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Salvar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("A#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        jFormattedTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(209, 209, 209))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(filler2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jRadioButton2))
                                .addGap(76, 76, 76)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2))
                                    .addComponent(jLabel1))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel3)
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addGap(8, 8, 8)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(248, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        if (transmissaoEscolhida == false) {
            tipoComunicacao = Comunicacao.UDPserver; //LEANDER MUDE AQUI PARA UDPclient
        }
        jLabel3.setText("Batalha Naval Com Conexï¿½o UDP");
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //SALVAR
        String ip = jButton2.getText();
        switch (tipoComunicacao) {
            case UDPcliente:
                udpclient = new UDPClient(ip);
                break;
            case UDPserver:
                udpserver = new UDPServer();
                break;
            case TCPclient:
                tcpclient = new TCPClient(ip);
                break;
            case TCPserver:
                tcpserver = new TCPServer();
                break;
                
            default:
                throw new AssertionError();
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (meuTurno) {
            String texto = jFormattedTextField1.getText();
            Quadrado quadTiro = null;
            if (verificaAcertoPorCodigo(texto)) {                 //Acerto
                if (quadTiro != null) {
                    if (quadTiro.cor != ACERTO) {
                        reproduzirAudio("src/acerto.wav"); // Substitua com o caminho do seu arquivo .wav
                    } else {
                        acerto = false;
                        reproduzirAudio("src/erro.wav"); // Substitua com o caminho do seu arquivo .wav
                    }
                    quadTiro.cor = ACERTO;
                }
            } else {                      //Erro
                if (quadTiro != null && quadTiro.cor != ERRO) {
                    quadTiro.cor = ERRO;
                    meuTurno = false;
                    sendMenssageUDPserver("perdi");
                    reproduzirAudio("src/erro.wav"); // Substitua com o caminho do seu arquivo .wav
                }
            }
            this.repaint();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        if (transmissaoEscolhida == false) {
            tipoComunicacao = Comunicacao.TCPserver; //LEANDER MUDE AQUI PARA TCPclient
        }
        jLabel3.setText("Batalha Naval Com Conexï¿½o TCP");
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jFormattedTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField2ActionPerformed
        // TODO add your handling code here:
        enderecoIPString = jFormattedTextField2.getText();
        enderecoIPlong = ipToLong(enderecoIPString);
        System.out.println("IP long: " + enderecoIPlong);
        System.out.println("IP string: " + enderecoIPString);
    }//GEN-LAST:event_jFormattedTextField2ActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Interface().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    // End of variables declaration//GEN-END:variables
}
