package servidor;

import enti.Usuario;
import servidor.controller.TratadorCliente;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Servidor {
    private static final List<ObjectOutputStream> clientesConectados = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Servidor iniciado na porta 12345...");

        while (true) {
            Socket clienteSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado: " + clienteSocket.getInetAddress());

            ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
            clientesConectados.add(out);

            TratadorCliente tratador = new TratadorCliente(clienteSocket, out);
            new Thread(tratador).start();
        }
    }

    public static void broadcast(List<Usuario> listaAtualizada) {
        System.out.println("Enviando broadcast para " + clientesConectados.size() + " clientes.");
        for (ObjectOutputStream out : clientesConectados) {
            try {
                out.writeObject(listaAtualizada);
                out.reset();
            } catch (IOException e) {
                System.out.println("Erro ao enviar broadcast: " + e.getMessage());
                clientesConectados.remove(out);
            }
        }
    }

    public static void removerCliente(ObjectOutputStream out) {
        clientesConectados.remove(out);
        System.out.println("Cliente desconectado. Clientes restantes: " + clientesConectados.size());
    }
}