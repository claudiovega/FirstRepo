package cl.propiedades.business;

import cl.propiedades.dao.OraclePortalInmobiliarioDAO;
import cl.propiedades.dao.OracleScrapInmobiliarioBatchDAO;
import cl.propiedades.enums.PortalesEnum;
import cl.propiedades.enums.TipoInmuebleEnum;
import cl.propiedades.enums.TipoTransaccionEnum;
import cl.propiedades.factory.Factory;
import cl.propiedades.factory.Portal;
import cl.propiedades.factory.ScrapExtractor;
import cl.propiedades.helper.LoggerHelper;
import cl.propiedades.repository.ErroresDAO;
import cl.propiedades.util.DateUtils;
import cl.propiedades.util.ScrapConstants;
import cl.propiedades.util.WebDataUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
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
public class PortalInmobiliarioRefactor implements Portal {

    @Autowired
    Factory factoryExtractor;

	@Autowired
	ErroresDAO erroresDAO;

    @Autowired
    LoggerHelper loggerHelper;

	@Autowired
	OracleScrapInmobiliarioBatchDAO ocbd;

    private static final String userAgent = "Mozilla/5.0 (jsoup)";
	private static final Logger LOGGER = LoggerFactory.getLogger(PortalInmobiliarioRefactor.class);
    private static final int timeout = 60 * 1000;
    
    private String log = "";
    private String path = null;
    private boolean procesoCasa = true;
    private String precioCasaHasta = "8000";
    private String precioDeptoHasta = "4100";

    private String precioCasaDesde = "1";
    private String precioDeptoDesde = "1";
	private String region;
	private String txtname = "";
	
    
	private String tipoTransaccion = "";
	private String tipoPropiedad = "";
	private String moneda = "";
	private String precioDesde = "";
	private String precioHasta = "";
	private String dormitoriosDesde = "";
	private String dormitoriosHasta = "";
	private Double uf = 0D;
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

	public String getDormitoriosDesde() {
		return dormitoriosDesde;
	}

	public void setDormitoriosDesde(String dormitoriosDesde) {
		this.dormitoriosDesde = dormitoriosDesde;
	}

	public String getDormitoriosHasta() {
		return dormitoriosHasta;
	}

	public void setDormitoriosHasta(String dormitoriosHasta) {
		this.dormitoriosHasta = dormitoriosHasta;
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
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public void run() throws Exception {

		List<String> listaLineas = new ArrayList<String>();
		uf = new Double(WebDataUtils.getUfToday());

		String totalAvisos = "";
		String selector = "";

		
		int maxPaginas = 200;//casas 165
		String strLista = "Portal|CodigoPublicacion|TipoPropiedad|TipoTransaccion|Fechapublicacion|TituloPublicacion|Comuna|Dormitorios|Ba�os|Construido|Terreno|PrecioUf|PrecioPesos|Url|uf "+uf;
		//String urlParams = "";

        boolean numPagsGeted = false;
        // fetch the specified URL and parse to a HTML DOM
        String strTipoPropiedad = "";
        //String strTipoTransaccion = "";
        String url = "https://www.portalinmobiliario.com/";
/*        if (tipoTransaccion.equals("Venta")){
        	strTipoTransaccion = "Venta";
        	url = url+"venta/";
        }else{
        	strTipoTransaccion = "Arriendo";
        	url = url+"arriendo/";
        	this.path =this.path+"\\Arriendo";
        }*/
		url = url+this.tipoTransaccion+"/";
//        if (tipoPropiedad.equals("Casa")){
//        	strTipoPropiedad = "Casa";
//        	url = url+"casa/";
//        }else{
//        	strTipoPropiedad = "Departamento";
//        	url = url+"departamento/";
//        }
		url = url+this.tipoPropiedad+"/";
		ScrapExtractor scrapExtractor = factoryExtractor.getBean(PortalesEnum.PORTALINMOBILIARIO.getPortal());
        
/*        if(!precioDesde.trim().isEmpty()){
        	urlParams = urlParams+precioDesdeRequestparam+precioDesde;
        }
        if(!precioHasta.trim().isEmpty()){
        	urlParams = urlParams+precioHastaRequestparam+precioHasta;
        }*/

		Integer dormi = null;
		String filtroDormitorios = "";
		if (!StringUtils.isEmpty(this.dormitoriosHasta)){
			if (!StringUtils.isEmpty(this.dormitoriosDesde)){
				if (this.dormitoriosDesde.trim().equals(this.dormitoriosHasta.trim())){
					filtroDormitorios = this.dormitoriosHasta.trim()+"-dormitorios";
				}else{
					filtroDormitorios = this.dormitoriosDesde.trim()+"-a-"+this.dormitoriosHasta.trim()+"-dormitorios";
				}


			}else{
				filtroDormitorios = "hasta-"+this.dormitoriosHasta+"-dormitorios";
			}
		}
		if (!filtroDormitorios.trim().isEmpty()) {
			url = url+filtroDormitorios+"/";
		}
		if (!this.region.trim().isEmpty()){
			url = url+this.region+"/";
		}
		url = url+this.region+"/";
		String orden = "_OrderId_BEGINS*DESC";
        /*

        if (moneda.equals("UF")){
        	urlParams = urlParams+monedaUf;
        }else{
        	urlParams = urlParams+monedaPesos;
        }*/
        //urlParams= urlParams+ordenMasRecientes;


        txtname = "PortalInmobiliario"+tipoPropiedad+tipoTransaccion;
		OracleScrapInmobiliarioBatchDAO oradel = new OracleScrapInmobiliarioBatchDAO();
		oradel.deleteRecordFromTable("PortalInmobiliario", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
		OraclePortalInmobiliarioDAO opi = new OraclePortalInmobiliarioDAO();
		LocalDate fechaTope = opi.getMaxDate("PortalInmobiliario", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), dormi);
		if (fechaTope != null) {
			fechaTope = fechaTope.minusDays(1L);
		}
		LOGGER.info("fechaTope: "+fechaTope);
		boolean salirPorFecha = false;
		String urlParaLogError = "";
		loggerHelper.logProceso("PortalInmobiliario "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "INICIO Proceso Inmobiliario PortalInmobiliario "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
		Integer numeroPagina = null;
		Integer posicionAviso = null;
		for(int x = 1; x<=maxPaginas;x++){


        	try {
        		if (x != 1){
        			Integer pagina= 50*(x-1)+1;
					orden = "_Desde_"+pagina+"_"+"_OrderId_BEGINS*DESC";
					numeroPagina = pagina;
				}

				urlParaLogError = url+orden;
				LOGGER.info(url+orden);
				loggerHelper.logProceso("PortalInmobiliario "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "Scraping page: "+ url+orden+" of "+maxPaginas);

				Document doc = Jsoup.connect(url+orden).userAgent(userAgent).timeout(timeout).get();
				String strListaTemp = "";
				listaLineas = new ArrayList<String>();

				if (!numPagsGeted){					
				    maxPaginas = scrapExtractor.getNumeroPaginas(doc);
				    numPagsGeted = true;
				    totalAvisos = scrapExtractor.getTotalAvisos(doc);
				    strLista = strLista+"|Total Avisos: "+totalAvisos;
				    strLista = strLista+"\n";
				}
				
				LOGGER.info("Scraping page: "+ TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(this.tipoTransaccion)+" portalinmobiliario.com "+x+" of "+maxPaginas);

				for (Integer i = 1; i< 50 ; i++){
					posicionAviso = i;
					if (salirPorFecha){
						i=1000;
					}


					selector = "#searchResults > li:nth-child("+i+")";
				    Elements elements = doc.select(selector); // get each element that matches the CSS selector
				    String fechaPublicacion = "";
				    for (Element element : elements) {
				    	if (salirPorFecha){
							break;
						}
				    				       
				        String strUrl = "";
				        String dorm = "";
				        String codigoPublicacion = "";
				        String baños = "";
				        String construidos = "";
				        String total = "";
				       
				        String comuna = "";
						String region = "";
				        String strPrecioUf = "";
				        String strPrecioPesos = "";
				        String tituloPublicacion = "";
						strUrl = scrapExtractor.getUrl(element);
						Long delay = (long) (Math.random() * 7 + 1000);
						Thread.sleep(delay);
						Document docu = Jsoup.connect(strUrl).userAgent("Mozilla/5.0 (jsoup)").timeout(60000).get();
						Elements elementsFecha = docu.select("#root-app > div > div.layout-main.u-clearfix > div.layout-col.layout-col--right > section.ui-view-more.vip-section-seller-info > div > div.official-store-info.info-property-date > p.info");
						fechaPublicacion = scrapExtractor.getFechaPublicacion(elementsFecha);
						Elements elementsRegion = docu.select("#root-app > div.vip-nav-bounds > div > div.layout-col.layout-col--left > div.item-map.vip-card > section > div.section-map-title > div > h3");
						region = scrapExtractor.getNombreRegion(elementsRegion);
						//
						if (fechaTope != null){
							urlParaLogError = strUrl;
							if (fechaTope.compareTo(DateUtils.stringToLocalDate(fechaPublicacion, ScrapConstants.DATE_MASK_PortalInmobiliario))>0){
								LOGGER.info("PortalInmobiliario Se sale del ciclo por fecha, FechaTope: "+fechaTope.toString()+" FechaEncontrada: "+fechaPublicacion);

								loggerHelper.logProceso("PortalInmobiliario "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), " Se sale del ciclo por fecha, FechaTope: "+fechaTope.toString()+" FechaEncontrada: "+fechaPublicacion);
								salirPorFecha = true;
								i = 1000;
								x = maxPaginas+1000;
								break;
							}
						}
						codigoPublicacion = scrapExtractor.getCodigoPublicacion(element);
						tituloPublicacion = scrapExtractor.getTituloPublicacion(element);

						comuna = scrapExtractor.getNombreComuna(element);

						strPrecioUf =scrapExtractor.getPrecioUF(element, uf);
						strPrecioPesos=scrapExtractor.getPrecioPesos(element, uf);
						dorm = scrapExtractor.getDormitorios(element);

						baños = scrapExtractor.getToilet(element);
						construidos = scrapExtractor.getConstruidos(element);
						total = scrapExtractor.getTerreno(element);
						strListaTemp = "PortalInmobiliario"+"|"+
								codigoPublicacion+"|"+
								TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+"|"+
								TipoTransaccionEnum.getDescripcionTipoTransaccion(this.tipoTransaccion)+"|"+
								fechaPublicacion+"|"+
								tituloPublicacion+"|"+
								comuna+"|"+
								dorm+"|"+
								baños+"|"+
								construidos+"|"+
								total+"|"+
								strPrecioUf.trim()+"|"+
								strPrecioPesos.trim()+"|"+
								strUrl+"|"+
								region;


				    }
				    
				    if (strListaTemp.contains("portalinmobiliario") && !fechaPublicacion.trim().isEmpty()){
				    	
				    	strLista = strLista+strListaTemp+"\n";
				    	listaLineas.add(strListaTemp);
				    	strListaTemp = "";
				    	fechaPublicacion = "";
				    	 
				    		
				    }
				    
				}
				insertIntoDatabase(listaLineas, numeroPagina, posicionAviso);
				listaLineas = new ArrayList<String>();
				Long delay = (long) (Math.random() * 15000 + 1000);
				Thread.sleep(delay);
			} catch (Exception e) {
				loggerHelper.logErroresProceso("PortalInmobiliario", urlParaLogError, TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad),  e);


			}
        }
        
        
        opi.insertDataFromTmp("PortalInmobiliario", TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad), TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));
        LOGGER.info("PortalInmobiliario proceso terminado");
		loggerHelper.logProceso("PortalInmobiliario "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion), "FIN Proceso Inmobiliario PortalInmobiliario "+TipoInmuebleEnum.getDescripcionTipoInmueble(tipoPropiedad)+" "+TipoTransaccionEnum.getDescripcionTipoTransaccion(tipoTransaccion));

	}
	private void insertIntoDatabase(List<String> lista, Integer pagina, Integer aviso){
		//OracleScrapInmobiliarioBatchDAO ocbd = new OracleScrapInmobiliarioBatchDAO();
		if (!lista.isEmpty()) {
			try {
				this.ocbd.batchInformacionInmobiliariaLoader(lista, ScrapConstants.DATE_MASK_PortalInmobiliario, pagina, aviso);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			LOGGER.info("PortalInmobiliario lista.isEmpty()"+lista.isEmpty());
		}
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
			result = doc.select("#inner-main > aside > div.quantity-results").text();
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
	public Integer getMaxPaginas(String url){
		Integer result = 0;
		
		try {
			Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
			

							
			    String strNumMaxpags = doc.select("#PaginacionSuperior > div > ul > li.ultima > a").toString();
			    strNumMaxpags = strNumMaxpags.replace("title=\"�ltima\">�</a>", "");
			    strNumMaxpags = strNumMaxpags.replace("\"", "");

			    String[] arrNumPags = strNumMaxpags.split("pg=");
			    if (arrNumPags.length <2){
			    	result = 1;
			    }else{
			    	result = Integer.parseInt(arrNumPags[1].trim()) ;
				    	
			    }
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}