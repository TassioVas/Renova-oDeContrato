package br.com.crowe.produtos;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;

public class Program implements AcaoRotinaJava{
	
	BigDecimal numContrato;
	String decimo;
	BigDecimal vlr;


	@Override
	public void doAction(ContextoAcao contexto) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("sysout inicio do codigo");
		
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		SessionHandle hnd = JapeSession.open();
		
		BigDecimal codusu = contexto.getUsuarioLogado();
		
		
		for (int i = 0; i < (contexto.getLinhas()).length; i++) {
			
			if ((contexto.getLinhas()).length == 0) {
				contexto.mostraErro("Selecione um registro antes.");
			} else if ((contexto.getLinhas()).length > 1) {
				contexto.mostraErro("Selecione apenas um registro.");
			}
			
			Registro linha = contexto.getLinhas()[i];
			numContrato = (BigDecimal) linha.getCampo("NUMCONTRATO");
			
			ResultSet query = nativeSql.executeQuery(" SELECT "
					+ " DECTERCEIRO_S, "
					+ " VLRREAJUSTADO"
					+ " FROM AD_RENOVCONT WHERE NUMCONTRATO = " +numContrato);
			
			while (query.next()) {
				 decimo = query.getString("DECTERCEIRO_S");
				 vlr = query.getBigDecimal("VLRREAJUSTADO");
			}
			
			if (vlr == null || decimo == null) {
				contexto.setMensagemRetorno("Valor Zerado ou Campo Decimo terceiro nao preenchido.\nFavor Ajustar! ");
				return;
			}

			InserirProduto inserirProduto = new InserirProduto();
			inserirProduto.produto(numContrato);
			
			InserirOcorrencia ocorrencia = new InserirOcorrencia();
			ocorrencia.ocorrencia(numContrato, codusu);
			
			ResultSet query2 = nativeSql.executeQuery("select VLRADICIONAL, CODPROD from AD_VLRADICIONAL WHERE NUMCONTRATO =" + numContrato);
			if (query2.next()) {
				
				BigDecimal codProd = query2.getBigDecimal("CODPROD");
				
				ValorAdicional vlr = new ValorAdicional();
				vlr.adicional(numContrato, codProd);
				
				System.out.println("Caiu no if para adicionar valor adicional");
			}
			
			AtualizaCampo atualiza = new AtualizaCampo();
			atualiza.update(numContrato);
			
			contexto.setMensagemRetorno("Produto/Serviço incluido com sucesso");
		
		}
	}

}
