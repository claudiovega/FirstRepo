package cl.propiedades.business;

import cl.propiedades.dao.OraclePropiedadesElMercurioDAO;
import cl.propiedades.dao.OracleScrapInmobiliarioBatchDAO;
import cl.propiedades.enums.TipoInmuebleEnum;
import cl.propiedades.enums.TipoTransaccionEnum;
import cl.propiedades.factory.Portal;
import cl.propiedades.helper.LoggerHelper;
import cl.propiedades.repository.ErroresDAO;
import cl.propiedades.util.*;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HTML to plain-text. This example program demonstrates the use of jsoup to convert HTML input to lightly-formatted
 * plain-text. That is divergent from the general goal of jsoup's .text() methods, which is to get clean data from a
 * scrape.
 * <p>
 * Note that this is a fairly simplistic formatter -- for real world use you'll want to embrace and extend.
 * </p>
 * <p>
 * To invoke from the command line, assuming you've downloaded the jsoup jar to your current directory:</p>
 * <p><code>java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]</code></p>
 * where <i>url</i> is the URL to fetch, and <i>selector</i> is an optional CSS selector.
 * 
 * @author Jonathan Hedley, jonathan@hedley.net
 */
@Service
public class PropiedadesElMercurioRefactor implements Portal {

	@Autowired
	ErroresDAO erroresDAO;

    @Autowired
    LoggerHelper loggerHelper;

	@Autowired
	OracleScrapInmobiliarioBatchDAO ocbd;

    private static final String userAgent = "Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0";
	private static final Logger LOGGER = LoggerFactory.getLogger(PropiedadesElMercurioRefactor.class);
    private static final int timeout = 60 * 1000;
    private String path = null;
    private boolean procesoCasa = true;
    private String precioCasaHasta = "N/A";
    private String precioDeptoHasta = "N/A";
	private String tipoTransaccion = "";
	private String tipoPropiedad = "";
	private String fechaDesde = "";
	private String region;
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
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(String dormitorios) {
		this.dormitorios = dormitorios;
	}
	private String dormitorios = "";
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
    public  void run() throws Exception  {
    	boolean salirPorFecha = false;
    	List<String> listaLineas = new ArrayList<String>();
       
        Double uf = new Double(WebDataUtils.getUfToday());
        String totalAvisos = "";
        String selector = "";
        String txtname = "";
        String casaDepto = "";
        int MaxPaginas = 122;//casas busqueda ultimos 7 dias
        //int MaxPaginas = 106;//deptos
        String strLista = "Portal|CodigoPublicacion|TipoPropiedad|TipoTransaccion|Fechapublicacion|TituloPublicacion|Comuna|Dormitorios|Baños|Construido|Terreno|PrecioUf|PrecioPesos|Url|uf "+uf;
        
        Integer orden = 0;
        OracleScrapInmobiliarioBatchDAO oradel = new OracleScrapInmobiliarioBatchDAO();
		oradel.deleteRecordFromTable("PropiedadesElMercurio", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
        boolean numPagsGeted = false;
        String url = "https://www.economicos.cl/"+this.region+"/";
//        if (tipoPropiedad.equals("Casa")){
//        	url = url+"casa";
//        }else{
//        	url = url+"departamento";
//        }
		url = url+this.tipoPropiedad;
/*        if (tipoTransaccion.equals("Venta")){
        	url = url+"?operacion=Venta";
        }else{
        	url = url+"?operacion=Arriendo";
        	this.path =this.path+"\\Arriendo"; 
        }*/

		url = url+"?operacion="+this.tipoTransaccion;
        String urlParams = "";
        if (!fechaDesde.trim().isEmpty()){
        	urlParams = "&age="+fechaDesde.trim();
        }
        Integer dormi = null;
        if (!dormitorios.trim().isEmpty() && !dormitorios.equals("Seleccione")){
        	urlParams = urlParams+"&dormitoriosDesde="+dormitorios.trim();
        	dormi = Integer.parseInt(dormitorios.trim());
        }
        url = url+urlParams;
        txtname = "ElMercurioPropiedades"+tipoPropiedad+tipoTransaccion;
        OraclePropiedadesElMercurioDAO opem = new OraclePropiedadesElMercurioDAO();
        
        LocalDate fechaTope = opem.getMaxDate("PropiedadesElMercurio", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), dormi);
		if (fechaTope != null) {
			fechaTope = fechaTope.minusDays(1L);
		}
        LOGGER.info("fechaTope: "+fechaTope);
		LOGGER.info(url+"&pagina="+0+"#results");
		String urlParaLogError = "";
		loggerHelper.logProceso("PropiedadesElMercurio "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "INICIO Proceso Inmobiliario PropiedadesElMercurio "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
		Integer numeroPagina = null;
		Integer posicionAviso = null;
		for(int x = 0; x<=MaxPaginas;x++){
			if (salirPorFecha){
				break;
			}
        	try {
				urlParaLogError = url+"&pagina="+x+"#results";
				loggerHelper.logProceso("PropiedadesElMercurio "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), url+"&pagina="+x+"#results" +" of "+MaxPaginas);
				numeroPagina = x;
				Document doc = Jsoup.connect(url+"&pagina="+x+"#results").userAgent(userAgent).timeout(timeout).get();
				String strListaTemp = "";
				listaLineas = new ArrayList<String>();
				if (!numPagsGeted){
					
				    MaxPaginas = getMaxPaginas(doc);
				    totalAvisos = getTotalAvisos(doc);
				    strLista = strLista+"|Total Avisos: "+totalAvisos;
				    strLista = strLista+"\n";
				    numPagsGeted = true;
				}

				LOGGER.info("Scraping page: "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+ TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion)+" "+url+"&pagina="+x+"#results"+" of "+MaxPaginas);
				
				for (Integer i = 1; i< 48 ; i++){
					posicionAviso = i;
					orden++;
					//          body > div > div.content.row-fluid > div.span9 > div.results.left > div:nth-child(1)
					selector = "body > div > div.content.row-fluid > div.span9 > div.results.left > div:nth-child("+i+")";
				    Elements elements = doc.select(selector); // get each element that matches the CSS selector
					if (salirPorFecha){
						i=1000;
						break;
					}
					String codigoPublicacion = "";
					String strUrl = "";
					String name = "";

					String construidos = "";
					String terreno = "";
					String time = "";
					String comuna = "";
					String region = "";
					String strPrecioUf = "";
					String strPrecioPesos = "";

					String dorm = "";
					String baños = "";
					if (strUrl.isEmpty()){
						strUrl = getUrl(strUrl, elements.toString());
						codigoPublicacion = EmolUtils.getCodigoEmolFromUrl(strUrl);
					}
					name= getDescripcion(elements.toString());
					strPrecioPesos = getPrecioEnPesos(elements.toString());
					if(strPrecioPesos != null && !strPrecioPesos.trim().isEmpty()){
						strPrecioPesos= strPrecioPesos.replace(".", "");
						strPrecioPesos= strPrecioPesos.replace("<li class=\"ecn_precio\"><i class=\"fa fa-usd\"></i>", "");
//						if (strPrecioPesos.contains("fa fa-usd")){
//							LOGGER.info();
//						}
						if (strPrecioPesos != null && !strPrecioPesos.trim().isEmpty()){
							strPrecioUf = NumberUtils.convertPesosAUf(uf, strPrecioPesos);

						}

					}

					comuna = getComuna(elements.toString());
					region = getRegion(elements.toString());

					time = getFecha(elements.toString());
					if (fechaTope != null && !StringUtils.isEmpty(time)){

						if (fechaTope.compareTo(DateUtils.stringToLocalDate(time, ScrapConstants.DATE_MASK_PropiedadesElMercurio))>0){
							LOGGER.info("PropiedadesElMercurio Se sale del ciclo por fecha, FechaTope: "+fechaTope.toString()+" FechaEncontrada: "+time);
							loggerHelper.logProceso("PropiedadesElMercurio "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), " Se sale del ciclo por fecha, FechaTope: "+fechaTope.toString()+" FechaEncontrada: "+time);
							x = MaxPaginas+50;

							salirPorFecha = true;
							break;
						}
					}
					if (strUrl.length() > 25){

						strListaTemp = "PropiedadesElMercurio|"+codigoPublicacion+"|"+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+"|"+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion)+"|"+time+"|"+name+"|"+comuna+"|"+dorm+"|"+baños+"|"+construidos+"|"+terreno+"|"+strPrecioUf.trim()+"|"+strPrecioPesos.trim()+"|"+strUrl+"|"+region;
						name = "";
						dorm = "";
						baños = "";
						construidos = "";
						terreno = "";
						time = "";
						comuna = "";
						strPrecioUf = "";
						strPrecioPesos = "";
						strUrl = "";

					}
					if (strListaTemp.contains("economicos")){

						strLista = strLista+strListaTemp+"\n";
						listaLineas.add(strListaTemp);

						strListaTemp = "";

					}else{
						orden--;
					}

				    

				    
				}
				insertIntoDatabase(listaLineas, numeroPagina, posicionAviso);
				listaLineas = new ArrayList<String>();

				Long delay = (long) (Math.random() * 22000 + 1000);
				Thread.sleep(delay);
        	} catch (Exception e) {
				loggerHelper.logErroresProceso("Yapo", urlParaLogError, TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad),  e);


			}
        }
        opem.insertDataFromTmp("PropiedadesElMercurio", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
		loggerHelper.logProceso("PropiedadesElMercurio "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "FIN Proceso Inmobiliario PropiedadesElMercurio "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));


	}
	private void insertIntoDatabase(List<String> lista, Integer pagina, Integer aviso){
		//OracleScrapInmobiliarioBatchDAO ocbd = new OracleScrapInmobiliarioBatchDAO();
		this.ocbd.batchInformacionInmobiliariaLoader(lista, ScrapConstants.DATE_MASK_PropiedadesElMercurio, pagina, aviso);
	}
	private String getUrl(String strUrl, String subElement) {
		String strTmp = subElement.replace(",","");
		String[] arr = strTmp.split("\n");
		for(int i = 0; i<arr.length; i++){
			String str = arr[i];
			if (str.contains("href") &&
					str.contains("<div") &&
					!str.contains("<h3>") &&
					!str.contains("<li")){
				str = str.replace("<div class=\"cont_img_tmb_mas_f\"> <a href=\"", "");
				str = str.replace("<a href=\"", "");
				str = str.replace("<div class=\"col1 span2\">", "");
				str = str.replace("class=\"tmb\">", "");
				if (str.contains("<!-- <img")){
					String[] tmpArr = str.split("<!-- <img");
					str = tmpArr[0];
				}
				str = str.replace("\">", "");
				str = str.replace("\"", "");
				str = str.trim();

				strUrl = "https://www.economicos.cl"+str;

				break;

			}
		}


		return strUrl;
	}
	private String getDescripcion( String subElement) {
		String result = "";
		String strTmp = subElement.replace(",","");
		String[] arr = strTmp.split("\n");
		for(int i = 0; i<arr.length; i++){
			String str = arr[i];
			if (str.contains("<h3>") ){

				str = str.replace("</h3>", "");
				str = str.replace("</a>", "");
				str = str.trim();
				String[] subArr = str.split("<h3>");
				if (subArr.length == 2){
					result = subArr[1].trim();
				}else{
					result = subArr[0].trim();
				}


				break;

			}
		}
		result = result.replace("|","");
		return result;
	}
	private String getPrecioEnPesos( String subElement) {
		String result = "";
		String strTmp = subElement.replace(",","");
		String[] arr = strTmp.split("\n");
		for(int i = 0; i<arr.length; i++){
			String str = arr[i];
			if (str.contains("ecn_precio") ){

				str = str.replace("-->", "");
				str = str.replace("</li>", "");
				str = str.trim();
				String[] subArr = str.split("<!--");
				if (subArr.length == 2){
					result = subArr[1].trim();
				}else{
					result = subArr[0].trim();
				}


				break;

			}
		}
		result = result.replace("</sup>","");
		return result.trim();
	}
	private String getComuna( String subElement) {
		String result = "";
		String strTmp = subElement.replace(",","");
		String[] arr = strTmp.split("\n");
		for(int i = 0; i<arr.length; i++){
			String str = arr[i];
			if (str.contains("waze:") ){

				String[] subArr = str.split("</a>");
				for(int x = 0; x<subArr.length; x++){
					if (subArr[x].contains("|")){

						String[] comunaArr = subArr[x].split("\\|");
						result = comunaArr[0];
						break;
					}

				}


				break;

			}else if(str.contains("</li>") && str.contains("|")){
				str = str.replace("<li class=\"cort_txt\">", "");
				String[] comunaArr = str.split("\\|");
				result = comunaArr[0].trim();
			}

		}

		result = result.replace("|","");
		return result;
	}
	private String getRegion( String subElement) {
		String result = "";
		String strTmp = subElement.replace(",","");
		String[] arr = strTmp.split("\n");
		for(int i = 0; i<arr.length; i++){
			String str = arr[i];
			if (str.contains("waze:") ){

				String[] subArr = str.split("</a>");
				for(int x = 0; x<subArr.length; x++){
					if (subArr[x].contains("|")){

						String[] comunaArr = subArr[x].split("\\|");
						String strSubTmp = comunaArr[subArr.length-1];
						String[] subTmpArr = strSubTmp.split("</li>");
						result = subTmpArr[0];
						if (result.toUpperCase().contains("RM")){
							result = "Region Metropolitana";
						}
						break;
					}

				}


				break;

			}else if(str.contains("</li>") && str.contains("|")){
				str = str.replace("<li class=\"cort_txt\">", "");
				String[] comunaArr = str.split("\\|");
				result = comunaArr[0].trim();
			}

		}

		result = result.replace("|","");
		return result;
	}
	private String getFecha( String subElement) {
		String result = "";
		String strTmp = subElement.replace(",","");
		String[] arr = strTmp.split("\n");
		for(int i = 0; i<arr.length; i++){
			String str = arr[i];
			if (str.contains("fa-clock-o") ){
				str = str.replace("</li>","");
				str = str.replace("</time>","");
				str = str.replace("\"","");


				String[] subArr = str.split("datetime=");
				result = subArr[1];
				result = result.trim();

				break;

			}
		}
		if (!result.trim().isEmpty()){
			result = result.substring(0, 10);
		}


		return result;
	}
	private int getMaxPaginas(Document doc) {
		int MaxPaginas;
		String strNumeroPaginas = doc.select("body > div > div.content.row-fluid > div.span9 > ul > li > a:nth-child(1)").toString();
		strNumeroPaginas = strNumeroPaginas.replace("<a href=\"#tab1\">", "");
		strNumeroPaginas = strNumeroPaginas.replace("</a>", "");
		LOGGER.info(strNumeroPaginas);
		String[] arrNumPag = strNumeroPaginas.split("de");
		String strMaxPags = arrNumPag[1].trim();
		strMaxPags = strMaxPags.replace(".", "");
		MaxPaginas = Integer.parseInt(strMaxPags) ;
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
		String result = "";
		try {
			result = doc.select("body > div > div.content.row-fluid > div.span9 > ul > li > a:nth-child(1)").text();
			result = result.replace("encontraron", "");
			result = result.replace("Se", "");
			result = result.replace(".", "");
			
			String[] arr = result.split("avisos");
			result = arr[0].trim();
		} catch (Exception ignored) {
			// TODO Auto-generated catch block
			
		}
		return result;
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
		this.region = region;
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