package mediaset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import mediaset.jobs.CacheMediasetProgramSectionsThread;

@CrossOrigin(origins = "*")
@RestController
public class MediasetController {

	private Archivio archivio;
	private Map<String, Program> programmi = new HashMap<String, Program>();
	Map<String, Sections> lista_sezioni = new HashMap<String, Sections>();

	final static Logger logger = Logger.getLogger(MediasetController.class);

	ObjectMapper mapper = new ObjectMapper();

	MediasetController() throws IOException {

		Date timestamp_start = new Date();
		// RestTemplate rt = new RestTemplate();
		// String json_string =
		// rt.getForObject("http://www.video.mediaset.it/programma/progr_archivio.json",
		// String.class);

		String archivio_json = FileUtils.readFileToString(new File("prog_archivio.json"), "UTF-8");

		/**
		 * sanitizzo il json sostituendo per il primo program un oggetto vuoto con un array vuoto 
		 **/
		archivio_json = archivio_json.replaceFirst("\\{\\}", "[]");
		archivio = mapper.readValue(archivio_json, Archivio.class);

		/** popolo la mappa di tutti i programmi **/
		for (Group group : archivio.getProgrammi().getGroup()) {
			for (Program program : group.getProgram()) {
				if(program.getId().equals(""))
					program.setId(program.getLabel());
				if(program.getUrl().contains("http"))
					program.setUrl(program.getUrl().replaceFirst("http://www.video.mediaset.it", ""));
				programmi.put(program.getId(), program);
			}
		}

//		CacheMediasetProgramSectionsThread cst = new CacheMediasetProgramSectionsThread(programmi);
//		cst.start();

		System.out.println("End Application constructors");

		logger.info("elenco programmi: " + programmi.size());

		Date timestamp_end = new Date();
		logger.info("tempo inizializzazione server: " + getDateDiff(timestamp_start, timestamp_end, TimeUnit.MILLISECONDS));
	}

	@RequestMapping(value = "/mediaset/sizesezioni", method = RequestMethod.GET)
	public @ResponseBody int sezioniSizeGET() throws IOException {
		return lista_sezioni.size();
	}
	
	
	@RequestMapping(value = "/mediaset/archivio", method = RequestMethod.GET)
	public @ResponseBody Archivio archivioGET() throws IOException {
		return archivio;
	}
	
	@RequestMapping(value = "/mediaset/archivio", method = RequestMethod.POST)
	public @ResponseBody Archivio archivioPOST() throws IOException {
		return archivio;
	}
	
	@RequestMapping(value = "/mediaset/elenco-gruppi", method = RequestMethod.GET)
	public @ResponseBody List<String> elencoGruppiGET() throws IOException {
		List<Group> gruppi = archivio.getProgrammi().getGroup();
		List<String> groups = new ArrayList<String>();
		for(Group g: gruppi) {
			groups.add(g.getIndex());
		}
		return groups;
	}
	
	@RequestMapping(value = "/mediaset/elenco-gruppi", method = RequestMethod.POST)
	public @ResponseBody List<String> elencoGruppiPOST() throws IOException {
		List<Group> gruppi = archivio.getProgrammi().getGroup();
		List<String> groups = new ArrayList<String>();
		for(Group g: gruppi) {
			groups.add(g.getIndex());
		}
		return groups;
	}
	
	@RequestMapping(value = "/mediaset/elenco-programmi/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Program> elencoProgrammiPerGruppoGET(@PathVariable String id) throws IOException {
		List<Group> gruppi = archivio.getProgrammi().getGroup();

		for(Group g: gruppi)
			if(id.equals(g.getIndex()))
				return g.getProgram();

		return null;
	}

	@RequestMapping(value = "/mediaset/elenco-programmi", method = RequestMethod.POST)
	public @ResponseBody List<Program> elencoProgrammiPerGruppoPOST(@RequestBody Input input) throws IOException {
		
		logger.debug("Request: " +input);
		List<Group> gruppi = archivio.getProgrammi().getGroup();
		
		for(Group g: gruppi)
			if(input.getId().equals(g.getIndex()))
				return g.getProgram();

		return null;
	}
	
	@RequestMapping(value = "/mediaset/elenco-programmi-full", method = RequestMethod.POST)
	public @ResponseBody Collection<Program> elencoProgrammiFullGET() throws IOException {
		return programmi.values();
	}
	
	@RequestMapping(value = "/mediaset/elenco-programmi-full", method = RequestMethod.GET)
	public @ResponseBody Collection<Program> elencoProgrammiFullPOST() throws IOException {
		return programmi.values();
	}

	
	@RequestMapping(value = "/mediaset/sezioni", method = RequestMethod.POST)
	public @ResponseBody Sections sezioniPOST(@RequestBody Input input) throws IOException {

		Date timestamp_start = new Date();
		logger.debug("Request: " + input);
		String path_url = programmi.get(input.getId()).getUrl();
		logger.debug("path_url: " + path_url);
		String program_url = "http://www.video.mediaset.it" + path_url;

		Document doc = Jsoup.connect(program_url).get();
		Element container = doc.select("div.page, div.brandpage").first();
		Elements secs = container.select("section");

		ArrayList<Section> sections = new ArrayList<>();

		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2.title").first();

			if (tag_h2 != null) {
				Elements videos = sec.select("div.clip, div.ic-none");
				Section s = new Section();
				s.setTitle(tag_h2.text());
				s.setNumVideo(videos.size());
				sections.add(s);
			}
		}

		Sections sezioni = new Sections();
		sezioni.setProgramId(input.getId());
		sezioni.setSections(sections);
		sezioni.setLabel(programmi.get(input.getId()).getLabel());
		
		Date timestamp_end = new Date();
		logger.debug("Response: " + sezioni.toString());
		logger.debug("sections time left: " + getDateDiff(timestamp_start, timestamp_end, TimeUnit.MILLISECONDS));
		return sezioni;
	}
	
	@RequestMapping(value = "/mediaset/sezioni/{id}", method = RequestMethod.GET)
	public @ResponseBody Sections sezioniGET(@PathVariable String id) throws IOException {

		Date timestamp_start = new Date();
		logger.debug("Request: " + id);
		String path_url = programmi.get(id).getUrl();
		logger.debug("path_url: " + path_url);
		String program_url = "http://www.video.mediaset.it" + path_url;

		Document doc = Jsoup.connect(program_url).get();
		Element container = doc.select("div.page, div.brandpage").first();
		Elements secs = container.select("section");

		ArrayList<Section> sections = new ArrayList<>();

		for (Element sec : secs) {
			Element tag_h2 = sec.select("h2.title").first();

			if (tag_h2 != null) {
				Elements videos = sec.select("div.clip, div.ic-none");
				Section s = new Section();
				s.setTitle(tag_h2.text());
				s.setNumVideo(videos.size());
				sections.add(s);
			}
		}

		Sections sezioni = new Sections();
		sezioni.setProgramId(id);
		sezioni.setSections(sections);
		sezioni.setLabel(programmi.get(id).getLabel());
		
		Date timestamp_end = new Date();
		logger.debug("Response: " + sezioni.toString());
		logger.debug("sections time left: " + getDateDiff(timestamp_start, timestamp_end, TimeUnit.MILLISECONDS));
		return sezioni;
	}
	
	
	@RequestMapping(value = "/mediaset/elenco-sezioni/{program_id}", method = RequestMethod.GET)
	public @ResponseBody Sections elencoSezioniGET(@PathVariable String program_id) throws IOException {
		return lista_sezioni.get(program_id);
	}
	
	@RequestMapping(value = "/mediaset/elenco-sezioni", method = RequestMethod.POST)
	public @ResponseBody Collection<Section> elencoSezioniPOST(@RequestBody Input input) throws IOException {
		
		return programmi.get(input.getId()).getSections().values();
	}


	@RequestMapping(value = "/mediaset/elenco-video", method = RequestMethod.POST)
	public @ResponseBody List<Video> elencoVideoPOST(@RequestBody Input input) throws IOException {
		logger.debug("Request: " + input);
		List<Video> video = programmi.get(input.getProgramId()).getSections().get(input.getSector()).getVideos();	
		logger.debug("Response: " + video);
		return video;

	}

	@RequestMapping(value = "/mediaset/video", method = RequestMethod.POST)
	public @ResponseBody Video videoPOST(@RequestBody Input input) throws IOException {

		String video_url_json = "http://cdnsel01.mediaset.net/GetCdn.aspx?streamid={id}&format=json";
		video_url_json = video_url_json.replace("{id}", input.getId());

		String json = Jsoup.connect(video_url_json).get().text();

		JSONObject jsono = new JSONObject(json);
		String direct_video_url = "";
		if (jsono.getString("state").equals("OK"))
			direct_video_url = (String) jsono.getJSONArray("videoList").get(0);

		Video video = new Video();
		video.setUrl(direct_video_url);
		logger.debug("Response: " + video);
		return video;
	}


	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
}
