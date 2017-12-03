package br.icarwash.model;

public class Modelo {

    private int id;
    private Marca marca;
    private String nome;
    private PorteVeiculo porte;

    public enum PorteVeiculo {
        PEQUENO(1), MEDIO(1.05), GRANDE(1.1);

        private final double taxa;

        PorteVeiculo(double taxa) {
            this.taxa = taxa;
        }

        public double getTaxa() {
            return taxa;
        }
    }

    public Modelo(ModeloBuilder builder) {
        this.id = builder.id;
        this.marca = builder.marca;
        this.nome = builder.nome;
        this.porte = builder.porte;
    }

    public static class ModeloBuilder {

        private int id;
        private Marca marca;
        private String nome;
        private PorteVeiculo porte;

        public ModeloBuilder from(Modelo modelo) {
            this.id = modelo.id;
            this.marca = modelo.marca;
            this.nome = modelo.nome;
            this.porte = modelo.porte;
            return this;
        }

        public ModeloBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public ModeloBuilder withMarca(Marca marca) {
            this.marca = marca;
            return this;
        }

        public ModeloBuilder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public ModeloBuilder withPorteVeiculo(PorteVeiculo porte) {
            this.porte = porte;
            return this;
        }

        public Modelo build() {
            return new Modelo(this);
        }
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PorteVeiculo getPorte() {
        return porte;
    }

    public void setPorte(PorteVeiculo porte) {
        this.porte = porte;
    }

}
