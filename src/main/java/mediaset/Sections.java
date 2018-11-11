package mediaset;

import java.util.ArrayList;
import java.util.List;

public class Sections {

	private List<Section> sections;
	private String programId;
	private String label;
	
	@Override
	public String toString() {
		return "Sections [sections=" + sections + ", programId=" + programId + ", label=" + label + "]";
	}

	public Sections() {
		super();
	}
	
	public Sections(List<Section> sections, String programId, String label) {
		super();
		this.sections = sections;
		this.programId = programId;
		this.label = label;
	}



	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

}