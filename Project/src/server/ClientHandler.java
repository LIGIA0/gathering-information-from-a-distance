package server;

import java.net.Socket;

public class ClientHandler {
	public String NumeClient;
	public Socket socket;
	public boolean conectat;
	public boolean ales;
	
	public ClientHandler(String NumeClient,Socket socket) {
		this.NumeClient=NumeClient;
		this.socket=socket;
		conectat = false;
		ales=false;
	}

	public boolean isAuthenticated() {
		return conectat;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.conectat = isAuthenticated;
	}

	public String getNumeClient() {
		return NumeClient;
	}

	public void setNumeClient(String numeClient) {
		NumeClient = numeClient;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isSelected() {
		return ales;
	}

	public void setSelected(boolean isSelected) {
		this.ales = isSelected;
	}
	
}
