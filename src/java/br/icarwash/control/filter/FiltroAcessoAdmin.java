package br.icarwash.control.filter;

import br.icarwash.model.Usuario;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "FiltroAcessoAdmin", urlPatterns = {"/Controle", "/ListarSolicitacaoEmAnalise"})
public class FiltroAcessoAdmin implements Filter {

    private static final boolean debug = true;

    private FilterConfig filterConfig = null;

    public FiltroAcessoAdmin() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        String url = ((HttpServletRequest) request).getRequestURL().toString();

        String queryString = ((HttpServletRequest) request).getQueryString();
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("user");

        if (usuario != null) {
            if (debug) {
                log("Usuario: " + usuario.getEmail() + " Nivel: " + usuario.getNivel() + " Acessando url: " + url + "?" + queryString);
            }
        } else {
            log("usuario sem login tentando acessar " + request.getRemoteAddr());
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        doBeforeProcessing(request, response);

        HttpSession session = ((HttpServletRequest) request).getSession(true);
        
        Usuario usuario = (Usuario) session.getAttribute("user");

        String URI = request.getParameter("action");
        
        Throwable problem = null;
        if (usuario.getNivel() == 3 || URI.equals("AtualizaCommand")) {
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException t) {
                problem = t;
            }
        } else {
            log("Acesso ao usuário: " + usuario.getEmail() + " negado. Usuário derrubado do sistema.");
            session.invalidate();
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
        doAfterProcessing(request, response);

        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("FiltroAcessoCadastros:Initializing filter");
            }
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("FiltroAcessoCadastros()");
        }
        StringBuffer sb = new StringBuffer("FiltroAcessoCadastros(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (IOException ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
