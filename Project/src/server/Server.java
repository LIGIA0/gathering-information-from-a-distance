package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket;
	private ExecutorService executorService;

	private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<ClientHandler>());

	@Override
	public void close() throws Exception {
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	public void start(int port) throws IOException  {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(100 * Runtime.getRuntime().availableProcessors());

		executorService.execute(() -> {

			try {
				while (!serverSocket.isClosed()) {
					ClientHandler client = new ClientHandler("null", serverSocket.accept());
					executorService.execute(() -> {
						try {
							
							clients.add(client);

							while (client != null && !client.socket.isClosed()) {

								String request = Transport.receive(client.socket);
								String response = procesare(request, client);

								System.out.println(client.NumeClient + " -> " + response);
								Transport.send(response, client.socket);
							}
						} catch (Exception e) {
							System.out.println("");
						} finally {
							clients.remove(client);
						}

					});
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("");
			}

		});
	}

	private String procesare(String request, ClientHandler ch) throws Exception {
		String[] command = request.strip().split("\\s");
		if (ch.conectat) {
			if (!ch.ales) {
				if (command[0].equals("query-wmi") && command[1].equals("exit")) {
					Transport.send("Please waiting...", ch.socket);
					for (ClientHandler c : clients) {
						if (!ch.NumeClient.equals(c.NumeClient)) {
							c.ales = true;
							Transport.send(ch.NumeClient + " are leaving the server. Send an ok to the server", c.socket);
						}
					}

					int flag = 0;
					while (flag == 0) {

						if (flag == 1) {
							flag = 0;
							Transport.send("Server disconnected!", ch.socket);
							ch.socket.close();
						}
					}

				}
				if (command[0].equals("query-wmi") && command[1].equals("send-request") && command[2].equals("for")) {
					for (int i = 3; i <= command.length; i++) {
						for (ClientHandler c : clients) {
							if (command[i].equals(c.NumeClient)) {
								c.ales = true;
								Transport.send("You was selected for the request. Please confirm with a specific command", c.socket);
							}
						}
					}
					return "I sent the request for users";
				} else if (command[0].equals("query-wmi") && command[1].equals("display") && command[2].equals("users")) {
					List<String> name = new ArrayList<String>();
					if (command.length == 3)
						for (ClientHandler c : clients) {
							name.add(c.NumeClient);
						}
					System.out.println("Online users:");
					for (ClientHandler c : clients) 
					 Transport.send(c.NumeClient,ch.socket);
					 return "The list of users - WMI query ";
					
					
				} else {
					return "Please use a specific WMI query !";
				}

			} else {
				if (command.length == 2) {
					if (command[0].equals(ch.NumeClient)) {
						if (command[1].equals("press-yes")) {
							ch.ales = false;
							return ("Thanks for confirmming!");
						} else {
							return "I don't know who you are";
						}
					} else {
						return "Please use the following form :[nume_client] press-yes";
					}
				}
				return "Something went wrong";
			}
		} else {
			if (command[0].equals("connect")) {
				if (command.length == 3) {
					if (ch.socket.getInetAddress().toString().contains(command[1])) {
						ch.NumeClient = command[2];
						ch.conectat = true;
						for (ClientHandler c : clients)
							if (!c.NumeClient.equals(ch.NumeClient))
								Transport.send(ch.NumeClient+"is online", c.socket);

						return "Hello "+ch.NumeClient;

					} else {
						return "IP Address is incorrect!";
					}
				}
			}
			return "Please try again!";
		}
	
	}
}
