package br.icarwash.model;

public class SolicitacaoServico {

    private Solicitacao solicitacao;
    private Servico servico;

    public SolicitacaoServico() {
    }

    public SolicitacaoServico(Servico servico, Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
        this.servico = servico;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

}
