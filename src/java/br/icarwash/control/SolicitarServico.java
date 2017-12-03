package br.icarwash.control;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.ClienteEnderecoDAO;
import br.icarwash.dao.EnderecoDAO;
import br.icarwash.dao.ServicoDAO;
import br.icarwash.model.Cliente;
import br.icarwash.model.ClienteEndereco;
import br.icarwash.model.Endereco;
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

@WebServlet(name = "SolicitarServico", urlPatterns = {"/SolicitarServico", "/solicitar-servico"})
public class SolicitarServico extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HttpSession session = ((HttpServletRequest) request).getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("user");

        Cliente cliente = new Cliente.ClienteBuilder()
                .withUsuario(usuario)
                .build();

        cliente = new ClienteDAO(conexao).localizarPorIdUsuario(cliente);

        ArrayList<ClienteEndereco> clienteEnderecos = new ClienteEnderecoDAO(conexao).selecionaEnderecoPorIdCliente(cliente);

        ArrayList<Endereco> enderecos = new ArrayList<>();

        for (ClienteEndereco clienteEndereco : clienteEnderecos) {
            enderecos.add(new EnderecoDAO(conexao).localizarPorId(clienteEndereco.getEndereco()));
        }

        request.setAttribute("servicos", new ServicoDAO(conexao).listar());
        request.setAttribute("enderecos", enderecos);
        request.setAttribute("taxaMedio", 1.05);
        request.setAttribute("taxaGrande", 1.10);

        RequestDispatcher rd = request.getRequestDispatcher("/solicitar_servico.jsp");
        rd.forward(request, response);
    }

}
