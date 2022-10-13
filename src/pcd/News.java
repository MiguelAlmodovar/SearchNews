package pcd;

import java.io.Serializable;

public class News implements Serializable {
private	String title;
private	String content;
	
	

	public News(String title, String content){
		this.title = title;
		this.content = content;

}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
}