package br.icarwash.control.state;

import br.icarwash.control.ControleSolicitacao;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.model.Avaliacao;
import br.icarwash.model.Solicitacao;
import br.icarwash.util.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agendado implements SolicitacaoState {

    @Override
    public SolicitacaoState analisarSolicitacao(Solicitacao solicitacao) {
        return this;
    }

    @Override
    public SolicitacaoState agendarSolicitacao(Solicitacao solicitacao) {
        return this;
    }

    @Override
    public SolicitacaoState processarSolicitacao(Solicitacao solicitacao) {
        Connection conexao = Conexao.getConexao();

        try {
            conexao.setAutoCommit(false);

            new LavadorDAO(conexao).ocuparLavador(solicitacao.getLavador().getId());

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);
            solicitacaoDAO.processarSolicitacao(solicitacao);

            conexao.commit();

            return new EmProcesso();

        } catch (SQLException e) {
            try {
                conexao.rollback();
                throw new RuntimeException(e);
            } catch (SQLException ex) {
                Logger.getLogger(ControleSolicitacao.class.getName()).log(Level.SEVERE, null, ex);
            }
            return this;
        } finally {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public SolicitacaoState finalizarSolicitacao(Solicitacao solicitacao) {
        return this;
    }

    @Override
    public SolicitacaoState avaliarSolicitacao(Solicitacao solicitacao, Avaliacao avaliacao) {
        return this;
    }

    @Override
    public SolicitacaoState concluirSolicitacao(Solicitacao solicitacao) {
        return this;
    }

    @Override
    public SolicitacaoState cancelarSolicitacao(Solicitacao solicitacao) {
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        solicitacaoDAO.cancelarSolicitacaoPorId(solicitacao.getId());
        return new Cancelado();
    }

    @Override
    public String toString() {
        return "Agendado";
    }

}
