package server;

import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {

	static ResourceBundle bundle = ResourceBundle.getBundle("settings");
	public static final String HOST = bundle.getString("host");
	public static final int PORT = Integer.parseInt(bundle.getString("port"));

	public static void main(String[] args) {
		try (Server server = new Server()) {
			server.start(PORT);
			System.out.println("Server started. Type 'exit' to close");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					if (command == null || "exit".equals(command)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exit");
		} finally {
			System.exit(0);
		}
	}
}
