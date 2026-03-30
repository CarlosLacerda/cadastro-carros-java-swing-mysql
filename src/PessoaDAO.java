import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de banco
 * referentes à entidade Pessoa.
 *
 * SQL para criar a tabela:
 *   CREATE TABLE pessoa (
 *     cpf  VARCHAR(14)  PRIMARY KEY,
 *     nome VARCHAR(100) NOT NULL
 *   );
 */
public class PessoaDAO {

    // ── Salvar (INSERT) ──────────────────────────────────────────────────────
    public void salvar(Pessoa p) throws SQLException {
        String sql = "INSERT INTO pessoa (cpf, nome) VALUES (?, ?)";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, p.getCpf());
            ps.setString(2, p.getNome());
            ps.executeUpdate();
        }
    }

    // ── Alterar (UPDATE) ─────────────────────────────────────────────────────
    public boolean alterar(Pessoa p) throws SQLException {
        String sql = "UPDATE pessoa SET nome = ? WHERE cpf = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            return ps.executeUpdate() > 0;
        }
    }

    // ── Excluir (DELETE) ─────────────────────────────────────────────────────
    public boolean excluir(String cpf) throws SQLException {
        String sql = "DELETE FROM pessoa WHERE cpf = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Buscar por CPF ───────────────────────────────────────────────────────
    public Pessoa buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM pessoa WHERE cpf = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pessoa(rs.getString("cpf"), rs.getString("nome"));
                }
            }
        }
        return null;
    }

    // ── Listar todos ─────────────────────────────────────────────────────────
    public List<Pessoa> listarTodos() throws SQLException {
        List<Pessoa> lista = new ArrayList<>();
        String sql = "SELECT * FROM pessoa ORDER BY nome";
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pessoa(rs.getString("cpf"), rs.getString("nome")));
            }
        }
        return lista;
    }
}
