package cliente;

import cliente.view.JanelaPrincipal;
import javax.swing.SwingUtilities;

public class Cliente {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JanelaPrincipal janela = new JanelaPrincipal();

                boolean conectou = janela.conectar();

                if (conectou) {
                    janela.setVisible(true);
                } else {
                    System.exit(1);
                }
            }
        });
    }
}