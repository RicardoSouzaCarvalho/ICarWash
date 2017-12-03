package br.icarwash.control.command;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.EnderecoDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.ProdutoDAO;
import br.icarwash.dao.ServicoDAO;
import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.dao.UsuarioDAO;
import br.icarwash.model.Cliente;
import br.icarwash.model.Endereco;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.icarwash.model.Lavador;
import br.icarwash.model.Solicitacao;
import br.icarwash.model.Usuario;
import java.sql.Connection;

public class ListaCommand implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");
        String quemListar = request.getParameter("listar");

        switch (quemListar) {
            case "cliente":
                return listarClientes(request);

            case "lavador":
                return listarLavadores(request);

            case "servico":
                request.setAttribute("servicos", new ServicoDAO(conexao).listar());
                request.setAttribute("produtos", new ProdutoDAO(conexao).listar());

                return "listar_servico.jsp";

            case "produto":
                request.setAttribute("produtos", new ProdutoDAO(conexao).listar());

                return "listar_produto.jsp";

            case "solicitacao":
                return listarSolicitacoes(request);

            default:
                return "painel_admin.jsp";
        }
    }

    private String listarClientes(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");
        ArrayList<Cliente> clientes = new ClienteDAO(conexao).listar();

        UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);

        clientes.forEach((cliente) -> {
            cliente.setUsuario(usuarioDAO.localizarUsuarioPorID(cliente.getUsuario()));
        });

        request.setAttribute("clientes", clientes);

        return "listar_cliente.jsp";
    }

    private String listarLavadores(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");
        ArrayList<Lavador> lavadores = new LavadorDAO(conexao).listar();

        UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);

        Usuario usuario;

        for (Lavador lavador : lavadores) {
            usuario = usuarioDAO.localizarUsuarioPorID(lavador.getUsuario());
            lavador.setUsuario(usuario);
        };

        request.setAttribute("lavadores", lavadores);

        return "listar_lavador.jsp";
    }

    private String listarSolicitacoes(HttpServletRequest request) {
        Connection conexao = (Connection) request.getAttribute("conexao");
        ArrayList<Solicitacao> solicitacoes = new SolicitacaoDAO(conexao).listar();
        ClienteDAO clienteDAO = new ClienteDAO(conexao);

        solicitacoes.forEach(solicitacao -> {
            solicitacao.setCliente(clienteDAO.localizarPorId(solicitacao.getCliente())
            );
        });

        request.setAttribute("solicitacoes", solicitacoes);

        return "listar_solicitacao.jsp";
    }

}
