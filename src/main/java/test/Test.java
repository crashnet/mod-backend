package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	private JSONObject pippo;

	Test() {

	};

	public static void main(String[] args) throws IOException {

		String temp = FileUtils.readFileToString(new File("prog_archivio.json"), "UTF-8");

		System.out.println(temp);

		JSONObject obj = new JSONObject(temp);
		// Test t = new Test();
		// Map map = t.GetListOfEpisodes("prog-amici");
		// // TODO Auto-generated catch block

	}

	Map<String, String> GetListOfEpisodes(String mc) throws IOException {

		HashMap<String, String> map = new HashMap<>();
		mc = mc.replaceFirst("-", "|");
		mc = mc.replaceAll("-", "_");
		String[] mc2 = mc.split("\\|");
		String url = "http://www.video.mediaset.it/programma/" + mc2[1] + "/archivio-video.shtml";

		Document doc = Jsoup.connect(url).get();
		Element container = doc.select("div.page, div.brandpage").first();
		Elements sections = container.getElementsByTag("section");
		for (Element section : sections) {
			Element h2 = section.select("h2.title").first();
			if (h2 != null) {
				System.out.println(h2.text());
				map.put("episode", h2.text());
			}
		}

		return map;
	}

}
