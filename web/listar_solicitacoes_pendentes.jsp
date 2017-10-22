<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="cabecalho.jsp"%>

<div class="row">
    <p class="titulo-controle">Solicitações</p>
    <div class="divider"></div>
</div>

<table id="produtos" class="table table-hover centered striped responsive-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Cliente</th>
            <th>Lavador</th>
            <th>Porte</th>
            <th>Data</th>
            <th>Valor</th>
            <th>Status</th>
            <th colspan=2></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="solicitacao" items="${solicitacoes}">
            <tr>
                <td>${solicitacao.id}</td>
                <td>${solicitacao.cliente.nome}</td>
                <td>${solicitacao.lavador.id}</td>
                <td>${solicitacao.porte}</td>
                <%--Formatar a data antes de exibir na tela--%>
                <fmt:formatDate value="${solicitacao.dataSolicitacao.time}" var="dataSolicitacao" type="date" pattern="dd/MM/yyyy" />
                <td>${dataSolicitacao}</td>
                <td>${solicitacao.valorTotal}</td>
                <td>${solicitacao.estado}</td>
                <td>
                    <form action="CancelarSolicitacao" method="post">
                        <input type="hidden" name="id_solicitacao" value="${solicitacao.id}"/> 
                        <button type="submit" class="btn btn-danger" value="Cancelar">Cancelar</button>                
                    </form>                    
                </td>
                <td>
                    <form action="AprovarSolicitacao" method="post">
                        <input type="hidden" name="id_solicitacao" value="${solicitacao.id}"/>
                        <button type="submit" class="btn btn-success">Aprovar</button>
                    </form>
                </td>
            </tr>
        </c:forEach> 
    </tbody>
</table>

<%@include file="rodape.jsp"%>