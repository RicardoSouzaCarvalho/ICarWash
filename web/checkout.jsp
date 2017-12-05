<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page  contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="cabecalho.jsp"%>

<div class="container">    
    <div class="row">
        <h4>Aprovar Serviço</h4>
        <div class="divider"></div>
    </div>

    <div class="row">
        <div class="col s12">
            <div class="card grey lighten-3">
                <div class="card-content black-text">
                    <span class="card-title">Solicitacao #${solicitacao.id}</span>
                    <table>
                        <c:forEach var="servico" items="${solicitacao.servicos}">
                            <tr>
                                <td>${servico.nome}</td>
                                <td>R$ <fmt:formatNumber type = "number" maxFractionDigits = "2" minFractionDigits = "2" value = "${servico.valor * solicitacao.modelo.porte.taxa}"/></td>                            
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>Total</td>     
                            <td>R$ <fmt:formatNumber type = "number" maxFractionDigits = "2" minFractionDigits = "2" value = "${solicitacao.valorTotal}" /></td>                         
                        </tr>
                    </table>
                </div>
                <div class="card-action">
                    <a class="text-black" href="#"></a>
                </div>
                <form action="via-boleto" method="post">
                    <button class="btn waves-effect waves-light" type="submit" name="action">Pagar com boleto
                        <i class="material-icons right">send</i>
                    </button>
                </form>

            </div>
        </div>
    </div>    
</div>

<!--<script src="js/solicitar-servico.js"></script>
<script src="js/validar-cep.js"></script>-->
<!--<script src="js/inicializar-mascara.js"></script>-->
<!--<script src="js/inicializar-validate.js"></script>-->

<%@include file="rodape.jsp"%>
