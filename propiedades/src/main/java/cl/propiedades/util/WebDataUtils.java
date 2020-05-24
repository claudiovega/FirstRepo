package cl.propiedades.util;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.IOException;

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
public class WebDataUtils {
    private static final String userAgent = "Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0";
    private static final int timeout = 60 * 1000;

    public static String getUfToday(){
    	String result = "";
    	String url ="https://valoruf.cl/";
    	try {
			//Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
    		org.jsoup.Connection.Response res = Jsoup.connect(url).timeout(60000).execute();
    		 Document doc = res.parse();
    		 //<span class="vpr">$ 28.656,37</span>
			String selector = ".vpr";
            Elements elements = doc.select(selector); // get each element that matches the CSS selector
            // $27.770,60
            for (Element element : elements) {
            	String strTmp = Jsoup.parse(element.html()).text() ;
            	strTmp = strTmp.replace("\"","");
            	result = strTmp;
            	result = result.replace("<span class=valor_principal>", "");
            	result = result.replace("</span>", "");
            	result = result.trim();
            	result = result.replace("$", "");
            	result = result.replace(".", "");
            	result = result.replace(",", ".");
            	break;
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    public static String getDolarToday(){
    	String result = "";
    	String url ="https://si3.bcentral.cl/bdemovil/BDE/IndicadoresDiarios";
    	try {
			Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
			String selector = "#main > div > div > div > div:nth-child(2) > table:nth-child(4) > tbody > tr > td.col-xs-2.text-center > p";
            Elements elements = doc.select(selector); // get each element that matches the CSS selector
            //<h3>1 UF = $ 26.789,50 Pesos Chilenos</h3>
            for (Element element : elements) {
            	String strTmp = Jsoup.parse(element.html()).text() ;

            	result = strTmp;
            	result = result.replace(".", "");
            	result = result.replace(",", ".");
            	break;
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    private static String getFechaPublicacion (String strUrl){
    	String result = "";
    	try {
    		//#wrapper > section > div > div > div.col-sm-9.span-fix-content > article > div > div.propiedad-ficha.clearfix > div.content-left-col > div.propiedad-ficha-mini > div.content-panel.small-content-panel > p:nth-child(2)
			Document doc = Jsoup.connect(strUrl).userAgent(userAgent).timeout(timeout).get();
			
			String selector = "#wrapper > section > div > div > div.col-sm-9.span-fix-content > article > div > div.propiedad-ficha.clearfix > div.content-left-col > div.propiedad-ficha-mini > div.content-panel.small-content-panel > p:nth-child(2)";
			String selector2 = "#wrapper > section > div > div > div.col-sm-9.span-fix-content > article > div > div.propiedad-ficha.clearfix > div.content-left-col > div.propiedad-ficha-mini > div.content-panel.small-content-panel";
            
			Elements elements = doc.select(selector2);
            for (Element element : elements) {
            	//System.out.println(element);
            	String strTmp = element.toString().replace("</strong></p>", "");
            	strTmp = strTmp.replace("</div>", "");
            	strTmp = strTmp.replace(":", "");
            	String[] arrTmp = strTmp.split("Publicada");
            	result = arrTmp[1];
            	break;
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
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
}