package br.icarwash.control;

import br.icarwash.dao.*;
import br.icarwash.model.*;
import br.icarwash.model.Usuario.UsuarioBuilder;
import br.icarwash.util.email.EmailNovoCliente;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControleNovoCliente", urlPatterns = {"/NovoCliente", "/novo-cliente"})
public class ControleNovoCliente extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conexao = (Connection) request.getAttribute("conexao");

        Usuario usuario = new UsuarioBuilder()
                .withEmail(request.getParameter("email"))
                .withSenha(request.getParameter("senha"))
                .withNivel(1)
                .withAtivo(true)
                .withCadastroCompleto(false)
                .build();

        usuario = new UsuarioDAO(conexao).cadastrar(usuario);

        EmailNovoCliente emailNovoCliente = new EmailNovoCliente("Novo cliente", usuario.getEmail());

        if (usuario.getId() != 0) {
            emailNovoCliente.start();

            new LoginController().validaLogin(request, response, new UsuarioDAO(conexao).usuarioLogin(usuario));
        }
    }
}
