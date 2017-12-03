package br.icarwash.dao;

import br.icarwash.model.Produto;
import br.icarwash.model.Servico;
import br.icarwash.model.ServicoProduto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServicoProdutoDAO {

    private final Connection conexao;
    private static final String INSERT = "insert into servico_produtos(id_servico, id_produto, quantidade) values (?,?,?)";
    private static final String UPDATE_QTD = "update servico_produtos set quantidade = ? where id_servico = ? and id_produto = ?";
    private static final String DELETE_BY_IDS = "DELETE FROM servico_produtos where id_servico = ? and id_produto = ?";
    private static final String SELECT_PRODUTO_BY_ID_SERVICO = "select * from servico_produtos where id_servico = ?";

    public ServicoProdutoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastraServicoProduto(ServicoProduto servicoProduto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INSERT);
            pstmt.setInt(1, servicoProduto.getServico().getId());
            pstmt.setInt(2, servicoProduto.getProduto().getId());
            pstmt.setInt(3, servicoProduto.getQuantidade());
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizarServicoProduto(ServicoProduto servicoProduto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(UPDATE_QTD);
            pstmt.setInt(1, servicoProduto.getQuantidade());
            pstmt.setInt(2, servicoProduto.getServico().getId());
            pstmt.setInt(3, servicoProduto.getProduto().getId());
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluirServicoProduto(ServicoProduto servicoProduto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(DELETE_BY_IDS);
            pstmt.setInt(1, servicoProduto.getServico().getId());
            pstmt.setInt(2, servicoProduto.getProduto().getId());
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ServicoProduto> selecionaProdutosPorIdServico(ServicoProduto servicoProduto) {

        ArrayList<ServicoProduto> servicosProdutos = new ArrayList<>();

        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_PRODUTO_BY_ID_SERVICO);
            pstmt.setInt(1, servicoProduto.getServico().getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Produto produto = new Produto.ProdutoBuilder()
                        .withId(rs.getInt("id_produto"))
                        .build();

                servicoProduto.setProduto(produto);
                servicoProduto.setQuantidade(rs.getInt("quantidade"));
                
                servicosProdutos.add(servicoProduto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return servicosProdutos;

    }

}
