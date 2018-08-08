package mediaset;

public class Input {

	private String id;
	private String programId;
	private String sector;

	public String getProgramId() {
		return programId;
	}

	@Override
	public String toString() {
		return "Input [id=" + id + ", programId=" + programId + ", sector=" + sector + "]";
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
