package mediaset;

import java.util.ArrayList;

public class Sections {

	private ArrayList<Section> sections;
	private String programId;
	private String label;

	@Override
	public String toString() {
		return "Sections [sections=" + sections + ", programId=" + programId + ", label=" + label + "]";
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

	public ArrayList<Section> getSections() {
		return sections;
	}

	public void setSections(ArrayList<Section> sections) {
		this.sections = sections;
	}

}