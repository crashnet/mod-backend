package rai;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestRai {
	
	public static void main(String[] args) throws IOException {
		
//        System.setProperty("http.proxyHost", "10.10.11.12");
//        System.setProperty("http.proxyPort", "8080");
//        System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
		
		String url_rai = "http://www.raiplay.it/programmi/Tutti-5a3ede4f-78fa-4ecc-a04f-296781dbcafd.html";

		Document doc = Jsoup.connect(url_rai).get();
		Elements els = doc.select("a.articolo");
		System.out.println("programmi:" + els.size());

		for(Element e: els) {
			Element useraction = e.select("div.useraction").first();
			Element titolo = e.select("div.titolo").first();
			Element img = e.select("img.landscape, img.lazy").first();

			Program p = new Program();
			p.setLabel(titolo.text());
			p.setThumbnail(img.attr("data-original"));
			p.setThumbnail_placeholder(img.attr("src"));
			p.setUrl(useraction.attr("data-path"));
			p.setId(useraction.attr("data-configuratore"));
			p.setMc(e.attr("data-azselector"));
		}
	}
}