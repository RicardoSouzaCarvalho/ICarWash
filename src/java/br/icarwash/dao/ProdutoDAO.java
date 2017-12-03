package br.icarwash.dao;

import br.icarwash.model.Produto;
import br.icarwash.model.Produto.ProdutoBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoDAO {

    private final Connection conexao;
    private static final String INSERT = "insert into produto(nome, descricao, ativo) values(?, ?, ?)";
    private static final String SELECT_ALL = "select * from produto";
    private static final String UPDATE = "update produto set nome = ?, descricao = ? WHERE id = ?";
    private static final String INACTIVE_BY_ID = "UPDATE produto SET ativo=0 where id=?";
    private static final String ACTIVE_BY_ID = "UPDATE produto SET ativo=1 where id=?";
    private static final String SELECT_BY_ID = "select * from produto where id = ?";

    public ProdutoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrar(Produto produto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INSERT);
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBoolean(3, produto.isAtivo());

            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList();
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_ALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ProdutoBuilder produtoBuilder = new Produto.ProdutoBuilder()
                        .withId(rs.getInt("id"))
                        .withNome(rs.getString("nome"))
                        .withDescricao(rs.getString("descricao"))
                        .withAtivo(rs.getBoolean("ativo"));

                produtos.add(produtoBuilder.build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public Produto localizarPorId(Produto produto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_BY_ID);
            pstmt.setString(1, Integer.toString(produto.getId()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                produto = new Produto.ProdutoBuilder()
                        .withId(rs.getInt("id"))
                        .withNome(rs.getString("nome"))
                        .withDescricao(rs.getString("descricao"))
                        .withAtivo(rs.getBoolean("ativo"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produto;
    }

    public void atualizar(Produto produto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(UPDATE);
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setInt(3, produto.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void inativar(Produto produto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INACTIVE_BY_ID);
            pstmt.setInt(1, produto.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ativar(Produto produto) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(ACTIVE_BY_ID);
            pstmt.setInt(1, produto.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
