<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page  contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="cabecalho.jsp"%>

<div class="container">    
    <div class="row">
        <h4>Solicitar Serviço</h4>
        <div class="divider"></div>
    </div>

    <form id="solicitar" name="solicitarServico" action="ControleSolicitacao" method="post" class="col s12">

        <div class="row">
            <h5 class="erro-data"></h5>
            <div class="input-field col s6">
                <input  type="text" class="datepicker" id="data_solicitacao" name="data_solicitacao">
                <label>Data da Solicitação</label>
            </div>
            <div class="input-field col s6">
                <select id="selectHora" name="selectHora">
                    <option value=" " selected></option>
                </select>
                <label>Hora da Solicitação</label>
            </div>
        </div>

        <div class="row">            
            <h5 class="erro-veiculo col s12">Veículo</h5>
            <div class="input-field col s6">
                <select id="marca" name="marca">
                    <option value="" disabled selected>Selecione a marca</option>
                </select>
                <label>Marca</label>
            </div>
            <div class="input-field col s6">
                <select id="modelo" name="modelo">
                    <option value="" disabled selected>Selecione o modelo</option>
                </select>
                <label>Modelo</label>
            </div>
        </div>
        <%--<div class="row">
            <h5 class="erro-porte col s12">Porte do Veículo</h5>
            <p class="col s4">
                <input class="with-gap" name="porte" type="radio" id="pequeno" value="pequeno"/>
                <label for="pequeno">Pequeno</label>
            </p>
            <p class="col s4">
                <input class="with-gap" name="porte" type="radio" id="medio" value="medio"/>
                <label for="medio">Médio</label>
            </p>
            <p class="col s4">
                <input class="with-gap" name="porte" type="radio" id="grande" value="grande"/>
                <label for="grande">Grande</label>
            </p>
        </div>--%>
        <div class="row col s6">
            <h5 class="erro-servico col s12">Serviços</h5>
            <c:forEach var="servico" items="${servicos}">
                <c:if test = "${servico.ativo}">
                    <p class="col s6">
                        <input type="checkbox" name="servico" id="${servico.id}" value="${servico.id}"/>
                        <label for="${servico.id}">${servico.nome}</label>
                    </p>  
                </c:if>
            </c:forEach> 
        </div>

        <div class="row col s6">
            <h5 class="erro-porte col s12">Endereço</h5>
            <c:choose>
                <c:when test = "${enderecos.size() >= 1}">
                    <c:forEach var="endereco" items="${enderecos}">
                        <p class="col s3">
                            <input type="radio" name="endereco" id="endereco${endereco.id}" value="${endereco.id}"/>
                            <label for="endereco${endereco.id}">${endereco.nome}</label>
                        </p>
                    </c:forEach> 
                </c:when>
                <c:otherwise>       
                    <input id="cadastraEndereco" type="hidden" name="cadastraEndereco" value="true"/> 
                    <div class="row">
                        <div class="input-field col s6">
                            <label>Nome Endereço</label> 
                            <input class="form-control erro-nomeEndereco" type="text" name="nomeEndereco" id="nomeEndereco"><br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s6">
                            <label>CEP</label> 
                            <input class="form-control erro-cep" type="text" name="cep" id="cep"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Estado</label> 
                            <input class="form-control erro-estado" type="text" name="estado" id="estado"><br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s6">
                            <label>Cidade</label> 
                            <input class="form-control erro-cidade" type="text" name="cidade" id="cidade"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Bairro</label> 
                            <input class="form-control erro-bairro" type="text" name="bairro" id="bairro"><br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s6">
                            <label>Endereço</label> 
                            <input class="form-control erro-endereco" type="text" name="endereco" id="endereco"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Número</label> 
                            <input class="form-control erro-numero" type="text" name="numero" id="numero"><br>
                        </div>
                    </div>   
                </c:otherwise>
            </c:choose>
        </div>

        <div class="fixed-action-btn">
            <a class="btn-floating btn-large waves-effect waves-light red"><i class="material-icons" id="confirmar">done</i></a>
        </div>
    </form>
</div>

<script src="js/solicitar-servico.js"></script>
<script src="js/validar-cep.js"></script>
<!--<script src="js/inicializar-mascara.js"></script>-->
<!--<script src="js/inicializar-validate.js"></script>-->

<%@include file="rodape.jsp"%>
