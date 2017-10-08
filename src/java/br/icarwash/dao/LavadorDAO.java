/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.icarwash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import br.icarwash.model.Cliente;
import br.icarwash.model.Endereco;
import br.icarwash.model.Lavador;
import br.icarwash.util.Conexao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author rezen
 */
public class LavadorDAO implements BasicoDAO {

    private boolean fechaConexao = false;
    private Connection conexao;
    private static final String INSERT = "insert into lavador(dt_contrato,email, nome, telefone, dt_nascimento, CPF, CEP, estado, cidade, bairro, endereco, numero) values(?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SELECT_ALL = "select l.dt_contrato, l.id, l.email, l.nome, l.telefone, l.dt_nascimento, l.cpf, l.cep, l.estado, l.cidade, l.bairro, l.endereco, l.numero, u.usuario, u.ativo from Lavador l, usuario u, lavador_usuario lu where l.id = lu.id_lavador and u.id = lu.id_usuario and u.ativo = 1";
    private static final String UPDATE = "update lavador set email = ?, nome = ?, telefone = ?, dt_nascimento = ?, cep = ?, estado = ?, cidade = ?, bairro = ?, endereco = ?, numero = ? WHERE id = ?";
    private static final String INACTIVE_BY_ID = "UPDATE usuario SET ativo=0 where id=(SELECT id_usuario FROM lavador_usuario where id_lavador = ?);";
    private static final String SELECT_BY_ID = "select id, dt_contrato, email, nome, telefone, dt_nascimento, CPF, CEP, estado, cidade, bairro, endereco, numero from lavador where id = ?";
    private static final String SELECT_ID_BY_EMAIL = "select id from lavador where email = ?";
    private static final String CHECK_DISPONIVEL = "select * FROM solicitacao where id_lavador = ? and data_solicitacao = ?";
    private static final String SELECT_QTD_LAVADORES = "select COUNT(*) as quantidade_lavadores from Lavador l, usuario u, lavador_usuario lu where l.id = lu.id_lavador and u.id = lu.id_usuario and u.ativo = 1";

    public LavadorDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public LavadorDAO() {
        this.conexao = Conexao.getConexao();
        fechaConexao = true;
    }

    @Override
    public void cadastrar(Object obj) {
        Lavador lavador = (Lavador) obj;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INSERT);
            pstmt.setDate(1, new java.sql.Date(lavador.getDataContrato().getTimeInMillis()));
            pstmt.setString(2, lavador.getEmail());
            pstmt.setString(3, lavador.getNome());
            pstmt.setString(4, lavador.getTelefone());
            pstmt.setDate(5, new java.sql.Date(lavador.getDataNascimento().getTimeInMillis()));
            pstmt.setString(6, lavador.getCPF());
            pstmt.setString(7, lavador.getEndereco().getCEP());
            pstmt.setString(8, lavador.getEndereco().getEstado());
            pstmt.setString(9, lavador.getEndereco().getCidade());
            pstmt.setString(10, lavador.getEndereco().getBairro());
            pstmt.setString(11, lavador.getEndereco().getEndereco());
            pstmt.setInt(12, lavador.getEndereco().getNumero());

            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
    }

    @Override
    public ArrayList<Lavador> listar() {
        ArrayList<Lavador> lavador = new ArrayList();
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_ALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
                Timestamp timestamp;
                timestamp = rs.getTimestamp("dt_contrato");
                cal1.setTimeInMillis(timestamp.getTime());
                timestamp = rs.getTimestamp("dt_nascimento");
                cal2.setTimeInMillis(timestamp.getTime());
                Lavador f = new Lavador(rs.getInt("id"), cal1, rs.getString("email"), rs.getString("nome"), rs.getString("telefone"), cal2, rs.getString("cpf"), new Endereco(rs.getString("cep"), rs.getString("estado"), rs.getString("cidade"), rs.getString("bairro"), rs.getString("endereco"), rs.getInt("numero")));
                lavador.add(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
        return lavador;
    }

    @Override
    public Lavador localizarPorId(int id) {
        Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
        Lavador lavador = null;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_BY_ID);
            pstmt.setString(1, Integer.toString(id));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cal1.setTimeInMillis(rs.getTime("dt_contrato").getTime());
                cal2.setTimeInMillis(rs.getTime("dt_nascimento").getTime());
                lavador = new Lavador(rs.getInt("id"), cal1, rs.getString("email"), rs.getString("nome"), rs.getString("telefone"), cal2, rs.getString("cpf"), new Endereco(rs.getString("cep"), rs.getString("estado"), rs.getString("cidade"), rs.getString("bairro"), rs.getString("endereco"), rs.getInt("numero")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
        return lavador;
    }

    @Override
    public void atualizar(Object obj) {
        Lavador lavador = (Lavador) obj;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(UPDATE);
            pstmt.setString(1, lavador.getEmail());
            pstmt.setString(2, lavador.getNome());
            pstmt.setString(3, lavador.getTelefone());
            pstmt.setDate(4, new java.sql.Date(lavador.getDataNascimento().getTimeInMillis()));
            pstmt.setString(5, lavador.getEndereco().getCEP());
            pstmt.setString(6, lavador.getEndereco().getEstado());
            pstmt.setString(7, lavador.getEndereco().getCidade());
            pstmt.setString(8, lavador.getEndereco().getBairro());
            pstmt.setString(9, lavador.getEndereco().getEndereco());
            pstmt.setInt(10, lavador.getEndereco().getNumero());
            pstmt.setInt(11, lavador.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
    }

    @Override
    public void excluir(int id) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INACTIVE_BY_ID);
            pstmt.setInt(1, id);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int localizarIdPorEmail(String email) {
        int IDLavador = 0;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_ID_BY_EMAIL);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                IDLavador = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
        return IDLavador;
    }

    public boolean isLavadorDisponivel(Lavador lavador, Calendar dataSolicitacao) {

        boolean disponivel = false;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataSolicitacaoFormatada = format.format(dataSolicitacao.getTime());

        try {
            PreparedStatement pstmt = conexao.prepareStatement(CHECK_DISPONIVEL);
            pstmt.setInt(1, lavador.getId());
            pstmt.setString(2, dataSolicitacaoFormatada);
            ResultSet rs = pstmt.executeQuery();

            disponivel = rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();
        return disponivel;
    }

    public int quantidadeLavadores() {
        int numeroLavadores = 0;

        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_QTD_LAVADORES);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                numeroLavadores = rs.getInt("quantidade_lavadores");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.fechaConexao();

        return numeroLavadores;
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
