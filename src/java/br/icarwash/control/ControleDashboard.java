package br.icarwash.control;

import br.icarwash.dao.AvaliacaoDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.model.Avaliacao;
import br.icarwash.model.Lavador;
import br.icarwash.model.Solicitacao;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControleDashboard", urlPatterns = {"/Dashboard"})
public class ControleDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HashMap<String, Integer> qtdPorStatusSolicitacao = geraMapaSolicitacaoQuantidadePorStatus(request);

        
//        ArrayList<Solicitacao> solicitacoesAvaliadas = new SolicitacaoDAO(conexao).listarAvaliado();
//
//        ArrayList<Lavador> lavadores = new LavadorDAO(conexao).listar();
//        ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
//        
//        Avaliacao avaliacao;
//        for (Solicitacao solicitacaoAvaliada : solicitacoesAvaliadas) {
//            avaliacao = new AvaliacaoDAO(conexao)
//                    .localizarAvaliacaoPorId(solicitacaoAvaliada.getAvaliacao());
//            avaliacoes.add(avaliacao);
//        }
//
        request.setAttribute("qtdPorStatusSolicitacao", qtdPorStatusSolicitacao);

        RequestDispatcher rd = request.getRequestDispatcher("/painel_admin.jsp");
        rd.forward(request, response);
    }

    private HashMap<String, Integer> geraMapaSolicitacaoQuantidadePorStatus(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");
        
        HashMap<String, Integer> estadosSolicitacoes = new HashMap<>();

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);
        
        estadosSolicitacoes.put("totalSolicitacao", solicitacaoDAO.quantidadeSolicitacoes());
        estadosSolicitacoes.put("agendado", solicitacaoDAO.quantidadeAgendado());
        estadosSolicitacoes.put("avaliado", solicitacaoDAO.quantidadeAvaliado());
        estadosSolicitacoes.put("cancelado", solicitacaoDAO.quantidadeCancelado());
        estadosSolicitacoes.put("emAnalise", solicitacaoDAO.quantidadeEmAnalise());
        estadosSolicitacoes.put("emProcesso", solicitacaoDAO.quantidadeEmProcesso());
        estadosSolicitacoes.put("finalizado", solicitacaoDAO.quantidadeFinalizado());
        return estadosSolicitacoes;

    }
}
