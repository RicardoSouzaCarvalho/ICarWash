package br.icarwash.control.command;

import br.icarwash.dao.ProdutoDAO;
import br.icarwash.dao.ServicoDAO;
import br.icarwash.dao.UsuarioDAO;
import br.icarwash.model.Cliente;
import br.icarwash.model.Cliente.ClienteBuilder;
import br.icarwash.model.Lavador;
import br.icarwash.model.Lavador.LavadorBuilder;
import br.icarwash.model.Produto;
import br.icarwash.model.Produto.ProdutoBuilder;
import br.icarwash.model.Servico;
import br.icarwash.model.Servico.ServicoBuilder;
import br.icarwash.model.Usuario;
import br.icarwash.model.Usuario.UsuarioBuilder;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DesativaCommand implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");
        String inativar = request.getParameter("q");
        int id = Integer.parseInt(request.getParameter("id"));

        switch (inativar) {
            case "cliente": {
                Usuario usuario = new UsuarioBuilder()
                        .withId(id)
                        .build();
                new UsuarioDAO(conexao).inativar(usuario);
                return "/Controle?action=ListaCommand&listar=cliente";
            }
            case "lavador": {
                Usuario usuario = new UsuarioBuilder()
                        .withId(id)
                        .build();
                new UsuarioDAO(conexao).inativar(usuario);
                return "/Controle?action=ListaCommand&listar=lavador";
            }
            case "produto": {
                Produto produto = new ProdutoBuilder()
                        .withId(id)
                        .build();
                new ProdutoDAO(conexao).inativar(produto);
                return "/Controle?action=ListaCommand&listar=produto";
            }
            case "servico": {
                Servico servico = new ServicoBuilder()
                        .withId(id)
                        .build();
                new ServicoDAO(conexao).inativar(servico);
                return "/Controle?action=ListaCommand&listar=servico";
            }
            default:
                return "painel_admin.jsp";
        }
    }

}
