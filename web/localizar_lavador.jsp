<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="cabecalho.jsp"%>

<c:if test="${!empty alterado}">
    <script>Materialize.toast('Endereço Alterado', 6000, 'rounded');</script>        
</c:if>
<c:if test="${!empty senhaInvalida}">
    <script>Materialize.toast('Senha atual invalida', 6000, 'rounded');</script>        
</c:if>

<div class="row">
    <p class="titulo-controle">Alterar Lavador</p>
    <div class="divider"></div>
</div>


<div class="row">
    <form id="FormValidate" action="Controle" method="post">
        <div class="form-group">
            <input type="hidden" name="action" value="AtualizaCommand"/>
            <input type="hidden" name="quem" value="lavador"/>
            <input type="hidden" name="DtAdmissao" value="${lavador.dataContrato}">
            <input type="hidden" name="id" value="${lavador.id}"/>
            <div class="input-field col m6 s12">
                <label>Nome</label>
                <input class="form-control erro-nome" type="text" name="nome" id="nome" value="${lavador.nome}"><br>
            </div>
            <div class="input-field col m3 s6">
                <label>Telefone</label> 
                <input class="form-control erro-telefone" type="text" id ="telefone" name="telefone" value="${lavador.telefone}"><br>
            </div>
            <div class="input-field col m3 s6">
                <%--Formatar a data antes de exibir na tela--%>
                <fmt:formatDate value="${lavador.dataNascimento.time}" var="dataNascimento" type="date" pattern="dd/MM/yyyy" />            
                <label>Data de Nascimento</label> 
                <input class="form-control erro-data" type="text" id ="nascimento" name="dataNascimento" value="${dataNascimento}"><br>
            </div>
            <div class="col s6 m3">
                <input class="form-control btn btn-primary" type="submit" value="Atualizar"><br>
            </div>

            <div class="col s6 m3">
                <a class="waves-effect waves-light btn green modal-trigger" href="#modal-senha">Senha</a>
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
            <% Usuario usuario = (Usuario) session.getAttribute("user");
                if (usuario.getNivel() == 3) { %>
                    <div class="card-action">
                        <a  class="modal-trigger" href="#modal${endereco.id}">Alterar</a>
                    </div>
            <% }%>
        </div>
    </div>
    <% if (usuario.getNivel() == 3) {%>
        <!-- Modal Structure -->
        <div id="modal${endereco.id}" class="modal modal-fixed-footer">
            <form id="FormValidate" action="AlterarEndereco" method="post">
                <div class="modal-content">
                    <h4>Alterar Endereço</h4>
                    <div class="form-group">
                        <input type="hidden" name="id" value="${lavador.id}"/>
                        <input type="hidden" name="quem" value="lavador"/>
                        <input type="hidden" name="idEndereco" value="${endereco.id}"/>
                        <div class="input-field col s12">
                            <label>Nome</label> 
                            <input class="form-control erro-nomeEndereco" type="text" id="nomeEndereco" name="nomeEndereco" value="${endereco.nome}"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>CEP</label> 
                            <input class="form-control erro-cep" type="text" id="cep" name="cep" value="${endereco.CEP}"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Estado</label> 
                            <input class="form-control erro-estado" type="text" id="estado" name="estado" value="${endereco.estado}"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Cidade</label> 
                            <input class="form-control erro-cidade" type="text" id="cidade" name="cidade" value="${endereco.cidade}"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Bairro</label> 
                            <input class="form-control erro-bairro" type="text" id="bairro" name="bairro" value="${endereco.bairro}"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Endereço</label> 
                            <input class="form-control erro-endereco" type="text" id="endereco" name="endereco" value="${endereco.endereco}"><br>
                        </div>
                        <div class="input-field col s6">
                            <label>Número</label> 
                            <input class="form-control erro-numero" type="text" name="numero" id="numero" value="${endereco.numero}"><br>
                        </div>
                        <div class="col s12">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a class="modal-action modal-close waves-effect waves-green btn-flat ">Fechar</a>
                    <input class="form-control btn btn-primary" type="submit">
                </div>
            </form>
        </div>
    <% }%>

    <!-- Modal Alterar Senha -->
    <div id="modal-senha" class="modal modal-fixed-footer">
        <form id="FormValidate" action="usuario" method="post">
            <div class="modal-content">
                <h4>Alterar Senha</h4>
                <div class="form-group">
                    <div class='input-field col m3 s6'>
                        <input class='form-control erro-senha' type='password' name='senha' id="senha" />
                        <label for='password'>Senha atual</label>
                    </div>
                    <div class='input-field col m3 s6'>
                        <input type="password" class="form-control erro-senha" id="nova_senha" name="nova_senha" />
                        <label for='password'>Nova senha</label>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a class="modal-action modal-close waves-effect waves-green btn-flat ">Fechar</a>
                <input class="form-control btn btn-primary" type="submit" value="">
            </div>
        </form>
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
