package br.icarwash.control;

import br.icarwash.dao.ClienteDAO;
import br.icarwash.dao.UsuarioDAO;
import br.icarwash.model.Cliente;
import br.icarwash.model.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ContinuarCadastro", urlPatterns = {"/ContinuarCadastro"})
public class ContinuarCadastro extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");

        HttpSession session = ((HttpServletRequest) request).getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("user");

        String[] nascimento = request.getParameter("dataNascimento").split("/");
        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.set(Integer.parseInt(nascimento[2]), Integer.parseInt(nascimento[1]), Integer.parseInt(nascimento[0]));

        Cliente cliente = new Cliente.ClienteBuilder()
                .withUsuario(usuario)
                .withNome(request.getParameter("nome"))
                .withTelefone(request.getParameter("telefone"))
                .withDataNascimento(dataNascimento)
                .withCpf(request.getParameter("cpf"))
                .build();

        new ClienteDAO(conexao).cadastrar(cliente);

        new UsuarioDAO(conexao).concluirCadastro(usuario);

        usuario.setCadastroCompleto(true);
        session.setAttribute("user", usuario);
        session.setAttribute("nome", request.getParameter("nome"));
        response.sendRedirect("solicitar-servico");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
