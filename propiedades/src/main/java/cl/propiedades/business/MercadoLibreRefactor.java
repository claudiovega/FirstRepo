package cl.propiedades.business;

import cl.propiedades.dao.OracleMercadoLibreDAO;
import cl.propiedades.dao.OracleScrapInmobiliarioBatchDAO;
import cl.propiedades.factory.Portal;
import cl.propiedades.util.DateUtils;
import cl.propiedades.util.NumberUtils;
import cl.propiedades.util.ScrapConstants;
import cl.propiedades.util.WebDataUtils;
import cl.propiedades.vo.InformacionInmobiliariaTempVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MercadoLibreRefactor implements Portal {
	//private static final String userAgent = "Mozilla/5.0 (jsoup)";
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0";
	private static final Logger LOGGER = LoggerFactory.getLogger(MercadoLibreRefactor.class);
    private static final int timeout = 60 * 1000;
    //private final static Logger log = Logger.getLogger(MercadoLibre.class);
    
    private String path = null;
    private boolean procesoCasa = true;
    private String precioCasaHasta = "";
    private String precioDeptoHasta = "";
    private Double uf = 0D;

    private String precioCasaDesde = "";
    private String precioDeptoDesde = "";
    private String tipoTransaccion = "";
	private String tipoPropiedad = "";
	private String moneda = "";
	private String precioDesde = "";
	private String precioHasta = "";
	private String dormitorios = "";
	private String region = "";

	public String getRegion() {
		return region;
	}

	@Override
	public void setRegion(String region) {
		this.region = region;
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
	public String getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(String dormitorios) {
		this.dormitorios = dormitorios;
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



	public void setPrecioDeptoDesde(String precioDeptoDesde) {
		this.precioDeptoDesde = precioDeptoDesde;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isProcesoCasa() {
		return procesoCasa;
	}
	public void setProcesoCasa(boolean procesoCasa) {
		this.procesoCasa = procesoCasa;
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
	@Override
	public void run() throws Exception {
		List<String> listaLineas = new ArrayList<String>();

		
		uf = new Double(WebDataUtils.getUfToday());
		//URL urlLog4j = Loader.getResource("log4j.properties");
/*    	if (urlLog4j != null) {
			PropertyConfigurator.configure(urlLog4j);
		}*/
		String totalAvisos = "";

        String txtname = "";
        String casaDepto = "";
        int MaxPaginas = 165;//casas 165
		Integer orden = 0;
        String strLista ="Portal|CodigoPublicacion|TipoPropiedad|TipoTransaccion|Fechapublicacion|TituloPublicacion|Comuna|Dormitorios|Ba�os|Construido|Terreno|PrecioUf|PrecioPesos|Url|uf "+uf;
        

        OracleScrapInmobiliarioBatchDAO oradel = new OracleScrapInmobiliarioBatchDAO();
		oradel.deleteRecordFromTable("MercadoLibre", tipoPropiedad, tipoTransaccion);
        boolean numPagsGeted = false;
        // fetch the specified URL and parse to a HTML DOM
        String url = "https://inmuebles.mercadolibre.cl/";
        String urlParams = "";
        String priceRange= "";
		urlParams = urlParams+tipoPropiedad+"/"+tipoTransaccion+"/";
		if (!this.region.trim().isEmpty()){
			urlParams = urlParams+this.region+"/";
		}


        String pags = "";
        txtname = "MercadoLibre"+tipoPropiedad+tipoTransaccion;

		Integer numeroPagina = null;
		Integer posicionAviso = null;
		String ordenStr = "_OrderId_BEGINS*DESC";
        for(int x = 1; x<=MaxPaginas;x++){
        	try{
				if (x != 1){
					Integer pagina= 50*(x-1)+1;
					ordenStr = "_Desde_"+pagina+"_"+"_OrderId_BEGINS*DESC";
					numeroPagina = pagina;
				}
        		LOGGER.info("URL: "+url+urlParams+ordenStr);
        		Document doc = Jsoup.connect(url+urlParams+ordenStr).userAgent(userAgent).timeout(timeout).get();
        		listaLineas = new ArrayList<String>();

				if (!numPagsGeted){					
				    String strNumMaxpags = doc.select("#inner-main > aside > div.quantity-results").text();
				    
				    strNumMaxpags = strNumMaxpags.replace("resultados", "");
				    strNumMaxpags = strNumMaxpags.replace(".", "");
				    totalAvisos = strNumMaxpags;
				    Double numPags = new Double(strNumMaxpags.trim()) /50;
				    numPags = new Double(Math.round(numPags.doubleValue())) ;
				    int i = 0;
				    
				    MaxPaginas = numPags.intValue();
				   
				    numPagsGeted = true;
				    //totalAvisos = getTotalAvisos(doc);
				    strLista = strLista+"|Total Avisos: "+totalAvisos;
				    strLista = strLista+"\n";
				}
				
				Elements els = doc.select("#searchResults"); 
				Element element = els.get(0);
				LOGGER.info("Scraping page: "+tipoPropiedad+" "+tipoTransaccion+" MercadoLibre.cl "+x+" of "+MaxPaginas);
        		for(int i = 1; i<=50; i++){
        			orden++;
        				
        			String selCss = "#searchResults > li:nth-child("+i+")";//#searchResults > li:nth-child(2)
        			Elements  el = null;
        			
        			el = element.select(selCss);
        			String id = getMercadoLibreId(el);
        			//LOGGER.info(id);
        			String strUrl = getUrl(el);
					String strComuna = "";
					if (id != null && !id.trim().isEmpty()) {
						try {
							strComuna = getComuna(el.select("#"+id.trim()+" > a > div > div.item__title").text());
							strComuna = strComuna.replace("�", "e");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							LOGGER.info("#"+ id.trim()+ " > div.item__info-container > div > h2 > a > span");
							LOGGER.info("Url: " + strUrl);

							e.printStackTrace();
						}

						String strPrecioUf = getPrecio(el.select("#"+ id.trim()+ " > a > div > div.item__price").text());
						String strPrecioPesos = NumberUtils.convertUfAPesosMercLibre(uf, strPrecioUf);
						
						String strName = el.select("#"+ id.trim()+ " > a > div > div.item__title").text();
						if (strName.isEmpty()) {
							
							strName = el.select("#"+ id.trim()).text();
						}

						strName = strName.replace("|", "");
						String strFecha = DateUtils.dateToString(new Date(), ScrapConstants.DATE_MASK_MercadoLibre);
						String strConstruido = getConstruido(el.select("#"+ id.trim()+ " > a > div > div.item__attrs").text());
						String strRooms = getDormitorios(el.select("#"+ id.trim()+ " > a > div > div.item__attrs").text());
						if (!strName.trim().isEmpty()) {
							strLista = strLista + "MercadoLibre|"+id+"|"+tipoPropiedad+"|"+tipoTransaccion+ "|" + strFecha + "|"+ strName.trim() + "|" + strComuna.trim()+"|" + strRooms+"|" + "|"+ strConstruido.trim()+"|"+""+"|"+strPrecioUf.trim()+"|"+strPrecioPesos+"|"+ strUrl + "\n";
							listaLineas.add("MercadoLibre|"+id+"|"+tipoPropiedad+"|"+tipoTransaccion+ "|" + strFecha + "|"+ strName.trim() + "|" + strComuna.trim()+"|" + strRooms+"|" + "|"+ strConstruido.trim()+"|"+""+"|"+strPrecioUf.trim()+"|"+strPrecioPesos+"|"+ strUrl);

						}
					}
        	    			

        		}
        		insertIntoDatabase(listaLineas);
				listaLineas = new ArrayList<String>();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	
        }
        OracleMercadoLibreDAO omld = new OracleMercadoLibreDAO();
        List<InformacionInmobiliariaTempVO> result =  omld.getDataToInsert("MercadoLibre",tipoPropiedad, tipoTransaccion);
        insertFromTmpToFinal(result);
        LOGGER.info("MercadoLibre Proceso terminado");
        //FileUtils.writeFile(this.path+"\\"+txtname+".txt", strLista);
	}
	private void insertIntoDatabase(List<String> lista){
		OracleScrapInmobiliarioBatchDAO ocbd = new OracleScrapInmobiliarioBatchDAO();
		//ocbd.batchInformacionInmobiliariaLoader(lista, ScrapConstants.DATE_MASK_MercadoLibre);
	}
	private void insertFromTmpToFinal(List<InformacionInmobiliariaTempVO> lista){
		OracleMercadoLibreDAO omld = new OracleMercadoLibreDAO();
		for(InformacionInmobiliariaTempVO obj : lista){
			try {
				omld.insertInformacionInmobiliaria(obj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private String getComuna(String str){
		String result = "";
		if (str != null){
			String[] arr = str.split("-");
			if (arr.length > 2){
				result = arr[arr.length-2].trim();
			}
		}
		return result;
	}
	private String getUrl(Elements  el){
		String result = "";
		String[] arr =  el.toString().split("\n");
		for (int i = 0; i < arr.length;i++){
			
			if (arr[i].contains("item-url")){
				//<div class="images-viewer" item-url="https://casa.mercadolibre.cl/MLC-456702568-en-puente-alto-_JM" item-id="MLC456702568">
				result =  arr[i];
				result = result.replace("\"", "");
				result = result.replace("<div", "");
				result = result.replace("class", "");
				result = result.replace("images-viewer", "");
				result = result.replace("item-url", "");
				result = result.replace("=", "");
				result = result.replace(">", "");
				String[] subArr = result.split("item-id");
				if (subArr.length>0){
					result = subArr[0].trim();
				}

				//result = result.trim();
				break;
				
				
			}
		}
		return result;
	}
	private String getPrecio(String str){
		String result = "";
		if (str != null){
			str = str.replace(".", "").trim();
			str = str.replace("uf", "").trim();
			str = str.replace("UF", "").trim();

			if (str.contains("$") && !str.contains("U$S")){
				str = str.replace("$", "");
				result = NumberUtils.convertPesosAUf(uf, str.trim());
			}else{
				result = str.trim();
			}
		}
		result = result.replace("U$S", "");
		result = result.replace(",", ".");
		return result;
	}
	private String getConstruido(String str){
		String result = "";
		if (str != null){
			if (str.contains("m�")){
				String[] arr = str.split("m�");
				if (arr.length > 0){
					result = arr[0].trim();	
				}	
			}
			
		}
		return result;
	}
	private String getDormitorios(String str){
		String result = "";
		if (str != null){

			String[] arr = str.split("\\|");
			if (arr.length > 0 && arr.length < 2 ){
				if (arr[0].contains("dorms") ||  arr[0].contains("hab")){
					result = arr[0].trim();
					if (!result.trim().isEmpty()){
						result = result.replace("dorms", "");
						result = result.trim();	
					}	
				}
				
				
			}else{
				result = arr[1].trim();
				result = result.replace("dorms", "");
				result = result.replace(".", "");
			}
			
		}
		result = result.replace("habs", "");
		result = result.replace("hab", "");
		result = result.replace("dorm", "");
		return result.trim();
	}
	private String getMercadoLibreId(Elements  el){
		String result = "";
		String[] arr =  el.toString().split("\n");
		for (int i = 0; i < arr.length;i++){
			
			if (arr[i].contains("id=")){
				result =  arr[i];
				result = result.replace("\"", "");
				result = result.replace(">", "");
				String[] subArr = result.split("id=");
				result = subArr[1].trim();
				
				break;
				
				
			}
		}
		
		
		return result;
	}
	@Override
	public String getTotalAvisos(Document doc) {
		// TODO Auto-generated method stub
		return null;
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
	private boolean existOnMap(String codigo){
		boolean result = false;
		Map<String,String> mapa = new HashMap<String,String>();  
		if (mapa.isEmpty()){
			mapa = loadMapForTest();
		}
		if (mapa.get(codigo) != null){
			result = true;
		}
		return result;
	}
	private Map<String,String> loadMapForTest(){
		Map<String,String> map = new HashMap<String,String>();  
		map.put("MLC458814968","MLC458814968");
		map.put("MLC458821226","MLC458821226");
		map.put("MLC458815451","MLC458815451");
		map.put("MLC457111503","MLC457111503");
		map.put("MLC457659517","MLC457659517");
		map.put("MLC458395524","MLC458395524");
		map.put("MLC458675944","MLC458675944");
		map.put("MLC458806122","MLC458806122");
		map.put("MLC458620632","MLC458620632");
		map.put("MLC458841132","MLC458841132");
		map.put("MLC458839476","MLC458839476");
		map.put("MLC458841353","MLC458841353");
		map.put("MLC458698813","MLC458698813");
		map.put("MLC458697054","MLC458697054");
		map.put("MLC458702067","MLC458702067");
		map.put("MLC458669819","MLC458669819");
		map.put("MLC458829246","MLC458829246");
		map.put("MLC458815307","MLC458815307");
		map.put("MLC458815293","MLC458815293");
		map.put("MLC458815191","MLC458815191");
		map.put("MLC458815150","MLC458815150");
		map.put("MLC458815146","MLC458815146");
		map.put("MLC458838345","MLC458838345");
		map.put("MLC458854437","MLC458854437");
		map.put("MLC456679777","MLC456679777");
		map.put("MLC458815578","MLC458815578");
		map.put("MLC458815474","MLC458815474");
		map.put("MLC458815206","MLC458815206");
		map.put("MLC458834115","MLC458834115");
		map.put("MLC458840466","MLC458840466");
		map.put("MLC458849435","MLC458849435");
		map.put("MLC458826175","MLC458826175");
		map.put("MLC458818414","MLC458818414");
		map.put("MLC457047085","MLC457047085");
		map.put("MLC456794941","MLC456794941");
		map.put("MLC457191969","MLC457191969");
		map.put("MLC458398404","MLC458398404");
		map.put("MLC458851730","MLC458851730");
		map.put("MLC458829962","MLC458829962");
		map.put("MLC458829654","MLC458829654");
		map.put("MLC458666108","MLC458666108");
		map.put("MLC458819066","MLC458819066");
		map.put("MLC458621290","MLC458621290");
		map.put("MLC458620803","MLC458620803");
		map.put("MLC458832285","MLC458832285");
		map.put("MLC458582939","MLC458582939");
		map.put("MLC458710121","MLC458710121");
		map.put("MLC458670443","MLC458670443");
		map.put("MLC458708775","MLC458708775");
		map.put("MLC458859403","MLC458859403");
		map.put("MLC458838157","MLC458838157");
		map.put("MLC457795618","MLC457795618");
		map.put("MLC457891476","MLC457891476");
		map.put("MLC458071626","MLC458071626");
		map.put("MLC458191672","MLC458191672");
		map.put("MLC458266273","MLC458266273");
		map.put("MLC458714269","MLC458714269");
		map.put("MLC458179344","MLC458179344");
		map.put("MLC458552148","MLC458552148");
		map.put("MLC457905598","MLC457905598");
		map.put("MLC457950214","MLC457950214");
		map.put("MLC457796347","MLC457796347");
		map.put("MLC458067781","MLC458067781");
		map.put("MLC458855785","MLC458855785");
		map.put("MLC457612105","MLC457612105");
		map.put("MLC458847411","MLC458847411");
		map.put("MLC458389312","MLC458389312");
		map.put("MLC458856947","MLC458856947");
		map.put("MLC458845550","MLC458845550");
		map.put("MLC458847124","MLC458847124");
		map.put("MLC458856397","MLC458856397");
		map.put("MLC458850097","MLC458850097");
		map.put("MLC457759928","MLC457759928");
		map.put("MLC457017981","MLC457017981");
		map.put("MLC458840989","MLC458840989");
		map.put("MLC456749171","MLC456749171");
		map.put("MLC458852340","MLC458852340");
		map.put("MLC458845510","MLC458845510");
		map.put("MLC456886638","MLC456886638");
		map.put("MLC458853137","MLC458853137");
		map.put("MLC456703779","MLC456703779");
		map.put("MLC456890915","MLC456890915");
		map.put("MLC457063082","MLC457063082");
		map.put("MLC458666109","MLC458666109");
		map.put("MLC458067829","MLC458067829");
		map.put("MLC458621747","MLC458621747");
		map.put("MLC458693080","MLC458693080");
		map.put("MLC458552004","MLC458552004");
		map.put("MLC458839420","MLC458839420");
		map.put("MLC458827436","MLC458827436");
		map.put("MLC458702217","MLC458702217");
		map.put("MLC458713238","MLC458713238");
		map.put("MLC458806396","MLC458806396");
		map.put("MLC458835244","MLC458835244");
		map.put("MLC458548893","MLC458548893");
		map.put("MLC458808694","MLC458808694");
		map.put("MLC458858414","MLC458858414");
		map.put("MLC458533844","MLC458533844");
		map.put("MLC458854460","MLC458854460");
		map.put("MLC457831332","MLC457831332");
		map.put("MLC457789328","MLC457789328");
		map.put("MLC458365335","MLC458365335");
		map.put("MLC458257539","MLC458257539");
		map.put("MLC458843781","MLC458843781");
		map.put("MLC458848410","MLC458848410");
		map.put("MLC458833335","MLC458833335");
		map.put("MLC458841172","MLC458841172");
		map.put("MLC457485591","MLC457485591");
		map.put("MLC458846497","MLC458846497");
		map.put("MLC458847684","MLC458847684");
		map.put("MLC458841099","MLC458841099");
		map.put("MLC458134825","MLC458134825");
		map.put("MLC457681027","MLC457681027");
		map.put("MLC457915726","MLC457915726");
		map.put("MLC458397537","MLC458397537");
		map.put("MLC456879791","MLC456879791");
		map.put("MLC457759897","MLC457759897");
		map.put("MLC457903087","MLC457903087");
		map.put("MLC457061984","MLC457061984");
		map.put("MLC457402480","MLC457402480");
		map.put("MLC457827867","MLC457827867");
		map.put("MLC458849892","MLC458849892");
		map.put("MLC456621178","MLC456621178");
		map.put("MLC458840365","MLC458840365");
		map.put("MLC456487757","MLC456487757");
		map.put("MLC457004875","MLC457004875");
		map.put("MLC458389967","MLC458389967");
		map.put("MLC458848698","MLC458848698");
		map.put("MLC457567535","MLC457567535");
		map.put("MLC457660469","MLC457660469");
		map.put("MLC458441948","MLC458441948");
		map.put("MLC458335196","MLC458335196");
		map.put("MLC458070149","MLC458070149");
		map.put("MLC458219185","MLC458219185");
		map.put("MLC458215709","MLC458215709");
		map.put("MLC458340234","MLC458340234");
		map.put("MLC457892852","MLC457892852");
		map.put("MLC458630414","MLC458630414");
		map.put("MLC458730530","MLC458730530");
		map.put("MLC458539516","MLC458539516");
		map.put("MLC458467971","MLC458467971");
		map.put("MLC458436259","MLC458436259");
		map.put("MLC458153117","MLC458153117");
		map.put("MLC457948596","MLC457948596");
		map.put("MLC458630487","MLC458630487");
		map.put("MLC458400261","MLC458400261");
		map.put("MLC458630840","MLC458630840");
		map.put("MLC458421007","MLC458421007");
		map.put("MLC457986570","MLC457986570");
		map.put("MLC458825966","MLC458825966");
		map.put("MLC458815555","MLC458815555");
		map.put("MLC458847631","MLC458847631");
		map.put("MLC458851740","MLC458851740");
		map.put("MLC458824600","MLC458824600");
		map.put("MLC458848513","MLC458848513");
		map.put("MLC458374598","MLC458374598");
		map.put("MLC457493405","MLC457493405");
		map.put("MLC457299926","MLC457299926");
		map.put("MLC458154862","MLC458154862");
		map.put("MLC458847560","MLC458847560");
		map.put("MLC458857506","MLC458857506");
		map.put("MLC458847375","MLC458847375");
		map.put("MLC457716858","MLC457716858");
		map.put("MLC458399015","MLC458399015");
		map.put("MLC458843838","MLC458843838");
		map.put("MLC458839192","MLC458839192");
		map.put("MLC458848030","MLC458848030");
		map.put("MLC457418342","MLC457418342");
		map.put("MLC457015826","MLC457015826");
		map.put("MLC456840705","MLC456840705");
		map.put("MLC458379051","MLC458379051");
		map.put("MLC458821980","MLC458821980");
		map.put("MLC458815364","MLC458815364");
		map.put("MLC458838922","MLC458838922");
		map.put("MLC458841863","MLC458841863");
		map.put("MLC458855063","MLC458855063");
		map.put("MLC458837324","MLC458837324");
		map.put("MLC458844295","MLC458844295");
		map.put("MLC458814994","MLC458814994");
		map.put("MLC458109034","MLC458109034");
		map.put("MLC458815452","MLC458815452");
		map.put("MLC458814977","MLC458814977");
		map.put("MLC458815447","MLC458815447");
		map.put("MLC458839635","MLC458839635");
		map.put("MLC458833161","MLC458833161");
		map.put("MLC458826622","MLC458826622");
		map.put("MLC458832787","MLC458832787");
		map.put("MLC458836065","MLC458836065");
		map.put("MLC458837108","MLC458837108");
		map.put("MLC458815392","MLC458815392");
		map.put("MLC457879005","MLC457879005");
		map.put("MLC458847600","MLC458847600");
		map.put("MLC458815448","MLC458815448");
		map.put("MLC458813893","MLC458813893");
		map.put("MLC458846651","MLC458846651");
		map.put("MLC458849837","MLC458849837");
		map.put("MLC456684383","MLC456684383");
		map.put("MLC456488777","MLC456488777");
		map.put("MLC457485687","MLC457485687");
		map.put("MLC457149748","MLC457149748");
		map.put("MLC456794995","MLC456794995");
		map.put("MLC458656141","MLC458656141");
		map.put("MLC458820626","MLC458820626");
		map.put("MLC458838923","MLC458838923");
		map.put("MLC458830882","MLC458830882");
		map.put("MLC458809639","MLC458809639");
		map.put("MLC458670252","MLC458670252");
		map.put("MLC458620362","MLC458620362");
		map.put("MLC458828943","MLC458828943");
		map.put("MLC458832192","MLC458832192");
		map.put("MLC458851084","MLC458851084");
		map.put("MLC458550772","MLC458550772");
		map.put("MLC458577735","MLC458577735");
		map.put("MLC458696173","MLC458696173");
		map.put("MLC458582004","MLC458582004");
		map.put("MLC458850258","MLC458850258");
		map.put("MLC458849924","MLC458849924");
		map.put("MLC458842785","MLC458842785");
		map.put("MLC458852902","MLC458852902");
		map.put("MLC458844484","MLC458844484");
		map.put("MLC457721581","MLC457721581");
		map.put("MLC458443589","MLC458443589");
		map.put("MLC458848125","MLC458848125");
		map.put("MLC458587752","MLC458587752");
		map.put("MLC458833244","MLC458833244");
		map.put("MLC458273117","MLC458273117");
		map.put("MLC457905171","MLC457905171");
		map.put("MLC458854948","MLC458854948");
		map.put("MLC458848915","MLC458848915");
		map.put("MLC458842889","MLC458842889");
		map.put("MLC456795029","MLC456795029");
		map.put("MLC457421658","MLC457421658");
		map.put("MLC456834003","MLC456834003");
		map.put("MLC458840896","MLC458840896");
		map.put("MLC456889844","MLC456889844");
		map.put("MLC456679315","MLC456679315");
		map.put("MLC458850231","MLC458850231");
		map.put("MLC458853214","MLC458853214");
		map.put("MLC458392131","MLC458392131");
		map.put("MLC456488833","MLC456488833");
		map.put("MLC457985728","MLC457985728");
		map.put("MLC458399110","MLC458399110");
		map.put("MLC457078925","MLC457078925");
		map.put("MLC456890985","MLC456890985");
		map.put("MLC458365238","MLC458365238");
		map.put("MLC458272843","MLC458272843");
		map.put("MLC458219885","MLC458219885");
		map.put("MLC457960868","MLC457960868");
		map.put("MLC458068898","MLC458068898");
		map.put("MLC458006355","MLC458006355");
		map.put("MLC458156327","MLC458156327");
		map.put("MLC458199076","MLC458199076");
		map.put("MLC457923693","MLC457923693");
		map.put("MLC458179651","MLC458179651");
		map.put("MLC458630749","MLC458630749");
		map.put("MLC458729932","MLC458729932");
		map.put("MLC458266293","MLC458266293");
		map.put("MLC458271936","MLC458271936");
		map.put("MLC458521999","MLC458521999");
		map.put("MLC458498309","MLC458498309");
		map.put("MLC458491615","MLC458491615");
		map.put("MLC458468210","MLC458468210");
		map.put("MLC458427389","MLC458427389");
		map.put("MLC458404717","MLC458404717");
		map.put("MLC458384135","MLC458384135");
		map.put("MLC458630513","MLC458630513");
		map.put("MLC458405427","MLC458405427");
		map.put("MLC458561002","MLC458561002");
		map.put("MLC458463953","MLC458463953");
		map.put("MLC458420977","MLC458420977");
		map.put("MLC458630584","MLC458630584");
		map.put("MLC458630335","MLC458630335");
		map.put("MLC458396430","MLC458396430");
		map.put("MLC458420803","MLC458420803");
		map.put("MLC458514805","MLC458514805");
		map.put("MLC458451734","MLC458451734");
		map.put("MLC458813940","MLC458813940");
		map.put("MLC458815583","MLC458815583");
		map.put("MLC458815183","MLC458815183");
		map.put("MLC458815207","MLC458815207");
		map.put("MLC458836618","MLC458836618");
		map.put("MLC457133645","MLC457133645");
		map.put("MLC458838931","MLC458838931");
		map.put("MLC457587449","MLC457587449");
		map.put("MLC458839149","MLC458839149");
		map.put("MLC458844497","MLC458844497");
		map.put("MLC458851778","MLC458851778");
		map.put("MLC457634818","MLC457634818");
		map.put("MLC456634226","MLC456634226");
		map.put("MLC458826939","MLC458826939");
		map.put("MLC456767161","MLC456767161");
		map.put("MLC458838615","MLC458838615");
		map.put("MLC457115536","MLC457115536");
		map.put("MLC458340472","MLC458340472");
		map.put("MLC457735989","MLC457735989");
		map.put("MLC456683571","MLC456683571");
		map.put("MLC456416301","MLC456416301");
		map.put("MLC458837039","MLC458837039");
		map.put("MLC458842880","MLC458842880");
		map.put("MLC458843678","MLC458843678");
		map.put("MLC457909878","MLC457909878");
		map.put("MLC457656655","MLC457656655");
		map.put("MLC457299399","MLC457299399");
		map.put("MLC456944484","MLC456944484");
		map.put("MLC458840624","MLC458840624");
		map.put("MLC457613837","MLC457613837");
		map.put("MLC458852944","MLC458852944");
		map.put("MLC458815328","MLC458815328");
		map.put("MLC458815142","MLC458815142");
		map.put("MLC458815519","MLC458815519");
		map.put("MLC458851970","MLC458851970");
		map.put("MLC457347551","MLC457347551");
		map.put("MLC458829940","MLC458829940");
		map.put("MLC458847209","MLC458847209");
		map.put("MLC458848796","MLC458848796");
		map.put("MLC458815566","MLC458815566");
		map.put("MLC458815453","MLC458815453");
		map.put("MLC458842674","MLC458842674");
		map.put("MLC458841829","MLC458841829");
		map.put("MLC458828254","MLC458828254");
		map.put("MLC458814974","MLC458814974");
		map.put("MLC458815218","MLC458815218");
		map.put("MLC458815180","MLC458815180");
		map.put("MLC458710182","MLC458710182");
		map.put("MLC458836988","MLC458836988");
		map.put("MLC458618483","MLC458618483");
		map.put("MLC458676197","MLC458676197");
		map.put("MLC458583544","MLC458583544");
		map.put("MLC458842666","MLC458842666");
		map.put("MLC458010091","MLC458010091");
		map.put("MLC458442865","MLC458442865");
		map.put("MLC458438460","MLC458438460");
		map.put("MLC458435695","MLC458435695");
		map.put("MLC458577854","MLC458577854");
		map.put("MLC458497883","MLC458497883");
		map.put("MLC458389588","MLC458389588");
		map.put("MLC458009282","MLC458009282");
		map.put("MLC458845740","MLC458845740");
		map.put("MLC458839729","MLC458839729");
		map.put("MLC458838989","MLC458838989");
		map.put("MLC458842955","MLC458842955");
		map.put("MLC458859437","MLC458859437");
		map.put("MLC457132080","MLC457132080");
		map.put("MLC458843401","MLC458843401");
		map.put("MLC456508647","MLC456508647");
		map.put("MLC457089730","MLC457089730");
		map.put("MLC458405336","MLC458405336");
		map.put("MLC458828998","MLC458828998");
		map.put("MLC458851071","MLC458851071");
		map.put("MLC457015493","MLC457015493");
		map.put("MLC457655200","MLC457655200");
		map.put("MLC458838583","MLC458838583");
		map.put("MLC458852969","MLC458852969");
		map.put("MLC458838798","MLC458838798");
		map.put("MLC456878851","MLC456878851");
		map.put("MLC457063704","MLC457063704");
		map.put("MLC456794920","MLC456794920");
		map.put("MLC456844874","MLC456844874");
		map.put("MLC457343668","MLC457343668");
		map.put("MLC457978243","MLC457978243");
		map.put("MLC456794706","MLC456794706");
		map.put("MLC457071030","MLC457071030");
		map.put("MLC458379609","MLC458379609");
		map.put("MLC457099421","MLC457099421");
		map.put("MLC456852424","MLC456852424");
		map.put("MLC457534767","MLC457534767");
		map.put("MLC457402421","MLC457402421");
		map.put("MLC457796196","MLC457796196");
		map.put("MLC457694750","MLC457694750");
		map.put("MLC456719579","MLC456719579");
		map.put("MLC458398373","MLC458398373");
		map.put("MLC458387584","MLC458387584");
		map.put("MLC457921224","MLC457921224");
		map.put("MLC458588104","MLC458588104");
		map.put("MLC457948981","MLC457948981");
		map.put("MLC458410943","MLC458410943");
		map.put("MLC458476075","MLC458476075");
		map.put("MLC458060304","MLC458060304");
		map.put("MLC458630384","MLC458630384");
		map.put("MLC458587987","MLC458587987");
		map.put("MLC457936222","MLC457936222");
		map.put("MLC458669391","MLC458669391");
		map.put("MLC457941151","MLC457941151");
		map.put("MLC457888599","MLC457888599");
		map.put("MLC458176211","MLC458176211");
		map.put("MLC458588008","MLC458588008");
		map.put("MLC458190425","MLC458190425");
		map.put("MLC458148260","MLC458148260");
		map.put("MLC458482259","MLC458482259");
		map.put("MLC458218424","MLC458218424");
		map.put("MLC458588130","MLC458588130");
		map.put("MLC458421016","MLC458421016");
		map.put("MLC458527151","MLC458527151");
		map.put("MLC458464533","MLC458464533");
		map.put("MLC458522952","MLC458522952");
		map.put("MLC458825734","MLC458825734");
		map.put("MLC458819133","MLC458819133");
		map.put("MLC458740490","MLC458740490");
		map.put("MLC458853336","MLC458853336");
		map.put("MLC458835614","MLC458835614");
		map.put("MLC458826668","MLC458826668");
		map.put("MLC458853833","MLC458853833");
		map.put("MLC458483134","MLC458483134");
		map.put("MLC458465465","MLC458465465");
		map.put("MLC457321436","MLC457321436");
		map.put("MLC458846907","MLC458846907");
		map.put("MLC458852354","MLC458852354");
		map.put("MLC456470716","MLC456470716");
		map.put("MLC458847105","MLC458847105");
		map.put("MLC457663918","MLC457663918");
		map.put("MLC458840673","MLC458840673");
		map.put("MLC458815354","MLC458815354");
		map.put("MLC458815124","MLC458815124");
		map.put("MLC458856890","MLC458856890");
		map.put("MLC458837751","MLC458837751");
		map.put("MLC458815551","MLC458815551");
		map.put("MLC457349882","MLC457349882");
		map.put("MLC458815587","MLC458815587");
		map.put("MLC458815471","MLC458815471");
		map.put("MLC458815198","MLC458815198");
		map.put("MLC458853913","MLC458853913");
		map.put("MLC458843230","MLC458843230");
		map.put("MLC457191213","MLC457191213");
		map.put("MLC458829744","MLC458829744");
		map.put("MLC457702270","MLC457702270");
		map.put("MLC456951355","MLC456951355");
		map.put("MLC456845330","MLC456845330");
		map.put("MLC457165827","MLC457165827");
		map.put("MLC458394965","MLC458394965");
		map.put("MLC456794977","MLC456794977");
		map.put("MLC458391046","MLC458391046");
		map.put("MLC458852238","MLC458852238");
		map.put("MLC458590287","MLC458590287");
		map.put("MLC458730069","MLC458730069");
		map.put("MLC458642917","MLC458642917");
		map.put("MLC458826540","MLC458826540");
		map.put("MLC458840837","MLC458840837");
		map.put("MLC458457921","MLC458457921");
		map.put("MLC458438468","MLC458438468");
		map.put("MLC458836048","MLC458836048");
		map.put("MLC458080834","MLC458080834");
		map.put("MLC458032846","MLC458032846");
		map.put("MLC458265823","MLC458265823");
		map.put("MLC458831973","MLC458831973");
		map.put("MLC458851086","MLC458851086");
		map.put("MLC458833804","MLC458833804");
		map.put("MLC458833515","MLC458833515");
		map.put("MLC456723063","MLC456723063");
		map.put("MLC457329565","MLC457329565");
		map.put("MLC458134808","MLC458134808");
		map.put("MLC456483165","MLC456483165");
		map.put("MLC458845966","MLC458845966");
		map.put("MLC458840049","MLC458840049");
		map.put("MLC458403668","MLC458403668");
		map.put("MLC456945577","MLC456945577");
		map.put("MLC457418923","MLC457418923");
		map.put("MLC457244664","MLC457244664");
		map.put("MLC458833019","MLC458833019");
		map.put("MLC458827018","MLC458827018");
		map.put("MLC458855001","MLC458855001");
		map.put("MLC458850233","MLC458850233");
		map.put("MLC456841455","MLC456841455");
		map.put("MLC458845896","MLC458845896");
		map.put("MLC456888314","MLC456888314");
		map.put("MLC458489705","MLC458489705");
		map.put("MLC458644713","MLC458644713");
		map.put("MLC458052985","MLC458052985");
		map.put("MLC457885017","MLC457885017");
		map.put("MLC457853220","MLC457853220");
		map.put("MLC458630343","MLC458630343");
		map.put("MLC458716158","MLC458716158");
		map.put("MLC457923216","MLC457923216");
		map.put("MLC457885993","MLC457885993");
		map.put("MLC457961493","MLC457961493");
		map.put("MLC458248524","MLC458248524");
		map.put("MLC458218408","MLC458218408");
		map.put("MLC457916527","MLC457916527");
		map.put("MLC458067315","MLC458067315");
		map.put("MLC458272215","MLC458272215");
		map.put("MLC458066671","MLC458066671");
		map.put("MLC457865163","MLC457865163");
		map.put("MLC458668521","MLC458668521");
		map.put("MLC458667973","MLC458667973");
		map.put("MLC458475125","MLC458475125");
		map.put("MLC458430379","MLC458430379");
		map.put("MLC458382433","MLC458382433");
		map.put("MLC458369108","MLC458369108");
		map.put("MLC458560963","MLC458560963");
		map.put("MLC458488174","MLC458488174");
		map.put("MLC458439391","MLC458439391");
		map.put("MLC458420922","MLC458420922");
		map.put("MLC458390941","MLC458390941");
		map.put("MLC458532291","MLC458532291");
		map.put("MLC458258344","MLC458258344");
		map.put("MLC458401581","MLC458401581");
		map.put("MLC458500080","MLC458500080");
		map.put("MLC458853984","MLC458853984");
		map.put("MLC458797467","MLC458797467");
		map.put("MLC456600924","MLC456600924");
		map.put("MLC458851731","MLC458851731");
		map.put("MLC458837844","MLC458837844");
		map.put("MLC458687404","MLC458687404");
		map.put("MLC458846761","MLC458846761");
		map.put("MLC458844084","MLC458844084");
		map.put("MLC458827143","MLC458827143");
		map.put("MLC458851460","MLC458851460");
		map.put("MLC457096919","MLC457096919");
		map.put("MLC457663961","MLC457663961");
		map.put("MLC458832958","MLC458832958");
		map.put("MLC458856990","MLC458856990");
		map.put("MLC458258188","MLC458258188");
		map.put("MLC457916022","MLC457916022");
		map.put("MLC457345394","MLC457345394");
		map.put("MLC456827919","MLC456827919");
		map.put("MLC458560517","MLC458560517");
		map.put("MLC457130566","MLC457130566");
		map.put("MLC458826946","MLC458826946");
		map.put("MLC458850806","MLC458850806");
		map.put("MLC458376135","MLC458376135");
		map.put("MLC458853124","MLC458853124");
		map.put("MLC458815458","MLC458815458");
		map.put("MLC458841599","MLC458841599");
		map.put("MLC458839450","MLC458839450");
		map.put("MLC458842614","MLC458842614");
		map.put("MLC458815195","MLC458815195");
		map.put("MLC458815202","MLC458815202");
		map.put("MLC457131643","MLC457131643");
		map.put("MLC458600413","MLC458600413");
		map.put("MLC458854942","MLC458854942");
		map.put("MLC458829939","MLC458829939");
		map.put("MLC458840951","MLC458840951");
		map.put("MLC458815475","MLC458815475");
		map.put("MLC458850428","MLC458850428");
		map.put("MLC458840398","MLC458840398");
		map.put("MLC458815506","MLC458815506");
		map.put("MLC458815463","MLC458815463");
		map.put("MLC458840589","MLC458840589");
		map.put("MLC456494673","MLC456494673");
		map.put("MLC458856019","MLC458856019");
		map.put("MLC458815215","MLC458815215");
		map.put("MLC458814970","MLC458814970");
		map.put("MLC458848065","MLC458848065");
		map.put("MLC458839851","MLC458839851");
		map.put("MLC458849605","MLC458849605");
		map.put("MLC456925541","MLC456925541");
		map.put("MLC458838885","MLC458838885");
		map.put("MLC458856208","MLC458856208");
		map.put("MLC458335336","MLC458335336");
		map.put("MLC456502665","MLC456502665");
		map.put("MLC458155953","MLC458155953");
		map.put("MLC458191062","MLC458191062");
		map.put("MLC458197656","MLC458197656");
		map.put("MLC458631058","MLC458631058");
		map.put("MLC458630434","MLC458630434");
		map.put("MLC458669255","MLC458669255");
		map.put("MLC457981796","MLC457981796");
		map.put("MLC458321438","MLC458321438");
		map.put("MLC458219890","MLC458219890");
		map.put("MLC458156378","MLC458156378");
		map.put("MLC457893080","MLC457893080");
		map.put("MLC458163793","MLC458163793");
		map.put("MLC458346211","MLC458346211");
		map.put("MLC458120254","MLC458120254");
		map.put("MLC458101481","MLC458101481");
		map.put("MLC457905467","MLC457905467");
		map.put("MLC458844478","MLC458844478");
		map.put("MLC458105414","MLC458105414");
		map.put("MLC457916761","MLC457916761");
		map.put("MLC458402548","MLC458402548");
		map.put("MLC458527886","MLC458527886");
		map.put("MLC457959475","MLC457959475");
		map.put("MLC458195833","MLC458195833");
		map.put("MLC458420772","MLC458420772");
		map.put("MLC458514723","MLC458514723");
		map.put("MLC458403764","MLC458403764");
		map.put("MLC458481157","MLC458481157");
		map.put("MLC458847132","MLC458847132");
		map.put("MLC457545410","MLC457545410");
		map.put("MLC458439318","MLC458439318");
		map.put("MLC458655378","MLC458655378");
		map.put("MLC458857347","MLC458857347");
		map.put("MLC458428896","MLC458428896");
		map.put("MLC458478444","MLC458478444");
		map.put("MLC458067613","MLC458067613");
		map.put("MLC458613312","MLC458613312");
		map.put("MLC458427623","MLC458427623");
		map.put("MLC457864234","MLC457864234");
		map.put("MLC457249399","MLC457249399");
		map.put("MLC458840458","MLC458840458");
		map.put("MLC458847121","MLC458847121");
		map.put("MLC458826927","MLC458826927");
		map.put("MLC458840234","MLC458840234");
		map.put("MLC458832214","MLC458832214");
		map.put("MLC456679353","MLC456679353");
		map.put("MLC457899751","MLC457899751");
		map.put("MLC457709614","MLC457709614");
		map.put("MLC457013522","MLC457013522");
		map.put("MLC456886812","MLC456886812");
		map.put("MLC456478595","MLC456478595");
		map.put("MLC456804397","MLC456804397");
		map.put("MLC457069491","MLC457069491");
		map.put("MLC457684925","MLC457684925");
		map.put("MLC458840349","MLC458840349");
		map.put("MLC458553949","MLC458553949");
		map.put("MLC458634701","MLC458634701");
		map.put("MLC458837411","MLC458837411");
		map.put("MLC456886500","MLC456886500");
		map.put("MLC457257332","MLC457257332");
		map.put("MLC456502081","MLC456502081");
		map.put("MLC458848253","MLC458848253");
		map.put("MLC457789273","MLC457789273");
		map.put("MLC458514363","MLC458514363");
		map.put("MLC457917442","MLC457917442");
		map.put("MLC458842882","MLC458842882");
		map.put("MLC457132258","MLC457132258");
		map.put("MLC458847283","MLC458847283");
		map.put("MLC458110680","MLC458110680");
		map.put("MLC457133042","MLC457133042");
		map.put("MLC457295711","MLC457295711");
		map.put("MLC458826623","MLC458826623");
		map.put("MLC458815049","MLC458815049");
		map.put("MLC458437721","MLC458437721");
		map.put("MLC457903032","MLC457903032");
		map.put("MLC458630608","MLC458630608");
		map.put("MLC457896247","MLC457896247");
		map.put("MLC457891677","MLC457891677");
		map.put("MLC458667878","MLC458667878");
		map.put("MLC458834519","MLC458834519");
		map.put("MLC458329880","MLC458329880");
		map.put("MLC458630359","MLC458630359");
		map.put("MLC458668710","MLC458668710");
		map.put("MLC458477658","MLC458477658");
		map.put("MLC458403721","MLC458403721");
		map.put("MLC458393485","MLC458393485");
		map.put("MLC458079171","MLC458079171");
		map.put("MLC458425420","MLC458425420");
		map.put("MLC458487490","MLC458487490");
		map.put("MLC458420909","MLC458420909");
		map.put("MLC458850341","MLC458850341");
		map.put("MLC458815177","MLC458815177");
		map.put("MLC458840358","MLC458840358");
		map.put("MLC458560102","MLC458560102");
		map.put("MLC458836448","MLC458836448");
		map.put("MLC458833213","MLC458833213");
		map.put("MLC458843795","MLC458843795");
		map.put("MLC458837806","MLC458837806");
		map.put("MLC458845937","MLC458845937");
		map.put("MLC458391204","MLC458391204");
		map.put("MLC458851785","MLC458851785");
		map.put("MLC458387723","MLC458387723");
		map.put("MLC458850486","MLC458850486");
		map.put("MLC458852103","MLC458852103");
		map.put("MLC458838353","MLC458838353");
		map.put("MLC456917674","MLC456917674");
		map.put("MLC457682416","MLC457682416");
		map.put("MLC457651287","MLC457651287");
		map.put("MLC456480769","MLC456480769");
		map.put("MLC458189556","MLC458189556");
		map.put("MLC456465375","MLC456465375");
		map.put("MLC456485518","MLC456485518");
		map.put("MLC456484973","MLC456484973");
		map.put("MLC458716058","MLC458716058");
		map.put("MLC458855483","MLC458855483");
		map.put("MLC458842410","MLC458842410");
		map.put("MLC457567174","MLC457567174");
		map.put("MLC457297156","MLC457297156");
		map.put("MLC458845582","MLC458845582");
		map.put("MLC456921890","MLC456921890");
		map.put("MLC458384912","MLC458384912");
		map.put("MLC458821774","MLC458821774");
		map.put("MLC458839924","MLC458839924");
		map.put("MLC458846580","MLC458846580");
		map.put("MLC458815357","MLC458815357");
		map.put("MLC458815356","MLC458815356");
		map.put("MLC458815348","MLC458815348");
		map.put("MLC458815158","MLC458815158");
		map.put("MLC458842708","MLC458842708");
		map.put("MLC458764817","MLC458764817");
		map.put("MLC458731520","MLC458731520");
		map.put("MLC458772826","MLC458772826");
		map.put("MLC458726685","MLC458726685");
		map.put("MLC458717365","MLC458717365");
		map.put("MLC458717382","MLC458717382");
		map.put("MLC458772822","MLC458772822");
		map.put("MLC457900450","MLC457900450");
		map.put("MLC457831540","MLC457831540");
		map.put("MLC458197862","MLC458197862");
		map.put("MLC458067111","MLC458067111");
		map.put("MLC458069207","MLC458069207");
		map.put("MLC458333316","MLC458333316");
		map.put("MLC458179588","MLC458179588");
		map.put("MLC458343403","MLC458343403");
		map.put("MLC458405762","MLC458405762");
		map.put("MLC457648983","MLC457648983");
		map.put("MLC457123428","MLC457123428");
		map.put("MLC458397979","MLC458397979");
		map.put("MLC458854134","MLC458854134");
		map.put("MLC458842857","MLC458842857");
		map.put("MLC456773765","MLC456773765");
		map.put("MLC456794865","MLC456794865");
		map.put("MLC457853104","MLC457853104");
		map.put("MLC456599542","MLC456599542");
		map.put("MLC458846308","MLC458846308");
		map.put("MLC457155430","MLC457155430");
		map.put("MLC458840544","MLC458840544");
		map.put("MLC456474361","MLC456474361");
		map.put("MLC456477978","MLC456477978");
		map.put("MLC458390735","MLC458390735");
		map.put("MLC456795031","MLC456795031");
		map.put("MLC457892754","MLC457892754");
		map.put("MLC458587716","MLC458587716");
		map.put("MLC458667907","MLC458667907");
		map.put("MLC458587290","MLC458587290");
		map.put("MLC458154506","MLC458154506");
		map.put("MLC458086784","MLC458086784");
		map.put("MLC458834989","MLC458834989");
		map.put("MLC458563986","MLC458563986");
		map.put("MLC458837267","MLC458837267");
		map.put("MLC458815315","MLC458815315");
		map.put("MLC458815241","MLC458815241");
		map.put("MLC458815267","MLC458815267");
		map.put("MLC458528228","MLC458528228");
		map.put("MLC458824453","MLC458824453");
		map.put("MLC458839539","MLC458839539");
		map.put("MLC458830010","MLC458830010");
		map.put("MLC458710400","MLC458710400");
		map.put("MLC458263305","MLC458263305");
		map.put("MLC458174410","MLC458174410");
		map.put("MLC458218130","MLC458218130");
		map.put("MLC458538281","MLC458538281");
		map.put("MLC458535157","MLC458535157");
		map.put("MLC458505806","MLC458505806");
		map.put("MLC458488165","MLC458488165");
		map.put("MLC458529510","MLC458529510");
		map.put("MLC458529816","MLC458529816");
		map.put("MLC458527043","MLC458527043");
		map.put("MLC458404115","MLC458404115");
		map.put("MLC458421197","MLC458421197");
		map.put("MLC458669110","MLC458669110");
		map.put("MLC458587482","MLC458587482");
		map.put("MLC458441039","MLC458441039");
		map.put("MLC458667953","MLC458667953");
		map.put("MLC458401116","MLC458401116");
		map.put("MLC458844218","MLC458844218");
		map.put("MLC458851735","MLC458851735");
		map.put("MLC457586836","MLC457586836");
		map.put("MLC458568904","MLC458568904");
		map.put("MLC458714526","MLC458714526");
		map.put("MLC457550706","MLC457550706");
		map.put("MLC458669439","MLC458669439");
		map.put("MLC458844044","MLC458844044");
		map.put("MLC458358645","MLC458358645");
		map.put("MLC457153581","MLC457153581");
		map.put("MLC458452955","MLC458452955");
		map.put("MLC457934775","MLC457934775");
		map.put("MLC458347571","MLC458347571");
		map.put("MLC458851083","MLC458851083");
		map.put("MLC456478582","MLC456478582");
		map.put("MLC457612175","MLC457612175");
		map.put("MLC458834522","MLC458834522");
		map.put("MLC458815429","MLC458815429");
		map.put("MLC458851530","MLC458851530");
		map.put("MLC458815317","MLC458815317");
		map.put("MLC458815280","MLC458815280");
		map.put("MLC458815287","MLC458815287");
		map.put("MLC458840120","MLC458840120");
		map.put("MLC458565314","MLC458565314");
		map.put("MLC458852137","MLC458852137");
		map.put("MLC457421331","MLC457421331");
		map.put("MLC458828295","MLC458828295");
		map.put("MLC458837591","MLC458837591");
		map.put("MLC458815485","MLC458815485");
		map.put("MLC458815526","MLC458815526");
		map.put("MLC458814954","MLC458814954");
		map.put("MLC458815459","MLC458815459");
		map.put("MLC458835269","MLC458835269");
		map.put("MLC458815053","MLC458815053");
		map.put("MLC458815454","MLC458815454");
		map.put("MLC458815537","MLC458815537");
		map.put("MLC456765704","MLC456765704");
		map.put("MLC458716288","MLC458716288");
		map.put("MLC458497748","MLC458497748");
		map.put("MLC458797728","MLC458797728");
		map.put("MLC458810377","MLC458810377");
		map.put("MLC458621529","MLC458621529");
		map.put("MLC458837545","MLC458837545");
		map.put("MLC458666685","MLC458666685");
		map.put("MLC458619879","MLC458619879");
		map.put("MLC458651685","MLC458651685");
		map.put("MLC458688509","MLC458688509");
		map.put("MLC458152948","MLC458152948");
		map.put("MLC458157061","MLC458157061");
		map.put("MLC457798462","MLC457798462");
		map.put("MLC458528914","MLC458528914");
		map.put("MLC458407769","MLC458407769");
		map.put("MLC458044107","MLC458044107");
		map.put("MLC457247950","MLC457247950");
		map.put("MLC458849959","MLC458849959");
		map.put("MLC458836136","MLC458836136");
		map.put("MLC457099112","MLC457099112");
		map.put("MLC458835828","MLC458835828");
		map.put("MLC458848579","MLC458848579");
		map.put("MLC458831907","MLC458831907");
		map.put("MLC458851993","MLC458851993");
		map.put("MLC457493997","MLC457493997");
		map.put("MLC458837433","MLC458837433");
		map.put("MLC458842412","MLC458842412");
		map.put("MLC456461318","MLC456461318");
		map.put("MLC457244883","MLC457244883");
		map.put("MLC456886343","MLC456886343");
		map.put("MLC457099370","MLC457099370");
		map.put("MLC458842742","MLC458842742");
		map.put("MLC458853863","MLC458853863");
		map.put("MLC456632446","MLC456632446");
		map.put("MLC456851632","MLC456851632");
		map.put("MLC456892089","MLC456892089");
		map.put("MLC456802933","MLC456802933");
		map.put("MLC458067069","MLC458067069");
		map.put("MLC458587692","MLC458587692");
		map.put("MLC458133167","MLC458133167");
		map.put("MLC457886948","MLC457886948");
		map.put("MLC458198999","MLC458198999");
		map.put("MLC458364364","MLC458364364");
		map.put("MLC458339028","MLC458339028");
		map.put("MLC458117722","MLC458117722");
		map.put("MLC458410209","MLC458410209");
		map.put("MLC458157296","MLC458157296");
		map.put("MLC458048591","MLC458048591");
		map.put("MLC458219318","MLC458219318");
		map.put("MLC458049974","MLC458049974");
		map.put("MLC458587913","MLC458587913");
		map.put("MLC458669174","MLC458669174");
		map.put("MLC458588206","MLC458588206");
		map.put("MLC457962238","MLC457962238");
		map.put("MLC458010833","MLC458010833");
		map.put("MLC457940780","MLC457940780");
		map.put("MLC458711942","MLC458711942");
		map.put("MLC458367568","MLC458367568");
		map.put("MLC458477041","MLC458477041");
		map.put("MLC458374693","MLC458374693");
		map.put("MLC458365537","MLC458365537");
		map.put("MLC458334740","MLC458334740");
		map.put("MLC458192455","MLC458192455");
		map.put("MLC458587782","MLC458587782");
		map.put("MLC458441385","MLC458441385");
		map.put("MLC458402409","MLC458402409");
		map.put("MLC458395276","MLC458395276");
		map.put("MLC458587141","MLC458587141");
		map.put("MLC458387300","MLC458387300");
		map.put("MLC458450612","MLC458450612");
		map.put("MLC457887212","MLC457887212");
		map.put("MLC458477791","MLC458477791");
		map.put("MLC458393777","MLC458393777");
		map.put("MLC458441011","MLC458441011");
		map.put("MLC458797004","MLC458797004");
		map.put("MLC457826032","MLC457826032");
		map.put("MLC458797419","MLC458797419");
		map.put("MLC458832225","MLC458832225");
		map.put("MLC458843565","MLC458843565");
		map.put("MLC458832946","MLC458832946");
		map.put("MLC458851992","MLC458851992");
		map.put("MLC457586877","MLC457586877");
		map.put("MLC458840749","MLC458840749");
		map.put("MLC458710808","MLC458710808");
		map.put("MLC457419797","MLC457419797");
		map.put("MLC458497799","MLC458497799");
		map.put("MLC458848869","MLC458848869");
		map.put("MLC456922493","MLC456922493");
		map.put("MLC457007799","MLC457007799");
		map.put("MLC456882591","MLC456882591");
		map.put("MLC458264468","MLC458264468");
		map.put("MLC457065585","MLC457065585");
		map.put("MLC457931611","MLC457931611");
		map.put("MLC457884787","MLC457884787");
		map.put("MLC458839619","MLC458839619");
		map.put("MLC458837326","MLC458837326");
		map.put("MLC458851881","MLC458851881");
		map.put("MLC457565744","MLC457565744");
		map.put("MLC457011985","MLC457011985");
		map.put("MLC457763503","MLC457763503");
		map.put("MLC458852049","MLC458852049");
		map.put("MLC458842637","MLC458842637");
		map.put("MLC458815298","MLC458815298");
		map.put("MLC458815176","MLC458815176");
		map.put("MLC458839826","MLC458839826");
		map.put("MLC458844660","MLC458844660");
		map.put("MLC456846518","MLC456846518");
		map.put("MLC456634392","MLC456634392");
		map.put("MLC458818443","MLC458818443");
		map.put("MLC458819717","MLC458819717");
		map.put("MLC458818819","MLC458818819");
		map.put("MLC458815418","MLC458815418");
		map.put("MLC458815383","MLC458815383");
		map.put("MLC458815170","MLC458815170");
		map.put("MLC458815482","MLC458815482");
		map.put("MLC458818361","MLC458818361");
		map.put("MLC458831331","MLC458831331");
		map.put("MLC458829002","MLC458829002");
		map.put("MLC458815533","MLC458815533");
		map.put("MLC458815483","MLC458815483");
		map.put("MLC458831396","MLC458831396");
		map.put("MLC458842707","MLC458842707");
		map.put("MLC458815465","MLC458815465");
		map.put("MLC458815518","MLC458815518");
		map.put("MLC457491996","MLC457491996");
		map.put("MLC458851724","MLC458851724");
		map.put("MLC458832955","MLC458832955");
		map.put("MLC458842761","MLC458842761");
		map.put("MLC456993292","MLC456993292");
		map.put("MLC456477516","MLC456477516");
		map.put("MLC456485028","MLC456485028");
		map.put("MLC457168518","MLC457168518");
		map.put("MLC457096387","MLC457096387");
		map.put("MLC458840176","MLC458840176");
		map.put("MLC456969647","MLC456969647");
		map.put("MLC458389694","MLC458389694");
		map.put("MLC458487991","MLC458487991");
		map.put("MLC457112644","MLC457112644");
		map.put("MLC457662510","MLC457662510");
		map.put("MLC457614280","MLC457614280");
		map.put("MLC457245796","MLC457245796");
		map.put("MLC456952507","MLC456952507");
		map.put("MLC457016242","MLC457016242");
		map.put("MLC456922526","MLC456922526");
		map.put("MLC458842063","MLC458842063");
		map.put("MLC458852218","MLC458852218");
		map.put("MLC458851455","MLC458851455");
		map.put("MLC458815403","MLC458815403");
		map.put("MLC458854550","MLC458854550");
		map.put("MLC458815469","MLC458815469");
		map.put("MLC458841649","MLC458841649");
		map.put("MLC458857829","MLC458857829");
		map.put("MLC458815440","MLC458815440");
		map.put("MLC458815378","MLC458815378");
		map.put("MLC458550071","MLC458550071");
		map.put("MLC458815350","MLC458815350");
		map.put("MLC458842861","MLC458842861");
		map.put("MLC458388757","MLC458388757");
		map.put("MLC458772591","MLC458772591");
		map.put("MLC458711078","MLC458711078");
		map.put("MLC458714055","MLC458714055");
		map.put("MLC458771081","MLC458771081");
		map.put("MLC458706782","MLC458706782");
		map.put("MLC458773420","MLC458773420");
		map.put("MLC458739290","MLC458739290");
		map.put("MLC458764939","MLC458764939");
		map.put("MLC458698775","MLC458698775");
		map.put("MLC458746129","MLC458746129");
		map.put("MLC458730560","MLC458730560");
		map.put("MLC458746124","MLC458746124");
		map.put("MLC458716520","MLC458716520");
		map.put("MLC458771950","MLC458771950");
		map.put("MLC458714984","MLC458714984");
		map.put("MLC458716101","MLC458716101");
		map.put("MLC458773228","MLC458773228");
		map.put("MLC458771917","MLC458771917");
		map.put("MLC458771978","MLC458771978");
		map.put("MLC458247572","MLC458247572");
		map.put("MLC458730758","MLC458730758");
		map.put("MLC458768871","MLC458768871");
		map.put("MLC458753985","MLC458753985");
		map.put("MLC458753138","MLC458753138");
		map.put("MLC458731294","MLC458731294");
		map.put("MLC458768802","MLC458768802");
		map.put("MLC458405846","MLC458405846");
		map.put("MLC458713760","MLC458713760");
		map.put("MLC458716569","MLC458716569");
		map.put("MLC458773234","MLC458773234");
		map.put("MLC458715930","MLC458715930");
		map.put("MLC458730617","MLC458730617");
		map.put("MLC458396640","MLC458396640");
		map.put("MLC458772731","MLC458772731");
		map.put("MLC458712622","MLC458712622");
		map.put("MLC458773345","MLC458773345");
		map.put("MLC458718326","MLC458718326");
		map.put("MLC458713456","MLC458713456");
		map.put("MLC458721825","MLC458721825");
		map.put("MLC458733218","MLC458733218");
		map.put("MLC458766735","MLC458766735");
		map.put("MLC458710529","MLC458710529");
		map.put("MLC458722315","MLC458722315");
		map.put("MLC458729872","MLC458729872");
		map.put("MLC458729648","MLC458729648");
		map.put("MLC458730405","MLC458730405");
		map.put("MLC458699112","MLC458699112");
		map.put("MLC458765731","MLC458765731");
		map.put("MLC458772757","MLC458772757");
		map.put("MLC458772050","MLC458772050");
		map.put("MLC458713116","MLC458713116");
		map.put("MLC458755682","MLC458755682");
		map.put("MLC458730413","MLC458730413");
		map.put("MLC458773041","MLC458773041");
		map.put("MLC458069948","MLC458069948");
		map.put("MLC458711283","MLC458711283");
		map.put("MLC458772516","MLC458772516");
		map.put("MLC458709547","MLC458709547");
		map.put("MLC458711731","MLC458711731");
		map.put("MLC458704856","MLC458704856");
		map.put("MLC458713450","MLC458713450");
		map.put("MLC458766543","MLC458766543");
		map.put("MLC458704359","MLC458704359");
		map.put("MLC458714011","MLC458714011");
		map.put("MLC458710864","MLC458710864");
		map.put("MLC458723285","MLC458723285");
		map.put("MLC458716388","MLC458716388");
		map.put("MLC458769521","MLC458769521");
		map.put("MLC458755488","MLC458755488");
		map.put("MLC458710047","MLC458710047");
		map.put("MLC458747228","MLC458747228");
		map.put("MLC458773350","MLC458773350");
		map.put("MLC458773384","MLC458773384");
		map.put("MLC458714997","MLC458714997");
		map.put("MLC458726220","MLC458726220");
		map.put("MLC458773890","MLC458773890");
		map.put("MLC458710442","MLC458710442");
		map.put("MLC458709514","MLC458709514");
		map.put("MLC457795184","MLC457795184");
		map.put("MLC457913483","MLC457913483");
		map.put("MLC458715102","MLC458715102");
		map.put("MLC458739358","MLC458739358");
		map.put("MLC458772102","MLC458772102");
		map.put("MLC458711961","MLC458711961");
		map.put("MLC458749430","MLC458749430");
		map.put("MLC458066857","MLC458066857");
		map.put("MLC458714123","MLC458714123");
		map.put("MLC458714827","MLC458714827");
		map.put("MLC458702781","MLC458702781");
		map.put("MLC458712559","MLC458712559");
		map.put("MLC458712227","MLC458712227");
		map.put("MLC458771908","MLC458771908");
		map.put("MLC458765368","MLC458765368");
		map.put("MLC458771239","MLC458771239");
		map.put("MLC458697024","MLC458697024");
		map.put("MLC458772948","MLC458772948");
		map.put("MLC458773396","MLC458773396");
		map.put("MLC458713993","MLC458713993");
		map.put("MLC458714514","MLC458714514");
		map.put("MLC458206078","MLC458206078");
		map.put("MLC458730944","MLC458730944");
		map.put("MLC458773002","MLC458773002");
		map.put("MLC458717888","MLC458717888");
		map.put("MLC458716191","MLC458716191");
		map.put("MLC458772231","MLC458772231");
		map.put("MLC458726267","MLC458726267");
		map.put("MLC458740543","MLC458740543");
		map.put("MLC458730584","MLC458730584");
		map.put("MLC458709607","MLC458709607");
		map.put("MLC458728597","MLC458728597");
		map.put("MLC458729850","MLC458729850");
		map.put("MLC458714203","MLC458714203");
		map.put("MLC458722943","MLC458722943");
		map.put("MLC458067172","MLC458067172");
		map.put("MLC458713205","MLC458713205");
		map.put("MLC458730781","MLC458730781");
		map.put("MLC458713348","MLC458713348");
		map.put("MLC458773566","MLC458773566");
		map.put("MLC458773495","MLC458773495");
		map.put("MLC458762306","MLC458762306");
		map.put("MLC458740538","MLC458740538");
		map.put("MLC458720262","MLC458720262");
		map.put("MLC458765589","MLC458765589");
		map.put("MLC458715413","MLC458715413");
		map.put("MLC458773189","MLC458773189");
		map.put("MLC458187388","MLC458187388");
		map.put("MLC458715741","MLC458715741");
		map.put("MLC458759361","MLC458759361");
		map.put("MLC458711991","MLC458711991");
		map.put("MLC458762612","MLC458762612");
		map.put("MLC458715153","MLC458715153");
		map.put("MLC458773775","MLC458773775");
		map.put("MLC458714001","MLC458714001");
		map.put("MLC458713987","MLC458713987");
		map.put("MLC458769374","MLC458769374");
		map.put("MLC458764872","MLC458764872");
		map.put("MLC458768177","MLC458768177");
		map.put("MLC458729557","MLC458729557");
		map.put("MLC458742874","MLC458742874");
		map.put("MLC458742494","MLC458742494");
		map.put("MLC458724065","MLC458724065");
		map.put("MLC458767485","MLC458767485");
		map.put("MLC458767473","MLC458767473");
		map.put("MLC458764343","MLC458764343");
		map.put("MLC458767504","MLC458767504");
		map.put("MLC458767956","MLC458767956");
		map.put("MLC458767276","MLC458767276");
		map.put("MLC458752622","MLC458752622");
		map.put("MLC458768994","MLC458768994");
		map.put("MLC458769393","MLC458769393");
		map.put("MLC458766147","MLC458766147");
		map.put("MLC458711451","MLC458711451");
		map.put("MLC458766758","MLC458766758");
		map.put("MLC458762502","MLC458762502");
		map.put("MLC458751499","MLC458751499");
		map.put("MLC458765902","MLC458765902");
		map.put("MLC458766446","MLC458766446");
		map.put("MLC458731288","MLC458731288");
		map.put("MLC458709101","MLC458709101");
		map.put("MLC458745496","MLC458745496");
		map.put("MLC458767872","MLC458767872");
		map.put("MLC458713850","MLC458713850");
		map.put("MLC458758600","MLC458758600");
		map.put("MLC458765061","MLC458765061");
		map.put("MLC458746465","MLC458746465");
		map.put("MLC458753410","MLC458753410");
		map.put("MLC458766940","MLC458766940");
		map.put("MLC458772586","MLC458772586");
		map.put("MLC458769390","MLC458769390");
		map.put("MLC458772823","MLC458772823");
		map.put("MLC458711769","MLC458711769");
		map.put("MLC458716636","MLC458716636");
		map.put("MLC458729859","MLC458729859");
		map.put("MLC458772440","MLC458772440");
		map.put("MLC458760866","MLC458760866");
		map.put("MLC458767375","MLC458767375");
		map.put("MLC458764820","MLC458764820");
		map.put("MLC458682206","MLC458682206");
		map.put("MLC458773782","MLC458773782");
		map.put("MLC458745447","MLC458745447");
		map.put("MLC458772636","MLC458772636");
		map.put("MLC458727283","MLC458727283");
		map.put("MLC458765981","MLC458765981");
		map.put("MLC458708841","MLC458708841");
		map.put("MLC458772509","MLC458772509");
		map.put("MLC458745522","MLC458745522");
		map.put("MLC458711556","MLC458711556");
		map.put("MLC458772789","MLC458772789");
		map.put("MLC458727108","MLC458727108");
		map.put("MLC458762398","MLC458762398");
		map.put("MLC458712222","MLC458712222");
		map.put("MLC458769958","MLC458769958");
		map.put("MLC458718349","MLC458718349");
		map.put("MLC458766292","MLC458766292");
		map.put("MLC458745534","MLC458745534");
		map.put("MLC458765826","MLC458765826");
		map.put("MLC458697827","MLC458697827");
		map.put("MLC458760117","MLC458760117");
		map.put("MLC458719480","MLC458719480");
		map.put("MLC458755749","MLC458755749");
		map.put("MLC458763689","MLC458763689");
		map.put("MLC458761524","MLC458761524");
		map.put("MLC458716168","MLC458716168");
		map.put("MLC458747568","MLC458747568");
		map.put("MLC458750012","MLC458750012");
		map.put("MLC458745030","MLC458745030");
		map.put("MLC458716010","MLC458716010");
		map.put("MLC458697968","MLC458697968");
		map.put("MLC458767199","MLC458767199");
		map.put("MLC458757777","MLC458757777");
		map.put("MLC458770577","MLC458770577");
		map.put("MLC458766746","MLC458766746");
		map.put("MLC458715331","MLC458715331");
		map.put("MLC458752561","MLC458752561");
		map.put("MLC458717751","MLC458717751");
		map.put("MLC458769408","MLC458769408");
		map.put("MLC458767990","MLC458767990");
		map.put("MLC458746570","MLC458746570");
		map.put("MLC458763609","MLC458763609");
		map.put("MLC458765390","MLC458765390");
		map.put("MLC458769404","MLC458769404");
		map.put("MLC458717695","MLC458717695");
		map.put("MLC458753935","MLC458753935");
		map.put("MLC458697034","MLC458697034");
		map.put("MLC458752298","MLC458752298");
		map.put("MLC458730177","MLC458730177");
		map.put("MLC458710932","MLC458710932");
		map.put("MLC458762503","MLC458762503");
		map.put("MLC458712502","MLC458712502");
		map.put("MLC458713506","MLC458713506");
		map.put("MLC458760801","MLC458760801");
		map.put("MLC458740539","MLC458740539");
		map.put("MLC458765982","MLC458765982");
		map.put("MLC458714719","MLC458714719");
		map.put("MLC458762509","MLC458762509");
		map.put("MLC458760526","MLC458760526");
		map.put("MLC458708343","MLC458708343");
		map.put("MLC458767979","MLC458767979");
		map.put("MLC458767807","MLC458767807");
		map.put("MLC458762604","MLC458762604");
		map.put("MLC458742781","MLC458742781");
		map.put("MLC458717443","MLC458717443");
		map.put("MLC458767989","MLC458767989");
		map.put("MLC458730140","MLC458730140");
		map.put("MLC458767970","MLC458767970");
		map.put("MLC458767066","MLC458767066");
		map.put("MLC458746470","MLC458746470");
		map.put("MLC458766756","MLC458766756");
		map.put("MLC458762376","MLC458762376");
		map.put("MLC458766939","MLC458766939");
		map.put("MLC458766913","MLC458766913");
		map.put("MLC458762360","MLC458762360");
		map.put("MLC458716841","MLC458716841");
		map.put("MLC458769470","MLC458769470");
		map.put("MLC458750751","MLC458750751");
		map.put("MLC458768353","MLC458768353");
		map.put("MLC458766911","MLC458766911");
		map.put("MLC458745145","MLC458745145");
		map.put("MLC458765515","MLC458765515");
		map.put("MLC458713886","MLC458713886");
		map.put("MLC458745397","MLC458745397");
		map.put("MLC458766923","MLC458766923");
		map.put("MLC458705844","MLC458705844");
		map.put("MLC458770093","MLC458770093");
		map.put("MLC458767961","MLC458767961");
		map.put("MLC458796874","MLC458796874");
		map.put("MLC458777356","MLC458777356");
		map.put("MLC458795540","MLC458795540");
		map.put("MLC458778265","MLC458778265");
		map.put("MLC458778027","MLC458778027");
		map.put("MLC458010189","MLC458010189");
		map.put("MLC458769591","MLC458769591");
		map.put("MLC458787669","MLC458787669");
		map.put("MLC458793532","MLC458793532");
		map.put("MLC458790277","MLC458790277");
		map.put("MLC458790871","MLC458790871");
		map.put("MLC458777498","MLC458777498");
		map.put("MLC458776937","MLC458776937");
		map.put("MLC458793359","MLC458793359");
		map.put("MLC458775081","MLC458775081");
		map.put("MLC458777776","MLC458777776");
		map.put("MLC458791766","MLC458791766");
		map.put("MLC458790782","MLC458790782");
		map.put("MLC458795403","MLC458795403");
		map.put("MLC458779715","MLC458779715");
		map.put("MLC458795607","MLC458795607");
		map.put("MLC458587164","MLC458587164");
		map.put("MLC457935925","MLC457935925");
		map.put("MLC458793312","MLC458793312");
		map.put("MLC458791900","MLC458791900");
		map.put("MLC458775527","MLC458775527");
		map.put("MLC458791268","MLC458791268");
		map.put("MLC458776788","MLC458776788");
		map.put("MLC458790307","MLC458790307");
		map.put("MLC458787541","MLC458787541");
		map.put("MLC458790069","MLC458790069");
		map.put("MLC458787695","MLC458787695");
		map.put("MLC458790482","MLC458790482");
		map.put("MLC458790371","MLC458790371");
		map.put("MLC458793986","MLC458793986");
		map.put("MLC458796121","MLC458796121");
		map.put("MLC458778047","MLC458778047");
		map.put("MLC458793562","MLC458793562");
		map.put("MLC458796864","MLC458796864");
		map.put("MLC458790662","MLC458790662");
		map.put("MLC458790819","MLC458790819");
		map.put("MLC458776738","MLC458776738");
		map.put("MLC458791812","MLC458791812");
		map.put("MLC458778850","MLC458778850");
		map.put("MLC458795380","MLC458795380");
		map.put("MLC458774952","MLC458774952");
		map.put("MLC458775242","MLC458775242");
		map.put("MLC458790021","MLC458790021");
		map.put("MLC458793759","MLC458793759");
		map.put("MLC458778233","MLC458778233");
		map.put("MLC458793749","MLC458793749");
		map.put("MLC458775007","MLC458775007");
		map.put("MLC458792514","MLC458792514");
		map.put("MLC458791415","MLC458791415");
		map.put("MLC458776572","MLC458776572");
		map.put("MLC458791517","MLC458791517");
		map.put("MLC458777225","MLC458777225");
		map.put("MLC458795685","MLC458795685");
		map.put("MLC458795047","MLC458795047");
		map.put("MLC458778310","MLC458778310");
		map.put("MLC458795375","MLC458795375");
		map.put("MLC458777117","MLC458777117");
		map.put("MLC458792528","MLC458792528");
		map.put("MLC458488994","MLC458488994");
		map.put("MLC458775607","MLC458775607");
		map.put("MLC458782447","MLC458782447");
		map.put("MLC458775475","MLC458775475");
		map.put("MLC458790539","MLC458790539");
		map.put("MLC458792601","MLC458792601");
		map.put("MLC458793545","MLC458793545");
		map.put("MLC458793396","MLC458793396");
		map.put("MLC458792344","MLC458792344");
		map.put("MLC458777478","MLC458777478");
		map.put("MLC458784981","MLC458784981");
		map.put("MLC458774170","MLC458774170");
		map.put("MLC458791842","MLC458791842");
		map.put("MLC458796198","MLC458796198");
		map.put("MLC458796544","MLC458796544");
		map.put("MLC458790404","MLC458790404");
		map.put("MLC458793404","MLC458793404");
		map.put("MLC458794201","MLC458794201");
		map.put("MLC458774715","MLC458774715");
		map.put("MLC458790936","MLC458790936");
		map.put("MLC458786680","MLC458786680");
		map.put("MLC458774543","MLC458774543");
		map.put("MLC458792020","MLC458792020");
		map.put("MLC458776953","MLC458776953");
		map.put("MLC458177982","MLC458177982");
		map.put("MLC458790795","MLC458790795");
		map.put("MLC458194717","MLC458194717");
		map.put("MLC458791076","MLC458791076");
		map.put("MLC458790461","MLC458790461");
		map.put("MLC458774464","MLC458774464");
		map.put("MLC458776657","MLC458776657");
		map.put("MLC458787845","MLC458787845");
		map.put("MLC458787515","MLC458787515");
		map.put("MLC458793674","MLC458793674");
		map.put("MLC458777167","MLC458777167");
		map.put("MLC458795009","MLC458795009");
		map.put("MLC458776232","MLC458776232");
		map.put("MLC458778062","MLC458778062");
		map.put("MLC458792066","MLC458792066");
		map.put("MLC458774389","MLC458774389");
		map.put("MLC458777129","MLC458777129");
		map.put("MLC458777207","MLC458777207");
		map.put("MLC458787520","MLC458787520");
		map.put("MLC458792157","MLC458792157");
		map.put("MLC458782844","MLC458782844");
		map.put("MLC458793924","MLC458793924");
		map.put("MLC458778282","MLC458778282");
		map.put("MLC458790826","MLC458790826");
		map.put("MLC458775379","MLC458775379");
		map.put("MLC458793657","MLC458793657");
		map.put("MLC458777695","MLC458777695");
		map.put("MLC458776206","MLC458776206");
		map.put("MLC458779543","MLC458779543");
		map.put("MLC458792658","MLC458792658");
		map.put("MLC458790105","MLC458790105");
		map.put("MLC458774705","MLC458774705");
		map.put("MLC458792138","MLC458792138");
		map.put("MLC458781691","MLC458781691");
		map.put("MLC458776779","MLC458776779");
		map.put("MLC458775355","MLC458775355");
		map.put("MLC458775967","MLC458775967");
		map.put("MLC458022250","MLC458022250");
		map.put("MLC458776172","MLC458776172");
		map.put("MLC458778615","MLC458778615");
		map.put("MLC458780709","MLC458780709");
		map.put("MLC458791043","MLC458791043");
		map.put("MLC458795007","MLC458795007");
		map.put("MLC458790498","MLC458790498");
		map.put("MLC458791212","MLC458791212");
		map.put("MLC458792947","MLC458792947");
		map.put("MLC458777576","MLC458777576");
		map.put("MLC458777334","MLC458777334");
		map.put("MLC458791481","MLC458791481");
		map.put("MLC458795851","MLC458795851");
		map.put("MLC458776126","MLC458776126");
		return map;
	}
}
