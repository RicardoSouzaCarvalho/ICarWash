package br.icarwash.dao;

import br.icarwash.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServicoProdutoDAO {

    private final Connection conexao;
    private boolean fechaConexao = false;

    public ServicoProdutoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public ServicoProdutoDAO() {
        this.conexao = Conexao.getConexao();
        fechaConexao = true;
    }

    public void cadastraServicoProduto(int idServico, int idProduto, int quantidade) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement("insert into servico_produtos(id_servico, id_produto, quantidade) values (?,?,?)");
            pstmt.setInt(1, idServico);
            pstmt.setInt(2, idProduto);
            pstmt.setInt(3, quantidade);
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
    }

    private void fechaConexao() throws RuntimeException {
        if (fechaConexao) {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
