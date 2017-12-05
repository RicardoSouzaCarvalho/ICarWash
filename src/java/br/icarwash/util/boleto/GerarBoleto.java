package br.icarwash.util.boleto;

import br.icarwash.model.Solicitacao;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;

/**
 * <p>
 * Exemplo de código para geração de um boleto simples.
 * </p>
 * <p>
 * Utiliza o Banco Bradesco como exemplo, já que possui um implementação
 * simples.
 * </p>
 *
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L</a>
 * @author <a href="mailto:misaelbarreto@gmail.com">Misael Barreto</a>
 * @author <a href="mailto:romulomail@gmail.com">Rômulo Augusto</a>
 *
 * @since 0.2
 *
 * @version 0.2
 */
public class GerarBoleto {

    Boleto boleto = null;
    public GerarBoleto(Solicitacao solicitacao) {

        /*
                 * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente("ICarWash", "21.414.662/0001-88");

        /*
                 * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(solicitacao.getCliente().getNome(), solicitacao.getCliente().getCPF());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        System.out.println(solicitacao.getEndereco().getEstado());
        enderecoSac.setUF(UnidadeFederativa.valueOf(solicitacao.getEndereco().getEstado()));
        enderecoSac.setLocalidade(solicitacao.getEndereco().getCidade());
        enderecoSac.setCep(new CEP(solicitacao.getEndereco().getCEP()));
        enderecoSac.setBairro(solicitacao.getEndereco().getBairro());
        enderecoSac.setLogradouro(solicitacao.getEndereco().getEndereco());
        enderecoSac.setNumero(String.valueOf(solicitacao.getEndereco().getNumero()));
        sacado.addEndereco(enderecoSac);

        /*
                 * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
         */
        SacadorAvalista sacadorAvalista = new SacadorAvalista("ICarWash Serviços de Lavagem", "21.414.662/0001-88");

        // Informando o endereço do sacador avalista.
        Endereco enderecoSacAval = new Endereco();
        enderecoSacAval.setUF(UnidadeFederativa.SP);
        enderecoSacAval.setLocalidade("Mogi das Cruzes");
        enderecoSacAval.setCep(new CEP("08745-310"));
        enderecoSacAval.setBairro("Vila Jundiai");
        enderecoSacAval.setLogradouro("Avenida Santo Antonio");
        enderecoSacAval.setNumero("366");
        sacadorAvalista.addEndereco(enderecoSacAval);

        /*
                 * INFORMANDO OS DADOS SOBRE O TÍTULO.
         */
        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_ITAU.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(10786, "5"));
        contaBancaria.setCarteira(new Carteira(30));
        contaBancaria.setAgencia(new Agencia(0064, "0"));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
        titulo.setNumeroDoDocumento("123456");
        titulo.setNossoNumero("993456");
        titulo.setDigitoDoNossoNumero("5");
        titulo.setValor(solicitacao.getValorTotal());
        titulo.setDataDoDocumento(new Date());
        titulo.setDataDoVencimento(new Date());
        titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
        titulo.setAceite(EnumAceite.A);
//        titulo.setDesconto(new BigDecimal(0));
//        titulo.setDeducao(BigDecimal.ZERO);
//        titulo.setMora(BigDecimal.ZERO);
//        titulo.setAcrecimo(BigDecimal.ZERO);
//        titulo.setValorCobrado(BigDecimal.ZERO);

        /*
                 * INFORMANDO OS DADOS SOBRE O BOLETO.
         */
        boleto = new Boleto(titulo);

        boleto.setLocalPagamento("ATÉ O VENCIMENTO, PODE SER PAGO EM QUALQUER BANCO OU VIA INTERNET");
        boleto.setInstrucaoAoSacado("Cliente Itaú: Pague também no BankFone, BankLine ou Caixas Eletrônicos");
        boleto.setInstrucao1("ATENÇÃO SR.(A) CLIENTE:");
        boleto.setInstrucao2("Só sera efetuada a sua solicitação após o pagamento deste boleto");
        boleto.setInstrucao3("Não confunda pagamento com agendamento - Pague o boleto com a data do próprio dia do pagamento");
        boleto.setInstrucao4("Qualquer dúvida, entre em contato: (11) 1111.4444");

    }

    public Boleto gerar(){
        return this.boleto;
    }
}
