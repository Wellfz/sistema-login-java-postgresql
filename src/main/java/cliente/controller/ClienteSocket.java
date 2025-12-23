package cliente.controller;

import cliente.view.JanelaPrincipal;
import enti.Usuario;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClienteSocket {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final JanelaPrincipal view;

    public ClienteSocket(String host, int porta, JanelaPrincipal view) throws IOException {
        Socket socket = new Socket(host, porta);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.view = view;

        iniciarListener();
    }

    public void enviarRequisicao(String comando, Usuario usuario) {
        try {
            out.writeObject(comando);
            out.writeObject(usuario);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Erro ao enviar dados para o servidor: " + e.getMessage(),
                    "Erro de Rede",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarListener() {
        new Thread(() -> {
            try {
                while (true) {
                    Object objetoRecebido = in.readObject();

                    if (objetoRecebido instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Usuario> listaAtualizada = (List<Usuario>) objetoRecebido;

                        SwingUtilities.invokeLater(() -> {
                            view.atualizarTabela(listaAtualizada);
                        });
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(view,
                            "Conexão perdida com o servidor. O programa será fechado.",
                            "Erro de Rede",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                });
            }
        }).start();
    }

}