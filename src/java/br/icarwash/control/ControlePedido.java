package br.icarwash.control;

import br.icarwash.model.Solicitacao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControlePedido", urlPatterns = {"/confirmar-pedido"})
public class ControlePedido extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = ((HttpServletRequest) request).getSession(true);

        if (session.getAttribute("solicitacao") != null) {
            Solicitacao solicitacao = (Solicitacao) session.getAttribute("solicitacao");

            request.setAttribute("solicitacao", solicitacao);

            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/solicitar-servico");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
