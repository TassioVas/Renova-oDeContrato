package br.com.crowe.produtos;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;

public class AtualizaCampo {
	
	BigDecimal nroDecimoTerceiro;
	
	public void update(BigDecimal numContrato) throws Exception { 
		
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		SessionHandle hnd = JapeSession.open();
		
		System.out.println("eNTROU NO METODO ATUALIZA CAMPO");
		
		boolean update = nativeSql
				.executeUpdate("UPDATE TCSCON SET AD_SEQFATCONTRATO = NULL"
						+ " WHERE NUMCONTRATO =  " + numContrato);
		System.out.println(update);
		
		ResultSet query = nativeSql.executeQuery("SELECT NROPARCDECTER, VLRREAJUSTADO FROM AD_RENOVCONT WHERE NUMCONTRATO = " + numContrato);
		
		if (query.next()) { 
			nroDecimoTerceiro = query.getBigDecimal("NROPARCDECTER");
		}
		
		System.out.println("nroDecimoTerceiro :" + nroDecimoTerceiro);
		
		if (nroDecimoTerceiro.equals(new BigDecimal(2)) ) {
			boolean delete = nativeSql.executeUpdate("DELETE FROM AD_VLRADICIONAL WHERE NUMCONTRATO = " + numContrato + " AND SEQUENCIAL IN (1, 2)");
			System.out.println("sysout delete...  ");
	
		} else if (nroDecimoTerceiro.equals(new BigDecimal(1))) {
			boolean delete = nativeSql.executeUpdate("DELETE FROM AD_VLRADICIONAL WHERE NUMCONTRATO = " + numContrato + " AND SEQUENCIAL = 1");
		}
		
		System.out.println("dPOIS DE ATUALIZAR");
	}

}
