package servidor.controller;

import enti.Usuario;
import servidor.Servidor;
import servidor.model.UsuarioDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class TratadorCliente implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final UsuarioDAO dao;

    public TratadorCliente(Socket socket, ObjectOutputStream out) throws IOException {
        this.socket = socket;
        this.out = out;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.dao = new UsuarioDAO();
    }


    @Override
    public void run() {
        try {
            enviarListaAtualizada();

            while (true) {
                String comando = (String) in.readObject();
                if (comando.equals("LISTAR")) {
                    enviarListaAtualizada();
                    continue;
                }

                Usuario usuario = (Usuario) in.readObject();

                System.out.println("Comando recebido: " + comando);
                switch (comando) {
                    case "INCLUIR":
                        dao.incluir(usuario);
                        break;
                    case "ALTERAR":
                        dao.alterar(usuario);
                        break;
                    case "EXCLUIR":
                        dao.excluir(usuario.getId());
                        break;
                }

                List<Usuario> listaAtualizada = dao.listar();
                Servidor.broadcast(listaAtualizada);
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Cliente desconectado: " + socket.getInetAddress() + " | Erro: " + e.getMessage());
        } finally {
            Servidor.removerCliente(out);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarListaAtualizada() {
        try {
            List<Usuario> listaInicial = dao.listar();
            out.writeObject(listaInicial);
            out.reset();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}