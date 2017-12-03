package br.icarwash.model.state;

import br.icarwash.model.Solicitacao;

public interface SolicitacaoState {

    SolicitacaoState analisarSolicitacao(Solicitacao solicitacao);

    SolicitacaoState agendarSolicitacao(Solicitacao solicitacao);

    SolicitacaoState processarSolicitacao(Solicitacao solicitacao);

    SolicitacaoState finalizarSolicitacao(Solicitacao solicitacao);

    SolicitacaoState avaliarSolicitacao(Solicitacao solicitacao);

    SolicitacaoState concluirSolicitacao(Solicitacao solicitacao);

    SolicitacaoState cancelarSolicitacao(Solicitacao solicitacao);

}
