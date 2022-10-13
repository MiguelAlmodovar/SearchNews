package pcd;

import java.io.File;
import java.io.Serializable;

public class Request implements Serializable {

	private int idCliente;
	private String input;

	public Request(int idCliente, String input) {
		this.idCliente = idCliente;
		this.input = input;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public String getInput() {
		return input;
	}


}
