package cl.propiedades.factory;

import cl.propiedades.enums.MesesYapoEnum;
import cl.propiedades.util.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class YapoScrapingExtractor implements ScrapExtractor{
    private final ZoneId zoneId = java.time.ZoneId.of("Chile/Continental");
    private Integer lastDayGetted = null;
    private Integer lastMonthGetted = null;
    private Integer lastYearGetted = null;


    @Override
    public Integer getNumeroPaginas(Document doc) {
        Integer result = 1;
        String strNumeroPaginas = doc.select("#listing-top > table > tbody > tr:nth-child(4) > td > div > span.nohistory.FloatRight > a").toString();
        strNumeroPaginas = strNumeroPaginas.replace("<a", "");
        strNumeroPaginas = strNumeroPaginas.replace("\"", "");
        strNumeroPaginas = strNumeroPaginas.replace("href=", "");
        strNumeroPaginas = strNumeroPaginas.replace("</a>", "");
        strNumeroPaginas = strNumeroPaginas.replace("Última página", "");
        strNumeroPaginas = strNumeroPaginas.replace(">", "");
        String[] arrTmp = strNumeroPaginas.split(";");
        strNumeroPaginas = arrTmp[arrTmp.length-1];
        strNumeroPaginas = strNumeroPaginas.replace("o=", "").trim();
        result = Integer.parseInt(strNumeroPaginas.trim());

        return result;
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
    public String getCodigoPublicacion(Object obj) {
        Node node = (Node)obj;
        String result = "";
        String url = this.getUrl(obj);
        String[] arr = url.split(";oa=");
        String strTmp = arr[arr.length-1];

        result = strTmp.replace("&amp;","");
        for(int i = 0; i<101;i++){
            result = result.replace("xsp="+i,"");
        }
        result = result.replace("|", "");
        return result;
    }

    @Override
    public String getCodigoPublicacionA() {
        return null;
    }

    @Override
    public String getTituloPublicacion(Object obj) {
        Node node = (Node)obj;
        String result = "";
        boolean exit = false;
        for(int i = 0; i< node.childNodeSize(); i++){
            Node childNode = node.childNode(i);

            for(int x = 0; x<childNode.childNodeSize(); x++){
                Node chidChilNode = childNode.childNode(x);
                if (!chidChilNode.toString().trim().isEmpty()){
                    if (chidChilNode.toString().contains("class=\"title\"")){
                        String strTmp = chidChilNode.toString().replace("</a>","");
                        String[] arr = strTmp.split("class=\"title\">");
                        result = arr[arr.length-1].trim();
                        result = result.replace("|", "");
                        exit = true;
                        break;
                    }
                }
            }
            if(exit){
                break;
            }
        }
        return result;
    }

    @Override
    public String getUrl(Object obj) {
        Node node = (Node)obj;
        String result = "";
        boolean exit = false;
        for(int i = 0; i< node.childNodeSize(); i++){
            Node childNode = node.childNode(i);

            for(int x = 0; x<childNode.childNodeSize(); x++){
                Node chidChilNode = childNode.childNode(x);
                if (!chidChilNode.toString().trim().isEmpty()){
                    if (chidChilNode.toString().contains("<a href=") && chidChilNode.toString().contains("class=\"redirect-to-url\"></a>")){
                        String strTmp = chidChilNode.toString().replace("<a href=\"","");
                        result = strTmp.replace("\" class=\"redirect-to-url\"></a>", "");
                        exit = true;
                        break;
                    }
                }
            }
            if(exit){
                break;
            }
        }
        return result;
    }

    @Override
    public String getFechaPublicacion(Object obj) {
        String result = "";
        Node node = (Node)obj;
        boolean exit = false;
        for(int i = 0; i< node.childNodeSize(); i++){
            Node childNode = node.childNode(i);
            for(int x = 0; x<childNode.childNodeSize(); x++){
                Node chidChilNode = childNode.childNode(x);
                if (!chidChilNode.toString().trim().isEmpty()){

                    if (chidChilNode.toString().contains("class=\"date\"")){
                        LocalDate localDate = LocalDate.now(zoneId);
                        if (chidChilNode.toString().contains("Hoy")){

                            result = localDate.toString();
                            lastDayGetted = localDate.getDayOfMonth();
                            lastYearGetted = localDate.getYear();
                            lastMonthGetted = localDate.getMonthValue();
                            exit = true;
                            break;
                        }else if (chidChilNode.toString().contains("Ayer")){
                            LocalDate yesterday = localDate.minusDays(1L);
                            result = yesterday.toString();
                            lastDayGetted = yesterday.getDayOfMonth();
                            lastYearGetted = yesterday.getYear();
                            lastMonthGetted = yesterday.getMonthValue();
                            exit = true;
                            break;
                        }else{
                        //<span class="date">2 Ago</span>
                            String strTmpDate = chidChilNode.toString().replace("<span class=\"date\">", "");
                            strTmpDate = strTmpDate.replace("</span>","");
                            String[] arr = strTmpDate.split(" ");
                            Integer day = Integer.parseInt(arr[0]);
                            Integer month = MesesYapoEnum.getNumeroMes(arr[1]);


                            if (lastMonthGetted == MesesYapoEnum.ENERO.getNumMes() && month == MesesYapoEnum.DICIEMBRE.getNumMes()){
                                lastYearGetted = lastYearGetted-1;
                            }
                            result = lastYearGetted.toString()+"-";
                            String mes = "";
                            String dia = "";
                            if (month<10){
                                mes = "0"+month;
                            }else{
                                mes = month.toString();
                            }
                            if(day < 10){
                                dia = "0"+day;
                            }else{
                                dia = day.toString();
                            }
                            result = result+mes+"-"+dia;

                            lastDayGetted = day;
                            lastMonthGetted = month;
                            exit = true;
                            break;
                        }

                    }
                }

            }
            if(exit){
                break;
            }
        }
        return result;
//Ene
//Feb
//Mar
//Apr
//May
//Jun
//Jul
//Ago
//Sep
//Oct
//Nov
//Dic
    }

    @Override
    public String getNombreComuna(Object obj) {
        Node node = (Node)obj;
        String result = "";
        boolean exit = false;
        String[] arr = node.toString().split("\n");
        for(int i = 0; i< arr.length; i++){
            if (arr[i].contains("class=\"commune\"")){
                String[] subArr = arr[i].split("<span class=\"commune\">");
                String tmpName = subArr[1];
                Integer posicion = 0;
                for(Integer x = 0; x<tmpName.length(); x++){
                    if (tmpName.substring(x,x+1).equals("<")){
                        posicion = x;
                        break;
                    }

                }
                result = tmpName.substring(0,posicion);
                exit = true;
                break;
            }
            if(exit){
                break;
            }
        }
        result = result.replace("|", "");
        return result;
    }

    @Override
    public String getNombreRegion(Object obj) {
        Node node = (Node)obj;
        String result = "";
        String[] arr = node.toString().split("\n");
        for(int i = 0; i< arr.length; i++){
            if (arr[i].contains("class=\"region\"")){
                String str = arr[i].replace("<td class=\"clean_links\">", "");
                str = str.replace("<p class=\"line_cc\">","");
                str = str.replace("<span class=\"region\">","");
                str = str.replace("</span></p>","");

                String[] subArr = str.split("<span class=\"commune\">");
                result = subArr[0];
                result = result.replace("  <i class=\"icon-yapo icon-location\" title=\"Este aviso tiene mapa\"></i>", "").trim();
                break;
            }
        }
        result = result.replace("|", "");
        return result;
    }

    @Override
    public String getTipoMoneda(Element element) {

        return null;
    }

    @Override
    public String getPrecioPesos(Object obj, Double valorUf) {
        Node node = (Node)obj;
        String result = "";
        String price1 = "";
        String price2 = "";
        boolean exit = false;
        for(int i = 0; i< node.childNodeSize(); i++){
            Node childNode = node.childNode(i);

            for(int x = 0; x<childNode.childNodeSize(); x++){
                Node chidChilNode = childNode.childNode(x);
                if (!chidChilNode.toString().trim().isEmpty()){

                    if (chidChilNode.toString().contains("<span class=\"convertedPrice\">")){
                        String strTmp = chidChilNode.toString();
                        String[] arr = strTmp.split("<span class=\"convertedPrice\">");
                        String[] subArr = arr[arr.length-1].split("</span>");
                        String strTmpPrecio = subArr[0];
                        price1 = strTmpPrecio.replace("(", "").trim();
                        price1 = price1.replace(")", "");

                        price1 = price1.replace(".", "");
                        price1 = price1.replace(",", ".").trim();
                        break;


                    }else if (chidChilNode.toString().contains("<span class=\"price\">")){
                        String strTmp = chidChilNode.toString();
                        String[] arr = strTmp.split("<span class=\"price\">");
                        String[] subArr = arr[arr.length-1].split("<span class=\"convertedPrice\">");
                        String strTmpPrecio = subArr[0];
                        price2 = strTmpPrecio.replace("</span>", "").trim();
                        price2 = price2.replace(".", "");
                        break;

                    }
                }
            }

        }
        if (price1.contains("$")){
            result = price1.replace("$", "").trim();
        }else{
            if (price2.contains("UF")){
                result = NumberUtils.convertUfAPesos(valorUf, price2);
            }else if (price2.contains("$")){
                result = price2.replace("$", "").trim();
            }

        }
        return result;
    }

    @Override
    public String getPrecioUF(Object obj, Double valorUf) {
        Node node = (Node)obj;
        String result = "";
        String price1 = "";
        String price2 = "";
        boolean exit = false;
        for(int i = 0; i< node.childNodeSize(); i++){
            Node childNode = node.childNode(i);

            for(int x = 0; x<childNode.childNodeSize(); x++){
                Node chidChilNode = childNode.childNode(x);
                if (!chidChilNode.toString().trim().isEmpty()){

                    if (chidChilNode.toString().contains("<span class=\"convertedPrice\">")){
                        String strTmp = chidChilNode.toString();
                        String[] arr = strTmp.split("<span class=\"convertedPrice\">");
                        String[] subArr = arr[arr.length-1].split("</span>");
                        String strTmpPrecio = subArr[0];
                        price1 = strTmpPrecio.replace("(", "").trim();
                        price1 = price1.replace(")", "");

                        price1 = price1.replace(".", "");
                        price1 = price1.replace(",", ".").trim();
                        break;


                    }else if (chidChilNode.toString().contains("<span class=\"price\">")){
                        String strTmp = chidChilNode.toString();
                        String[] arr = strTmp.split("<span class=\"price\">");
                        String[] subArr = arr[arr.length-1].split("<span class=\"convertedPrice\">");
                        String strTmpPrecio = subArr[0];
                        price2 = strTmpPrecio.replace("</span>", "").trim();
                        price2 = price2.replace(".", "");
                        break;
                    }
                }
            }

        }

        if (price1.contains("UF")){
            result = price1.replace("UF", "").trim();
        }else{
            if (price2.contains("UF")){
                result = price2.replace("UF", "").trim();
                result = result.replace(",",".");
            }else if (price2.contains("$")){
                result = NumberUtils.convertPesosAUf(valorUf, price2);
            }

        }
        return result;

    }

    @Override
    public String getDormitorios(Object obj) {
        Node node = (Node)obj;
        String result = "";
        String[] arr = node.toString().split("\n");
        for(int i = 0; i< arr.length; i++){
            if (arr[i].contains("class=\"fal fa-bed icons__element-icon\">")){
                String str = arr[i].replace("<div class=\"icons__element\"> <i class=\"fal fa-bed icons__element-icon\"></i>", "");
                str = str.replace("<span class=\"icons__element-text\">","");
                result = str.replace("</span>","").trim();
                result = result.replace("+", "");

                break;
            }
        }

        return result;
    }

    @Override
    public String getToilet(Object obj) {
        Node node = (Node)obj;
        String result = "";
        String[] arr = node.toString().split("\n");
        for(int i = 0; i< arr.length; i++){
            if (arr[i].contains("<i class=\"fal fa-bath icons__element-icon\">")){
                String str = arr[i].replace("<div class=\"icons__element\"> <i class=\"fal fa-bath icons__element-icon\"></i>", "");
                str = str.replace("<span class=\"icons__element-text\">","");
                result = str.replace("</span>","").trim();
                result = result.replace("+", "");
                break;
            }
        }

        return result;
    }


    @Override
    public String getConstruidos(Object obj) {
        Node node = (Node)obj;
        String result = "";
        String[] arr = node.toString().split("\n");
        for(int i = 0; i< arr.length; i++){
            if (arr[i].contains("<i class=\"fal fa-expand icons__element-icon\">")){
                String str = arr[i].replace("<div class=\"icons__element\"> <i class=\"fal fa-expand icons__element-icon\"></i>", "");
                str = str.replace("<span class=\"icons__element-text\">","");
                String[] arrTmp = str.split("m<sup class=\"icons__element-text-sup\">");
                result = arrTmp[0].trim();
                result = result.replace("</span>","").trim();
                result = result.replace("ha","").trim();
                result = result.replace("<span class=\"separator\">","").trim();
                result = result.replace("/","").trim();
                break;
            }
        }

        return result;
    }

    @Override
    public String getTerreno(Object obj) {
        Node node = (Node)obj;
        String result = "";
        String[] arr = node.toString().split("\n");
        for(int i = 0; i< arr.length; i++){
            if (arr[i].contains("<i class=\"fal fa-expand icons__element-icon\">")){
                String str = arr[i].replace("<div class=\"icons__element\"> <i class=\"fal fa-expand icons__element-icon\"></i>", "");
                str = str.replace("<span class=\"icons__element-text\">","");
                str = str.replace("m<sup class=\"icons__element-text-sup\">2</sup></span>","");
                String[] arrTmp = str.split("<span class=\"separator\">");
                result = arrTmp[arrTmp.length-1].trim();
                result = result.replace("/","");
                result = result.replace("<span>","").trim();
                result = result.replace("</span>","").trim();
                result = result.replace("ha","").trim();
                result = result.replace("<span class=\"separator\">","").trim();

                break;
            }
        }

        return result;
    }
}
