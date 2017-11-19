package br.icarwash.model;

import br.icarwash.control.ControleSolicitacao;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.model.state.SolicitacaoState;
import br.icarwash.model.state.EmAnalise;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.util.Conexao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Solicitacao {

    private int id;
    private Cliente cliente;
    private Lavador lavador;
    private Avaliacao avaliacao;
    private Endereco endereco;
    protected SolicitacaoState estado;
    private String porte;
    private Calendar dataSolicitacao;
    private BigDecimal valorTotal;

    public Solicitacao() {
    }

    public Solicitacao(SolicitacaoBuilder builder) {
        this.id = builder.id;
        this.cliente = builder.cliente;
        this.lavador = builder.lavador;
        this.avaliacao = builder.avaliacao;
        this.endereco = builder.endereco;
        this.estado = builder.estado;
        this.porte = builder.porte;
        this.dataSolicitacao = builder.dataSolicitacao;
        this.valorTotal = builder.valorTotal;
    }

    public static class SolicitacaoBuilder {

        private int id;
        private Cliente cliente;
        private Lavador lavador;
        private Avaliacao avaliacao;
        private Endereco endereco;
        protected SolicitacaoState estado;
        private String porte;
        private Calendar dataSolicitacao;
        private BigDecimal valorTotal;

        public SolicitacaoBuilder from(Solicitacao solicitacao) {
            this.id = solicitacao.id;
            this.cliente = solicitacao.cliente;
            this.lavador = solicitacao.lavador;
            this.avaliacao = solicitacao.avaliacao;
            this.endereco = solicitacao.endereco;
            this.estado = solicitacao.estado;
            this.porte = solicitacao.porte;
            this.dataSolicitacao = solicitacao.dataSolicitacao;
            this.valorTotal = solicitacao.valorTotal;

            return this;
        }

        public SolicitacaoBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public SolicitacaoBuilder withCliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public SolicitacaoBuilder withLavador(Lavador lavador) {
            this.lavador = lavador;
            return this;
        }

        public SolicitacaoBuilder withAvaliacao(Avaliacao avaliacao) {
            this.avaliacao = avaliacao;
            return this;
        }

        public SolicitacaoBuilder withEndereco(Endereco endereco) {
            this.endereco = endereco;
            return this;
        }

        public SolicitacaoBuilder withSolicitacaoState(SolicitacaoState estado) {
            this.estado = estado;
            return this;
        }

        public SolicitacaoBuilder withPorte(String porte) {
            this.porte = porte;
            return this;
        }

        public SolicitacaoBuilder withDataSolicitacao(Calendar dataSolicitacao) {
            this.dataSolicitacao = dataSolicitacao;
            return this;
        }

        public SolicitacaoBuilder withValorTotal(BigDecimal valorTotal) {
            this.valorTotal = valorTotal;
            return this;
        }

        public Solicitacao build() {
            return new Solicitacao(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Lavador getLavador() {
        return lavador;
    }

    public void setLavador(Lavador lavador) {
        this.lavador = lavador;

    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public SolicitacaoState getEstado() {
        return estado;
    }

    public void setEstado(SolicitacaoState estado) {
        this.estado = estado;
    }

    public Calendar getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Calendar dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void analisarSolicitacao() {
        this.estado = this.estado.analisarSolicitacao(this);
    }

    public void agendarSolicitacao() {
        this.estado = this.estado.agendarSolicitacao(this);
    }

    public void processarSolicitacao() {
        this.estado = this.estado.processarSolicitacao(this);
    }

    public void finalizarSolicitacao() {
        this.estado = this.estado.finalizarSolicitacao(this);
    }

    public void avaliarSolicitacao() {
        this.estado = this.estado.avaliarSolicitacao(this, this.avaliacao);
    }

    public void concluirSolicitacao() {
        this.estado = this.estado.concluirSolicitacao(this);
    }

    public void cancelarSolicitacao() {
        this.estado = this.estado.cancelarSolicitacao(this);
    }

    public void atribuirLavador() {
        Connection conexao = Conexao.getConexao();

        try {
            conexao.setAutoCommit(false);

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);
            LavadorDAO lavadorDAO = new LavadorDAO(conexao);

            ArrayList<Lavador> lavadoresDisponiveis = solicitacaoDAO.lavadoresDisponives(lavadorDAO.listar(), this.dataSolicitacao);

            lavadoresDisponiveis = removeLavadoresDaLista(lavadoresDisponiveis, solicitacaoDAO);
            Random random = new Random();

            int qtdLavadores = random.nextInt(lavadoresDisponiveis.size());

            this.setLavador(lavadoresDisponiveis.get(qtdLavadores));
            solicitacaoDAO.atribuirLavador(this.lavador, this);
            solicitacaoDAO.agendarSolicitacao(this);

            conexao.commit();

        } catch (SQLException e) {
            try {
                conexao.rollback();
                throw new RuntimeException(e);
            } catch (SQLException ex) {
                Logger.getLogger(ControleSolicitacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retorna uma lista com os lavadores que possuem menor quantidade de
     * lavagem no dia.
     */
    private ArrayList<Lavador> removeLavadoresDaLista(ArrayList<Lavador> lavadoresDisponiveis, SolicitacaoDAO solicitacaoDAO) throws NumberFormatException {
        Map<String, Integer> mapa = new HashMap<>();

        lavadoresDisponiveis.forEach((lavadorDisponivel) -> {
            mapa.put(String.valueOf(lavadorDisponivel.getId()), solicitacaoDAO.quantidadeSolicitacao(lavadorDisponivel.getId(), this.dataSolicitacao));
        });

        Map<String, Integer> mapaDecrescente = sortByComparator(mapa, false);

        ArrayList<Lavador> lavadoresParaRemover = criarListaDeLavadoresParaRemover(mapaDecrescente, lavadoresDisponiveis);

        lavadoresParaRemover.forEach(lavadorParaRemover -> {
            lavadoresDisponiveis.remove(lavadorParaRemover);
        });

        return lavadoresDisponiveis;
    }

    /**
     * Retorna uma lista de Lavadores que serão removidos do sorteio, no momento
     * de atribuir um lavador
     */
    private ArrayList<Lavador> criarListaDeLavadoresParaRemover(Map<String, Integer> mapaDecrescente, ArrayList<Lavador> lavadoresDisponiveis) throws NumberFormatException {
        List<String> idLavadores = new LinkedList<>(mapaDecrescente.keySet());

        List<Integer> quantidadeDeSolicitacoes = new LinkedList<>(mapaDecrescente.values());
        ArrayList<Lavador> lavadoresParaRemover = new ArrayList<>();

        int valorMaisAlto = 0;
        int count = 0;
        if ((quantidadeDeLavadoresParaRemover(quantidadeDeSolicitacoes) < new LavadorDAO(Conexao.getConexao()).quantidadeLavadores()) && lavadoresNaoPossuemMesmaQuantidade(quantidadeDeSolicitacoes)) {
            for (int quantidade : quantidadeDeSolicitacoes) {
                int idLav = Integer.parseInt(idLavadores.get(count));
                if (quantidade >= valorMaisAlto) {
                    lavadoresDisponiveis.forEach((lavadorDisponivel) -> {
                        if (lavadorDisponivel.getId() == idLav) {
                            lavadoresParaRemover.add(lavadorDisponivel);
                        }
                    });
                    valorMaisAlto = quantidade;
                }
                count++;
            }
        }
        return lavadoresParaRemover;
    }

    /**
     * Retorna um verdadeiro caso lavadores não possuam a mesma quantidade
     */
    private boolean lavadoresNaoPossuemMesmaQuantidade(List<Integer> quantidadeDeSolicitacoes) {
        double total = 0;
        for (int quantidade : quantidadeDeSolicitacoes) {
            total += quantidade;
        }
        boolean remove = true;
        if ((total / quantidadeDeSolicitacoes.size()) == quantidadeDeSolicitacoes.get(0)) {
            remove = false;
        }
        return remove;
    }

    /**
     * Retorna um inteiro com a quantidade de lavadores para remover,
     */
    private int quantidadeDeLavadoresParaRemover(List<Integer> quantidadeDeSolicitacoes) {
        int valorMaisAlto = 0;
        int aux = 0;

        for (int quantidade : quantidadeDeSolicitacoes) {
            if (quantidade >= valorMaisAlto) {
                valorMaisAlto = quantidade;
                aux++;
            }
        }
        return aux;
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
