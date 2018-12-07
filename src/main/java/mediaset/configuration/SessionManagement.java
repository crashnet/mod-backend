package mediaset.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import mediaset.beans.Archivio;
import mediaset.beans.Program;
import mediaset.beans.Sections;

@Component
public class SessionManagement {

	private Map<String, Program> programmi;
	private Map<String, Sections> cache_sezioni;
	private Archivio archivio;
	
	public SessionManagement() {
		programmi = new HashMap<String, Program>();
		cache_sezioni = new HashMap<String, Sections>();
		archivio = new Archivio();
	}

	public Map<String, Program> getProgrammi() {
		return programmi;
	}

	public void setProgrammi(Map<String, Program> programmi) {
		this.programmi = programmi;
	}

	public Map<String, Sections> getCache_sezioni() {
		return cache_sezioni;
	}

	public void setCache_sezioni(Map<String, Sections> cache_sezioni) {
		this.cache_sezioni = cache_sezioni;
	}

	public Archivio getArchivio() {
		return archivio;
	}

	public void setArchivio(Archivio archivio) {
		this.archivio = archivio;
	}

}