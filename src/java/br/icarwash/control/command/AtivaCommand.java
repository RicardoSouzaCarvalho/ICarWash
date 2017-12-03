package br.icarwash.control.command;

import br.icarwash.dao.ProdutoDAO;
import br.icarwash.dao.ServicoDAO;
import br.icarwash.model.Produto;
import br.icarwash.model.Produto.ProdutoBuilder;
import br.icarwash.model.Servico;
import br.icarwash.model.Servico.ServicoBuilder;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtivaCommand implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");
        String ativar = request.getParameter("q");
        final int id = Integer.parseInt(request.getParameter("id"));

        switch (ativar) {
            case "produto": {
                Produto produto = new ProdutoBuilder()
                        .withId(id)
                        .build();
                new ProdutoDAO(conexao).ativar(produto);
                return "/Controle?action=ListaCommand&listar=produto";
            }
            case "servico": {
                Servico servico = new ServicoBuilder()
                        .withId(id)
                        .build();
                new ServicoDAO(conexao).ativar(servico);
                return "/Controle?action=ListaCommand&listar=servico";
            }
            default:
                return "painel_admin.jsp";
        }

    }

}
