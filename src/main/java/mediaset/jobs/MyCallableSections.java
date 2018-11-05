package mediaset.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mediaset.Program;
import mediaset.Section;
import mediaset.Video;

public class MyCallableSections implements Callable<Map<String, Section>> {

	final static Logger logger = Logger.getLogger(MyCallableSections.class);

	private Program program;
	private String program_url;
	private Document doc;
	private Map<String, Section> sections = new HashMap<String, Section>();

	MyCallableSections(Program program) {
		this.program = program;
	}

	@Override
	public Map<String, Section> call() {

		program_url = "http://www.video.mediaset.it" + program.getUrl();

		logger.debug("ProgramId: " + program.getId() + " - program_title: " + program.getLabel() + " - program_url: "
				+ program_url);

		try {
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
					sections.put(s.getTitle(), s);

				}
			}

			program.setSections(sections);
			Element descr = doc.select("div._1bCA7").first();
			logger.debug("Program description:" + descr.text());
			program.setDescription(descr.text());

		} catch (IOException e2) {
			logger.error("ProgramId: " + program.getId() + " error code: " + e2.getMessage() + " - program_title: "
					+ program.getLabel() + " - program_url: " + program_url);
		}

		return sections;
	}

	private void crawl(String url) throws IOException {

		Response response = Jsoup.connect(url).followRedirects(false).timeout(50000).execute();

		if (response.hasHeader("location"))
			crawl(response.header("location"));
		else
			doc = Jsoup.connect(url).followRedirects(false).timeout(50000).get();

		logger.debug("ProgramId: " + program.getId() + " status code: " + response.statusCode() + " program_title: "
				+ program.getLabel() + " - url: " + url);
	}

	private boolean checkNull(Object obj) {
		return obj != null ? true : false;
	}
}
