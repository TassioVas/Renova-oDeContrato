package br.com.crowe.produtos;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;

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

public class ValorAdicional {

	Timestamp agora = new Timestamp(System.currentTimeMillis());

	BigDecimal vlrAdicional;
	BigDecimal nroDecimoTerceiro;
	BigDecimal vlrAdicional2;

	String obs;

	public void adicional(BigDecimal numContrato, BigDecimal codProd) throws Exception {

		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		SessionHandle hnd = JapeSession.open();

		System.out.println("Entrou no metodo Adiciona, classe valor adicional numcontrato " + numContrato);
		System.out.println("codprod : " + codProd);

		// boolean update = nativeSql
		// .executeUpdate("DELETE FROM AD_VLRADICIONAL WHERE NUMCONTRATO = " +
		// numContrato);

		ResultSet query = nativeSql.executeQuery(
				"select " + " OBSERVACAO, VLRREAJUSTADO from AD_RENOVCONT WHERE NUMCONTRATO = " + numContrato);

		while (query.next()) {

			obs = query.getString("OBSERVACAO");
			vlrAdicional = query.getBigDecimal("VLRREAJUSTADO");

			System.out.println("Entrou no while para vlr adicional");
			System.out.println("vlr ajustado " + vlrAdicional);

			ResultSet rs = nativeSql.executeQuery(
					"SELECT NROPARCDECTER, VLRREAJUSTADO FROM AD_RENOVCONT WHERE NUMCONTRATO = " + numContrato);

			while (rs.next()) {

				nroDecimoTerceiro = rs.getBigDecimal("NROPARCDECTER");

				if (nroDecimoTerceiro.equals(new BigDecimal(2))) {
					vlrAdicional = Calculator.calcular(vlrAdicional);
				}

				System.out.println("Valor adicional antes de converter :" + vlrAdicional2);

				// System.out.println("Vlr calculado "+ vlr);
				System.out.println("numContrato " + numContrato);
				System.out.println("dtvenc :" + agora);
				System.out.println("observação :" + obs);
				System.out.println("codprod :" + codProd);

				//DecimalFormat df = new DecimalFormat("##.####");
				//System.out.println("Formatado  : " + df.format(vlrAdicional2));
				try {

					System.out.println("inserindo vlr adicional");

					EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
					DynamicVO dynamicVO1 = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("AD_VLRADICIONAL");

					dynamicVO1.setProperty("VLRADICIONAL", vlrAdicional);
					dynamicVO1.setProperty("NUMCONTRATO", numContrato);
					dynamicVO1.setProperty("DTVENC", agora);
					dynamicVO1.setProperty("OBSERVACAO", obs);
					dynamicVO1.setProperty("CODPROD", codProd);

					PersistentLocalEntity createEntity = dwfFacade.createEntity("AD_VLRADICIONAL",
							(EntityVO) dynamicVO1);
					DynamicVO save = (DynamicVO) createEntity.getValueObject();

				} catch (Exception e) {
					String msg = "Erro na inclusao dos Itens " + e.getMessage();
					System.out.println(msg);
				}
			}
		}
	}

}
