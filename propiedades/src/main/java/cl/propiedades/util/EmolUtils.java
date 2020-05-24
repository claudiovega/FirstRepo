package cl.propiedades.util;


public class EmolUtils {

	public EmolUtils() {
		// TODO Auto-generated constructor stub
	}
	public static String getCodigoEmolFromUrl(String urlPublicacion){
		String result = "";
		if (urlPublicacion != null && !urlPublicacion.trim().isEmpty()){
			String tmpUrl = "";
			tmpUrl = urlPublicacion.replace("-cod", "\\|");
			String[] arr = tmpUrl.split("\\|");
			String strTmp = arr[1];
			String[] arr2 = strTmp.split(".html");
			strTmp = arr2[0];

			strTmp = "cod"+strTmp.replace(".html", "");
			result = strTmp.trim();
			
		}
		

		return result;
	}
	public static String getTipoPropiedad(String htmlTipoPropiedad){
		String result = "";
		if (htmlTipoPropiedad != null && !htmlTipoPropiedad.trim().isEmpty()){
			

			String[] arr = htmlTipoPropiedad.split("/");
			result = arr[0].trim();
			
			
		}
		
		
		return result;
	}
	public static String getUrlFromElement(String element){
		String result = "";
		String[] arrTemp = element.split("target=");
		String  strTemp= arrTemp[0];
		strTemp = strTemp.replace("<a href=\"", "");
		strTemp = strTemp.replace("\"", "");
		strTemp = strTemp.replace("<h3>", "");
		strTemp = strTemp.replace("</h3>", "");
		strTemp = strTemp.replace("</a>", "");
		strTemp = strTemp.replace("<div class=\"thumbnail\">", "");
		strTemp = strTemp.replace("<div class=\"numimages\">", "");
		strTemp = strTemp.replace("</div> </a>", "");
		strTemp = strTemp.replace("</div>", "");
		strTemp = strTemp.replace("<a href=\"", "");
		strTemp = strTemp.replace("\"", "");
		strTemp = strTemp.replace("", "");

		result = "http://www.propiedades.emol.com"+strTemp.trim();

		return result;
	
	}
}
