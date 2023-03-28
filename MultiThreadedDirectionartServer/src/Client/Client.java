package Client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;@

public class Client extends Thread {
	//private static int port = 7878;
	private static int port;
	
	//private static String address = "localhost";
	private static String address;
	private static Socket clientSocket = null;
	private static BufferedReader reader;
	private static BufferedWriter writer;

	// The message queue can protect thread safety in order to use synchronization
	// locks
	// (forwarding between the client and the server. The protection does not affect
	// each other in concurrency)
	public static LinkedBlockingQueue<String> reqQ = new LinkedBlockingQueue<String>(10);

	public static void main(String[] args) throws Exception {
		// Handle args from commend line.
		// handle inputs
		try {
			// the length of args are not 2.
			if (args.length != 2) {
				throw new Exception("Error: Wrong number of arguments。Need address and port as inputs");
			} else {
				// Handle the ip address
				try {
					address = args[0];
				} catch (Exception e) {
					// main exceptions
					System.out.println("Invalid address");

				}
				if (!address.equals("localhost")) {
					System.out.println(" not the localhost ip address");

				}
				// Handle the port numbers
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid port number format: " + e.getMessage());
					// System.exit(0);
				} catch (Exception e2) {
					// main exceptions
					System.out.println(" main exceptions(inputs part)");
					// System.exit(-1);
				}
				// Handle the value of port is not in the normal port range.
				if (port <= 1024 || port > 65535) {
					System.out
							.println("Error: port number out of range, need Port number to be between 1024 and 65535.");

				}
			}
		} catch (Exception e) {
			System.out.println("(inputs of client)Exception:" + e.getMessage());
			System.out.println("Client Start By Default");
		}

		// A client click to open a client and UI interface
		// start the client
		Client client = new Client();
		client.start();

		// Start the GUI.
		System.out.println("start the GUI");
		UiShow ui = new UiShow();
		ui.start();
	}

	@Override
	public void run() {
		// Handle the connection to Server from clients.
		// Handle connection to servers
		try {
			//thread is not interrupted
			while (!isInterrupted()) {
				// create a new socket to connect to the server
				System.out.println("start to connect the server");
				clientSocket = new Socket(address, port);

				// create reader and writer to communicate with the server
				reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
				// read requests from the request queue and send them to the server
				String reqString = null;
				while ((reqString = reqQ.take()) != null) {
					writer.write(reqString + "\n");
					writer.flush();
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("Error: Invalid hostname for server, please try other inputs(address+port).");

		} catch (ConnectException e2) {

			System.out.println("Error: Connection refused, please try it later or try other inputs(address+port).");

		} catch (SocketException e3) {
			// handle exceptions related to socket problems

			System.out.println("Error: Socket error occurred, please try again later.");

		} catch (IOException e4) {
			// handle general IO exceptions

			System.out.println("Error: IO error occurred, please try again later.");

		} catch (Exception e5) {
			System.out.println("Error: Main Exception handled" + e5.getMessage());

		}
	}

	// send msg should be managed by client if using msg queue
	public String getResponse() {
		String resMsq = null;

		try {
			// blocked method (timeout?? consider)
			System.out.println("start to get response from server");
			//  readLine should only once!
			resMsq = reader.readLine(); 
		} catch (IOException e) {

			System.out.println("Error: IOException occur in the client when getResponse from server");

		} catch (Exception e2) {
			System.out.println("Error: Exception occur in the client when getResponse from server");

		}

		return resMsq;
	}

}
