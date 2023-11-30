package rs.rnk.example.sportnews.model;

public class NavigationItem {
	
	private int id;
	private String text;
	private String url;
	
	public NavigationItem() {
		this(0, "", "");
	}
	
	public NavigationItem(int id, String text, String url) {
		this.id = id;
		this.text = text;
		this.url = url;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public NavigationItem(String text, String url) {
		this(0, text, url);
	}
	
	public NavigationItem(int id, String text) {
		this(id, text, "");
	}
	
	public NavigationItem(String text) {
		this(0, text, "");
	}

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
	
}
