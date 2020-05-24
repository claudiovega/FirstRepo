package cl.propiedades.factory;

import cl.propiedades.util.EmolUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class EmolPropiedadesScrapingExtractor implements ScrapExtractor {
    @Override
    public Integer getNumeroPaginas(Document doc) {
        // #content > div.controls-panel > div.paginator.top > ul > li:nth-child(13)
        Integer result = null;
        String numPags = doc.select("#content > div.controls-panel > div.paginator.top").text();
        // 10 9 8 7 6 5 4 3 2 1 Página 1 de 36 |
        numPags = numPags.replace("|","");
        String[] arr = numPags.split("Página 1 de");
        String  num = arr[arr.length - 1].trim();
        result = Integer.parseInt(num.trim());
        System.out.println();

        return result;
    }

    @Override
    public String getTotalAvisos(Document doc) {
        return null;
    }

    @Override
    public String getCodigoPublicacion(Object obj) {
        String strUrl = (String)obj;

        return EmolUtils.getCodigoEmolFromUrl(strUrl);
    }

    @Override
    public String getCodigoPublicacionA() {
        return null;
    }

    @Override
    public String getTituloPublicacion(Object obj) {
        String result = "";
        String[] arrElement = (String[])obj;

        for (int i = 0; i< arrElement.length; i++){
            if (arrElement[i].contains("class=\"description")){
                String str = arrElement[i].replace("</a>", "");
                String[] subArr  = str.split("target=\"_blank\">");
                str = subArr[0].replace("\"", "");
                result = str.trim();
                break;
            }

        }
        return result;
    }

    @Override
    public String getUrl(Object obj) {
        String result = "";
        String[] arrElement = (String[])obj;

        for (int i = 0; i< arrElement.length; i++){
            if (arrElement[i].contains("thumbnail") && arrElement[i].contains("a href=")){
                String str = arrElement[i].replace("<div class=\"thumbnail\"> <a href=\"", "");
                String[] subArr  = str.split("target=\"_blank\"");
                str = subArr[0].replace("\"", "");
                result = "http://www.propiedades.emol.com"+str.trim();
                break;
            }

        }
        return result;
    }

    @Override
    public String getFechaPublicacion(Object obj) {
        String result = "";
        String[] arrElement = (String[])obj;

        for (int i = 0; i< arrElement.length; i++){
            if (arrElement[i].contains("Publicado")){
                String str = arrElement[i].replace("Publicado", "");
                str = str.replace("el", "");
                str = str.replace("\"", "");
                result = str.trim();
                break;
            }

        }
        return result;
    }

    @Override
    public String getNombreComuna(Object obj) {
        return null;
    }

    @Override
    public String getNombreRegion(Object obj) {
        return null;
    }

    @Override
    public String getTipoMoneda(Element element) {
        return null;
    }

    @Override
    public String getPrecioPesos(Object obj, Double valorUf) {
        return null;
    }

    @Override
    public String getPrecioUF(Object obj, Double valorUf) {
        return null;
    }

    @Override
    public String getDormitorios(Object obj) {
        return null;
    }

    @Override
    public String getToilet(Object obj) {
        return null;
    }


    @Override
    public String getConstruidos(Object obj) {
        String result = "";
        String[] arrElement = (String[])obj;

        for (int i = 0; i< arrElement.length; i++){
            if (arrElement[i].contains("const.")){
                String str = arrElement[i].replace("</a>", "");
                String[] subArr1 = str.split("target=\"_blank\">");
                String subStr1 = subArr1[subArr1.length-1].replace("\"", "");
                subStr1 = subStr1.replace("</a>", "");
                String[] subArr2 = subStr1.split("/");
                for(int x = 0; x< subArr2.length; x++){
                    if (subArr2[x].contains("const.")){
                        String subStr2 = subArr2[x].replace("m<sup>2</sup>", "");
                        subStr2 = subStr2.replace("const.", "");
                        result = subStr2.trim();
                        break;
                    }
                }


            }

        }
        return result;
    }

    @Override
    public String getTerreno(Object obj) {
        String result = "";
        String[] arrElement = (String[])obj;

        for (int i = 0; i< arrElement.length; i++){
            if (arrElement[i].contains("terr.")){
                String str = arrElement[i].replace("</a>", "");
                String[] subArr1 = str.split("target=\"_blank\">");
                String subStr1 = subArr1[subArr1.length-1].replace("\"", "");
                subStr1 = subStr1.replace("</a>", "");
                String[] subArr2 = subStr1.split("/");
                for(int x = 0; x< subArr2.length; x++){
                    if (subArr2[x].contains("const.")){
                        String subStr2 = subArr2[x].replace("m<sup>2</sup>", "");
                        subStr2 = subStr2.replace("terr.", "");
                        result = subStr2.trim();
                        break;
                    }
                }


            }

        }
        return result;
    }
}
