package mediaset.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import configuration.SessionManagement;
import mediaset.Program;
import mediaset.Section;
import mediaset.Video;

public class MyCallableSections implements Callable<Program> {

	final static Logger logger = Logger.getLogger(MyCallableSections.class);

	private Program program;
	private String program_url;
	private Document doc;
	private List<Section> sections = new ArrayList<Section>();

	MyCallableSections(Program program) {
		this.program = program;
	}

	@Override
	public Program call() {

		if(program.getUrl().equals(""))
			return null;
		program_url = "http://www.video.mediaset.it" + program.getUrl();

		logger.debug("ProgramId: " + program.getId() + " - program_title: " + program.getLabel() + " - program_url: "
				+ program_url);

		crawl(program_url);
		
		Elements secs = doc.select("section.videoMixed");

		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2").first();

			if (checkNull(tag_h2)) {

				Elements tags_a = sec.select("a");

				List<Video> video_array = new ArrayList<Video>();

				Section s = new Section();
				s.setTitle(tag_h2.text());
				s.setNumVideo(tags_a.size());

				for (Element tag_a : tags_a) {

					Video video = new Video();
					String attr_href = tag_a.attr("href");
					video.setId((attr_href.split("_").length > 1 ? attr_href.split("_")[1] : ""));

					Element tag_img = tag_a.select("img").first();

					if (checkNull(tag_img)) {
						video.setThumbnails(tag_img.attr("src"));
						video.setTitle(tag_img.attr("title"));
					}

					if (!video.getId().equals("")) {
						video_array.add(video);
						logger.debug(video.toString());
					}
					
				}

				s.setVideos(video_array);
				sections.add(s);
			}
		}

		program.setSections(sections);

		Element descr = doc.select("div._1bCA7").first();
		logger.info("Program description:" + descr);
		program.setDescription((descr != null) ? descr.text() : "description not found");

		Element poster = doc.select("img._2BHAN").first();
		logger.info("Program poster:" + poster);
		program.setPoster((poster != null) ? poster.attr("src") : null);

		
		return program;
	}

	private void crawl(String url) {

		Response response = null;
		Connection con = null;
		try {
			con =Jsoup.connect(url);
			response = con.followRedirects(false).timeout(100000).userAgent("Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36").execute();

		if (response.hasHeader("location"))
			crawl(response.header("location"));
		else
			doc = Jsoup.connect(url).followRedirects(false).timeout(100000).userAgent("Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36").get();

		logger.debug("ProgramId: " + program.getId() + " status code: " + response.statusCode() + " program_title: "
				+ program.getLabel() + " - url: " + url);
		
		} catch (IOException e) {
			logger.error("ProgramId: " + program.getId() + " error code: "+ e.getMessage() +" status code: " + response.statusCode() + " program_title: "
					+ program.getLabel() + " - url: " + url);
		}
	}

	private boolean checkNull(Object obj) {
		return obj != null ? true : false;
	}
}
