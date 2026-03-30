import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão com o banco de dados MySQL.
 * Ajuste as constantes URL, USUARIO e SENHA conforme seu ambiente.
 */
public class ConexaoBD {

	private static final String URL     = "jdbc:mysql://localhost:3306/SEU_BANCO";
	private static final String USUARIO = "SEU_USUARIO";
	private static final String SENHA   = "SUA_SENHA";

    private static Connection conexao;

    /** Retorna (ou abre) a conexão singleton com o banco. */
    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
            	Class.forName("com.mysql.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado: " + e.getMessage());
            }
        }
        return conexao;
    }

    /** Fecha a conexão caso esteja aberta. */
    public static void fechar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
