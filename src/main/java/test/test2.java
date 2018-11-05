package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mediaset.Section;
import mediaset.Video;

public class test2 {

	public static void main(String[] args) {

		final Logger logger = Logger.getLogger(test2.class);

//		String path_url = program.getUrl();
//		String program_url = "http://www.video.mediaset.it" + path_url;
		Map<String, Section> sections = new HashMap<String, Section>();

		String url = "https://www.mediasetplay.mediaset.it/fiction/carabinieri7_b6331554";
		try {
			Document doc = Jsoup.connect(url).followRedirects(false).get();

			Elements secs = doc.select("section.videoMixed");

			logger.info(secs.size());

			for (Element sec : secs) {
				Element tag_h2 = sec.select("h2").first();
				if (tag_h2 != null) {
					Elements tags_a = sec.select("a");

					List<Video> video_array = new ArrayList<Video>();
					Section s = new Section();
					s.setTitle(tag_h2.text());
					s.setNumVideo(tags_a.size());

					for (Element tag_a : tags_a) {
						Video v = new Video();
						Element tag_img = tag_a.select("img").first();

						if (tag_a != null) {
							String attr_href = tag_a.attr("href");
							v.setId((attr_href.split("_").length > 1 ? attr_href.split("_")[1] : ""));
							v.setBrand(tag_a.attr("data-brand"));
						}
						if (tag_img != null) {
							v.setThumbnails(tag_img.attr("src"));
							v.setTitle(tag_img.attr("title"));
						}
						video_array.add(v);
						
						s.setVideos(video_array);
						sections.put(s.getTitle(), s);
						logger.info(v.toString());
					}

				}
			}

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
