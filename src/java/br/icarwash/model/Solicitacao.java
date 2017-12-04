package br.icarwash.model;

import br.icarwash.control.ControleSolicitacao;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.model.state.SolicitacaoState;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.dao.UsuarioDAO;
import br.icarwash.model.state.Agendado;
import br.icarwash.model.state.Avaliado;
import br.icarwash.model.state.Cancelado;
import br.icarwash.model.state.Concluido;
import br.icarwash.model.state.EmAnalise;
import br.icarwash.model.state.EmProcesso;
import br.icarwash.model.state.Finalizado;
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
    private Modelo modelo;
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
        this.modelo = builder.modelo;
        this.dataSolicitacao = builder.dataSolicitacao;
        this.valorTotal = builder.valorTotal;
    }

    public SolicitacaoState validarStatus(String status) throws UnsupportedOperationException, SQLException {
        SolicitacaoState solicitacaoState;
        switch (status) {
            case "Em Analise":
                solicitacaoState = new EmAnalise();
                break;
            case "Agendado":
                solicitacaoState = new Agendado();
                break;
            case "Em Processo":
                solicitacaoState = new EmProcesso();
                break;
            case "Finalizado":
                solicitacaoState = new Finalizado();
                break;
            case "Avaliado":
                solicitacaoState = new Avaliado();
                break;
            case "Concluido":
                solicitacaoState = new Concluido();
                break;
            case "Cancelado":
                solicitacaoState = new Cancelado();
                break;
            default:
                throw new UnsupportedOperationException("Solicita\u00e7\u00e3o sem Status");
        }
        return solicitacaoState;
    }

    public static class SolicitacaoBuilder {

        private int id;
        private Cliente cliente;
        private Lavador lavador;
        private Avaliacao avaliacao;
        private Endereco endereco;
        protected SolicitacaoState estado;
        private Modelo modelo;
        private Calendar dataSolicitacao;
        private BigDecimal valorTotal;

        public SolicitacaoBuilder from(Solicitacao solicitacao) {
            this.id = solicitacao.id;
            this.cliente = solicitacao.cliente;
            this.lavador = solicitacao.lavador;
            this.avaliacao = solicitacao.avaliacao;
            this.endereco = solicitacao.endereco;
            this.estado = solicitacao.estado;
            this.modelo = solicitacao.modelo;
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

        public SolicitacaoBuilder withModelo(Modelo modelo) {
            this.modelo = modelo;
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

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
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
        this.estado = this.estado.avaliarSolicitacao(this);
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
            ArrayList<Lavador> lavadoresAtivos = new LavadorDAO(conexao).listar();
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            
            for (int i = 0; i < lavadoresAtivos.size() - 1; i++) {
                if (!usuarioDAO.isAtivo(lavadoresAtivos.get(i).getUsuario())) {
                    lavadoresAtivos.remove(lavadoresAtivos.get(i));
                }
            }

            ArrayList<Lavador> lavadoresDisponiveis = solicitacaoDAO.lavadoresDisponives(lavadoresAtivos, this.dataSolicitacao);

            lavadoresDisponiveis = removeLavadoresDaLista(lavadoresDisponiveis, conexao);
            Random random = new Random();

            int lavadorSorteado = random.nextInt(lavadoresDisponiveis.size());

            setLavador(lavadoresDisponiveis.get(lavadorSorteado));
            
            solicitacaoDAO.atribuirLavador(this);
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
    private ArrayList<Lavador> removeLavadoresDaLista(ArrayList<Lavador> lavadoresDisponiveis, Connection conexao) throws NumberFormatException {
        Map<String, Integer> mapa = new HashMap<>();
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);
        
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
