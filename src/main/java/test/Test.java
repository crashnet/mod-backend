package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

public class Test {

	static Document doc;

	public static void main(String[] args) throws IOException {
		String url = "https://api-ott-prod-fe.mediaset.net/PROD/play/rec/azlisting/v1.0?query=*:*&page={page}&hitsPerPage=50";

		retrieveAZList(url.replace("{page}", "1"));
	}

	static void retrieveAZList(String url) {

		 try {

				URL url2 = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			  }

	}

	static void GetListOfEpisodes(String url) {

		crawl(url);

		Elements images = doc.select("div._3G-Rv");//

		for (Element image : images) {
			Element first = image.select("a").first();
			if (first != null)
				System.out.println(first.attr("href"));
		}
//		System.out.println(doc.toString());

	}

	private static void crawl(String url1) {

		Response response = null;
		Connection con = null;
		try {
			con = Jsoup.connect(url1);
			response = con.followRedirects(false).timeout(100000).userAgent(
					"Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36")
					.execute();

			if (response.hasHeader("location"))
				crawl(response.header("location"));
			else
				doc = Jsoup.connect(url1).followRedirects(false).timeout(100000).userAgent(
						"Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36")
						.get();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
