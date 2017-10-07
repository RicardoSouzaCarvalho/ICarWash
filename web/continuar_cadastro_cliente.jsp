<%-- 
    Document   : continuar_cadastro_cliente
    Created on : 23/09/2017, 21:26:25
    Author     : Mirian
--%>

<%@include file="cabecalho.jsp"%>

<div class="jumbotron">
    <h2>Antes de solicitar um servi�o, por favor conclua seu Cadastro :)</h2>
</div>
<form id="ContinuarCadastro" action="ContinuarCadastro" method="post">
    <div class="form-group">
        <div class="row">
            <div class="col-md-5">

                <label class="control-label">Nome:</label> 
                <input class="form-control erro-nome" type="text" name="nome"><br>
            </div>
            <div class="col-md-5">
                <label>Telefone celular:</label> 
                <input class="form-control" type="text" name="telefone" id="telefone"><br>
            </div>
        </div>
        <div class="row">
            <div class="col-md-5">
                <label>Data de Nascimento:</label>
                <div class="input-group date form_datetime col-md-5" data-date-format="dd MM yyyy" data-link-field="dtp_input1" data-date-end-date="0d">
                    <input class="form-control erro.nascimento" size="16" type="text" readonly name="data" id="data">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                </div>
                <input class="form-control" type="hidden" id="dtp_input1" name="nascimento" id="nascimento"><br>

            </div>
            <div class="col-md-5">
                <label>CPF:</label>
                <input class="form-control erro-cpf" type="text" name="cpf" id="cpf"><br>
            </div>
        </div>
        <div class="row">
            <div class="col-md-5">
                <label>CEP:</label>
                <input class="form-control erro-cep" type="text" name="cep" id="cep"><br>
            </div>
            <div class="col-md-5">
                <label>Estado:</label>
                <input class="form-control erro-estado" type="text" name="estado" id="estado"><br>
            </div>
        </div>
        <div class="row">
            <div class="col-md-5">
                <label>Cidade:</label>
                <input class="form-control erro-cidade" type="text" name="cidade" id="cidade"><br>
            </div>
            <div class="col-md-5">
                <label>Bairro:</label>
                <input class="form-control erro-bairro" type="text" name="bairro" id="bairro"><br>
            </div>
        </div>
        <div class="row">
            <div class="col-md-8"><label>Endere�o:</label> <input class="form-control erro-endereco" type="text" name="endereco" id="endereco"><br></div>
            <div class="col-md-4"><label>N�mero:</label> <input class="form-control erro-numero" type="text" name="numero" id="numero"><br></div>
        </div>   
    </div>
    <div class="form-group">
        <input class="form-control btn btn-primary" type="submit" name="action" value="Cadastrar"><br>
    </div>
</form>
<script src="js/jquery.validate.js"></script>
<script type="text/javascript">

    jQuery.validator.addMethod("cpf", function (value, element) {
        value = jQuery.trim(value);

        value = value.replace('.', '');
        value = value.replace('.', '');
        cpf = value.replace('-', '');
        while (cpf.length < 11)
            cpf = "0" + cpf;
        var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
        var a = [];
        var b = new Number;
        var c = 11;
        for (i = 0; i < 11; i++) {
            a[i] = cpf.charAt(i);
            if (i < 9)
                b += (a[i] * --c);
        }
        if ((x = b % 11) < 2) {
            a[9] = 0
        } else {
            a[9] = 11 - x
        }
        b = 0;
        c = 11;
        for (y = 0; y < 10; y++)
            b += (a[y] * c--);
        if ((x = b % 11) < 2) {
            a[10] = 0;
        } else {
            a[10] = 11 - x;
        }

        var retorno = true;
        if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg))
            retorno = false;

        return this.optional(element) || retorno;

    }, "Informe um CPF v�lido");


    var msg_cep;
    $("#cep").change(function () {
        var cep_code = $(this).val();
        if (cep_code.length <= 0)
            return;
        $.get("http://apps.widenet.com.br/busca-cep/api/cep.json", {code: cep_code},
                function (result) {
                    if (result.status != 1) {
                        alert(result.message || "Houve um erro desconhecido");
                        return;
                    }
                    $("input#cep").val(result.code);
                    $("input#estado").val(result.state);
                    $("input#cidade").val(result.city);
                    $("input#bairro").val(result.district);
                    $("input#endereco").val(result.address);
                    $("input#estado").val(result.state);
                });
    });

    $(document).ready(function () {
        $("#ContinuarCadastro").validate({
            rules: {
                nome: {
                    required: true,
                    minlength: 3
                },
                telefone: {
                    required: true
                },
                data: {
                    required: true
                },
                cpf: {
                    cpf: true,
                    required: true
                },
                cep: {
                    required: true
                },
                estado: {
                    required: true,
                    minlength: 2,
                    maxlength: 2
                },
                cidade: {
                    required: true
                },
                bairro: {
                    required: true
                },
                endereco: {
                    required: true
                },
                numero: {
                    required: true
                }
            },
            messages: {
                nome: {
                    required: "Por favor, digite seu nome aqui",
                    minlength: "Por favor, digite um nome de no m�nimo 3 d�gitos"
                },
                telefone: {
                    required: "Por favor, digite seu celular de contato aqui"
                },
                data: {
                    required: "Por favor, seleciona uma data de nascimento"
                },
                cpf: {
                    cpf: 'CPF inv�lido',
                    required: "Por favor, digite seu CPF aqui."
                },
                cep: {
                    required: "Por favor, digite seu CEP aqui"
                },
                estado: {
                    required: "Por favor, digite corretamente as siglas de seu estado aqui.",
                    minlength: "Por favor, digite corretamente as siglas de seu estado aqui.",
                    maxlength: "Por favor, digite corretamente as siglas de seu estado aqui."
                },
                cidade: {
                    required: "Por favor, digite sua cidade aqui"
                },
                bairro: {
                    required: "Por favor, digite sua bairro aqui"
                },
                endereco: {
                    required: "Por favor, digite seu endere�o aqui"
                },
                numero: {
                    required: "Por favor, digite seu n�mero aqui"
                }
            },
            errorElement: "em",
            errorPlacement: function (error, element) {
                // Add the `help-block` class to the error element
                error.addClass("help-block");

                if (element.prop("name") === "nome") {
                    error.insertAfter(".erro-nome");
                } else if (element.prop("name") === "cpf") {
                    error.insertAfter(".erro-cpf");
                } else if (element.prop("name") === "cep") {
                    error.insertAfter(".erro-cep");
                } else if (element.prop("name") === "estado") {
                    error.insertAfter(".erro-estado");
                } else if (element.prop("name") === "cidade") {
                    error.insertAfter(".erro-cidade");
                } else if (element.prop("name") === "bairro") {
                    error.insertAfter(".erro-bairro");
                } else if (element.prop("name") === "endereco") {
                    error.insertAfter(".erro-endereco");
                } else if (element.prop("name") === "numero") {
                    error.insertAfter(".erro-numero");
                } else {
                    error.insertAfter(".erro-data");
                }

            }
        });
    });
</script>
<script type="text/javascript" src="./js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="./js/moment.js" charset="UTF-8"></script>
<script type="text/javascript" src="./js/locales/bootstrap-datetimepicker.pt-BR.js" charset="UTF-8"></script>

<script type="text/javascript">
    $(function () {
        var date = new Date(1900, 1, 1);

        $('.form_datetime').datetimepicker({
            language: 'pt-BR',
            autoclose: 1,
            format: "dd MM yyyy",
            startDate: date,
            viewMode: 'years'
        });
    });
</script>


<%@include file="rodape.jsp"%>
