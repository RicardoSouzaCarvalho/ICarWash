<%@page  contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="cabecalho.jsp"%>

<div class="row">
    <p class="titulo-controle">ICarWash - Painel Administrativo</p>
    <div class="divider"></div>
</div>

<div class="row">
    <ul class="collection with-header col s12 m6">
        <li class="collection-header"><p class="titulo-controle2" style="text-align: left;">Quantidade de Solicitacoes <a class="secondary-content">${qtdPorStatusSolicitacao.get("totalSolicitacao")}- 100%</a></p></li>
        <li class="collection-item"><div>Agendado<a class="secondary-content">${qtdPorStatusSolicitacao.get("agendado")} ㅤㅤㅤㅤㅤㅤ <fmt:formatNumber minIntegerDigits="2" minFractionDigits="0" maxFractionDigits="0" value="${(qtdPorStatusSolicitacao.get('agendado')*100)/qtdPorStatusSolicitacao.get('totalSolicitacao')}"/>%</a></div></li>
        <li class="collection-item"><div>Avaliado<a class="secondary-content">${qtdPorStatusSolicitacao.get("avaliado")} ㅤㅤㅤㅤㅤㅤ <fmt:formatNumber minIntegerDigits = "2" minFractionDigits="0" maxFractionDigits="0" value="${(qtdPorStatusSolicitacao.get('avaliado')*100)/qtdPorStatusSolicitacao.get('totalSolicitacao')}"/>%</a></div></li>
        <li class="collection-item"><div>Cancelado<a class="secondary-content">${qtdPorStatusSolicitacao.get("cancelado")} ㅤㅤㅤㅤㅤㅤ <fmt:formatNumber minIntegerDigits = "2" minFractionDigits="0" maxFractionDigits="0" value="${(qtdPorStatusSolicitacao.get('cancelado')*100)/qtdPorStatusSolicitacao.get('totalSolicitacao')}"/>%</a></div></li>
        <li class="collection-item"><div>EmAnalise<a class="secondary-content">${qtdPorStatusSolicitacao.get("emAnalise")} ㅤㅤㅤㅤㅤㅤ <fmt:formatNumber minIntegerDigits = "2" minFractionDigits="0" maxFractionDigits="0" value="${(qtdPorStatusSolicitacao.get('emAnalise')*100)/qtdPorStatusSolicitacao.get('totalSolicitacao')}"/>%</a></div></li>
        <li class="collection-item"><div>Finalizado<a class="secondary-content">${qtdPorStatusSolicitacao.get("finalizado")} ㅤㅤㅤㅤㅤㅤ <fmt:formatNumber minIntegerDigits = "2" minFractionDigits="0" maxFractionDigits="0" value="${(qtdPorStatusSolicitacao.get('finalizado')*100)/qtdPorStatusSolicitacao.get('totalSolicitacao')}"/>%</a></div></li>
        <li class="collection-item"><div>EmProcesso<a class="secondary-content">${qtdPorStatusSolicitacao.get("emProcesso")} ㅤㅤㅤㅤㅤㅤ <fmt:formatNumber minIntegerDigits = "2" minFractionDigits="0" maxFractionDigits="0" value="${(qtdPorStatusSolicitacao.get('emProcesso')*100)/qtdPorStatusSolicitacao.get('totalSolicitacao')}"/>%</a></div></li>
    </ul>
    <div class="col s12 m6">
        <p class="titulo-controle2">Status das solicitações no mês</p>
        <div class="divider"></div>
        <div id="canvas-holder" style="width:100%">
            <canvas id="chart-data" /></canvas>
        </div>
    </div>
    <div class="col s12 m6">
        <p class="titulo-controle2">Número de serviços realizados</p>
        <div class="divider"></div>
        <canvas id="canvas"></canvas>
    </div>
    <div class="col s12 m6">
        <p class="titulo-controle2">Status das solicitações de hoje</p>
        <div class="divider"></div>
        <div id="canvas-holder" style="width:100%">
            <canvas id="chart-area" /></canvas>
        </div>
    </div>
</div>

<div class="col s12 m6">
    <table class="responsive-table centered striped">
        <thead>
        <p class="titulo-controle2">Ranking de Lavador - Média de avaliações</p>
        <div class="divider"></div>
        <tr>
            <th>Posição</th>
            <th>Lavador</th>
            <th>Média</th>
        </tr>
        </thead>

        <tbody>
            <tr>
                <td>#1</td>
                <td>Epaminondas</td>
                <td>4.90</td>
            </tr>
            <tr>
                <td>#2</td>
                <td>Mafalda</td>
                <td>4.91</td>
            </tr>
            <tr>
                <td>#3</td>
                <td>Lollipop</td>
                <td>4.85</td>
            </tr>
            <tr>
                <td>#4</td>
                <td>João</td>
                <td>4.75</td>
            </tr>
            <tr>
                <td>#5</td>
                <td>Maria</td>
                <td>4.74</td>
            </tr>
        </tbody>
    </table>
</div>
<script src="js/Chart.bundle.js"></script>
<script src="js/utils.js"></script>
<script src="js/carregar-line.js"></script>
<script>
    var config_pie = {
        type: 'pie',
        data: {
            datasets: [{
                    data: [
    ${qtdPorStatusSolicitacaoHoje.get("emProcesso")},
    ${qtdPorStatusSolicitacaoHoje.get("emAnalise")},
    ${qtdPorStatusSolicitacaoHoje.get("finalizado")},
    ${qtdPorStatusSolicitacaoHoje.get("avaliado")},
    ${qtdPorStatusSolicitacaoHoje.get("cancelado")},
    ${qtdPorStatusSolicitacaoHoje.get("agendado")}
                    ],
                    backgroundColor: [
                        window.chartColors.red,
                        window.chartColors.orange,
                        window.chartColors.yellow,
                        window.chartColors.green,
                        window.chartColors.blue
                    ],
                    label: 'Dataset 1'
                }],
            labels: [
                "Em Processo",
                "Em Analise",
                "Finalizado",
                "Avaliado",
                "Cancelado",
                "Agendado"
            ]
        },
        options: {
            responsive: true
        }
    };
    var barChartData = {

        datasets: [
            {
                label: 'Em Análise',
                borderColor: window.chartColors.red,
                borderWidth: 2,
                data: [
    ${qtdPorStatusSolicitacaoMes.get("emAnalise")}
                ]
            },
            {
                label: 'Agendado',
                borderColor: window.chartColors.purple,
                borderWidth: 2,
                data: [
    ${qtdPorStatusSolicitacaoMes.get("agendado")}
                ]
            },
            {
                label: 'Em Processo',
                borderColor: window.chartColors.blue,
                borderWidth: 2,
                fill: false,
                data: [
    ${qtdPorStatusSolicitacaoMes.get("emProcesso")}
                ]
            },
            {
                label: 'Finalizado',
                borderColor: window.chartColors.yellow,
                borderWidth: 2,
                data: [
    ${qtdPorStatusSolicitacaoMes.get("finalizado")}
                ]
            },
            {
                label: 'Avaliado',
                borderColor: window.chartColors.green,
                borderWidth: 2,
                data: [
    ${qtdPorStatusSolicitacaoMes.get("avaliado")}
                ]
            },
            {
                label: 'Cancelado',
                borderColor: window.chartColors.orange,
                borderWidth: 2,
                data: [
    ${qtdPorStatusSolicitacaoMes.get("cancelado")}
                ]
            }]
    };
    window.onload = function () {
        var ctx = document.getElementById("canvas").getContext("2d");
        window.myLine = new Chart(ctx, config_line);

        var ctx2 = document.getElementById("chart-area").getContext("2d");
        window.myPie = new Chart(ctx2, config_pie);

        var ctx = document.getElementById("chart-data").getContext("2d");
        window.myMixedChart = new Chart(ctx, {
            type: 'bar',
            data: barChartData,
            options: {
                responsive: true,
                tooltips: {
                    mode: 'index',
                    intersect: true
                },

                scales: {
                    yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                }
            }
        });
    };
</script>


<%@include file="rodape.jsp"%>