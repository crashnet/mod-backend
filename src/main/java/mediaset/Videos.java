package mediaset;

import java.util.List;

public class Videos {

	private List<Video> videos;

	public List<Video> getVideos() {
		return videos;
	}

	@Override
	public String toString() {
		return "Videos [videos=" + videos + "]";
	}

	public void setVideos(List<Video> list) {
		this.videos = list;
	}

}
