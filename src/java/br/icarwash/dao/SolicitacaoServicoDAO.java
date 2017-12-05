package br.icarwash.dao;

import br.icarwash.model.Servico;
import br.icarwash.model.SolicitacaoServico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SolicitacaoServicoDAO {

    private final Connection conexao;
    private static final String INSERT = "insert into solicitacao_servico(id_solicitacao, id_servico) values (?,?)";
    private static final String SELECT_SERVICO_BY_ID_SOLICITACAO = "select * from solicitacao_servico where id_solicitacao = ?";

    public SolicitacaoServicoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastraSolicitacaoServico(SolicitacaoServico solicitacaoServico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement(INSERT);
            pstmt.setInt(1, solicitacaoServico.getSolicitacao().getId());
            pstmt.setInt(2, solicitacaoServico.getServico().getId());
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
        public ArrayList<SolicitacaoServico> selecionaProdutosPorIdSolicitacao(SolicitacaoServico solicitacaoServico) {

        ArrayList<SolicitacaoServico> solicitacaoServicos = new ArrayList<>();

        try {
            PreparedStatement pstmt = conexao.prepareStatement(SELECT_SERVICO_BY_ID_SOLICITACAO);
            pstmt.setInt(1, solicitacaoServico.getSolicitacao().getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Servico servico = new Servico.ServicoBuilder()
                        .withId(rs.getInt("id_produto"))
                        .build();

                solicitacaoServico.setServico(servico);
                
                solicitacaoServicos.add(solicitacaoServico);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return solicitacaoServicos;

    }

}
