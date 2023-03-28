package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Connections extends Thread {
	Socket s;
	BufferedReader reader;
	BufferedWriter writer;

	// create a connection based on the socket given
	// Constructor run first
	public Connections(Socket socket) throws Exception {
		this.s = socket;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
			reader = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Error: UnsupportedEncodingException" + e1.getMessage());
		} catch (IOException e2) {
			System.out.println("Error: IOException" + e2.getMessage());
		} catch (NullPointerException e3) {
			System.out.println("Error: Socket has not been initialized or is already closed." + e3.getMessage());
		} catch (Exception e4) {
			System.out.println("Error: Exception" + e4.getMessage());
			// System.exit(-1);
		}

	}

	@Override
	public void run() {
		// In order to accept the message queue transmitted by the client
		String request;
		try {
			// When the message queue transmitted by the string has something, it will start
			// to split (execute logic)
			while ((request = reader.readLine()) != null) {
				System.out.println(request);
				// use @ to split the command
				String[] divided = request.split("@"); // err
				String action = divided[0];
				String word = divided[1];
				String meaning = divided[2];
				// skip to check divided length part controlled by client request
				System.out.println("action is " + action);
				System.out.println("word is " + word);
				System.out.println("meaning is " + meaning);

				switch (action) {

				// 1，The operation of adding words
				case "ADD":// Find word exists in the dictionary
					if (Server.dictionary.containsKey(word)) {
						// can do the action 1
						writer.write("5\n");
						writer.flush();
					} else {
						//
						writer.write("0\n");
						writer.flush();
						// no word then add into
						Server.dictionary.put(word, meaning);
					}
					Server.writeDictionary();
					break;

				// 2find the word meaning
				case "SEARCH"://
					if (Server.dictionary.containsKey(word)) {
						//
						System.out.println("here");
						writer.write("0\n");
						writer.flush();
						String definition = Server.dictionary.get(word);
						writer.write(definition + "\n");
						writer.flush();
						System.out.println("the word meaning is " + definition);
					} else {
						//
						writer.write("5\n");
						writer.flush();
					}
					break;

				// 3.delete
				case "REMOVE"://
					if (Server.dictionary.containsKey(word)) {
						// can remove
						Server.dictionary.remove(word);
						writer.write("0\n");
						writer.flush();

						System.out.println("delete successfuly！");
					} else {
						// can not remove
						writer.write("5\n");
						writer.flush();
					}
					Server.writeDictionary();
					break;

				// 4.update
				case "UPDATE":
					if (Server.dictionary.containsKey(word)) {
						//
						Server.dictionary.put(word, meaning);
						writer.write("0\n");
						writer.flush();
						System.out.println("update successfuly！");
					} else {
						//
						writer.write("5\n");
						writer.flush();
					}
					Server.writeDictionary();
					break;
				default:
					System.out.println("undepexted command here!");
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e3) {
			System.out.println("Error:ArrayIndexOutOfBoundsException " + e3.getMessage());
		} catch (SocketException e) {
			System.out.println("Error: SocketException occurred when read from socket. " + e.getMessage());
		} catch (IOException e1) {
			System.out.println("Error: IOException  when use method" + e1.getMessage());
		} catch (Exception e2) {
			System.out.println("Error: Exception when use method" + e2.getMessage());
		}
	}
}
