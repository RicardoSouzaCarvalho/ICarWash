package br.icarwash.control;

import br.icarwash.dao.AvaliacaoDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.model.Avaliacao;
import br.icarwash.model.Lavador;
import br.icarwash.model.Solicitacao;
import java.io.IOException;
import java.sql.Connection;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.DateUtils;

@WebServlet(name = "ControleDashboard", urlPatterns = {"/Dashboard"})
public class ControleDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HashMap<String, Integer> qtdPorStatusSolicitacao = geraMapaSolicitacaoQuantidadePorStatus(request);
        HashMap<String, Integer> qtdPorStatusSolicitacaoHoje = geraMapaSolicitacaoQuantidadePorStatusHoje(request);
        HashMap<String, Integer> qtdPorStatusSolicitacaoMes = geraMapaSolicitacaoQuantidadePorStatusMes(request);

        request.setAttribute("qtdPorStatusSolicitacao", qtdPorStatusSolicitacao);
        request.setAttribute("qtdPorStatusSolicitacaoHoje", qtdPorStatusSolicitacaoHoje);
        request.setAttribute("qtdPorStatusSolicitacaoMes", qtdPorStatusSolicitacaoMes);

        RequestDispatcher rd = request.getRequestDispatcher("/painel_admin.jsp");
        rd.forward(request, response);
    }

    private HashMap<String, Integer> geraMapaSolicitacaoQuantidadePorStatus(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HashMap<String, Integer> estadosSolicitacoes = new HashMap<>();

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);

        final int quantidadeTotal = solicitacaoDAO.listar().size();
        final int quantidadeAgendado = solicitacaoDAO.listarAgendado().size();
        final int quantidadeAvaliado = solicitacaoDAO.listarAvaliado().size();
        final int quantidadeCancelado = solicitacaoDAO.listarCancelado().size();
        final int quantidadeEmAnalise = solicitacaoDAO.listarEmAnalise().size();
        final int quantidadeEmProcesso = solicitacaoDAO.listarEmProcesso().size();
        final int quantidadeFinalizado = solicitacaoDAO.listarFinalizado().size();

        estadosSolicitacoes.put("totalSolicitacao", quantidadeTotal);
        estadosSolicitacoes.put("agendado", quantidadeAgendado);
        estadosSolicitacoes.put("avaliado", quantidadeAvaliado);
        estadosSolicitacoes.put("cancelado", quantidadeCancelado);
        estadosSolicitacoes.put("emAnalise", quantidadeEmAnalise);
        estadosSolicitacoes.put("emProcesso", quantidadeEmProcesso);
        estadosSolicitacoes.put("finalizado", quantidadeFinalizado);
        return estadosSolicitacoes;

    }

    private HashMap<String, Integer> geraMapaSolicitacaoQuantidadePorStatusHoje(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HashMap<String, Integer> estadosSolicitacoesHoje = new HashMap<>();

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);

        ArrayList<Solicitacao> solicitacoesAgendado = solicitacaoDAO.listarAgendado();
        ArrayList<Solicitacao> solicitacoesAvaliado = solicitacaoDAO.listarAvaliado();
        ArrayList<Solicitacao> solicitacoesCancelado = solicitacaoDAO.listarCancelado();
        ArrayList<Solicitacao> solicitacoesEmAnalise = solicitacaoDAO.listarEmAnalise();
        ArrayList<Solicitacao> solicitacoesEmProcesso = solicitacaoDAO.listarEmProcesso();
        ArrayList<Solicitacao> solicitacoesFinalizado = solicitacaoDAO.listarFinalizado();

        mantemSolicitacaoHoje(solicitacoesAgendado);
        mantemSolicitacaoHoje(solicitacoesAvaliado);
        mantemSolicitacaoHoje(solicitacoesCancelado);
        mantemSolicitacaoHoje(solicitacoesEmAnalise);
        mantemSolicitacaoHoje(solicitacoesEmProcesso);
        mantemSolicitacaoHoje(solicitacoesFinalizado);

        estadosSolicitacoesHoje.put("agendado", solicitacoesAgendado.size());
        estadosSolicitacoesHoje.put("avaliado", solicitacoesAvaliado.size());
        estadosSolicitacoesHoje.put("cancelado", solicitacoesCancelado.size());
        estadosSolicitacoesHoje.put("emAnalise", solicitacoesEmAnalise.size());
        estadosSolicitacoesHoje.put("emProcesso", solicitacoesEmProcesso.size());
        estadosSolicitacoesHoje.put("finalizado", solicitacoesFinalizado.size());
        return estadosSolicitacoesHoje;

    }

    private void mantemSolicitacaoHoje(ArrayList<Solicitacao> solicitacoes) {
        Calendar calendarHoje = Calendar.getInstance();
        for (Iterator<Solicitacao> i = solicitacoes.iterator(); i.hasNext();) {
            Solicitacao solicitacao = i.next();
            if (!DateUtils.isSameDay(calendarHoje, solicitacao.getDataSolicitacao())) {
                i.remove();
            }
        }
    }

    private HashMap<String, Integer> geraMapaSolicitacaoQuantidadePorStatusMes(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HashMap<String, Integer> estadosSolicitacoesMes = new HashMap<>();

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(conexao);

        ArrayList<Solicitacao> solicitacoesAgendado = solicitacaoDAO.listarAgendado();
        ArrayList<Solicitacao> solicitacoesAvaliado = solicitacaoDAO.listarAvaliado();
        ArrayList<Solicitacao> solicitacoesCancelado = solicitacaoDAO.listarCancelado();
        ArrayList<Solicitacao> solicitacoesEmAnalise = solicitacaoDAO.listarEmAnalise();
        ArrayList<Solicitacao> solicitacoesEmProcesso = solicitacaoDAO.listarEmProcesso();
        ArrayList<Solicitacao> solicitacoesFinalizado = solicitacaoDAO.listarFinalizado();

        mantemSolicitacaoMes(solicitacoesAgendado);
        mantemSolicitacaoMes(solicitacoesAvaliado);
        mantemSolicitacaoMes(solicitacoesCancelado);
        mantemSolicitacaoMes(solicitacoesEmAnalise);
        mantemSolicitacaoMes(solicitacoesEmProcesso);
        mantemSolicitacaoMes(solicitacoesFinalizado);

        estadosSolicitacoesMes.put("agendado", solicitacoesAgendado.size());
        estadosSolicitacoesMes.put("avaliado", solicitacoesAvaliado.size());
        estadosSolicitacoesMes.put("cancelado", solicitacoesCancelado.size());
        estadosSolicitacoesMes.put("emAnalise", solicitacoesEmAnalise.size());
        estadosSolicitacoesMes.put("emProcesso", solicitacoesEmProcesso.size());
        estadosSolicitacoesMes.put("finalizado", solicitacoesFinalizado.size());
        return estadosSolicitacoesMes;
    }

    private void mantemSolicitacaoMes(ArrayList<Solicitacao> solicitacoes) {
        Calendar calendarHoje = Calendar.getInstance();
        Instant i1 = calendarHoje.toInstant();
        ZoneId zoneId = ZoneId.of("America/Montreal");

        ZonedDateTime zdt1 = ZonedDateTime.ofInstant(i1, zoneId);
        YearMonth ym1 = YearMonth.from(zdt1);

        for (Iterator<Solicitacao> i = solicitacoes.iterator(); i.hasNext();) {
            Solicitacao solicitacao = i.next();

            Instant i2 = solicitacao.getDataSolicitacao().toInstant();
            ZonedDateTime zdt2 = ZonedDateTime.ofInstant(i2, zoneId);
            YearMonth ym2 = YearMonth.from(zdt2);

            if (!ym1.equals(ym2)) {
                i.remove();
            }
        }
    }
}
