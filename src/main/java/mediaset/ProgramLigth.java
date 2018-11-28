package mediaset;

public class ProgramLigth {

	private String id;
	private String Label;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public ProgramLigth(String id, String label) {
		super();
		this.id = id;
		Label = label;
	}
	@Override
	public String toString() {
		return "ProgramLigth [id=" + id + ", Label=" + Label + "]";
	}
	
	
}
