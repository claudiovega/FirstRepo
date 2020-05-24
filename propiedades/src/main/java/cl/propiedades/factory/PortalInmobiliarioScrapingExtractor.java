package cl.propiedades.factory;

import cl.propiedades.util.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
@Service
public class PortalInmobiliarioScrapingExtractor implements ScrapExtractor {
    @Override
    public Integer getNumeroPaginas(Document doc) {
        Integer maxPaginas = null;
        String num = this.getTotalAvisos(doc);
        if (num != null && !num.trim().isEmpty()) {
            Integer avisos = Integer.parseInt(num.trim());
            maxPaginas = avisos/50;
            if (maxPaginas > 40){
                maxPaginas = 40;
            }

        }
        return maxPaginas;
    }

    @Override
    public String getTotalAvisos(Document doc) {
        //#inner-main > aside > div.quantity-results
        String result = "";
        try {
            result = doc.select("#inner-main > aside > div.quantity-results").text();
            result = result.replace(".","");
            result = result.replace("resultados","");

        } catch (Exception ignored) {
            // TODO Auto-generated catch block

        }
        return result.trim();

    }

    @Override
    public String getCodigoPublicacion(Object obj) {
        Element element = (Element)obj;
        String result = element.select("#bookmarkForm > input[type=hidden]:nth-child(3)").toString();
        if (result  != null){
            result = result.replace("<input type=\"hidden\" name=\"itemId\" value=\"","");
            result = result.replace("\">","");
            result = result.trim();
        }
        result = result.replace("|", "");
        return result;
    }

    public String getCodigoPublicacionA() {
        return "10ssrrrdsdsd";
    }
    @Override
    public String getTituloPublicacion(Object obj) {
        Element element = (Element)obj;
        // #MLC524420256 > div.item__info-container > div > div > a.item__info-title-link > span
        String result = element.toString();


        String[] arr = result.split("\n");
        result = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("main-title nowrap")){
                String strVal = arr[i].replace("\"", "");
                strVal = strVal.replace("</span></a>", "");
                strVal = strVal.replace(">", "");
               String[] subArr = strVal.split("main-title nowrap");
                result = subArr[subArr.length-1].trim();
                if (result.contains("Ley Pereira")){
                    System.out.println();
                }
                result = result.replace("\\|", "");
                result = result.replace("|", "");
                break;
            }
        }
        result = result.replace("|", "");
        return result;
    }

    @Override
    public String getUrl(Object obj) {
        Element element = (Element)obj;
        String result = element.toString();
        String[] arr = result.split("\n");
        result = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("item-url")){
                result = (arr[i].replace("<div class=\"images-viewer\" item-url=\"", ""));
                String[] subArr = result.split("item-id");
                result = subArr[0];
                result = result.replace("\"", "").trim();
                break;
            }
        }

        return result;
    }

    @Override
    public String getFechaPublicacion(Object obj) {
        Elements elements = (Elements) obj;

        String result = "";
        try {
            //#root-app > div > div.layout-main.u-clearfix > div.layout-col.layout-col--right > section.ui-view-more.vip-section-seller-info > div > div.official-store-info.info-property-date > p.info
            for (Element el : elements){

                result = el.toString();
                result = result.replace("<p class=\"info\">","");
                result = result.replace("</p>","");
                break;
            }
        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public String getNombreComuna(Object obj) {
        Element element = (Element) obj;
        String result = element.toString();
        String[] arr = result.split("\n");
        result = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("main-title nowrap")){
                result = (arr[i].replace("\"", ""));
                result = result.replace("</span></a>","");
                result = result.replace(">","");
                String[] subArr = result.split("main-title nowrap");
                result = subArr[1];
                String[] subArr2 = result.split(",");
                result = subArr2[subArr2.length-1].trim();

                break;
            }
        }
        result = result.replace("|", "");
        return result;
    }
    @Override
    public String getNombreRegion(Object obj) {
        Elements elements = (Elements) obj;

        String result = "";
        try {
            //#root-app > div > div.layout-main.u-clearfix > div.layout-col.layout-col--right > section.ui-view-more.vip-section-seller-info > div > div.official-store-info.info-property-date > p.info
            for (Element el : elements){

                String tmp = el.toString();
                tmp = tmp.replace("<h3 class=\"map-location\">","");
                tmp = tmp.replace("</h3>","");
                String[] arr = tmp.split(",");
                result = arr[arr.length-1];
                if (result.contains(" Rm (metropolitana)")){
                    result = "Region Metropolitana";
                }
                break;
            }
        } catch (Exception e) {

        }
        result = result.replace("|", "");
        return result;
    }
    @Override
    public String getTipoMoneda(Element element) {
        return null;
    }

    @Override
    public String getPrecioPesos(Object obj, Double valorUf ) {
        Element element = (Element)obj;
        String result = element.toString();
        String[] arr = result.split("\n");
        result = "";
        String priceSymbol = "";
        String precio = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("price__symbol")){

                priceSymbol = arr[i].replace("<div class=\"item__price \"> <span class=\"price__symbol\">","");
                String[] arrsub = priceSymbol.split("</span>");
                priceSymbol = arrsub[0].trim();
                break;
            }
        }
        //<span class="price__fraction">900.000</span>
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("price__fraction")){

                precio = arr[i].replace("<div class=\"item__price \"> <span class=\"price__symbol\">","");
                precio = precio.replace("</span>","").trim();
                String[] subArr = precio.split("<span class=\"price__fraction\">");
                precio = subArr[subArr.length-1];
                break;
            }else if (arr[i].contains("price__clf-full")){
                precio = arr[i].replace("<div class=\"item__price \"> <span class=\"price__symbol\">","");
                precio = precio.replace("</span>","").trim();
                String[] subArr = precio.split("<span class=\"price__clf-full\">");
                precio = subArr[subArr.length-1];
                break;
            }
        }
        if (!precio.trim().isEmpty()) {
            if (priceSymbol.equals("$")){
                result = precio.replace(".","");
            }else{
                precio = precio.replace(".","");
                precio = precio.replace(",",".");
                Double precioDouble = Double.parseDouble(precio.trim());
                Double valUf = precioDouble*valorUf;
                result = valUf.toString();
            }
        }else{
            System.out.println(element.toString());
        }
        return result;
    }

    @Override
    public String getPrecioUF(Object obj, Double valorUf ) {
        Element element = (Element)obj;
        String result = element.toString();
        String[] arr = result.split("\n");
        result = "";

        String priceSymbol = "";
        String precio = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("price__symbol")){

                priceSymbol = arr[i].replace("<div class=\"item__price \"> <span class=\"price__symbol\">","");
                String[] arrsub = priceSymbol.split("</span>");
                priceSymbol = arrsub[0].trim();
                break;
            }
        }
        //<span class="price__fraction">900.000</span>
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("price__fraction")){

                precio = arr[i].replace("<div class=\"item__price \"> <span class=\"price__symbol\">","");
                precio = precio.replace("</span>","").trim();
                String[] subArr = precio.split("<span class=\"price__fraction\">");
                precio = subArr[subArr.length-1];
                break;
            }else if (arr[i].contains("price__clf-full")){
                precio = arr[i].replace("<div class=\"item__price \"> <span class=\"price__symbol\">","");
                precio = precio.replace("</span>","").trim();
                String[] subArr = precio.split("<span class=\"price__clf-full\">");
                precio = subArr[subArr.length-1];
                break;
            }

        }
        if (!precio.trim().isEmpty()) {
            if (!priceSymbol.equals("$")){
                precio = precio.replace(".","");
                result = precio;
            }else{
                precio = precio.replace(".","");
                precio = precio.replace(",",".");
                Double precioDouble = Double.parseDouble(precio.trim());
                Double valUf = precioDouble/valorUf;
                result = NumberUtils.convertPesosAUf(valorUf, precio);

            }
        } else {
            System.out.println(element.toString());
        }
        return result;
    }

    @Override
    public String getDormitorios(Object obj) {
        Element element = (Element)obj;
        String result = element.toString();
        String[] arr = result.split("\n");

        result = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("dormitorio") && arr[i].contains("|")){
                //280 m² útiles | 4 dormitorios | 5 baños
                String[] subArr =  (arr[i].split("\\|"));
                for(int x = 0; x<subArr.length;x++){
                    if (subArr[x].contains("dormitorio")){
                        result =subArr[x].replace("dormitorio","");
                        result =result.replace("s","").trim();
                        break;
                    }
                }


            }
        }

        return result;
    }

    @Override
    public String getToilet(Object obj) {
        Element element = (Element)obj;
        String result = element.toString();
        String[] arr = result.split("\n");
        result = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("baño") && arr[i].contains("|")){
                //280 m² útiles | 4 dormitorios | 5 baños
                String[] subArr =  (arr[i].split("\\|"));
                for(int x = 0; x<subArr.length;x++){
                    if (subArr[x].contains("baño")){
                        result =subArr[x].replace("baño","");
                        result =result.replace("s","").trim();
                        break;
                    }
                }


            }
        }

        return result;
    }

    @Override
    public String getConstruidos(Object obj) {
        Element element = (Element)obj;
        String result = element.toString();
        String[] arr = result.split("\n");
        result = "";
        for(int i = 0; i<arr.length;i++){
            if (arr[i].contains("útiles")){
                //280 m² útiles | 4 dormitorios | 5 baños
                String[] subArr =  (arr[i].split("\\|"));
                for(int x = 0; x<subArr.length;x++){
                    if (subArr[x].contains("útiles")){
                        result =subArr[x].replace("útiles","");
                        result =result.replace("m²","").trim();
                        break;
                    }
                }


            }
        }

        return result;
    }

    @Override
    public String getTerreno(Object obj) {
        return null;
    }
}
