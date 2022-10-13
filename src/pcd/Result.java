package pcd;

import java.io.File;
import java.io.Serializable;

public class Result implements Serializable, Comparable<Result> {

	private int nOcor;
	private News news;
	private String name;

	public Result(int n, News news, String name) {
		this.nOcor = n;
		this.news = news;
		this.name = name;
	}

	public int getnOcor() {
		return nOcor;
	}

	public News getNews() {
		return news;
	}

	public String getName() {
		return name;
	}
	public String getString() {
		return name + "-" + Integer.toString(nOcor);
		
	}
	@Override
	public int compareTo(Result o) {
		return o.getnOcor() - nOcor;
	}

}
