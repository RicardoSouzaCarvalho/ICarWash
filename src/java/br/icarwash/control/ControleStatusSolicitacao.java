package br.icarwash.control;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.EnderecoDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.dao.UsuarioDAO;
import br.icarwash.model.Avaliacao;
import br.icarwash.model.Avaliacao.AvaliacaoBuilder;
import br.icarwash.model.Cliente;
import br.icarwash.model.Lavador;
import br.icarwash.model.Solicitacao;
import br.icarwash.model.Solicitacao.SolicitacaoBuilder;
import br.icarwash.model.Usuario;
import br.icarwash.util.email.EmailStatusSolicitacaoCliente;
import br.icarwash.util.email.EmailStatusSolicitacaoLavador;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControleStatusSolicitacao", urlPatterns = {"/AprovarSolicitacao", "/CancelarSolicitacao", "/ProcessarSolicitacao", "/FinalizarSolicitacao", "/AvaliarSolicitacao"})
public class ControleStatusSolicitacao extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String URI = ((HttpServletRequest) request).getRequestURI();

        String URIRetorno = "painel_admin.jsp";

        if (URI.endsWith("/AprovarSolicitacao")) {
            URIRetorno = aprovarSolicitacao(request);

        } else if (URI.endsWith("/CancelarSolicitacao")) {
            URIRetorno = cancelarSolicitacao(request);

        } else if (URI.endsWith("/ProcessarSolicitacao")) {
            URIRetorno = processarSolicitacao(request);

        } else if (URI.endsWith("/FinalizarSolicitacao")) {
            URIRetorno = finalizarSolicitacao(request);

        } else if (URI.endsWith("/AvaliarSolicitacao")) {
            URIRetorno = avaliarSolicitacao(request);
        }
        response.sendRedirect(URIRetorno);

    }

    private String avaliarSolicitacao(HttpServletRequest request) throws NumberFormatException {

        Solicitacao solicitacao = atribuirDadosSolicitacao(request);

        Avaliacao avaliacao = new AvaliacaoBuilder()
                .withNotaPontualidade(BigDecimal.valueOf(Double.parseDouble(request.getParameter("pontualidade"))))
                .withNotaServico(BigDecimal.valueOf(Double.parseDouble(request.getParameter("servico"))))
                .withNotaAtendimento(BigDecimal.valueOf(Double.parseDouble(request.getParameter("atendimento"))))
                .withNotaAgilidade(BigDecimal.valueOf(Double.parseDouble(request.getParameter("agilidade"))))
                .build();
        solicitacao.setAvaliacao(avaliacao);
        solicitacao.avaliarSolicitacao();
        new EmailStatusSolicitacaoLavador(solicitacao).start();

        return "ListarSolicitacaoCliente";
    }

    private String finalizarSolicitacao(HttpServletRequest request) {

        Solicitacao solicitacao = atribuirDadosSolicitacao(request);

        solicitacao.finalizarSolicitacao();
        new EmailStatusSolicitacaoCliente(solicitacao).start();

        return "ListarSolicitacaoLavador";
    }

    private String processarSolicitacao(HttpServletRequest request) {
        String URIRetorno = "painel_admin.jsp";

        Solicitacao solicitacao = atribuirDadosSolicitacao(request);

        if (!solicitacao.getLavador().isOcupado()) {
            solicitacao.processarSolicitacao();
            new EmailStatusSolicitacaoCliente(solicitacao).start();

            URIRetorno = "ListarSolicitacaoLavador";
        }
        return URIRetorno;
    }

    private String cancelarSolicitacao(HttpServletRequest request) {
        String URIRetorno = "painel_admin.jsp";

        Solicitacao solicitacao = atribuirDadosSolicitacao(request);

        solicitacao.cancelarSolicitacao();
        new EmailStatusSolicitacaoCliente(solicitacao).start();

        if (solicitacao.getLavador().getId() != 0) {
            new EmailStatusSolicitacaoLavador(solicitacao).start();
        }

        Usuario usuario = (Usuario) ((HttpServletRequest) request).getSession(true).getAttribute("user");

        if (usuario.getNivel() == 1) {
            URIRetorno = "ListarSolicitacaoCliente?x";
        } else if (usuario.getNivel() == 3) {
            URIRetorno = "Controle?action=ListaCommand&listar=solicitacao";
        }
        return URIRetorno;
    }

    private String aprovarSolicitacao(HttpServletRequest request) {
        Solicitacao solicitacao = atribuirDadosSolicitacao(request);

        solicitacao.analisarSolicitacao();

        Lavador lavador = buscarLavador(solicitacao.getLavador(), request);
        solicitacao.setLavador(lavador);

        new EmailStatusSolicitacaoCliente(solicitacao).start();
        new EmailStatusSolicitacaoLavador(solicitacao).start();

        return "ListarSolicitacaoEmAnalise";
    }

    private Solicitacao atribuirDadosSolicitacao(HttpServletRequest request) throws NumberFormatException {

        Connection conexao = (Connection) request.getAttribute("conexao");

        Solicitacao solicitacao = new SolicitacaoBuilder()
                .withId(Integer.parseInt(request.getParameter("id_solicitacao")))
                .build();
        
        solicitacao = new SolicitacaoDAO(conexao).localizarPorId(solicitacao);

        Cliente cliente = new ClienteDAO(conexao).localizarPorId(solicitacao.getCliente());
        cliente.setUsuario(new UsuarioDAO(conexao).localizarUsuarioPorID(cliente.getUsuario()));

        solicitacao.setCliente(cliente);

        if (solicitacao.getLavador().getId() != 0) {
            Lavador lavador = buscarLavador(solicitacao.getLavador(), request);
            solicitacao.setLavador(lavador);
        }
        solicitacao.setEndereco(new EnderecoDAO(conexao).localizarPorId(solicitacao.getEndereco()));

        return solicitacao;
    }

    private Lavador buscarLavador(Lavador lavador, HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");
        lavador = new LavadorDAO(conexao).localizarPorId(lavador);
        lavador.setUsuario(new UsuarioDAO(conexao).localizarUsuarioPorID(lavador.getUsuario()));
        return lavador;
    }
}
