package br.icarwash.control;

import br.icarwash.dao.UsuarioDAO;
import br.icarwash.model.Usuario;
import br.icarwash.model.Usuario.UsuarioBuilder;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = "/Login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");
        
        response.setContentType("text/html");

        Usuario usuario = new UsuarioBuilder()
                .withEmail(request.getParameter("email"))
                .withSenha(request.getParameter("senha"))
                .build();

        usuario = new UsuarioDAO(conexao).usuarioLogin(usuario);

        this.validaLogin(request, response, usuario);

    }

    public void validaLogin(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws IOException, ServletException {

        if (!usuario.isAtivo() || usuario == null) {
            response.sendRedirect("?invalido");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", usuario);
            //tempo de limite da sesssão em segundos
            session.setMaxInactiveInterval(600);
            response.sendRedirect("painel");
        }

    }
}
