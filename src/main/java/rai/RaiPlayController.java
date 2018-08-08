package rai;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin(origins = "*")
@RestController
public class RaiPlayController {

	private Archivio archivio;
	private Map<String, Program> programmi = new HashMap<String, Program>();
	final static Logger logger = Logger.getLogger(RaiPlayController.class);

	ObjectMapper mapper = new ObjectMapper();

	RaiPlayController() throws IOException {

		Date timestamp_start = new Date();
		archivio = new Archivio();
		Programmi programs = new Programmi();
		List<Group> groupList = new ArrayList<Group>();
		Group g = new Group();
		List<Program> programList = new ArrayList<Program>();
		
		String url_rai = "http://www.raiplay.it/programmi/Tutti-5a3ede4f-78fa-4ecc-a04f-296781dbcafd.html";

		Document doc = Jsoup.connect(url_rai).get();
		Elements els = doc.select("a.articolo");

		String currentIndex = "";
		for(Element e: els) {
			Element useraction = e.select("div.useraction").first();
			Element titolo = e.select("div.titolo").first();
			Element img = e.select("img.landscape, img.lazy").first();
			
			Program program = new Program();
			program.setLabel(titolo.text());
			program.setThumbnail(img.attr("data-original"));
			program.setThumbnail_placeholder(img.attr("src"));
			program.setUrl(useraction.attr("data-path"));
			program.setId(useraction.attr("data-configuratore"));
			program.setMc(e.attr("data-azselector"));
			programmi.put(program.getId(), program);

			if(!currentIndex.equals(program.getMc())) {
				g.setProgram(programList);
				groupList.add(g);
				currentIndex = program.getMc();
				g = new Group();
				g.setIndex(currentIndex);
				programList = new ArrayList<Program>();
			}
			programList.add(program);
		}

		programs.setGroup(groupList);
		archivio.setProgrammi(programs);

		Date timestamp_end = new Date();
		
		logger.info("elenco programmi: " + programmi.size());
		logger.info("tempo inizializzazione server: " + getDateDiff(timestamp_start, timestamp_end, TimeUnit.MILLISECONDS));
	}

	@RequestMapping(value = "/rai/archivio", method = RequestMethod.GET)
	public @ResponseBody Archivio archivioGET() throws IOException {
		return archivio;
	}

	@RequestMapping(value = "/rai/archivio", method = RequestMethod.POST)
	public @ResponseBody Archivio archivioPOST() throws IOException {

		return archivio;
	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
}
