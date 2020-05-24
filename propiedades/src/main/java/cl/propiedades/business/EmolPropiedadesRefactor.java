package cl.propiedades.business;

import cl.propiedades.dao.OracleEmolDAO;
import cl.propiedades.dao.OracleScrapInmobiliarioBatchDAO;
import cl.propiedades.enums.PortalesEnum;
import cl.propiedades.factory.Factory;
import cl.propiedades.factory.Portal;
import cl.propiedades.factory.ScrapExtractor;
import cl.propiedades.util.*;
import cl.propiedades.vo.InformacionInmobiliariaTempVO;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
public class EmolPropiedadesRefactor implements Portal {
	@Autowired
	Factory factoryExtractor;

	private static final String userAgent = "Mozilla/5.0 (jsoup)";
	private static final int timeout = 60 * 1000;
	private String path = null;

	String tipoPropiedad = "";
	String tipoTransaccion = "";
	String precioDesde = "";
	String precioHasta = "";
	String moneda = "";
	String dormitorios = "";


	public String getTipoPropiedad() {
		return tipoPropiedad;
	}
	public void setTipoPropiedad(String tipoPropiedad) {
		this.tipoPropiedad = tipoPropiedad;
	}

	@Override
	public void setFechaDesde(String fechaDesde) {

	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getPrecioDesde() {
		return precioDesde;
	}
	public void setPrecioDesde(String precioDesde) {
		this.precioDesde = precioDesde;
	}
	public String getPrecioHasta() {
		return precioHasta;
	}
	public void setPrecioHasta(String precioHasta) {
		this.precioHasta = precioHasta;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(String dormitorios) {
		this.dormitorios = dormitorios;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void run() throws Exception {
		List<String> listaLineas = new ArrayList<String>();
		Double uf = new Double(WebDataUtils.getUfToday());
		String selector = "";
		String txtname = "";
		String casaDepto = "";
		int MaxPaginas = 4;//casas
		String totalAvisos = "";
		String strLista = "Portal|CodigoPublicacion|TipoPropiedad|TipoTransaccion|Fechapublicacion|TituloPublicacion|Comuna|Dormitorios|Ba�os|Construido|Terreno|PrecioUf|PrecioPesos|Url|uf "+uf;
		String url = "";
		String monedaPesos = "&m=pesos";
		String monedaUf = "&m=uf";
		String venta = "o=Venta";
		String arriendo = "o=Arriendo";
		String region = "&r=Metropolitana%20de%20Santiago";
		String departamento = "&tp=Departamento";
		String casa = "&tp=Casa";
		String precioDesdeParam = "&pd=";
		String precioHastaParam = "&ph=";
		String dormitoriosParam = "&d=";
		url = "http://www.propiedades.emol.com/propiedades/buscar?";
		Integer orden = 0;
		OracleScrapInmobiliarioBatchDAO oradel = new OracleScrapInmobiliarioBatchDAO();
		oradel.deleteRecordFromTable("Emol", tipoPropiedad, tipoTransaccion);
		if (tipoTransaccion.equals("Venta")){
			url = url+venta;	
		}else{
			this.path =this.path+"\\Arriendo"; 
			url = url+arriendo;	
		}
		url = url+region;
		if (tipoPropiedad.equals("Casa")){
			url = url+casa;
		}else{
			url = url+departamento;
		}
		Integer dormi = null;
		if (!dormitorios.trim().isEmpty()){
			dormi = Integer.parseInt(dormitorios.trim());
			url = url+dormitoriosParam+dormitorios.trim();
		}
		if (!precioDesde.trim().isEmpty()){
			url = url+precioDesdeParam+precioDesde;
		}
		if (!precioHasta.trim().isEmpty()){
			url = url+precioHastaParam+precioHasta;
		}
		if (moneda.equals("UF")){
			url = url+monedaUf;
		}else{
			url = url+monedaPesos;
		}
		ScrapExtractor scrapExtractor = factoryExtractor.getBean(PortalesEnum.EMOL.getPortal());
		txtname = "EmolPropiedades"+tipoPropiedad+tipoTransaccion;
		boolean numPagsGeted = false;
		OracleEmolDAO oed = new OracleEmolDAO();
		LocalDate fechaTope = oed.getMaxDate("Emol", tipoPropiedad, tipoTransaccion,dormi);
		boolean salirPorFecha = false;
		for(int x = 0; x<=MaxPaginas;x++){
			System.out.println(url+"&p="+x);
			try {
				Document doc = Jsoup.connect(url+"&p="+x).userAgent(userAgent).timeout(timeout).get();
				listaLineas = new ArrayList<String>();
				/***********************************************************/
				String strListaTemp = "";

				if (!numPagsGeted){
					MaxPaginas = scrapExtractor.getNumeroPaginas(doc);

					strLista = strLista+"|Total Avisos: "+totalAvisos;
					strLista = strLista+"\n";
					numPagsGeted = true;
				}
				System.out.println("Scraping page: "+casaDepto+" propiedades.emol.com "+x+" of "+MaxPaginas);
				
				  String codigoPublicacion = "";
			        String name = "";
			        //String rooms = "";
			        String construidos = "";
			        String terreno = "";
			        String time = "";
			        String comuna = "";
			        String precioUf = "";
			        String precioPesos = "";
			        
			        String dorm = "";
					String baños = "";
				for (Integer i = 1; i<= 20 ; i++){
					//orden++;

					selector = "#results > div:nth-child("+i+")";

					String[] arrElement = doc.select(selector).toString().split("\n");
					String strUrl = scrapExtractor.getUrl(arrElement);
					codigoPublicacion = scrapExtractor.getCodigoPublicacion(strUrl);
					name = scrapExtractor.getTituloPublicacion(arrElement);
					time = scrapExtractor.getFechaPublicacion(arrElement);
					construidos = scrapExtractor.getConstruidos(arrElement);
					terreno = scrapExtractor.getTerreno(arrElement);
					comuna = scrapExtractor.getNombreComuna(arrElement);
					//<a href="/propiedad/ficha/departamento-en-arriendo-en-estacion-central-2-dormitorios-cod34353580.html" target="_blank"> <img src="http://www.economicos.cl/assets/img/no_image.png"> </a>
					for(String strLineaElemento : arrElement){
						if (salirPorFecha){
							i=1000;
							x=MaxPaginas+1000;
							break;
						}
						//System.out.println(strLineaElemento);
						if (strLineaElemento.contains("href=")){
							String[] strSplt = strLineaElemento.split("target=\"_blank\"");
							String strTmp = strSplt[0];
							
							strTmp = strTmp.replace("<a", "");
							strTmp = strTmp.replace("href=", "");
							strTmp = strTmp.replace("\"", "");
							strTmp = strTmp.replace("</a>", "");
							strTmp = strTmp.replace(">", "");
							strTmp = strTmp.replace("<", "");
							
							strUrl = "http://www.propiedades.emol.com"+strTmp.trim();

							break;
						}
					}

					if (!strUrl.trim().isEmpty()){
						Document subDoc = Jsoup.connect(strUrl).userAgent(userAgent).timeout(timeout).get();
						if (!strUrl.contains("proyecto")){


							Elements subElName = subDoc.select("#ficha_Left > div.Header_ficha > h1");
							name = subElName.toString().replace("<h1 class=\"titulo_Ficha\">","");
							name = name.replace("</h1>", "");
							Elements subElComuna = subDoc.select("#ficha_Left > div.Header_ficha > p");
							String strComunaTmp = subElComuna.toString().replace("<p class=\"bajada_caracteristicas\">", "");
							strComunaTmp = strComunaTmp.replace("</p>","");
							if (strComunaTmp.contains(",")){
								String[] arrComuna = strComunaTmp.split(",");
								comuna = arrComuna[arrComuna.length-1].trim();
							}else{
								comuna = strComunaTmp.trim();
							}
							Elements subElPrecio = subDoc.select("#ficha_Left > div.Header_ficha > h2");
							
							precioUf= subElPrecio.toString().replace("<h2 class=\"precio_propiedad\">", "");
							precioUf = precioUf.replace("</h2>", "");
							precioUf = precioUf.replace(".", "");
							precioUf = precioUf.replace(",", ".");
							if (precioUf.contains("UF")){
								precioUf = precioUf.replace("UF","").trim();
								
								precioPesos = NumberUtils.convertUfAPesos(uf, precioUf.trim());
							}else if (precioUf.contains("$")){
								
								precioUf = precioUf.replace("$","").trim();
								precioPesos= precioUf.trim();
								precioUf = NumberUtils.convertPesosAUf(uf, precioPesos);
								
							}else if (precioUf.contains("USD") || precioUf.contains("usd")){
								System.out.println();
							}
							if (!time.trim().isEmpty()){
								if (fechaTope != null){
									if (fechaTope.compareTo(DateUtils.stringToLocalDate(time, ScrapConstants.DATE_MASK_Emol))>0){
										System.out.println("Se sale del ciclo por fecha");
										break;
									}	
								}
							}
							for(int z = 1; z<10; z++){
								Elements subEl = subDoc.select("#ficha_Left > div.Header_ficha > div:nth-child("+z+")");
								if (subEl.toString().contains("Fecha")){
									String strTmp = subEl.toString().replace("<div class=\"List_detail_header\">", "");
									strTmp = strTmp.replace("</div>", "");
									strTmp = strTmp.replace("</span>", "");
									strTmp = strTmp.replace("<strong>", "");
									strTmp = strTmp.replace("</strong>", "");
									String[] arrTmp = strTmp.split("<span>"); 
									for (int a = 0; a<arrTmp.length;a++){
										if (arrTmp[a].contains("Fecha")){
											time = arrTmp[a].replace("Fecha publicaci�n:", "").trim();

											break;
										}
									}
								}
								if (subEl.toString().contains("Dormitorios")){
									String strTmp = subEl.toString().replace("<div class=\"List_detail_header\">", "");
									strTmp = strTmp.replace("</div>", "");
									strTmp = strTmp.replace("</span>", "");
									strTmp = strTmp.replace("N.", "");
									strTmp = strTmp.replace("M2", "");
									String[] arrTmp = strTmp.split("<span>"); 

									for (int a = 0; a<arrTmp.length;a++){
										if (arrTmp[a].contains("Dormitorios")){
											dorm = arrTmp[a].replace("Dormitorios:", "").trim();
										}
										if (arrTmp[a].contains("ba�os")){
											baños = arrTmp[a].replace("baños:", "").trim();
										}
										if (arrTmp[a].contains("construidos")){
											construidos = arrTmp[a].replace("construidos:", "").trim();
										}
										if (arrTmp[a].contains("terreno")){
											terreno = arrTmp[a].replace("terreno:", "").trim();
										}

									}
									
								}

								

							}
						}else{
							SimpleDateFormat sdf = new SimpleDateFormat(ScrapConstants.DATE_MASK_Emol); // Set your date format
							time = sdf.format(new Date());
							name = Jsoup.parse(subDoc.select("#content > div.cont_corredoras > div > div.tab-info-controls > div > div.tab-info-general > h1").toString()).text();
							
							//Elements subElPrecio = subDoc.select("#precio");
							precioUf = Jsoup.parse(subDoc.select("#precio").toString()).text();
							
							
							precioUf = precioUf.replace("Precio Desde", "");
							precioUf = precioUf.replace(".","");
							precioUf = precioUf.replace("(*)","");
							//precioUf = precioUf.replace("UF","").trim();
							if (precioUf.contains("$")){
								precioUf = precioUf.replace("$","").trim();
								precioPesos= precioUf.trim();
								precioUf = NumberUtils.convertPesosAUf(uf, precioPesos);
							}else if (precioUf.contains("UF")){
								precioUf = precioUf.replace("UF","").trim();
								
								precioPesos = NumberUtils.convertUfAPesos(uf, precioUf);
							}



							Elements subElComuna = subDoc.select("#content > div.cont_corredoras > div > div.tab-info-controls > div > div.tab-info-general");
							String strTmp =subElComuna.toString();

							String[] artmp= strTmp.split("<br>"); 

							String[] atmp = artmp[0].split("</h2>");
							comuna = Jsoup.parse(atmp[0]).text();
							comuna = comuna.replace(", RM", "");
							String[] arrTmp = comuna.split("Horario");
							comuna = arrTmp[0];
							arrTmp = comuna.split(",");
							comuna = arrTmp[arrTmp.length-1].trim();
							comuna = comuna.replace("�", "e");
						}	
						strLista = strLista+"Emol"+"|"+codigoPublicacion+"|"+tipoPropiedad+"|"+tipoTransaccion+"|"+time+"|"+name+"|"+comuna+"|"+dorm+"|"+baños+"|"+construidos+"|"+terreno+"|"+precioUf.trim()+"|"+precioPesos+"|"+strUrl+"\n";
						listaLineas.add("Emol"+"|"+codigoPublicacion+"|"+tipoPropiedad+"|"+tipoTransaccion+"|"+time+"|"+name+"|"+comuna+"|"+dorm+"|"+baños+"|"+construidos+"|"+terreno+"|"+precioUf.trim()+"|"+precioPesos+"|"+strUrl);
					}
					

				}
				insertIntoDatabase(listaLineas);
				listaLineas = new ArrayList<String>();
				/*******************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

      
        List<InformacionInmobiliariaTempVO> result =  oed.getDataToInsert("Emol", tipoPropiedad, tipoTransaccion);
        insertFromTmpToFinal(result);
        System.out.println("Proceso Emol Terminado");
		//FileUtils.writeFile(this.path+"\\"+txtname+".txt", strLista);
	}
	private void insertFromTmpToFinal(List<InformacionInmobiliariaTempVO> lista){
		OracleEmolDAO omld = new OracleEmolDAO();
		for(InformacionInmobiliariaTempVO obj : lista){
			try {
				omld.insertInformacionInmobiliaria(obj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void insertIntoDatabase(List<String> lista){
		OracleScrapInmobiliarioBatchDAO ocbd = new OracleScrapInmobiliarioBatchDAO();
		try {
			//ocbd.batchInformacionInmobiliariaLoader(lista, ScrapConstants.DATE_MASK_Emol);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getSubUrl( Element subElement) {
		String result = "";
		if (subElement.toString().contains("thumbnail")  
				&&  !subElement.toString().contains("detail")){
			String  strTemp= subElement.toString();
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

			String[] arrTemp = strTemp.split(">");
			String[] arrTemp2 = arrTemp[1].split("target=_blank");
			result = "http://www.propiedades.emol.com"+arrTemp2[0].trim();
		}
		return result;
	}
	private int getNumeroPaginas(Document doc) {
		int MaxPaginas;
		Elements elNumeroPaginas = null;
		String strNumeroPaginas = "";
		for(int s =0;s<15;s++){

			elNumeroPaginas = doc.select("#content > div.controls-panel > div.paginator.top > ul > li:nth-child("+s+")");	
			if (elNumeroPaginas.toString().contains("P�gina") && !elNumeroPaginas.toString().contains("href")){
				strNumeroPaginas = elNumeroPaginas.toString();
				break;
			}
		}


		strNumeroPaginas = strNumeroPaginas.replace("|", "");
		strNumeroPaginas = strNumeroPaginas.replace("&nbsp", "");
		strNumeroPaginas = strNumeroPaginas.replace(";", "");
		strNumeroPaginas = strNumeroPaginas.replace("</li>", "");
		strNumeroPaginas = strNumeroPaginas.replace("<li>", "");
		strNumeroPaginas = strNumeroPaginas.replace("P�gina", "");


		String[] arrNumPag = strNumeroPaginas.split("de");
		MaxPaginas = Integer.parseInt(arrNumPag[1].trim()) ;
		return MaxPaginas;
	}

	/**
	 * Format an Element to plain-text
	 * @param element the root element to format
	 * @return formatted text
	 */
	public String getPlainText(Element element) {
		FormattingVisitor formatter = new FormattingVisitor();
		NodeTraversor.traverse(formatter, element); // walk the DOM, and call .head() and .tail() for each node

		return formatter.toString();
	}

	// the formatting rules, implemented in a breadth-first DOM traverse
	private class FormattingVisitor implements NodeVisitor {
		private static final int maxWidth = 80;
		private int width = 0;
		private StringBuilder accum = new StringBuilder(); // holds the accumulated text

		// hit when the node is first seen
		public void head(Node node, int depth) {
			String name = node.nodeName();
			if (node instanceof TextNode)
				append(((TextNode) node).text()); // TextNodes carry all user-readable text in the DOM.
			else if (name.equals("li"))
				append("\n * ");
			else if (name.equals("dt"))
				append("  ");
			else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
				append("\n");
		}

		// hit when all of the node's children (if any) have been visited
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5"))
				append("\n");
			else if (name.equals("a"))
				append(String.format(" <%s>", node.absUrl("href")));
		}

		// appends text to the string builder with a simple word wrap method
		private void append(String text) {
			if (text.startsWith("\n"))
				width = 0; // reset counter if starts with a newline. only from formats above, not in natural text
			if (text.equals(" ") &&
					(accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
				return; // don't accumulate long runs of empty spaces

			if (text.length() + width > maxWidth) { // won't fit, needs to wrap
				String words[] = text.split("\\s+");
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					boolean last = i == words.length - 1;
					if (!last) // insert a space if not the last word
						word = word + " ";
					if (word.length() + width > maxWidth) { // wrap and reset counter
						accum.append("\n").append(word);
						width = word.length();
					} else {
						accum.append(word);
						width += word.length();
					}
				}
			} else { // fits as is, without need to wrap text
				accum.append(text);
				width += text.length();
			}
		}

		@Override
		public String toString() {
			return accum.toString();
		}
	}

	@Override
	public String getTotalAvisos(Document doc) {
		// 
		String result = "";
		try {
			result = doc.select("#title > h1 > small").text();
			result = result.replace("(", ":");
			result = result.replace("resultados", "");
			result = result.replace(")", "");

			String[] arr = result.split(":");
			result = arr[arr.length-1].trim();
		} catch (Exception ignored) {
			// TODO Auto-generated catch block

		}
		return result;
	}
	@Override
	public void setFechaCasaHasta(String precio) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getFechaCasaHasta() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setFechaDeptoHasta(String precio) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getFechaDeptoHasta() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setProcesoCasa(boolean procesoCasa) {
		// TODO Auto-generated method stub

	}
	@Override
	public void setPrecioCasaHasta(String precio) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getPrecioCasaHasta() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPrecioDeptoHasta(String precio) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getPrecioDeptoHasta() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPrecioCasaDesde(String precio) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getPrecioCasaDesde() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPrecioDeptoDesde(String precio) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getPrecioDeptoDesde() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void setRegion(String region) {

    }
}