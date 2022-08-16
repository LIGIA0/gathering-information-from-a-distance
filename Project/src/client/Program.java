package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {
	
	static ResourceBundle bundle = ResourceBundle.getBundle("settings");
	public static final String HOST= bundle.getString("host");
	public static final int  PORT = Integer.parseInt(bundle.getString("port"));
	
	public static void main(String[] args) {
		try (Client client = new Client(HOST, PORT, message -> {
			System.out.println(message);
		})) {
			System.out.println("Client connected. Type 'exit' to stop it");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					client.send(command);
					if (command == null || "exit".equals(command)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
