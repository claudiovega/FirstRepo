package cl.propiedades.business;

import cl.propiedades.dao.OracleScrapInmobiliarioBatchDAO;
import cl.propiedades.dao.OracleYapoDAO;
import cl.propiedades.enums.PortalesEnum;
import cl.propiedades.enums.RegionesYapoEnum;
import cl.propiedades.enums.TipoInmuebleEnum;
import cl.propiedades.enums.TipoTransaccionEnum;
import cl.propiedades.factory.Factory;
import cl.propiedades.factory.Portal;
import cl.propiedades.factory.ScrapExtractor;
import cl.propiedades.helper.LoggerHelper;
import cl.propiedades.repository.ErroresDAO;
import cl.propiedades.util.DateUtils;
import cl.propiedades.util.ListUtils;
import cl.propiedades.util.ScrapConstants;
import cl.propiedades.util.WebDataUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class YapoChile implements Portal {

	@Autowired
	Factory factoryExtractor;

	@Autowired
	ErroresDAO erroresDAO;

    @Autowired
    LoggerHelper loggerHelper;

    @Autowired
	OracleScrapInmobiliarioBatchDAO ocbd;

    private static final String userAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.8; it; rv:1.93.26.2658) Gecko/20141026 Camino/2.176.223 (MultiLang) (like Firefox/3.64.2268)0";
	private static final Logger LOGGER = LoggerFactory.getLogger(YapoChile.class);
    private static final int timeout = 60 * 1000;
    private String path = null;
    private boolean procesoCasa = true;
    
    private String precioCasaHasta = "";
    private String precioDeptoHasta = "";

    private String precioCasaDesde = "";
    private String precioDeptoDesde = "";

    private String tipoTransaccion = "";
	private String tipoPropiedad = "";
	private String moneda = "";
	private String precioDesde = "";
	private String precioHasta = "";
	private String dormitorios = "";
	private String region = "";
    
	public String getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(String dormitorios) {
		this.dormitorios = dormitorios;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getTipoPropiedad() {
		return tipoPropiedad;
	}
	public void setTipoPropiedad(String tipoPropiedad) {
		this.tipoPropiedad = tipoPropiedad;
	}

	@Override
	public void setFechaDesde(String fechaDesde) {

	}

	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
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
	public String getPrecioCasaDesde() {
		return precioCasaDesde;
	}
	public void setPrecioCasaDesde(String precioCasaDesde) {
		this.precioCasaDesde = precioCasaDesde;
	}
	public String getPrecioDeptoDesde() {
		return precioDeptoDesde;
	}

	@Override
	public void setRegion(String region) {
		this.region = region;
	}

	public void setPrecioDeptoDesde(String precioDeptoDesde) {
		this.precioDeptoDesde = precioDeptoDesde;
	}
	public String getPrecioCasaHasta() {
		return precioCasaHasta;
	}
	public void setPrecioCasaHasta(String precioCasaHasta) {
		this.precioCasaHasta = precioCasaHasta;
	}
	public String getPrecioDeptoHasta() {
		return precioDeptoHasta;
	}
	public void setPrecioDeptoHasta(String precioDeptoHasta) {
		this.precioDeptoHasta = precioDeptoHasta;
	}
	public boolean isProcesoCasa() {
		return procesoCasa;
	}
	public void setProcesoCasa(boolean procesoCasa) {
		this.procesoCasa = procesoCasa;
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
        String txtname = "";
       
        String totalAvisos = "";
        int MaxPaginas = 122;//
        String url = "";
        String urlParams = "";
        OracleScrapInmobiliarioBatchDAO oradel = new OracleScrapInmobiliarioBatchDAO();
		oradel.deleteRecordFromTable("Yapo", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
        
        String strLista = "Portal|CodigoPublicacion|TipoPropiedad|TipoTransaccion|Fechapublicacion|TituloPublicacion|Comuna|Dormitorios|Ba�os|Construido|Terreno|PrecioUf|PrecioPesos|Url|uf ";
		if (RegionesYapoEnum.Chile.getRegion().equals(region)){
			url = "https://www.yapo.cl/"+region+"/"+tipoTransaccion+"?ca=15_s&ret="+tipoPropiedad+"&cg=1220";

		}else{
			url = "https://www.yapo.cl/"+region+"/"+tipoTransaccion+"?ca="+RegionesYapoEnum.getCodigoRegion(region)+"_s&l=0&w=1&cmn="+"&ret="+tipoPropiedad;
		}

        Integer numDorm = null;

        OracleYapoDAO oyd = new OracleYapoDAO();
        LocalDate fechaTope = oyd.getMaxDate("Yapo", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion),numDorm);
		if (fechaTope != null) {
			fechaTope = fechaTope.minusDays(1L);
		}
		LOGGER.info("fechaTope: "+fechaTope);
//		else{
//			fechaTope = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String date = "01/01/2020";
//
//			//convert String to LocalDate
//			fechaTope = LocalDate.parse(date, formatter);
//		}

		ScrapExtractor scrapExtractor = factoryExtractor.getBean(PortalesEnum.YAPO.getPortal());
        url = url+urlParams+"&o=";
        txtname= "Yapo"+tipoPropiedad+tipoTransaccion;
        boolean numPagsGeted = false;
        boolean salirPorFecha = false;
        String urlParaLogError = "";
        loggerHelper.logProceso("Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "INICIO Proceso Inmobiliario Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
		Integer numeroPagina = null;
		Integer posicionAviso = null;
        for(int x = 1; x<=MaxPaginas;x++){
			numeroPagina = x;
			if (salirPorFecha){
				break;
			}
			LOGGER.info("Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
			LOGGER.info(url+x);
            loggerHelper.logProceso("Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), url+x +" of "+MaxPaginas);

            try {

				try {
					urlParaLogError = url+x;
					Document doc = Jsoup.connect(url+x).userAgent(userAgent).timeout(timeout).get();
					listaLineas = new ArrayList<String>();
					if (!numPagsGeted){
						

					    MaxPaginas = scrapExtractor.getNumeroPaginas(doc); //getNumpages(doc,"#listing-top > table > tbody > tr:nth-child(4) > td > div > span.nohistory.FloatRight > a");
					    totalAvisos = scrapExtractor.getTotalAvisos(doc);
					    strLista = strLista+"|Total Avisos: "+totalAvisos;
					    strLista = strLista+"\n";
					    numPagsGeted = true;
					}

					LOGGER.info("Scraping page: "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion)+" Yapo "+x+" of "+MaxPaginas);
					Elements elements  = doc.select("#hl > tbody"); //.elementData(0).childNodes;
					Element element = elements.get(0);

					for(int i = 0; i<element.childNodeSize(); i++){
						posicionAviso = i;
						if (salirPorFecha){
							break;
						}
						Node node = element.childNode(i);
						if(!node.toString().trim().isEmpty()){

							String codigoPublicacion = scrapExtractor.getCodigoPublicacion(node);
							String strUrl = scrapExtractor.getUrl(node);
							String strComuna = scrapExtractor.getNombreComuna(node);
							String strRegion = scrapExtractor.getNombreRegion(node);

							String strPrecioUf = scrapExtractor.getPrecioUF(node, uf);
							String strPrecioPesos = scrapExtractor.getPrecioPesos(node, uf);
							String strName = scrapExtractor.getTituloPublicacion(node);

							String strFecha = scrapExtractor.getFechaPublicacion(node);
							String strConstruido = scrapExtractor.getConstruidos(node);
							String strRooms = scrapExtractor.getDormitorios(node);
							String strBanos = scrapExtractor.getToilet(node);
							String strTerreno = scrapExtractor.getTerreno(node);
							if (fechaTope != null && !StringUtils.isEmpty(strFecha)){
								if (fechaTope.compareTo(DateUtils.stringToLocalDate(strFecha, ScrapConstants.DATE_MASK_Yapo))>0){
									LOGGER.info("Yapo Se sale del ciclo por fecha, FechaTope: "+fechaTope.toString()+" FechaEncontrada: "+strFecha);

									loggerHelper.logProceso("Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), " Se sale del ciclo por fecha, FechaTope: "+fechaTope.toString()+" FechaEncontrada: "+strFecha);

									salirPorFecha = true;
									i = 1000;
									x = MaxPaginas+1000;
									break;
								}
							}
							if(!codigoPublicacion.trim().isEmpty()  ){

								listaLineas.add("Yapo|"+codigoPublicacion+"|"+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+"|"+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion)+"|"+strFecha+"|"+strName.trim()+"|"+strComuna.trim()+"|"+strRooms+"|"+strBanos+"|"+strConstruido.trim()+"|"+strTerreno+"|"+strPrecioUf.trim()+"|"+strPrecioPesos+"|"+strUrl+"|"+strRegion);

							}

						}

					}



					insertIntoDatabase(listaLineas, numeroPagina, posicionAviso);
					listaLineas = new ArrayList<String>();

					Long delay = (long) (Math.random() * 30000 + 1000);
					Thread.sleep(delay);
				} catch (Exception e) {
                    loggerHelper.logErroresProceso("Yapo", urlParaLogError, TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad),  e);

				}
				
			} catch (Exception e) {
                loggerHelper.logErroresProceso("Yapo", urlParaLogError, TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad),  e);

            }
        }

        OracleYapoDAO omld = new OracleYapoDAO();
        omld.insertDataFromTmp("Yapo",TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));

        LOGGER.info("Yapo Proceso terminado");
        loggerHelper.logProceso("Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "FIN Proceso Inmobiliario Yapo "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));


    }

    private void insertIntoDatabase(List<String> lista, Integer pagina, Integer aviso){
		//OracleScrapInmobiliarioBatchDAO ocbd = new OracleScrapInmobiliarioBatchDAO();
		this.ocbd.batchInformacionInmobiliariaLoader(lista, ScrapConstants.DATE_MASK_Yapo, pagina, aviso);
	}
	public static String getHabitaciones(Document doc) {
		String result = "";
		
		for(int i = 1; i<12;i++){

			String str = doc.select("#content > section.box.da-wrapper > article > div.box.info > div.details > table > tbody > tr:nth-child("+i+")").toString();
			if (str.contains("Dormitorios")){

				result = Jsoup.parse(str).text();
				
				
				result = result.replace("dormitorios", "");
				result = result.replace("Dormitorios", "");
				result = result.replace("dormitorio", "");
				result = result.replace("Dormitorio", "");
				
				result = result.replace("o", "");
				result = result.replace("m�s", "");
				break;
			}
		}	
		
		
		return result.trim();
	}
	public static String getTerreno(Document doc) {
		String result = "";
		
		for(int i = 1; i<12;i++){
			
			String str = doc.select("#content > section.box.da-wrapper > article > div.box.info > div.details > table > tbody > tr:nth-child("+i+")").toString();
			if (str.contains("Superficie total")){
				
				result = Jsoup.parse(str).text();
				result = result.replace("Superficie total", "");
				result = result.replace("m�", "");
				break;
			}
		}
		return result.trim();
	}
    public static String getAmountAttr(Document doc, String tipoMoneda) {
		String result = "";
		for(int i = 1; i<12;i++){
			
			String str = doc.select("#content > section.box.da-wrapper > article > div.box.info > div.details > table > tbody > tr:nth-child("+i+")").toString();
			if (str.contains("Precio")){
				
				//Precio UF 3.800,00 ($ 102.146.166)*
				result = Jsoup.parse(str).text();
				if (tipoMoneda.equals("UF")){
					if (result.indexOf("UF") < result.indexOf("(")){
						result = result.replace(")", "");
						result = result.replace("(", ";");
						result = result.replace("*", "");
						result = result.replace("Precio", "");
						result = result.replace(".", "");
						result = result.replace("UF", "");
						String[] arr = result.split(";");
						result= arr[0];
						
					}else{
						result = result.replace(")", "");
						result = result.replace("*", "");
						result = result.replace("(", "");
						result = result.replace(".", "");
						String[] arr = result.split("UF");
						result= arr[1];
							
					}
				}else{
					if (result.indexOf("$") < result.indexOf("(")){
						result = result.replace(")", "");
						result = result.replace("(", ";");
						result = result.replace("*", "");
						result = result.replace("Precio", "");
						result = result.replace(".", "");
						result = result.replace("$", "");
						String[] arr = result.split(";");
						result= arr[0];
						
					}else{
						result = result.replace(")", "");
						result = result.replace("*", "");
						result = result.replace("(", "");
						result = result.replace(".", "");
						result = result.replace("$", ";");
						
						String[] arr = result.split(";");
						result= arr[1];
							
					}
				}
				
				break;
				
			}
		}
		return result.trim();
	}
	private String getCodigoPublicacion(String html){
		String result = "";
		List<String> lst = new ArrayList<String>();
		ListUtils.fromArrayToList(html.split("\n"), lst);
		for(String str : lst){
			//LOGGER.info(str);
			if (str.contains("<tr id=") && str.contains("class=\"ad listing_thumbs\"") ){
				str = str.replace("\"", "");
				str = str.replace("<tr", "");
				str = str.replace("id=", "");
				str = str.replace("class=ad listing_thumbs>", "");
				
				str = str.trim();
				result = str;
				break;
			}
		}
		
		return result;
	}
    private static String replaceHtmlCharacters(String param){
    	param = param.replace("\"", "");
    	param = param.replace("<a href=", "");
    	param = param.replace("</a>", "");
    	//param = param.replace("", "");
    	//param = param.replace("", "");
    	//param = param.replace("", "");
    	
    	
    	return param;
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

	public Double getAmountAttr(Element element, String cssTarget) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String getAmountAttr(Document doc) {
		String result = "";
		for(int i = 1; i<12;i++){
			
			String str = doc.select("#content > section.box.da-wrapper > article > div.box.info > div.details > table > tbody > tr:nth-child("+i+")").toString();
			if (str.contains("Precio")){
				
				//Precio UF 3.800,00 ($ 102.146.166)*
				result = Jsoup.parse(str).text();
				if (result.indexOf("UF") < result.indexOf("(")){
					result = result.replace(")", "");
					result = result.replace("(", ";");
					result = result.replace("*", "");
					result = result.replace("Precio", "");
					result = result.replace(".", "");
					result = result.replace("UF", "");
					String[] arr = result.split(";");
					result= arr[0];
					
				}else{
					result = result.replace(")", "");
					result = result.replace("*", "");
					result = result.replace("(", "");
					result = result.replace(".", "");
					String[] arr = result.split("UF");
					result= arr[1];
						
				}
				break;
				
			}
		}
		return result.trim();
	}

	public static String getNameAttr(String html) {
		String result = "";
		List<String> lst = new ArrayList<String>();
		ListUtils.fromArrayToList(html.split("\n"), lst);
		for(String str : lst){
			if (str.contains("title")){
				result = Jsoup.parse(str).text();
				result = result.replace("|", "");
				if (!result.trim().isEmpty()){
					break;
				}
			}
		}
		return result;
	}
	public static String getPublishDate(Document doc) {
		String result = "";

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String strTmp = doc.select("[datetime]").toString(); 
		strTmp = strTmp.replace("\"", "");
		strTmp = strTmp.replace("<time datetime=", "");
		strTmp = strTmp.replace("</time>", "");
		String[] arrTmp = strTmp.split(">");
		strTmp = arrTmp[0];

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(strTmp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result = df.format(date);
		return result;
	}

	public static String getBuilt(Document doc) {
		String result = "";
		
		for(int i = 1; i<12;i++){
			
			String str = doc.select("#content > section.box.da-wrapper > article > div.box.info > div.details > table > tbody > tr:nth-child("+i+")").toString();
			if (str.contains("construida")){
				
				result = Jsoup.parse(str).text();
				result = result.replace("Superficie construida", "");
				result = result.replace("m�", "");
				break;
			}
		}
		return result.trim();
	}

	public static Integer getNumpages(Document doc, String cssTarget) {
		Integer result = 1; 
		String strNumeroPaginas = doc.select(cssTarget).toString();
		strNumeroPaginas = strNumeroPaginas.replace("<a", "");
		strNumeroPaginas = strNumeroPaginas.replace("\"", "");
		strNumeroPaginas = strNumeroPaginas.replace("href=", "");
		strNumeroPaginas = strNumeroPaginas.replace("</a>", "");
		strNumeroPaginas = strNumeroPaginas.replace("�ltima p�gina", "");
		strNumeroPaginas = strNumeroPaginas.replace(">", "");
		String[] arrTmp = strNumeroPaginas.split(";");
		strNumeroPaginas = arrTmp[arrTmp.length-1];
		strNumeroPaginas = strNumeroPaginas.replace("o=", "").trim();
		result = Integer.parseInt(strNumeroPaginas.trim());

		return result;
	}

	public static String getSubUrl(String html) {
		
		String result = "";
		List<String> lst = new ArrayList<String>();
		ListUtils.fromArrayToList(html.split("\n"), lst);
		for(String str : lst){
			if (str.contains("href=") && str.contains("redirect-to-url") && !str.contains("listing_thumbs_date")&& !str.contains("thumbs_subject")){
				str = str.replace("\"", "");
				str = str.replace("<a href=", "");
				str = str.replace("<!-- end da_image -->", "");
				str = str.replace("class=redirect-to-url></a> </td>", "");
				str = str.replace("</div>", "");
				str = str.trim();
				result = str;
				break;
			}
		}
		
		return result;
	
	}
	public static String getComuna(Document doc) {
		String result = "";
		for(int i = 1; i<12;i++){
			
			String str = doc.select("#content > section.box.da-wrapper > article > div.box.info > div.details > table > tbody > tr:nth-child("+i+")").toString();
			if (str.contains("Comuna")){
				
				result = Jsoup.parse(str).text();
				result= result.replace("Comuna","");
				result= result.replace("�","e");
				
				break;
				
			}
		}
		return result.trim();
	}
	public Integer getNumPages(Document doc, String cssTarget) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String getUrl(Element element, String cssTarget) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPublishDate(Element element, String cssTarget) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPublishDate(Elements elements, String cssTarget) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTotalAvisos(Document doc) {
		String result = "";
		try {
			result = doc.select("#tabnav > li.tab_region > h2 > span:nth-child(2)").text();
			result = result.replace(".", "");
			String[] arr = result.split("de");

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
}