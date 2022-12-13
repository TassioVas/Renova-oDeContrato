package br.com.crowe.produtos;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.bmp.PersistentLocalEntity;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class InserirProduto {
	
	BigDecimal codProd;
	BigDecimal vlrReajustado;
	BigDecimal codServ; 
	
	Timestamp date;
	
	
	public void produto(BigDecimal numContrato) throws Exception {
		
		System.out.println("Entrou no metodo produto com o numContrato : " + numContrato);
		
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		SessionHandle hnd = JapeSession.open();
		
		ResultSet query = nativeSql.executeQuery(" SELECT PRE.CODPROD, GETDATE() AS DATE, "
				+ " CONT.VLRREAJUSTADO, PRE.CODSERV  FROM TCSCON CON\r\n"
				+ " JOIN TCSPRE PRE ON PRE.NUMCONTRATO = CON.NUMCONTRATO\r\n"
				+ " JOIN AD_RENOVCONT CONT ON CONT.NUMCONTRATO = CON.NUMCONTRATO\r\n"
				+ " WHERE CON.NUMCONTRATO = " + numContrato);
		
		if (query.next()) {
			
			codProd = query.getBigDecimal("CODPROD");
			vlrReajustado = query.getBigDecimal("VLRREAJUSTADO");
			codServ = query.getBigDecimal("CODSERV");
			date = query.getTimestamp("DATE");
			
			try {

				System.out.println("Inserindo na tabela Contrato");

				EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
				DynamicVO dynamicVO1 = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("PrecoContrato");

				dynamicVO1.setProperty("CODPROD", codProd);
				dynamicVO1.setProperty("NUMCONTRATO", numContrato);
				dynamicVO1.setProperty("VALOR", vlrReajustado);
				dynamicVO1.setProperty("CODSERV", codServ);
				 dynamicVO1.setProperty("REFERENCIA", date);

				// dynamicVO1.setProperty("CODPROD", codProd);
				PersistentLocalEntity createEntity = dwfFacade.createEntity("PrecoContrato", (EntityVO) dynamicVO1);
				DynamicVO save = (DynamicVO) createEntity.getValueObject();

				// newCodProj = save.asBigDecimal("CODPROJ");

			} catch (Exception e) {
				String msg = "Erro na inclusao dos Itens " + e.getMessage();
				System.out.println(msg);
			}
		}
	}
}
