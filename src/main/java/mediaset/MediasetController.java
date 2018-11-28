package mediaset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import configuration.SessionManagement;

@CrossOrigin(origins = "*")
@Component
@RestController
public class MediasetController {

	final static Logger logger = Logger.getLogger(MediasetController.class);

	@Autowired
	private SessionManagement sessionManagement;

	public MediasetController() {
	}

	@GetMapping(value = "/mediaset/sizesezioni")
	public @ResponseBody int sezioniSizeGET() throws IOException {
		return sessionManagement.getCache_sezioni().size();
	}

	@RequestMapping(value = "/mediaset/archivio", method = RequestMethod.GET)
	public @ResponseBody Archivio archivioGET() throws IOException {
		logger.debug("MethodName:" + Thread.currentThread().getStackTrace()[1].getMethodName());
		return sessionManagement.getArchivio();
	}

	@PostMapping(value = "/mediaset/archivio")
	public @ResponseBody Archivio archivioPOST() throws IOException {
		logger.debug("MethodName:" + Thread.currentThread().getStackTrace()[1].getMethodName());
		return sessionManagement.getArchivio();
	}

	@GetMapping(value = "/mediaset/elenco-gruppi")
	public @ResponseBody List<Group> elencoGruppiGET() throws IOException, CloneNotSupportedException {

		List<Group> groups = new ArrayList<Group>();

		for (Group g : sessionManagement.getArchivio().getProgrammi().getGroup()) {
			Group ng = new Group();
			ng.setIndex(g.getIndex());
			groups.add(ng);
		}
		return groups;
	}

	@PostMapping(value = "/mediaset/elenco-gruppi")
	public @ResponseBody List<String> elencoGruppiPOST() throws IOException {
		List<Group> gruppi = sessionManagement.getArchivio().getProgrammi().getGroup();
		List<String> groups = new ArrayList<String>();
		for (Group g : gruppi) {
			groups.add(g.getIndex());
		}
		return groups;
	}

	@GetMapping(value = "/mediaset/elenco-programmi/{id}")
	public @ResponseBody List<Program> elencoProgrammiPerGruppoGET(@PathVariable String id) throws IOException {
		List<Group> gruppi = sessionManagement.getArchivio().getProgrammi().getGroup();

		for (Group g : gruppi)
			if (id.equals(g.getIndex()))
				return g.getProgram();

		return null;
	}

	@PostMapping(value = "/mediaset/elenco-programmi")
	public @ResponseBody List<Program> elencoProgrammiPerGruppoPOST(@RequestBody Input input) throws IOException {

		logger.debug("Request: " + input);
		List<Group> gruppi = sessionManagement.getArchivio().getProgrammi().getGroup();

		for (Group g : gruppi)
			if (input.getId().equals(g.getIndex()))
				return g.getProgram();

		return null;
	}

	@PostMapping(value = "/mediaset/elenco-programmi-full")
	public @ResponseBody Collection<Program> elencoProgrammiFullGET() throws IOException {
		return sessionManagement.getProgrammi().values();
	}

	@GetMapping(value = "/mediaset/elenco-programmi-full")
	public @ResponseBody Collection<Program> elencoProgrammiFullPOST() throws IOException {
		return sessionManagement.getProgrammi().values();
	}

	@PostMapping(value = "/mediaset/sezioni")
	public @ResponseBody Sections sezioniPOST(@RequestBody Input input) throws IOException {

		Date timestamp_start = new Date();
		logger.debug("Request: " + input);
		String path_url = sessionManagement.getProgrammi().get(input.getId()).getUrl();
		logger.debug("path_url: " + path_url);
		String program_url = "http://www.video.mediaset.it" + path_url;
		Document doc = crawl(program_url);

		Elements secs = doc.select("section.videoMixed");

		ArrayList<Section> sections = new ArrayList<Section>();

		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2").first();

			if (checkNull(tag_h2)) {

				Elements tags_a = sec.select("a");

				List<Video> video_array = new ArrayList<Video>();

				Section section = new Section();
				section.setTitle(tag_h2.text());
				section.setNumVideo(tags_a.size());

				for (Element tag_a : tags_a) {

					Video video = new Video();
					String attr_href = tag_a.attr("href");
					video.setId((attr_href.split("_").length > 1 ? attr_href.split("_")[1] : ""));

					Element tag_img = tag_a.select("img").first();

					if (checkNull(tag_img)) {
						video.setThumbnails(tag_img.attr("src"));
						video.setTitle(tag_img.attr("title"));
					}
					if (!video.getId().equals(""))
						video_array.add(video);

					logger.debug(video.toString());
				}

				section.setVideos(video_array);
				sections.add(section);

			}
		}

		Sections pippo = new Sections(sections, input.getId(),
				sessionManagement.getProgrammi().get(input.getId()).getLabel());

		Date timestamp_end = new Date();
		logger.debug("Response: " + pippo.toString());
		logger.debug("sections time left: " + Utils.getDateDiff(timestamp_start, timestamp_end, TimeUnit.MILLISECONDS));
		return pippo;
	}

	@GetMapping(value = "/mediaset/sezioni/{id}")
	public @ResponseBody Sections sezioniGET(@PathVariable String id) throws IOException {

		logger.debug("Request: " + id);
		String path_url = sessionManagement.getProgrammi().get(id).getUrl();
		logger.debug("path_url: " + path_url);
		String program_url = "http://www.video.mediaset.it" + path_url;

		Document doc = crawl(program_url);
		logger.debug("doc: " + doc.textNodes());

		Element descr = doc.select("div._1bCA7").first();
		logger.debug("Program description:" + descr.text());

		Elements secs = doc.select("section.videoMixed");
		logger.debug("secs.size: " + secs.size());
		ArrayList<Section> sections = new ArrayList<Section>();

		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2").first();

			if (checkNull(tag_h2)) {

				Elements tags_a = sec.select("a");

				List<Video> video_array = new ArrayList<Video>();

				Section section = new Section();
				section.setTitle(tag_h2.text());
				section.setNumVideo(tags_a.size());

				for (Element tag_a : tags_a) {

					Video video = new Video();
					String attr_href = tag_a.attr("href");
					video.setId((attr_href.split("_").length > 1 ? attr_href.split("_")[1] : ""));

					Element tag_img = tag_a.select("img").first();

					if (checkNull(tag_img)) {
						video.setThumbnails(tag_img.attr("src"));
						video.setTitle(tag_img.attr("title"));
					}
					if (!video.getId().equals(""))
						video_array.add(video);

					logger.debug(video.toString());
				}

				section.setVideos(video_array);
				sections.add(section);

			}
		}

		Sections pippo = new Sections(sections, id, sessionManagement.getProgrammi().get(id).getLabel());
		Date timestamp_end = new Date();
		logger.debug("Response: " + pippo.toString());
		return pippo;
	}

	/**
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "/mediaset/programma/{id}")
	public @ResponseBody Program programmaGET(@PathVariable String id) throws IOException {

		logger.debug("Request: " + id);
		Program program = sessionManagement.getProgrammi().get(id);
		if(checkNull(program))
			if(checkNull(program.getSections()))
				return program;


		String path_url = program.getUrl();
		logger.debug("path_url: " + path_url);
		String program_url = "http://www.video.mediaset.it" + path_url;

		List<Section> sections = new ArrayList<Section>();

		Document doc = crawl(program_url);
		logger.debug("doc: " + doc.textNodes());


		Elements secs = doc.select("section.videoMixed");
		logger.debug("secs.size: " + secs.size());

		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2").first();

			if (checkNull(tag_h2)) {

				Elements tags_a = sec.select("a");

				

				Section section = new Section();
				
				section.setTitle(tag_h2.text());
				List<Video> video_array = new ArrayList<Video>();

				for (Element tag_a : tags_a) {

					Video video = new Video();
					String attr_href = tag_a.attr("href");
					video.setId((attr_href.split("_").length > 1 ? attr_href.split("_")[1] : ""));

					Element tag_img = tag_a.select("img").first();

					if (checkNull(tag_img)) {
						video.setThumbnails(tag_img.attr("src"));
						video.setTitle(tag_img.attr("title"));
					}
					if (!video.getId().equals(""))
						video_array.add(video);

					logger.debug(video.toString());
				}

				section.setId(String.valueOf(secs.indexOf(sec)));
				section.setVideos(video_array);
				section.setNumVideo(video_array.size());
				sections.add(section);

			}
		}

		Element descr = doc.select("div._1bCA7").first();
		logger.debug("Program description:" + descr);
		program.setDescription((descr != null) ? descr.text() : "description not found");
		
		Element poster = doc.select("img._2BHAN").first();		
		program.setPoster((poster != null) ? poster.attr("src") : null);
		
		program.setSections(sections);
		
		logger.debug("Response: " + program.toString());
		return program;
	}

	private Document crawl(String url) throws IOException {

		Document doc;

		Response response = Jsoup.connect(url).followRedirects(false).timeout(50000).execute();

		if (response.hasHeader("location"))
			doc = crawl(response.header("location"));
		else
			doc = Jsoup.connect(url).followRedirects(false).timeout(50000).get();

		logger.debug("status code: " + response.statusCode() + " - url: " + url);

		return doc;
	}

	private boolean checkNull(Object obj) {
		return obj != null ? true : false;
	}

	@GetMapping(value = "/mediaset/elenco-sezioni/{program_id}")
	public @ResponseBody Sections elencoSezioniGET(@PathVariable String program_id) throws IOException {
		return sessionManagement.getCache_sezioni().get(program_id);
	}

	@PostMapping(value = "/mediaset/elenco-sezioni")
	public @ResponseBody Collection<Section> elencoSezioniPOST(@RequestBody Input input) throws IOException {

		return sessionManagement.getProgrammi().get(input.getId()).getSections();
	}

	@PostMapping(value = "/mediaset/elenco-video")
	public @ResponseBody List<Video> elencoVideoPOST(@RequestBody Input input) throws IOException {
		logger.debug("Request: " + input);
		List<Video> video = sessionManagement.getProgrammi().get(input.getProgramId()).getSections().get(1).getVideos();
		logger.debug("Response: " + video);
		return video;

	}

	@GetMapping(value = "/mediaset/elenco-video")
	public @ResponseBody List<Video> elencoVideoGET(@RequestBody Input input) throws IOException {
		logger.debug("Request: " + input);
		List<Video> video = sessionManagement.getProgrammi().get(input.getProgramId()).getSections().get(1).getVideos();
		logger.debug("Response: " + video);
		return video;

	}

	@PostMapping(value = "/mediaset/video")
	public @ResponseBody Video videoPOST(@PathVariable String id) throws IOException {

		String video_url_json = "http://cdnsel01.mediaset.net/GetCdn.aspx?streamid={id}&format=json";
		video_url_json = video_url_json.replace("{id}", id);

		String json = Jsoup.connect(video_url_json).get().text();
		logger.debug("video json: " + json);
		JSONObject jsono = new JSONObject(json);
		String direct_video_url = "";
		if (jsono.getString("state").equals("OK"))
			direct_video_url = (String) jsono.getJSONArray("videoList").get(0);

		Video video = new Video();
		video.setUrl(direct_video_url);
		logger.debug("Response: " + video);
		return video;
	}

	@GetMapping(value = "/mediaset/video/{id}")
	public @ResponseBody Video videoGET(@PathVariable String id) throws IOException {

		String video_url_json = "http://cdnsel01.mediaset.net/GetCdn.aspx?streamid={id}&format=json";
		video_url_json = video_url_json.replace("{id}", id);

		String json = Jsoup.connect(video_url_json).get().text();
		logger.debug("video json: " + json);
		JSONObject jsono = new JSONObject(json);
		String direct_video_url = "";
		if (jsono.getString("state").equals("OK"))
			direct_video_url = (String) jsono.getJSONArray("videoList").get(0);

		Video video = new Video();
		video.setUrl(direct_video_url);
		logger.debug("Response: " + video);
		return video;
	}
	
	@GetMapping(value = "/mediaset/getmenu")
	public @ResponseBody List getMenu() throws IOException {

		List<MenuItem> list = new ArrayList<MenuItem>();
		list.add(new MenuItem("Offerta per Te", "https://wwww.google.it" ));
		list.add(new MenuItem("Porta i tuoi Amici", "https://wwww.google.it"));
		list.add(new MenuItem("Your next Market", "https://wwww.google.it"));

		return list;
	}
	
	@GetMapping(value = "/mediaset/searchByTitle/{search}")
	public @ResponseBody List<Program> getProgramList(@PathVariable String search) throws IOException {

		List<Program> list = new ArrayList<Program>();

		for(Program prog: sessionManagement.getProgrammi().values())
			if(prog.getLabel().toUpperCase().contains(search.toUpperCase()))
				list.add(prog);

		return list;
	}

}
