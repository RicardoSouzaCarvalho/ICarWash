<%-- 
    Document   : listaCliente
    Created on : 17/11/2016, 22:13:43
    Author     : rezen
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page  contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="cabecalho.jsp"%>
<div class="jumbotron">
    <h1>Solicitar Serviço</h1>
</div>
<% if (request.getParameter("erro") == "data") {
%><p>Marque o campo de porte do veiculo</p><%
    }%>
<div class="container" style="max-width: 1000.0px;">
    <form action="ControleSolicitacao" method="post">
        <div class="form-group">
            <input type="hidden" value="cliente">
            <fieldset>
                <legend>Data da Solicitação</legend>
                <div>
                    <div class="input-group date form_datetime col-md-5" data-date-format="dd MM yyyy - HH:00 p" data-link-field="dtp_input1" data-date-end-date="0d">
                        <input class="form-control" size="16" type="text" readonly>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                    </div>
                    <input type="hidden" id="dtp_input1" name="data_solicitacao" value="" /><br/>
                </div>
            </fieldset> 
            <legend>Serviços</legend>
            <div>
                <c:forEach var="servico" items="${servicos}">
                    <c:if test = "${servico.ativo}">
                        <div class="checkbox">
                            <label style="padding-left: 35px;">
                                <input type="checkbox" name="servico" value="${servico.id}">${servico.nome} R$${servico.valor}
                            </label>
                        </div>   
                    </c:if>
                </c:forEach> 
            </div>
            <fieldset>
                <div>
                    <legend>Tamanho do Veículo</legend>
                    <label class="radio-inline" style="padding-left: 35px;">
                        <input type="radio" name="porte" value="pequeno">Pequeno
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="porte" value="medio">Médio
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="porte" value="grande">Grande
                    </label>
                </div>     
            </fieldset>
        </div>


        <div class="form-group">
            <input class="form-control btn btn-primary" type="submit" name="action" value="Cadastrar"><br>
        </div>
    </form>
</div>

<script type="text/javascript" src="./js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="./js/moment.js" charset="UTF-8"></script>
<script type="text/javascript" src="./js/locales/bootstrap-datetimepicker.pt-BR.js" charset="UTF-8"></script>
<script type="text/javascript">
    $(function () {
        var date = new Date();
        date.setHours(date.getHours() + 1);
        $('.form_datetime').datetimepicker({
            language: 'pt-BR',
            autoclose: 1,
            startDate: date,
            todayHighlight: 0,
            startView: 2,
            showMeridian: 1,
            daysOfWeekDisabled: [0, 6],
            hoursDisabled: '0,1,2,3,4,5,6,7,18,19,20,21,22,23',
            minView: 1
        });
    });
</script>

<%@include file="rodape.jsp"%>
