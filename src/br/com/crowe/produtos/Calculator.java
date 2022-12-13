package br.com.crowe.produtos;

import java.math.BigDecimal;
import java.util.Locale;

public class Calculator {
	
	public static BigDecimal VALOR_ADICIONAL = new BigDecimal(2);
	
	public static BigDecimal calcular (BigDecimal vlrAdicional) {
         return vlrAdicional.divide(VALOR_ADICIONAL);
	}

}
