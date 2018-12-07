package mediaset.beans;

import java.util.ArrayList;

public class Video {

	private String id;
	private String title;
	private String brand;
	private String description;
	private String thumbnails;
	private String url;
	private String extension;
	private ArrayList<String> direct_video_urls;
	private final String static_url = "http://cdnsel01.mediaset.net/GetCdn.aspx?streamid={id}&format=json";
	private String programId;
	private String section;
	
	
	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		setUrl(static_url.replace("{id}", id));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public ArrayList<String> getDirect_video_urls() {
		return direct_video_urls;
	}

	public void setDirect_video_urls(ArrayList<String> direct_video_urls) {
		this.direct_video_urls = direct_video_urls;
	}

	public String getStatic_url() {
		return static_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", title=" + title + ", brand=" + brand + ", description=" + description
				+ ", thumbnails=" + thumbnails + ", url=" + url + ", extension=" + extension + ", direct_video_urls="
				+ direct_video_urls + ", static_url=" + static_url + "]";
	}

}
