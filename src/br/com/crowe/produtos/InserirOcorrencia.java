package br.com.crowe.produtos;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class InserirOcorrencia {
	
	Timestamp agora = new Timestamp(System.currentTimeMillis());
	
	BigDecimal codProd;
	BigDecimal codParc;
	
	public void ocorrencia(BigDecimal numContrato, BigDecimal codusu) throws Exception {
		
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		SessionHandle hnd = JapeSession.open();
		
		System.out.println("eNTROU NO METODO INSERIR OCORRENCIA");
		
		ResultSet query = nativeSql.executeQuery("SELECT "
				+ " CODPROD, CODPARC FROM TCSOCC WHERE NUMCONTRATO = " + numContrato);
		
		if (query.next())
		
			System.out.println("Caiu no if da ocorrencia");
		
			codProd = query.getBigDecimal("CODPROD");
			codParc = query.getBigDecimal("CODPARC");
		
		String descricao = "Renovação Anual.";
		
		try {
			
			System.out.println("Entrou no try para inserir ocorrencia ");
			
			System.out.println("Inserindo na tabela Ocorrencia Contrato");

			EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
			DynamicVO dynamicVO1 = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("OcorrenciaContrato");

			dynamicVO1.setProperty("CODCONTATO", new BigDecimal(1));
			dynamicVO1.setProperty("DTOCOR", agora);
			dynamicVO1.setProperty("NUMCONTRATO", numContrato);
			dynamicVO1.setProperty("DESCRICAO", descricao);
			dynamicVO1.setProperty("CODPROD", codProd);
			dynamicVO1.setProperty("CODOCOR", new BigDecimal(5));
			dynamicVO1.setProperty("CODUSU", codusu);
			dynamicVO1.setProperty("AD_GERADOSPROVISAO", "N");
			dynamicVO1.setProperty("CODPARC", codParc);
			
			PersistentLocalEntity createEntity = dwfFacade.createEntity("OcorrenciaContrato", (EntityVO) dynamicVO1);
			DynamicVO save = (DynamicVO) createEntity.getValueObject();
			
			System.out.println("Dpois de ter inserido ocorrencia ");
			
		} catch (Exception e) {
			String msg = "Erro na inclusao dos Itens " + e.getMessage();
			System.out.println(msg);
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		} finally {
			JapeSession.close(hnd);
		}
	}

}
