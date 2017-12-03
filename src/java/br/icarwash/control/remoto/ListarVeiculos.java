package br.icarwash.control.remoto;

import br.icarwash.dao.MarcaDAO;
import br.icarwash.dao.ModeloDAO;
import br.icarwash.model.Marca;
import br.icarwash.model.Marca.MarcaBuilder;
import br.icarwash.model.Modelo;
import br.icarwash.model.Modelo.ModeloBuilder;
import br.icarwash.util.Conexao;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ListarVeiculos", urlPatterns = {"/listar-marcas", "/listar-modelos"})
public class ListarVeiculos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String URI = ((HttpServletRequest) request).getRequestURI();

        if (URI.endsWith("/listar-marcas")) {

            //busca a lista de marcas
            ArrayList<Marca> marcas = new MarcaDAO(Conexao.getConexao()).listar();

            //serializa para JSON
            Gson gson = new Gson();
            String listaJSON = gson.toJson(marcas);
            out.println(listaJSON);

        } else if (URI.endsWith("/listar-modelos")) {

            Marca marca = new MarcaBuilder()
                    .withId(Integer.parseInt(request.getParameter("marca")))
                    .build();

            Modelo modelo = new ModeloBuilder()
                    .withMarca(marca)
                    .build();
            
            Gson gson = new Gson();
            String listaJSON = gson.toJson(new ModeloDAO(Conexao.getConexao()).listarPorIdMarca(modelo));
            out.println(listaJSON);

        }

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
