package pcd;

import java.io.File;
import java.io.Serializable;

public class Task implements Serializable {

	private int id;
	private News news;
	private String input;
	private String name;

	public Task(int id, News news, String input, String name) {
		this.id = id;
		this.news = news;
		this.input = input;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public News getNews() {
		return news;
	}

	public String getInput() {
		return input;
	}

	public String getName() {
		return name;
	}
}
