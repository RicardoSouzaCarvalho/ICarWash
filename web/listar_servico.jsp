<%-- 
    Document   : listaServico
    Created on : 17/11/2016, 22:13:43
    Author     : rezen
--%>

<%@page import="br.icarwash.model.Produto"%>
<%@page import="br.icarwash.dao.ProdutoDAO"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="br.icarwash.model.Servico"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.icarwash.dao.ServicoDAO"%>
<%@include file="cabecalho.jsp"%>
<div class="jumbotron">
    <h2>Controle de Servicos</h2>
</div>
<table class="table table-hover">
    <thead>
        <tr>
            <th>Nome</th>
            <th>Descricao</th>
            <th>Valor</th>
            <th colspan=2>Action</th>
        </tr>
    </thead>
    <tbody>
        <%//recupera a lista do request
            ArrayList<Servico> listaServico = (ArrayList<Servico>) request.getAttribute("lista");
            for (Servico servico : listaServico) {%>  
        <tr>
            <td><%= servico.getNome()%></td>
            <td><%= servico.getDescricao()%></td>
            <td><%= servico.getValor()%></td>
            <td><a type="button" class="glyphicon glyphicon-search text-info" href="Controle?action=LocalizarPorId&q=servico&id=<%=servico.getId()%>"></a></td>
                <% if (servico.isAtivo()) {%>          	
            <td><a type="button" class="glyphicon glyphicon-remove text-danger"  href="Controle?action=Excluir&q=servico&id=<%=servico.getId()%>"></a></td>
                <%} else {%> 
            <td><a type="button" class="glyphicon glyphicon glyphicon-ok text-success"  href="Controle?action=Ativar&q=servico&id=<%=servico.getId()%>"></a></td>
                <%}%>
        </tr>
        <%}%> 
    </tbody>
</table>

<button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal">Add Servico</button>

<div class="container">
</div>
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Cadastrar Servico</h4>
            </div>
            <div class="modal-body">
                <form action="Controle" method="post">
                    <div class="form-group">
                        <input type="hidden" name="quem" value="servico">
                        <div class="row">
                            <div class="col-md-12"><label>Nome:</label> <input class="form-control" type="text" name="nome"><br></div>
                            <div class="col-md-12"><label>Descri��o:</label> <input class="form-control" type="text" name="descricao" id="descricao"><br></div>
                            <div class="col-md-12"><label>Valor:</label> <input class="form-control" type="text" name="valor" id="valor"><br></div>
<!--                                <
                                    ProdutoDAO produtoDAO = new ProdutoDAO();
                                    ArrayList<Produto> listaProduto = produtoDAO.listar();
                                    for (Produto produto : listaProduto) {%>
                                            < if (produto.isAtivo()) {%>-->
<!--                                        <div>
                                            <div class="checkbox col-md-5">
                                                <label><input type="checkbox" value="<= produto.getNome()%>" name="<= produto.getId()%>"><= produto.getNome()%></label>
                                            </div>

                                            <div class="col-md-7">
                                                <label></label>
                                                <select class="form-control" id="<= produto.getId()%>">
                                                    <option>0</option>
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                                
                                            </div>

                                        </div>    -->
<!--                                        < }
                                            }%>-->
                        </div>
                    </div>
                    <div class="form-group">
                        <input class="form-control btn btn-primary" type="submit" name="action" value="Cadastrar"><br>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
            </div>
        </div>
    </div>
</div>
<%@include file="rodape.jsp"%>