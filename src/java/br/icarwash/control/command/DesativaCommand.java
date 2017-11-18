package br.icarwash.control.command;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.LavadorDAO;
import br.icarwash.dao.ProdutoDAO;
import br.icarwash.dao.ServicoDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DesativaCommand implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String excluir = request.getParameter("q");

        switch (excluir) {
            case "cliente": {
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.excluir(Integer.parseInt(request.getParameter("id")));
                return "/Controle?action=ListaCommand&listar=cliente";
            }
            case "lavador": {
                LavadorDAO lavadorDAO = new LavadorDAO();
                lavadorDAO.excluir(Integer.parseInt(request.getParameter("id")));
                return "/Controle?action=ListaCommand&listar=lavador";
            }
            case "produto": {
                ProdutoDAO produtoDAO = new ProdutoDAO();
                produtoDAO.excluir(Integer.parseInt(request.getParameter("id")));
                return "/Controle?action=ListaCommand&listar=produto";
            }
            case "servico": {
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.excluir(Integer.parseInt(request.getParameter("id")));
                return "/Controle?action=ListaCommand&listar=servico";
            }
            default:
                return "painel_admin.jsp";
        }
    }

}