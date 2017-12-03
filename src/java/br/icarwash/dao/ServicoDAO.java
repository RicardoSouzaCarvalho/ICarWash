package br.icarwash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.icarwash.model.Servico;
import br.icarwash.model.Servico.ServicoBuilder;
import java.sql.Statement;
import java.util.ArrayList;

public class ServicoDAO {

    private final Connection conexao;
    private static final String INSERT = "insert into servico(nome, descricao, valor, ativo) values(?, ?, ?, ?)";
    private static final String SELECT_ALL = "select * from servico";
    private static final String UPDATE = "update servico set nome = ?, descricao = ?, valor = ? WHERE id = ?";
    private static final String INACTIVE_BY_ID = "UPDATE servico SET ativo=0 where id=?";
    private static final String ACTIVE_BY_ID = "UPDATE servico SET ativo=1 where id=?";
    private static final String SELECT_BY_ID = "select id, nome, descricao, valor, ativo from servico where id = ?";

    public ServicoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public Servico cadastrar(Servico servico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, servico.getNome());
            pstmt.setString(2, servico.getDescricao());
            pstmt.setBigDecimal(3, servico.getValor());
            pstmt.setBoolean(4, servico.isAtivo());

            pstmt.execute();

            final ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                servico.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return servico;
    }

    public ArrayList<Servico> listar() {
        ArrayList<Servico> servicos = new ArrayList();
        Servico servico;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_ALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                servico = new ServicoBuilder()
                        .withId(rs.getInt("id"))
                        .withNome(rs.getString("nome"))
                        .withDescricao(rs.getString("descricao"))
                        .withValor(rs.getBigDecimal("valor"))
                        .withAtivo(rs.getBoolean("ativo"))
                        .build();
                servicos.add(servico);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return servicos;
    }

    public Servico localizarPorId(Servico servico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_BY_ID);
            pstmt.setString(1, Integer.toString(servico.getId()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                servico = new ServicoBuilder()
                        .withId(rs.getInt("id"))
                        .withNome(rs.getString("nome"))
                        .withDescricao(rs.getString("descricao"))
                        .withValor(rs.getBigDecimal("valor"))
                        .withAtivo(rs.getBoolean("ativo"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return servico;
    }

    public void atualizar(Servico servico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(UPDATE);
            pstmt.setString(1, servico.getNome());
            pstmt.setString(2, servico.getDescricao());
            pstmt.setBigDecimal(3, servico.getValor());
            pstmt.setInt(4, servico.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void inativar(Servico servico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INACTIVE_BY_ID);
            pstmt.setInt(1, servico.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ativar(Servico servico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(ACTIVE_BY_ID);
            pstmt.setInt(1, servico.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
