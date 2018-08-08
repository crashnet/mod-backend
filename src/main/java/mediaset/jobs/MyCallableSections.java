package mediaset.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mediaset.Program;
import mediaset.Section;
import mediaset.Video;

public class MyCallableSections implements Callable<Map<String,Section>> {


	final static Logger logger = Logger.getLogger(MyCallableSections.class);

	private Program program;

	MyCallableSections(Program program) {
		this.program = program;
	}

	@Override
	public Map<String,Section> call()  {

		String path_url = program.getUrl();
		String program_url = "http://www.video.mediaset.it" + path_url;
		Map<String, Section> sections = new HashMap<String, Section>();
		
		Connection con = Jsoup.connect(program_url);
		con.timeout(30000);
		Document doc;
		try {
			doc = con.get();
		
		Element container = doc.select("div.page, div.brandpage").first();
		Elements secs = container.select("section");

		
		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2.title").first();

			if (tag_h2 != null) {
				Elements videos = sec.select("div.clip, div.ic-none");
				List<Video> video_array = new ArrayList<Video>();
				Section s = new Section();
				s.setTitle(tag_h2.text());
				s.setNumVideo(videos.size());

				for (Element video : videos) {
					Element tag_a = video.select("a").first();
					Element tag_img = tag_a.select("img").first();
					Element tag_p = video.select("p").first();

					Video v = new Video();
					if (tag_a != null) {
						v.setId(tag_a.attr("data-vid"));
						v.setTitle(tag_a.attr("title"));
						v.setBrand(tag_a.attr("data-brand"));
					}
					if (tag_img != null)
						v.setThumbnails(tag_img.attr("data-lazy"));
					if (tag_p != null)
						v.setDescription(tag_p.text());
					video_array.add(v);
				}
				s.setVideos(video_array);
				sections.put(s.getTitle(),s);
			}
		}

		} catch (IOException e) {
			
			logger.error("program: "+ program + "\nmessage: " + e.getMessage());
		}
		program.setSections(sections);
		return sections;
	}

}
