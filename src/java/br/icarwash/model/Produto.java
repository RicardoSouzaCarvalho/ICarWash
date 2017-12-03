package br.icarwash.model;

public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private boolean ativo;

    public Produto(ProdutoBuilder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.descricao = builder.descricao;
        this.ativo = builder.ativo;

    }

    public static class ProdutoBuilder {

        private int id;
        private String nome;
        private String descricao;
        private boolean ativo;


        public ProdutoBuilder from(Produto produto) {
            this.id = produto.id;
            this.nome = produto.nome;
            this.descricao = produto.descricao;
            this.ativo = produto.ativo;
            return this;
        }
        
        public ProdutoBuilder withId(int id) {
            this.id = id;
            return this;
        }
        
        public ProdutoBuilder withNome(String nome) {
            this.nome = nome;
            return this;
        }
        
        public ProdutoBuilder withDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }
        
        public ProdutoBuilder withAtivo(boolean ativo) {
            this.ativo = ativo;
            return this;
        }
        
        public Produto build(){
            return new Produto(this);
        }
    }
    
    public Produto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
