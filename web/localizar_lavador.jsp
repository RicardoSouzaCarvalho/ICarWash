<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="cabecalho.jsp"%>

<c:if test="${!empty alterado}">
    <script>Materialize.toast('Endere�o Alterado', 6000, 'rounded');</script>        
</c:if>

<div class="row">
    <p class="titulo-controle">Alterar Lavador</p>
    <div class="divider"></div>
</div>

<div class="container">
    <div class="row">
        <form id="FormValidate" action="Controle" method="post">
            <div class="form-group">
                <input type="hidden" name="quem" value="lavador"/>
                <input type="hidden" name="DtAdmissao" value="${lavador.dataContrato}">
                <input type="hidden" name="id" value="${lavador.id}"/>
                <div class="input-field col m6 s12">
                    <label>Nome</label>
                    <input class="form-control erro-nome" type="text" name="nome" value="${lavador.nome}"><br>
                </div>
                <div class="input-field col m3 s6">
                    <label>Telefone</label> 
                    <input class="form-control erro-telefone" type="text" id ="telefone" name="telefone" value="${lavador.telefone}"><br>
                </div>
                <div class="input-field col m3 s6">
                    <%--Formatar a data antes de exibir na tela--%>
                    <fmt:formatDate value="${lavador.dtNascimento.time}" var="dataNascimento" type="date" pattern="dd/MM/yyyy" />            
                    <label>Data de Nascimento</label> 
                    <input class="form-control erro-data" type="text" id ="nascimento" name="dataNascimento" value="${dataNascimento}"><br>
                </div>
                <div class="col s12">
                    <input class="form-control btn btn-primary" type="submit" name="action" value="Atualizar"><br>
                </div>
            </div>
        </form>
    </div>
    <div class="row">
            <div class="col s6">
                <div class="card blue-grey">
                    <div class="card-content white-text">
                        <span class="card-title">${lavador.endereco.nome}</span>
                        <p>${lavador.endereco.endereco}, ${lavador.endereco.numero} - ${lavador.endereco.bairro}</p>
                        <p>${lavador.endereco.cidade} - ${lavador.endereco.estado}</p>
                        <p>CEP: ${lavador.endereco.CEP}</p>
                    </div>
                    <div class="card-action">
                        <a  class="modal-trigger" href="#modal${lavador.endereco.id}">Alterar</a>
                    </div>
                </div>
            </div>

            <!-- Modal Structure -->
            <div id="modal${lavador.endereco.id}" class="modal modal-fixed-footer">
                <form id="FormValidate" action="AlterarEndereco" method="post">
                    <div class="modal-content">
                        <h4>Alterar Endere�o</h4>
                        <div class="form-group">
                            <input type="hidden" name="id" value="${lavador.id}"/>
                            <input type="hidden" name="quem" value="lavador"/>
                            <input type="hidden" name="idEndereco" value="${lavador.endereco.id}"/>
                            <div class="input-field col s12">
                                <label>Nome</label> 
                                <input class="form-control erro-nomeEndereco" type="text" id="nomeEndereco" name="nomeEndereco" value="${lavador.endereco.nome}"><br>
                            </div>
                            <div class="input-field col s6">
                                <label>CEP</label> 
                                <input class="form-control erro-cep" type="text" id="cep" name="cep" value="${lavador.endereco.CEP}"><br>
                            </div>
                            <div class="input-field col s6">
                                <label>Estado</label> 
                                <input class="form-control erro-estado" type="text" id="estado" name="estado" value="${lavador.endereco.estado}"><br>
                            </div>
                            <div class="input-field col s6">
                                <label>Cidade</label> 
                                <input class="form-control erro-cidade" type="text" id="cidade" name="cidade" value="${lavador.endereco.cidade}"><br>
                            </div>
                            <div class="input-field col s6">
                                <label>Bairro</label> 
                                <input class="form-control erro-bairro" type="text" id="bairro" name="bairro" value="${lavador.endereco.bairro}"><br>
                            </div>
                            <div class="input-field col s6">
                                <label>Endere�o</label> 
                                <input class="form-control erro-endereco" type="text" id="endereco" name="endereco" value="${lavador.endereco.endereco}"><br>
                            </div>
                            <div class="input-field col s6">
                                <label>N�mero</label> 
                                <input class="form-control erro-numero" type="text" name="numero" value="${lavador.endereco.numero}"><br>
                            </div>
                            <div class="col s12">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a class="modal-action modal-close waves-effect waves-green btn-flat ">Fechar</a>
                        <input class="form-control btn btn-primary" type="submit" name="action" value="Atualizar">
                    </div>
                </form>
            </div>


    </div>
</div>

<script src="js/jquery.validate.js"></script>
<script src="js/validar-cep.js"></script>
<script src="js/jquery.maskedinput.min.js"></script>
<script src="js/validar-data-de-nascimento.js"></script>
<script src="js/inicializar-mascara.js"></script>
<script src="js/validar-apenas-letras.js"></script>
<script src="js/validar-apenas-letras-numeros.js"></script>
<script src="js/inicializar-validate.js"></script>
<script src="js/inicializar-modal.js"></script>

<%@include file="rodape.jsp"%>
