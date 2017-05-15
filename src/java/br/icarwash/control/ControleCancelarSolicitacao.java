/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.icarwash.control;

import br.icarwash.dao.SolicitacaoDAO;
import br.icarwash.model.Solicitacao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rezen
 */
@WebServlet(name = "ControleCancelarSolicitacao", urlPatterns = "/ControleCancelarSolicitacao")
public class ControleCancelarSolicitacao extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        Solicitacao solicitacao = solicitacaoDAO.localizarPorId(Integer.parseInt(request.getParameter("id_solicitacao")));
        
        solicitacao.cancelarSolicitacao();
        

        request.getRequestDispatcher("/minha_solicitacao.jsp").forward(request, response);

    }
}
