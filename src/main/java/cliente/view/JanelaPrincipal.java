package cliente.view;

import cliente.controller.ClienteSocket;
import enti.Usuario;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JanelaPrincipal extends JFrame {

    private final DefaultTableModel modeloTabela;
    private final JTable tabela;
    private final JTextField txtNome;
    private final JTextField txtEmail;

    private final JButton btnAdicionar;
    private final JButton btnAlterar;
    private final JButton btnExcluir;
    private final JButton btnLimpar;

    private ClienteSocket clienteSocket;

    private int idSelecionado = -1;

    public JanelaPrincipal() {
        setTitle("Cadastro de Usuários (Cliente)");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        painelForm.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtEmail = new JTextField(20);
        painelForm.add(txtEmail, gbc);

        JPanel painelBotoes = new JPanel();
        btnAdicionar = new JButton("Adicionar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        String[] colunas = {"ID", "Nome", "Email"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelForm, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                    int linhaSelecionada = tabela.getSelectedRow();

                    idSelecionado = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
                    String nome = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
                    String email = (String) modeloTabela.getValueAt(linhaSelecionada, 2);

                    txtNome.setText(nome);
                    txtEmail.setText(email);

                    btnAdicionar.setEnabled(false);
                    btnAlterar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                }
            }
        });

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos()) {
                    Usuario u = new Usuario();
                    u.setNome(txtNome.getText());
                    u.setEmail(txtEmail.getText());

                    clienteSocket.enviarRequisicao("INCLUIR", u);
                    limparCampos();
                }
            }
        });

        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos() && idSelecionado != -1) {
                    Usuario u = new Usuario();
                    u.setId(idSelecionado);
                    u.setNome(txtNome.getText());
                    u.setEmail(txtEmail.getText());

                    clienteSocket.enviarRequisicao("ALTERAR", u);
                    limparCampos();
                }
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado != -1) {

                    int resposta = JOptionPane.showConfirmDialog(
                            JanelaPrincipal.this,
                            "Tem certeza que deseja excluir este usuário?",
                            "Confirmação de Exclusão",
                            JOptionPane.YES_NO_OPTION);

                    if (resposta == JOptionPane.YES_OPTION) {
                        Usuario u = new Usuario();
                        u.setId(idSelecionado);

                        clienteSocket.enviarRequisicao("EXCLUIR", u);
                        limparCampos();
                    }
                }
            }
        });

        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        idSelecionado = -1;
        tabela.clearSelection();

        btnAdicionar.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Email são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean conectar() {
        try {
            this.clienteSocket = new ClienteSocket("localhost", 12345, this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível conectar ao servidor.\nVerifique se o servidor está rodando.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void atualizarTabela(List<Usuario> lista) {
        modeloTabela.setRowCount(0);
        if (lista != null) {
            for (Usuario u : lista) {
                modeloTabela.addRow(new Object[]{
                        u.getId(),
                        u.getNome(),
                        u.getEmail()
                });
            }
        }
    }
}