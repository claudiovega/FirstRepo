package cl.propiedades.util;

import java.text.DecimalFormat;

public class NumberUtils {

	public NumberUtils() {
		// TODO Auto-generated constructor stub
	}
	public static String convertPesosAUf(Double uf, String value) {
		String result = "";
		if (value != null && !value.trim().isEmpty()){
			try {
				value = value.replace("$", "").trim();
				Double dTmp = new Double(value);
				Double tmpRes = dTmp/uf;
				DecimalFormat df = new DecimalFormat("#########.##");
				result = df.format(tmpRes);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return result;
	}
	public static String convertUfAPesosMercLibre(Double uf, String strPrecioUf) {
		String result = "";
		if (strPrecioUf != null && !strPrecioUf.trim().isEmpty()){
			try {
				strPrecioUf = strPrecioUf.replace("UF", "");
				//strPrecioUf = strPrecioUf.replace(".", "");
				strPrecioUf = strPrecioUf.replace(",", ".");
				Double ufTmp = new Double(strPrecioUf.trim());
				Double tmpPrecioPesos = ufTmp*uf;
				DecimalFormat df = new DecimalFormat("###############");
				result = df.format(tmpPrecioPesos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return result;
	}
	public static String convertDolarAPesos(Double dolar, String strPrecioUS) {
		String result = "";
		if (strPrecioUS != null && !strPrecioUS.trim().isEmpty()){
			try {
				strPrecioUS = strPrecioUS.replace("US", "");
				strPrecioUS = strPrecioUS.replace(".", "");
				Double ufTmp = new Double(strPrecioUS.trim());
				Double tmpPrecioPesos = ufTmp*dolar;
				DecimalFormat df = new DecimalFormat("###############");
				result = df.format(tmpPrecioPesos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return result;
	}
	public static String convertUfAPesos(Double uf, String strPrecioUf) {
		String result = "";
		if (strPrecioUf != null && !strPrecioUf.trim().isEmpty()){
			try {
				strPrecioUf = strPrecioUf.replace("UF", "");
				strPrecioUf = strPrecioUf.replace(".", "");
				strPrecioUf = strPrecioUf.replace(",", ".");
				Double ufTmp = new Double(strPrecioUf.trim());
				Double tmpPrecioPesos = ufTmp*uf;
				DecimalFormat df = new DecimalFormat("###############");
				result = df.format(tmpPrecioPesos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return result;
	}
	public static boolean isNumber(String number){
		boolean result = false;
		try{
			Integer.parseInt(number);
			result = true;
		}catch(Exception e){
			result = false;
		}
		return result;
	}
}
