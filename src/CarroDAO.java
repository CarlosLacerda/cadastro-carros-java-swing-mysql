import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de banco
 * referentes à entidade Carro.
 *
 * SQL para criar a tabela:
 *   CREATE TABLE carro (
 *     placa      VARCHAR(10)  PRIMARY KEY,
 *     cor        VARCHAR(50),
 *     modelo     VARCHAR(100),
 *     ano        INT,
 *     cpf_pessoa VARCHAR(14),
 *     FOREIGN KEY (cpf_pessoa) REFERENCES pessoa(cpf)
 *   );
 */
public class CarroDAO {

    // ── Salvar (INSERT) ──────────────────────────────────────────────────────
    public void salvar(Carro c) throws SQLException {
        String sql = "INSERT INTO carro (placa, cor, modelo, ano, cpf_pessoa) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, c.getPlaca());
            ps.setString(2, c.getCor());
            ps.setString(3, c.getModelo());
            ps.setInt   (4, c.getAno());
            ps.setString(5, c.getCpfPessoa());
            ps.executeUpdate();
        }
    }

    // ── Alterar (UPDATE) ─────────────────────────────────────────────────────
    public boolean alterar(Carro c) throws SQLException {
        String sql = "UPDATE carro SET cor = ?, modelo = ?, ano = ? WHERE placa = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, c.getCor());
            ps.setString(2, c.getModelo());
            ps.setInt   (3, c.getAno());
            ps.setString(4, c.getPlaca());
            return ps.executeUpdate() > 0;
        }
    }

    // ── Excluir (DELETE) ─────────────────────────────────────────────────────
    public boolean excluir(String placa) throws SQLException {
        String sql = "DELETE FROM carro WHERE placa = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, placa);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Buscar por Placa ─────────────────────────────────────────────────────
    public Carro buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM carro WHERE placa = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Carro(
                            rs.getString("placa"),
                            rs.getString("cor"),
                            rs.getString("modelo"),
                            rs.getInt   ("ano"),
                            rs.getString("cpf_pessoa")
                    );
                }
            }
        }
        return null;
    }

    // ── Listar todos (JOIN com Pessoa) ───────────────────────────────────────
    /**
     * Retorna uma lista de arrays de Object com os dados combinados:
     * [cpf, nome, placa, cor, modelo, ano]
     * Usado para preencher a JTable na tela.
     */
    public List<Object[]> listarComPessoa() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.cpf, p.nome, c.placa, c.cor, c.modelo, c.ano "
                   + "FROM pessoa p "
                   + "LEFT JOIN carro c ON p.cpf = c.cpf_pessoa "
                   + "ORDER BY p.nome";
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("placa"),
                        rs.getString("cor"),
                        rs.getString("modelo"),
                        rs.getInt   ("ano")
                });
            }
        }
        return lista;
    }
}
