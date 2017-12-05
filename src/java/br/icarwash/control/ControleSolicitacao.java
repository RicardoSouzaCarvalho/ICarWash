package br.icarwash.control;

import br.icarwash.model.state.EmAnalise;
import br.icarwash.dao.*;
import br.icarwash.model.*;
import br.icarwash.model.Cliente.ClienteBuilder;
import br.icarwash.model.Endereco.EnderecoBuilder;
import br.icarwash.model.Modelo.ModeloBuilder;
import br.icarwash.model.Servico.ServicoBuilder;
import br.icarwash.model.Solicitacao.SolicitacaoBuilder;
import br.icarwash.util.boleto.GerarBoleto;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;

@WebServlet(name = "ControleSolicitacao", urlPatterns = "/ControleSolicitacao")
public class ControleSolicitacao extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Calendar dataHoraSolicitacao = Calendar.getInstance();

        Connection conexao = (Connection) request.getAttribute("conexao");

        try {

            HttpSession session = ((HttpServletRequest) request).getSession(true);
            Usuario usuario = (Usuario) session.getAttribute("user");
            Cliente cliente = new ClienteBuilder()
                    .withUsuario(usuario)
                    .build();

            ClienteDAO clienteDAO = new ClienteDAO(conexao);
            cliente = clienteDAO.localizarPorIdUsuario(cliente);
            cliente = clienteDAO.localizarPorId(cliente);

            /*PEGA OS PARAMETROS DA VIEW*/
            String[] IdServicosSolicitados = request.getParameterValues("servico");
            String[] data = request.getParameter("data_solicitacao").split("/");
            String dataSolicitacao = data[2] + "-" + data[1] + "-" + data[0];
            dataHoraSolicitacao.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dataSolicitacao + " " + request.getParameter("selectHora")));

            Endereco endereco;

            if (Boolean.parseBoolean(request.getParameter("cadastraEndereco"))) {
                endereco = new EnderecoBuilder()
                        .withCep(request.getParameter("cep"))
                        .withEstado(request.getParameter("estado"))
                        .withCidade(request.getParameter("cidade"))
                        .withBairro(request.getParameter("bairro"))
                        .withEndereco(request.getParameter("endereco"))
                        .withNumero(Integer.parseInt(request.getParameter("numero")))
                        .withNome(request.getParameter("nomeEndereco"))
                        .build();

                endereco = new EnderecoDAO(conexao).cadastrar(endereco);

            } else {
                int idEndereco = Integer.parseInt(request.getParameter("endereco"));
                endereco = new EnderecoBuilder()
                        .withId(idEndereco)
                        .build();
                endereco = new EnderecoDAO(conexao).localizarPorId(endereco);
            }

            Modelo modelo = new ModeloBuilder()
                    .withId(Integer.parseInt(request.getParameter("modelo")))
                    .build();
            modelo = new ModeloDAO(conexao).localizarPorId(modelo);

            Solicitacao solicitacao = new SolicitacaoBuilder()
                    .withCliente(cliente)
                    .withSolicitacaoState(new EmAnalise())
                    .withModelo(modelo)
                    .withDataSolicitacao(dataHoraSolicitacao)
                    .withValorTotal(somaValorTotalSolicitacao(request, modelo))
                    .withEndereco(endereco)
                    .build();

            solicitacao = new SolicitacaoDAO(conexao).cadastrar(solicitacao);

            SolicitacaoServicoDAO solicitacaoServicoDAO = new SolicitacaoServicoDAO(conexao);
            SolicitacaoServico solicitacaoServico = new SolicitacaoServico();
            solicitacaoServico.setSolicitacao(solicitacao);

            Servico servico;
            ServicoDAO servicoDAO = new ServicoDAO(conexao);
            ArrayList<Servico> servicos = new ArrayList<>();

            for (String idServico : IdServicosSolicitados) {
                servico = new ServicoBuilder()
                        .withId(Integer.parseInt(idServico))
                        .build();
                servico = servicoDAO.localizarPorId(servico);
                servicos.add(servico);

                solicitacaoServico.setServico(servico);
                solicitacaoServicoDAO.cadastraSolicitacaoServico(solicitacaoServico);
            }

            solicitacao.setServicos(servicos);
//            session.setAttribute("boleto", boleto);
            session.setAttribute("solicitacao", solicitacao);

//            RequestDispatcher rd = request.getRequestDispatcher("/via-boleto");
//            rd.forward(request, response);
        } catch (ParseException ex) {
            try {
                conexao.rollback();
                throw new RuntimeException(ex);
            } catch (SQLException ex1) {
                throw new RuntimeException(ex1);
            }
        }

        PrintWriter out = response.getWriter();
        out.println("/ICarWash/confirmar-pedido");
        out.flush();

    }

    private BigDecimal somaValorTotalSolicitacao(HttpServletRequest request, Modelo modelo) {
        Connection conexao = (Connection) request.getAttribute("conexao");
        String[] IdServicosSolicitados = request.getParameterValues("servico");

        Servico servico;
        ServicoDAO servicoDAO = new ServicoDAO(conexao);
        double valor = 0;

        for (String idServico : IdServicosSolicitados) {
            servico = new ServicoBuilder()
                    .withId(Integer.parseInt(idServico))
                    .build();
            servico = servicoDAO.localizarPorId(servico);
            valor += servico.getValor().doubleValue();
        }

        valor *= modelo.getPorte().getTaxa();

        BigDecimal valorTotal = new BigDecimal(valor);
        return valorTotal;

    }
}
