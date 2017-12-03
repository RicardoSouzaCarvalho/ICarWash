<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page  contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="cabecalho.jsp"%>

<link rel="stylesheet" href="css/star-rating-svg.css">
<script src="js/jquery.star-rating-svg.js"></script>

<div class="row">
    <p class="titulo-controle">Solicitações Gerais</p>
    <div class="divider"></div>
</div>
<table class="table table-hover centered striped responsive-table">
    <thead>
        <tr>
            <th>Solicitação</th>
            <th>Cliente</th>
            <th>Cidade</th>
            <th>Bairro</th>
            <th>Veículo</th>
            <th>Data</th>
            <th>Total</th>
            <th>Status</th>
            <th colspan=2></th>
        </tr>
    </thead>
    <tbody>  
        <c:forEach var="solicitacao" items="${solicitacoes}">
            <fmt:formatDate value="${solicitacao.dataSolicitacao.time}" var="dataSolicitacao" type="date" pattern="dd/MM/yyyy HH:mm" />
            <tr>
                <td>#${solicitacao.id}</td>
                <td>${solicitacao.cliente.nome}</td>
                <td>${solicitacao.endereco.cidade}</td>
                <td>${solicitacao.endereco.bairro}</td>
                <td>${solicitacao.modelo.porte}</td>            
                <td>${dataSolicitacao}</td>
                <td>${solicitacao.valorTotal.doubleValue()}</td>
                <td>${solicitacao.estado}</td>
                <c:if test="${solicitacao.estado == 'Agendado' && !ocupado}">
                    <td>
                        <form action="CancelarSolicitacao" method="post">
                            <input type="hidden" name="id_solicitacao" value="${solicitacao.id}"/> 
                            <button type="submit" class="btn waves-effect waves-light red lighten-1" value="Cancelar">Cancelar</button>
                        </form>
                    </td>
                </c:if>
                <c:if test="${solicitacao.estado == 'Em Processo'}">
                    <td>
                        <form action="FinalizarSolicitacao" method="post">
                            <input type="hidden" name="id_solicitacao" value="${solicitacao.id}"/>
                            <button type="submit" class="btn waves-effect waves-light blue lighten-1">Finalizar</button>
                        </form>
                    </td>
                </c:if>
                <c:if test="${solicitacao.estado == 'Avaliado'}">
                    <td> 
            <center><div class="nota-${solicitacao.id}"></div></center>

            <script>
                $(".nota-${solicitacao.id}").starRating({
                    readOnly: true,
                    starSize: 25,
                    initialRating: ${solicitacao.avaliacao.notaMedia},
                    strokeWidth: 0
                });
            </script>
        </td>
    </c:if>
    <c:if test="${solicitacao.estado == 'Finalizado'}">
        <td> 
        <center>
            <div class="nota-${solicitacao.id}"></div>
            Aguardando Avaliação</center>
        <script>
            $(".nota-${solicitacao.id}").starRating({
                readOnly: true,
                starSize: 25,
                initialRating: 0,
                strokeWidth: 0
            });
        </script>
    </td>
</c:if>
</tr>        
</c:forEach>
</tbody>
</table>

<%@include file="rodape.jsp"%>