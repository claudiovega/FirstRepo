package cl.propiedades.factory;

import org.jsoup.nodes.Document;

public interface Portal {
	void run() throws Exception;
	void setPath(String path);
	void setProcesoCasa(boolean procesoCasa);
	String getTotalAvisos(Document doc);
	void setPrecioCasaHasta(String precio);
	String getPrecioCasaHasta();
	void setPrecioDeptoHasta(String precio);
	String getPrecioDeptoHasta();
	void setFechaCasaHasta(String precio);
	String getFechaCasaHasta();
	void setFechaDeptoHasta(String precio);
	String getFechaDeptoHasta();
	
	void setPrecioCasaDesde(String precio);
	String getPrecioCasaDesde();
	void setPrecioDeptoDesde(String precio);
	String getPrecioDeptoDesde();
	void setRegion(String region);
	void setTipoTransaccion(String tipoTransaccion);
	void setTipoPropiedad(String tipoPropiedad);
	void setFechaDesde(String fechaDesde);
	
}
