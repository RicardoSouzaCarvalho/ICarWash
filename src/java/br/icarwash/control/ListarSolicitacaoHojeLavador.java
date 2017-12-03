package br.icarwash.control;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.EnderecoDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.model.Lavador;
import br.icarwash.model.Lavador.LavadorBuilder;
import br.icarwash.model.Solicitacao;
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
import javax.servlet.http.HttpSession;

@WebServlet(name = "ListarSolicitacaoHojeLavador", urlPatterns = {"/ListarSolicitacaoHojeLavador", "/solicitacoes-hoje"})
public class ListarSolicitacaoHojeLavador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conexao = (Connection) request.getAttribute("conexao");

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("user");

        Lavador lavador = new LavadorBuilder()
                .withUsuario(usuario)
                .build();

        lavador = new LavadorDAO(conexao).localizarPorIdUsuario(lavador);

        Solicitacao solicitacaoWithLavador = new Solicitacao.SolicitacaoBuilder()
                .withLavador(lavador)
                .build();

        ArrayList<Solicitacao> solicitacoes = solicitacaoDAO.listarSolicitacaoHojeLavador(solicitacaoWithLavador);

        EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);
        ClienteDAO clienteDAO = new ClienteDAO(conexao);

        solicitacoes.forEach(solicitacao -> {
            solicitacao.setEndereco(enderecoDAO.localizarPorId(solicitacao.getEndereco()));
            solicitacao.setCliente(clienteDAO.localizarPorId(solicitacao.getCliente()));
        });

        request.setAttribute("solicitacoes", solicitacoes);

        RequestDispatcher rd = request.getRequestDispatcher("/solicitacao_lavador_hoje.jsp");
        rd.forward(request, response);

    }
}
