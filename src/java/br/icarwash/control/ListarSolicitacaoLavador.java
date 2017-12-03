package br.icarwash.control;

import br.icarwash.dao.AvaliacaoDAO;
import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.EnderecoDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.model.Lavador;
import br.icarwash.model.Solicitacao;
import br.icarwash.model.Solicitacao.SolicitacaoBuilder;
import br.icarwash.model.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ListarSolicitacaoLavador", urlPatterns = {"/ListarSolicitacaoLavador", "/solicitacoes-lavador"})
public class ListarSolicitacaoLavador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conexao = (Connection) request.getAttribute("conexao");

        Usuario usuario = (Usuario) ((HttpServletRequest) request).getSession(true).getAttribute("user");

        Lavador lavador = new Lavador.LavadorBuilder()
                .withUsuario(usuario)
                .build();
        lavador = new LavadorDAO(conexao).localizarPorIdUsuario(lavador);

        Solicitacao solicitacaoWithLavador = new SolicitacaoBuilder()
                .withLavador(lavador)
                .build();

        ArrayList<Solicitacao> solicitacoes = new SolicitacaoDAO(conexao).listarSolicitacaoDoLavador(solicitacaoWithLavador);

        EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);
        ClienteDAO clienteDAO = new ClienteDAO(conexao);
        AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO(conexao);

        solicitacoes.forEach(solicitacao -> {
            if (solicitacao.getAvaliacao().getId() != 0) {
                solicitacao.setAvaliacao(avaliacaoDAO.localizarAvaliacaoPorId(solicitacao.getAvaliacao()));
            }
            solicitacao.setEndereco(enderecoDAO.localizarPorId(solicitacao.getEndereco()));
            System.out.println(solicitacao.getEndereco().getBairro());
            solicitacao.setCliente(clienteDAO.localizarPorId(solicitacao.getCliente()));
        });

        request.setAttribute("ocupado", lavador.isOcupado());
        request.setAttribute("solicitacoes", solicitacoes);

        RequestDispatcher rd = request.getRequestDispatcher("/solicitacao_lavador.jsp");
        rd.forward(request, response);

    }

}
