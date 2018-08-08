package mediaset;

import java.util.List;

public class Section {

	@Override
	public String toString() {
		return "Section [title=" + title + ", numVideo=" + numVideo + "]";
	}

	private String title;
	private int numVideo = 0;
	private List<Video> videos;
	
	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public int getNumVideo() {
		return numVideo;
	}

	public void setNumVideo(int numVideo) {
		this.numVideo = numVideo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}