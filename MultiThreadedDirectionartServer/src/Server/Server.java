package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread {
	private static Server server;
	//static int port = 7878;
	static int port;
	static ServerSocket serverSocket;
	//static String dictPath = "src/dictionary.txt";
	static String dictPath;
	// The thread-safety hashmap: key,value pair <word,meaning> init!
	public static ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<>(); //
	private static int clientCount = 0;

	public static void main(String[] args) throws Exception {
		// the length of args are not 2.
		if (args.length != 2) {
			System.out.println(
					"Error: Wrong number of arguments。need two arguments Port number and Path of dictionary file(txt)");
			System.out.println("Server Start By Default"); // test start with default is better
		} else {
			// Handle the types of args are right.
			try {
				// convert the inputs to port and dictionary file path
				port = Integer.parseInt(args[0]);
				dictPath = args[1];

				// Handle the value of port is not in the normal port range.
				if (port <= 1024 || port > 65535) {
					System.out
							.println("Error: port number out of range, need Port number to be between 1024 and 65535.");
					System.exit(0);
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid port number format, can not conve" + e.getMessage());

			} catch (Exception e2) {
				System.out.println("Invalid port number" + e2.getMessage());

			}
		}

		//
		server = new Server();
		// read the dictionary file
		readDictionary();

		// start the server thread
		server.start();

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			// server socket

			Socket clientSocket = null;
			System.out.println("start to build connection");
			// keep listen for the upcoming thread
			while (true) {
				System.out.println("waiting the client to connect ...");
				
				clientSocket = serverSocket.accept();
				// client count numbers
				clientCount++;
				System.out.println("Client " + clientCount + ": is Applying for connection.");
				System.out.println("Connected!");

				// assign to a new thread(per connection) multi-thread
				// muti-thread
				Connections clientConnection = new Connections(clientSocket);
				// start the thread
				// thread per connection
				clientConnection.start();
			}
		} catch (SocketException e) {
			System.err.println("SocketException: " + e.getMessage());
			System.err.println("Trying to restart the server...");

		} catch (IllegalArgumentException e2) {
			System.out.println(
					"Error: IllegalArgumentException occur when try to accept the client thread" + e2.getMessage());
			System.out.println("Please enter the correct arguments: Port number and Path of dictionary file(txt)");

		} catch (IOException e3) {
			System.out.println("Error: error accepting client connection:);" + e3.getMessage());

		} catch (Exception e4) {
			System.err.println("Error: Exception occurred cover when client connection: " + e4.getMessage());

		}
	}

	@Override
	public void run() {
		// auto close,create the server socket

	}

	// read file dictionary
	// read from disk into memory
	public static void readDictionary() {
		try {
			// create the file according to the path
			File readDic = new File(dictPath);
			// convention check
			if (readDic.isFile() && readDic.exists()) {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(readDic), "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String line = null;

				// keep read the line
				while ((line = br.readLine()) != null) {
					// split :
					String[] lines = line.split(":");
					dictionary.put(lines[0], lines[1]);
				}
				System.out.println("succesfully read");
				br.close();
				isr.close();
			} else {
				System.out.println("dictionary not found");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: Dictionary file not found." + e.getMessage());

		} catch (UnsupportedEncodingException e2) {
			System.out.println("Error: Unsupported encoding format." + e2.getMessage());

		} catch (IOException e3) {
			System.out.println("Error: IO exception occurred while reading the dictionary file." + e3.getMessage());

		} catch (Exception e4) {
			System.out.println("read dict failed" + e4.getMessage());

		}
	}

	// write to dictionary
	// write from memory to disk
	public static void writeDictionary() {
		File readDic = new File(dictPath);
		BufferedWriter bw = null;
		try {
			if (!readDic.exists()) {
				readDic = new File(dictPath);
			}

			bw = new BufferedWriter(new FileWriter(readDic));
			// entry set
			for (Entry<String, String> ent : dictionary.entrySet()) {
				// !readline() ---> so add \n
				bw.write(ent.getKey() + ":" + ent.getValue() + "\n");

			}
		} catch (FileNotFoundException e) {
			System.err.println("Error: The specified file cannot be found." + e.getMessage());

		} catch (IOException e2) {
			System.err.println("Error: An IO Exception has occurred: " + e2.getMessage());

		} catch (Exception e3) {
			System.err.println("Error: An unexpected Exception has occurred: " + e3.getMessage());

		} finally {
			try {
				bw.flush();
				bw.close();
			} catch (IOException e) {
				System.err.println(
						"Error: An IO Exception has occurred while closing the file writer: " + e.getMessage());

			} catch (Exception e2) {
				System.err
						.println("Error: An Exception has occurred while closing the file writer: " + e2.getMessage());

			}
		}
	}

}