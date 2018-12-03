package mediaset;

public class ProgramLight {

	private String id;
	private String label;
	private String description;
	private String thumbnail;
	
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public ProgramLight(String id, String label) {
		super();
		this.id = id;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ProgramLight(String id, String label,String thumbnail, String description) {
		super();
		this.id = id;
		this.label = label;
		this.thumbnail = thumbnail;
		this.description = description;
		
	}
	@Override
	public String toString() {
		return "ProgramLight [id=" + id + ", label=" + label + ", description=" + description + ", thumbnail="
				+ thumbnail + "]";
	}
	
	
}
