import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

/**
 * Tela principal do sistema de Cadastro de Carros e Pessoas.
 * Responsável apenas pela interface gráfica (View).
 * A lógica de banco fica nos DAOs; as entidades nos modelos.
 */
public class TelaCadastro extends JFrame {

    // ── Campos do formulário ─────────────────────────────────────────────────
    private JTextField txtCpf, txtNome, txtPlaca, txtCor, txtModelo, txtAno;

    // ── Botões ───────────────────────────────────────────────────────────────
    private JButton btnSalvar, btnAlterar, btnExcluir, btnConsultar, btnLimpar;

    // ── Tabela ───────────────────────────────────────────────────────────────
    private JTable           tabela;
    private DefaultTableModel modeloTabela;

    // ── DAOs ─────────────────────────────────────────────────────────────────
    private final PessoaDAO pessoaDAO = new PessoaDAO();
    private final CarroDAO  carroDAO  = new CarroDAO();

    // ========================================================================
    //  CONSTRUTOR
    // ========================================================================
    public TelaCadastro() {
        setTitle("Cadastro de Carros e Pessoas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        conectarECarregar();
    }

    // ========================================================================
    //  INICIALIZAÇÃO
    // ========================================================================
    private void inicializarComponentes() {
        txtCpf    = criarCampo(14);
        txtNome   = criarCampo(20);
        txtPlaca  = criarCampo(10);
        txtCor    = criarCampo(20);
        txtModelo = criarCampo(20);
        txtAno    = criarCampo(6);

        btnSalvar    = criarBotao("Salvar",    new Color(59, 130, 246));
        btnAlterar   = criarBotao("Alterar",   new Color(245, 158, 11));
        btnExcluir   = criarBotao("Excluir",   new Color(239, 68, 68));
        btnConsultar = criarBotao("Consultar", new Color(16, 185, 129));
        btnLimpar    = criarBotao("Limpar",    new Color(107, 114, 128));

        String[] colunas = {"CPF", "Nome", "Placa", "Cor", "Modelo", "Ano"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setGridColor(new Color(220, 220, 220));
        tabela.setSelectionBackground(new Color(219, 234, 254));
        tabela.setSelectionForeground(Color.BLACK);
    }

    // ========================================================================
    //  LAYOUT
    // ========================================================================
    private void configurarLayout() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        painelPrincipal.setBackground(new Color(245, 247, 250));

        // ── Formulário ────────────────────────────────────────────────────
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(Color.WHITE);
        painelForm.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 215, 225), 1, true),
                new EmptyBorder(15, 20, 15, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);

        JLabel titulo = new JLabel("Dados do Cadastro");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(30, 58, 138));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        painelForm.add(titulo, gbc);
        gbc.gridwidth = 1;

        addCampo(painelForm, gbc, "CPF:",    txtCpf,    0, 1, labelFont);
        addCampo(painelForm, gbc, "Nome:",   txtNome,   2, 1, labelFont);
        addCampo(painelForm, gbc, "Placa:",  txtPlaca,  0, 2, labelFont);
        addCampo(painelForm, gbc, "Cor:",    txtCor,    2, 2, labelFont);
        addCampo(painelForm, gbc, "Modelo:", txtModelo, 0, 3, labelFont);
        addCampo(painelForm, gbc, "Ano:",    txtAno,    2, 3, labelFont);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        painelBotoes.setBackground(Color.WHITE);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnConsultar);
        painelBotoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        gbc.insets = new Insets(12, 6, 4, 6);
        painelForm.add(painelBotoes, gbc);

        // ── Tabela ────────────────────────────────────────────────────────
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(
                        new LineBorder(new Color(210, 215, 225), 1, true),
                        " Registros Cadastrados ",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13),
                        new Color(30, 58, 138)),
                new EmptyBorder(5, 5, 5, 5)));
        scroll.setPreferredSize(new Dimension(700, 250));

        painelPrincipal.add(painelForm, BorderLayout.NORTH);
        painelPrincipal.add(scroll,     BorderLayout.CENTER);
        add(painelPrincipal);
    }

    private void addCampo(JPanel p, GridBagConstraints gbc, String texto,
                          JTextField campo, int x, int y, Font f) {
        gbc.gridx = x;     gbc.gridy = y; gbc.weightx = 0;
        p.add(new JLabel(texto) {{ setFont(f); }}, gbc);
        gbc.gridx = x + 1; gbc.weightx = 1;
        p.add(campo, gbc);
    }

    // ========================================================================
    //  EVENTOS
    // ========================================================================
    private void configurarEventos() {

        // ── Salvar ────────────────────────────────────────────────────────
        btnSalvar.addActionListener(e -> {
            if (!validarCampos()) return;
            try {
                Pessoa pessoa = new Pessoa(
                        txtCpf.getText().trim(),
                        txtNome.getText().trim());

                Carro carro = new Carro(
                        txtPlaca.getText().trim(),
                        txtCor.getText().trim(),
                        txtModelo.getText().trim(),
                        Integer.parseInt(txtAno.getText().trim()),
                        txtCpf.getText().trim());

                pessoaDAO.salvar(pessoa);
                carroDAO.salvar(carro);

                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                recarregarTabela();

            } catch (SQLException ex) {
                mostrarErro("Erro ao salvar: " + ex.getMessage());
            }
        });

        // ── Alterar ───────────────────────────────────────────────────────
        btnAlterar.addActionListener(e -> {
            if (!validarCampos()) return;
            try {
                Pessoa pessoa = new Pessoa(
                        txtCpf.getText().trim(),
                        txtNome.getText().trim());

                Carro carro = new Carro(
                        txtPlaca.getText().trim(),
                        txtCor.getText().trim(),
                        txtModelo.getText().trim(),
                        Integer.parseInt(txtAno.getText().trim()),
                        txtCpf.getText().trim());

                boolean okP = pessoaDAO.alterar(pessoa);
                boolean okC = carroDAO.alterar(carro);

                if (okP || okC) {
                    JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    recarregarTabela();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Nenhum registro encontrado com esse CPF/Placa.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                mostrarErro("Erro ao alterar: " + ex.getMessage());
            }
        });

        // ── Excluir ───────────────────────────────────────────────────────
        btnExcluir.addActionListener(e -> {
            String placa = txtPlaca.getText().trim();
            String cpf   = txtCpf.getText().trim();
            if (placa.isEmpty() && cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Informe ao menos a Placa ou o CPF para excluir.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int conf = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir este registro?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (conf != JOptionPane.YES_OPTION) return;

            try {
                if (!placa.isEmpty()) carroDAO.excluir(placa);
                if (!cpf.isEmpty())   pessoaDAO.excluir(cpf);

                JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                recarregarTabela();

            } catch (SQLException ex) {
                mostrarErro("Erro ao excluir: " + ex.getMessage());
            }
        });

        // ── Consultar / Limpar ────────────────────────────────────────────
        btnConsultar.addActionListener(e -> recarregarTabela());
        btnLimpar.addActionListener(e -> limparCampos());

        // ── Clique na linha da tabela preenche o formulário ───────────────
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha < 0) return;
                txtCpf.setText   ((String) modeloTabela.getValueAt(linha, 0));
                txtNome.setText  ((String) modeloTabela.getValueAt(linha, 1));
                txtPlaca.setText ((String) modeloTabela.getValueAt(linha, 2));
                txtCor.setText   ((String) modeloTabela.getValueAt(linha, 3));
                txtModelo.setText((String) modeloTabela.getValueAt(linha, 4));
                txtAno.setText   (String.valueOf(modeloTabela.getValueAt(linha, 5)));
            }
        });
    }

    // ========================================================================
    //  AUXILIARES
    // ========================================================================
    private void conectarECarregar() {
        try {
            ConexaoBD.getConexao();
            recarregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Banco de dados não encontrado.\n"
                  + "A interface funcionará em modo demonstração.\n\n"
                  + "Configure ConexaoBD.java com seus dados.",
                    "Aviso de Conexão", JOptionPane.WARNING_MESSAGE);
            adicionarDemostracao();
        }
    }

    private void recarregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Object[]> lista = carroDAO.listarComPessoa();
            for (Object[] linha : lista) {
                modeloTabela.addRow(linha);
            }
        } catch (SQLException ex) {
            mostrarErro("Erro ao consultar: " + ex.getMessage());
        }
    }

    private boolean validarCampos() {
        if (txtCpf.getText().trim().isEmpty()    ||
            txtNome.getText().trim().isEmpty()   ||
            txtPlaca.getText().trim().isEmpty()  ||
            txtCor.getText().trim().isEmpty()    ||
            txtModelo.getText().trim().isEmpty() ||
            txtAno.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos antes de continuar.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtAno.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "O campo 'Ano' deve conter apenas números.",
                    "Formato inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limparCampos() {
        txtCpf.setText("");    txtNome.setText("");
        txtPlaca.setText("");  txtCor.setText("");
        txtModelo.setText(""); txtAno.setText("");
        tabela.clearSelection();
        txtCpf.requestFocus();
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void adicionarDemostracao() {
        modeloTabela.addRow(new Object[]{"123.456.789-00", "João Silva",   "ABC-1234", "Prata",  "Civic",   2022});
        modeloTabela.addRow(new Object[]{"987.654.321-00", "Maria Santos", "XYZ-5678", "Branco", "Corolla", 2021});
        modeloTabela.addRow(new Object[]{"111.222.333-44", "Pedro Lima",   "DEF-9012", "Preto",  "HB20",    2023});
    }

    private JTextField criarCampo(int colunas) {
        JTextField f = new JTextField(colunas);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(180, 30));
        return f;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 34));
        Color escura = cor.darker();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(escura); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(cor);    }
        });
        return btn;
    }

    // ========================================================================
    //  MAIN
    // ========================================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new TelaCadastro().setVisible(true));
    }
}
