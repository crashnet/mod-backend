package mediaset;

public class MenuItem {

	private String text;
	private String url;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public MenuItem(String text, String url) {
		super();
		this.text = text;
		this.url = url;
	}
	
}
