package mediaset;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import mediaset.beans.Archivio;
import mediaset.beans.Group;
import mediaset.beans.Program;
import mediaset.configuration.SessionManagement;
import mediaset.jobs.CacheMediasetProgramSectionsThread;

@RestController
public class MediasetInit {

	@Autowired
	private SessionManagement sessionManagement;
	
	final static Logger logger = Logger.getLogger(MediasetInit.class);
	@GetMapping("/prova")
	public void start() {

		RestTemplate rt = new RestTemplate();
		String archivio_json = rt.getForObject("http://www.video.mediaset.it/programma/progr_archivio.json",
				String.class);
		ObjectMapper mapper = new ObjectMapper();

		// String archivio_json = FileUtils.readFileToString(new
		// File("prog_archivio.json"), "UTF-8");

		/**
		 * sanitizzo il json sostituendo per il primo program un oggetto vuoto con un
		 * array vuoto
		 **/
		archivio_json = archivio_json.replaceFirst("\\{\\}", "[]");
		try {
			Archivio arc = mapper.readValue(archivio_json, Archivio.class);
			sessionManagement.setArchivio(arc);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		/** popolo la mappa di tutti i programmi **/
		for (Group group : sessionManagement.getArchivio().getProgrammi().getGroup()) {
			
			for (Program program : group.getProgram()) {
				if (program.getId().equals(""))
					program.setId(program.getLabel());
				if (program.getUrl().contains("http"))
					program.setUrl(program.getUrl().replaceFirst("http://www.video.mediaset.it", ""));
				program.setDescription("");
				sessionManagement.getProgrammi().put(program.getId(), program);
			}
		}
		

		CacheMediasetProgramSectionsThread cst = new CacheMediasetProgramSectionsThread(sessionManagement);
		cst.start();

		logger.info("End Application constructors");
		logger.info("elenco programmi: " + sessionManagement.getProgrammi().size());

	}
	
}