/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.icarwash.dao;

import br.icarwash.model.Servico;
import br.icarwash.model.Solicitacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author rezen
 */
public class SolicitacaoServicoDAO {

    private Connection conexao;

    public SolicitacaoServicoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastraSolicitacaoServico(Solicitacao solicitacao, Servico servico) {
        try {
            PreparedStatement pstmt = conexao.prepareStatement("insert into solicitacao_servico(id_solicitacao, id_servico) values (?,?)");
            pstmt.setInt(1, solicitacao.getId());
            pstmt.setInt(2, servico.getId());
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
