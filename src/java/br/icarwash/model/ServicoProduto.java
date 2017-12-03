package br.icarwash.model;

public class ServicoProduto {

    private Servico servico;
    private Produto produto;
    private int quantidade;

    public ServicoProduto() {
    }

    public ServicoProduto(Servico servico, Produto produto, int quantidade) {
        this.servico = servico;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
