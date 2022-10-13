package pcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Algorithm {

	private String inputSearch;
	private News news;
	private int nOccur;

	public Algorithm(String inputSearch, News news) {
		this.inputSearch = inputSearch;
		this.news = news;
		this.nOccur = 0;

	}

	public void verifica() {
		int i = 0;
		Pattern p = Pattern.compile(inputSearch);
		Matcher m = p.matcher(news.getContent());
		while (m.find()) {
		    i++;
		}
		nOccur = i;
	}

	public int getnOccur() {
		return nOccur;
	}

	public News getNews() {
		return news;
	}
	
	
	}
	


