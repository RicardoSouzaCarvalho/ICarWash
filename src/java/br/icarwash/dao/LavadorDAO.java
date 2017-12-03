package br.icarwash.dao;

import br.icarwash.model.Endereco;
import br.icarwash.model.Endereco.EnderecoBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import br.icarwash.model.Lavador;
import br.icarwash.model.Lavador.LavadorBuilder;
import br.icarwash.model.Usuario;
import br.icarwash.model.Usuario.UsuarioBuilder;
import java.sql.Statement;

public class LavadorDAO {

    private final Connection conexao;
    private static final String INSERT = "insert into lavador(id_usuario, dt_contrato, nome, telefone, dt_nascimento, CPF, id_endereco) values(?,?,?,?,?,?,?)";
    private static final String SELECT_ALL = "select * from lavador";
    private static final String UPDATE = "update lavador set nome = ?, telefone = ?, dt_nascimento = ? WHERE id = ?";
    private static final String OCUPAR_LAVADOR = "UPDATE lavador SET ocupado = true where id = ?;";
    private static final String DESOCUPAR_LAVADOR = "UPDATE lavador SET ocupado = false where id = ?;";
    private static final String SELECT_BY_ID = "select * from lavador where id = ?";
    private static final String SELECT_BY_ID_USUARIO = "select * from lavador where id_usuario = ?";
    private static final String SELECT_ID_BY_CPF = "select id from lavador where cpf = ?";
    private static final String SELECT_QTD_LAVADORES = "select COUNT(*) as quantidade_lavadores from Lavador l, usuario u where u.id = l.id_usuario and u.ativo = 1";

    public LavadorDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public Lavador cadastrar(Lavador lavador) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, lavador.getUsuario().getId());
            pstmt.setDate(2, new java.sql.Date(lavador.getDataContrato().getTimeInMillis()));
            pstmt.setString(3, lavador.getNome());
            pstmt.setString(4, lavador.getTelefone());
            pstmt.setDate(5, new java.sql.Date(lavador.getDataNascimento().getTimeInMillis()));
            pstmt.setString(6, lavador.getCPF());
            pstmt.setInt(7, lavador.getEndereco().getId());

            pstmt.execute();

            final ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                lavador.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lavador;
    }

    public ArrayList<Lavador> listar() {
        ArrayList<Lavador> lavadores = new ArrayList();
        Lavador lavador;
        Usuario usuario;
        Endereco endereco;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_ALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Calendar dataContrato = Calendar.getInstance(), dataNascimento = Calendar.getInstance();
                Timestamp timestamp;
                timestamp = rs.getTimestamp("dt_contrato");
                dataContrato.setTimeInMillis(timestamp.getTime());
                timestamp = rs.getTimestamp("dt_nascimento");
                dataNascimento.setTimeInMillis(timestamp.getTime());

                usuario = new UsuarioBuilder()
                        .withId(rs.getInt("id_usuario"))
                        .build();

                endereco = new EnderecoBuilder()
                        .withId(rs.getInt("id_endereco"))
                        .build();

                lavador = new LavadorBuilder()
                        .withId(rs.getInt("id"))
                        .withUsuario(usuario)
                        .withDataContrato(dataContrato)
                        .withNome(rs.getString("nome"))
                        .withTelefone(rs.getString("telefone"))
                        .withDataNascimento(dataNascimento)
                        .withCpf(rs.getString("cpf"))
                        .withEndereco(endereco)
                        .build();

                lavador.setOcupado(rs.getBoolean("ocupado"));
                lavadores.add(lavador);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lavadores;
    }

    public Lavador localizarPorId(Lavador lavador) {
        Calendar dataContrato = Calendar.getInstance(), dataNascimento = Calendar.getInstance();
        Usuario usuario;
        Endereco endereco;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_BY_ID);
            pstmt.setString(1, Integer.toString(lavador.getId()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {

                dataContrato.setTimeInMillis(rs.getDate("dt_contrato").getTime());
                dataNascimento.setTimeInMillis(rs.getDate("dt_nascimento").getTime());

                usuario = new UsuarioBuilder()
                        .withId(rs.getInt("id_usuario"))
                        .build();

                endereco = new EnderecoBuilder()
                        .withId(rs.getInt("id_endereco"))
                        .build();

                lavador = new LavadorBuilder()
                        .withId(rs.getInt("id"))
                        .withUsuario(usuario)
                        .withDataContrato(dataContrato)
                        .withNome(rs.getString("nome"))
                        .withTelefone(rs.getString("telefone"))
                        .withDataNascimento(dataNascimento)
                        .withCpf(rs.getString("cpf"))
                        .withEndereco(endereco)
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lavador;
    }

    public Lavador localizarPorIdUsuario(Lavador lavador) {
        Calendar dataContrato = Calendar.getInstance(), dataNascimento = Calendar.getInstance();
        Usuario usuario;
        Endereco endereco;
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_BY_ID_USUARIO);
            pstmt.setString(1, Integer.toString(lavador.getUsuario().getId()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {

                dataContrato.setTimeInMillis(rs.getTime("dt_contrato").getTime());
                dataNascimento.setTimeInMillis(rs.getTime("dt_nascimento").getTime());

                usuario = new UsuarioBuilder()
                        .withId(rs.getInt("id_usuario"))
                        .build();

                endereco = new EnderecoBuilder()
                        .withId(rs.getInt("id_endereco"))
                        .build();

                lavador = new LavadorBuilder()
                        .withId(rs.getInt("id"))
                        .withUsuario(usuario)
                        .withDataContrato(dataContrato)
                        .withNome(rs.getString("nome"))
                        .withTelefone(rs.getString("telefone"))
                        .withDataNascimento(dataNascimento)
                        .withCpf(rs.getString("cpf"))
                        .withEndereco(endereco)
                        .build();

                lavador.setOcupado(rs.getBoolean("ocupado"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lavador;
    }

    public void atualizar(Lavador lavador) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(UPDATE);
            pstmt.setString(1, lavador.getNome());
            pstmt.setString(2, lavador.getTelefone());
            pstmt.setDate(3, new java.sql.Date(lavador.getDataNascimento().getTimeInMillis()));
            pstmt.setInt(4, lavador.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna um inteiro com a quantidade de lavadores
     *
     * @return
     */
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

        return numeroLavadores;
    }

    public boolean checkCpfDisponivel(Lavador lavador) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_ID_BY_CPF);
            pstmt.setString(1, lavador.getCPF());
            ResultSet rs = pstmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ocuparLavador(Lavador lavador) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(OCUPAR_LAVADOR);
            pstmt.setInt(1, lavador.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void desocuparLavador(Lavador lavador) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(DESOCUPAR_LAVADOR);
            pstmt.setInt(1, lavador.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
