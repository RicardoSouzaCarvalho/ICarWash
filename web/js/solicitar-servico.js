
var diaSemana = ['Domingo', 'Segunda-Feira', 'Terca-Feira', 'Quarta-Feira', 'Quinta-Feira', 'Sexta-Feira', 'Sabado'];
var mesAno = ['Janeiro', 'Fevereiro', 'Marco', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
//var data = new Date();
//var hoje = data.getDay() + ', ' + mesAno[data.getMonth()] + ' de ' + data.getFullYear();

//$("#dataNascimento").attr("value", hoje);

var amanha = new Date();
amanha.setDate(amanha.getDate() + 1);
$(".datepicker").pickadate({
    monthsFull: mesAno,
    monthsShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
    weekdaysFull: diaSemana,
    weekdaysShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab'],
    weekdaysLetter: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
    selectMonths: true,
    selectYears: false,
    clear: false,
    format: 'dd/mm/yyyy',
    today: "Hoje",
    close: "Fechar",
    min: amanha,
    max: +90,
    closeOnSelect: true,
    disable: [1, 7],
    onClose: function () {
        popularSelect();
    }
});

$("#dataNascimento").click(function (event) {
    event.stopPropagation();
    $(".datepicker").first().pickadate("picker").open();
});


function popularSelect() {
    $("#selectHora").empty();
    var checkData = {data: $('#data_solicitacao').val()};
    $.getJSON("CheckSolicitacaoData", checkData, function (dataVal) {
        $.each(dataVal, function (index, item) {
            $('#selectHora').append($('<option></option>').val(item).html(item));
        });
        $('#selectHora').material_select();
    });
}

$("#confirmar").click(function () {
    var servico = $('input[name=servico]').is(':checked');
    var modelo = $('#modelo').val() !== '';
    var data = $('#data_solicitacao').val() !== '';
    var hora = $('#selectHora').val() !== '';


    if (servico && modelo && data && hora) {
        var form = document.solicitarServico;
        var dataString = $(form).serialize();
        // AJAX Code To Submit Form.
        $.ajax({
            type: "POST",
            url: "ControleSolicitacao",
            data: dataString,
            cache: false,
            success: function (result) {
                
                window.location = result;
            }
        });
    } else {
        Materialize.toast('Por favor preencha todos os campos.', 3000, 'rounded');
    }
    return false;
});

$(document).ready(function () {
    $.getJSON('listar-marcas', function (data) {
        var options = '';
        $.each(data, function (key, val) {
            options += '<option value="' + val.id + '">' + val.nome + '</option>';
        });

        $("#marca").append(options);
        $('#marca').material_select();

        $("#marca").change(function () {

            var idMarca = $("#marca").val();

            $.ajax({
                // url o recurso no servidor
                url: "listar-modelos",
                type: 'POST',
                // tipo de retorno
                dataType: "json",
                //parametros da requisição
                data: {
                    marca: idMarca
                },

                // função para tratar o retorno
                success: function (json) {
                    //lista os options 
                    $("#modelo").empty();

                    $("#modelo").append("<option></option>");

                    // preenche as options
                    for (i = 0; i < json.length; i++) {

                        var option = "<option value=" + json[i].id + ">" + json[i].nome + "</option>";

                        $("#modelo").append(option);

                    }
                    $('#modelo').material_select();
                }
            });

        }).change();
        
        $("#modelo").change(function () {
            var idModelo = $("#modelo").val();

            $.ajax({
                // url o recurso no servidor
                url: "consultar-porte",
                type: 'POST',
                // tipo de retorno
                dataType: "json",
                //parametros da requisição
                data: {
                    modelo: idModelo
                },

                // função para tratar o retorno
                success: function (json) {
                    //lista os options 
                    $("#porte").empty();

                    $('#porte').text(json.porte);

                }
            });
        }).change();

    });
});