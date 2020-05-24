package cl.propiedades.factory;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public interface ScrapExtractor {

    Integer getNumeroPaginas(Document doc);
    String getTotalAvisos(Document doc);
    String getCodigoPublicacion(Object obj );
    String getCodigoPublicacionA();
    String getTituloPublicacion(Object obj );
    String getUrl(Object obj );
    String getFechaPublicacion(Object obj);


    String getNombreComuna(Object obj );
    String getNombreRegion(Object obj );
    String getTipoMoneda(Element element );
    String getPrecioPesos(Object obj, Double valorUf );
    String getPrecioUF(Object obj, Double valorUf);
    String getDormitorios(Object obj );
    String getToilet(Object obj );
    String getConstruidos(Object obj );
    String getTerreno(Object obj );
}
