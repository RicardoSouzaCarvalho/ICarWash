package br.icarwash.control.command;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.ProdutoDAO;
import br.icarwash.dao.ServicoDAO;
import br.icarwash.dao.ServicoProdutoDAO;
import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.icarwash.model.Cliente;
import br.icarwash.model.Lavador;
import br.icarwash.model.Lavador.LavadorBuilder;
import br.icarwash.model.Produto;
import br.icarwash.model.Produto.ProdutoBuilder;
import br.icarwash.model.Servico;
import br.icarwash.model.Servico.ServicoBuilder;
import br.icarwash.model.ServicoProduto;
import br.icarwash.model.Usuario;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

public class AtualizaCommand implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HttpSession session = ((HttpServletRequest) request).getSession(true);

        Usuario usuario = (Usuario) session.getAttribute("user");

        final int id = Integer.parseInt(request.getParameter("id"));

        switch (request.getParameter("quem")) {
            case "cliente": {

                Calendar calendarNascimento = Calendar.getInstance();
                String[] nascimento = request.getParameter("dataNascimento").split("/");
                calendarNascimento.set(Integer.parseInt(nascimento[2]), Integer.parseInt(nascimento[1]) - 1, Integer.parseInt(nascimento[0]));

                Cliente cliente = new Cliente.ClienteBuilder()
                        .withId(id)
                        .withNome(request.getParameter("nome"))
                        .withTelefone(request.getParameter("telefone"))
                        .withDataNascimento(calendarNascimento)
                        .build();

                new ClienteDAO(conexao).atualizar(cliente);

                if (usuario.getNivel() == 3) {
                    return "Controle?action=ListaCommand&listar=cliente";
                } else {
                    return "usuario";
                }
            }
            case "lavador": {

                Calendar nascimentoLavador = Calendar.getInstance();

                String[] nascimento = request.getParameter("dataNascimento").split("/");
                nascimentoLavador.set(Integer.parseInt(nascimento[2]), Integer.parseInt(nascimento[1]) - 1, Integer.parseInt(nascimento[0]));

                Lavador lavador = new LavadorBuilder()
                        .withId(id)
                        .withNome(request.getParameter("nome"))
                        .withTelefone(request.getParameter("telefone"))
                        .withDataNascimento(nascimentoLavador)
                        .withCpf(request.getParameter("cpf"))
                        .build();

                new LavadorDAO(conexao).atualizar(lavador);

                if (usuario.getNivel() == 3) {
                    return "Controle?action=ListaCommand&listar=lavador";
                } else {
                    return "usuario";
                }
            }
            case "produto": {
                Produto produto = new Produto.ProdutoBuilder()
                        .withId(id)
                        .withNome(request.getParameter("nome"))
                        .withDescricao(request.getParameter("descricao"))
                        .build();

                new ProdutoDAO(conexao).atualizar(produto);

                return "Controle?action=ListaCommand&listar=produto";
            }
            case "servico": {

                Servico servico = new ServicoBuilder()
                        .withId(id)
                        .withNome(request.getParameter("nome"))
                        .withDescricao(request.getParameter("descricao"))
                        .withValor(new BigDecimal(request.getParameter("valor")))
                        .build();

                new ServicoDAO(conexao).atualizar(servico);

                atualizarProdutosDoServico(servico, request);

                return "Controle?action=ListaCommand&listar=servico";
            }

            default:
                return "painel";
        }
    }

    private void atualizarProdutosDoServico(Servico servico, HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");

        Map<Integer, Object> mapaProdutosDoServicoAtual = new HashMap<>();
        ServicoProdutoDAO servicoProdutoDAO = new ServicoProdutoDAO(conexao);

        ServicoProduto servicoProduto = new ServicoProduto();
        servicoProduto.setServico(servico);

        ArrayList<ServicoProduto> servicoProdutos = servicoProdutoDAO.selecionaProdutosPorIdServico(servicoProduto);

        Map<Integer, Integer> mapaProdutosAtualizados = criarMapaProdutosParaAtualizar(request);

        servicoProdutos.forEach(servicoProduto2 -> {
            //Adiciona no HashMap os produtos do serviço atual
            mapaProdutosDoServicoAtual.put(servicoProduto2.getProduto().getId(), servicoProduto2.getProduto());

            //EXCLUI produtos que perderem a vinculaçao
            if (!mapaProdutosAtualizados.containsKey(servicoProduto2.getProduto().getId())) {
                servicoProdutoDAO.excluirServicoProduto(servicoProduto2);
            }

        });
        
        Produto produto;
        for (Map.Entry<Integer, Integer> produtoQuantidade : mapaProdutosAtualizados.entrySet()) {

            produto = new ProdutoBuilder()
                    .withId(produtoQuantidade.getKey())
                    .build();
            servicoProduto.setProduto(produto);
            servicoProduto.setQuantidade(produtoQuantidade.getValue());

            if (mapaProdutosDoServicoAtual.containsKey(produtoQuantidade.getKey())) {//Atualiza um novo produto ao serviço
                servicoProdutoDAO.atualizarServicoProduto(servicoProduto);

            } else {//Vincula um novo produto ao serviço
                servicoProdutoDAO.cadastraServicoProduto(servicoProduto);
            }
        }
    }

    private Map<Integer, Integer> criarMapaProdutosParaAtualizar(HttpServletRequest request) throws NumberFormatException {
        //cria mapa dos produtos enviado pelo form
        String[] produtos = request.getParameterValues("produtos");
        Map<Integer, Integer> mapaProdutosAtualizados = new HashMap<>();
        for (String idProduto : produtos) {
            mapaProdutosAtualizados.put(Integer.parseInt(idProduto), Integer.parseInt(request.getParameter("quantidade" + idProduto)));
        }
        //fim mapa dos produtos enviado pelo form
        return mapaProdutosAtualizados;
    }

}
