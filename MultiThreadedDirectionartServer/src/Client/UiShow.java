package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UiShow extends Thread {

	private JFrame frame;
	private JTextField InputWord;
	private JTextField WordMeaning;

	Client client = new Client();

	/**
	 * Launch the application.
	 */
	@Override
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UiShow window = new UiShow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		initialize();

	}

	/**
	 * Create the application.
	 */
	public UiShow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(300, 300, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel UIName = new JLabel("The Client UI");
		UIName.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 22));
		UIName.setBounds(137, 6, 238, 34);
		frame.getContentPane().add(UIName);
		// set the JTextArea before the button ActionPerformed set
		JTextArea ResultShow = new JTextArea();
		ResultShow.setLineWrap(true);
		ResultShow.setEditable(false);
		ResultShow.setBounds(238, 76, 192, 123);
		frame.getContentPane().add(ResultShow);

		JButton SearchBtn = new JButton("Search");
		SearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// when you click the search button
				String word = InputWord.getText();
				String meaning = "search";

				// here to do the check conditions first
				if (word.isEmpty()) {
					// System.out.println("please input a word!");

					ResultShow.setText("please input a word!");
					return; //return if error
				}
				// search method do not need word meaning
				System.out.println("Non-empty inputs get!");

				// to do the next action (non-empty inputs)
				try {

					// !! Perform preprocessing in case there are separators in word or meaning
					word = word.replaceAll("@", "");
					meaning = meaning.replaceAll("@", "");

					// Use @ as a split, and there must be meaning (all lowercase for case-sensitive
					// distinction)
					String command = "SEARCH" + "@" + word.toLowerCase() + "@" + meaning;

					while (client.reqQ.size() == 0) {
						// When there is no content in the message queue, directly
						// write the content after clicking the button for convenient interaction
						try {
							client.reqQ.put(command);
						} catch (InterruptedException e) {
							System.out.println("InterruptedException" + e.getMessage());

						}
						break;

					}
				} catch (Exception e1) {
					System.out.println("Error: Exception occur when click the search  button");
					// System.exit(-1);
				}

				String response = client.getResponse();
				System.out.println("response is = " + response);

				if (response == null) {
					ResultShow.setText("No any response from the server, no result can show!");
				} else if (response.equals("5")) {
					ResultShow.setText("no such word, can not search meaning of it !");
				} else if (response.equals("0")) {
					ResultShow.setText("Search word meaning successfully!");
					// accept again, this accept meaning
					response = client.getResponse() + ".";
					// 
					// This line of code is to replace all occurrences of "&" in a string with ";\n"
					ResultShow.setText(response.replaceAll("&", ";\n"));
				} else {
					ResultShow.setText("Search word failed!");
				}

			}
		});
		SearchBtn.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		SearchBtn.setBounds(6, 211, 92, 29);
		frame.getContentPane().add(SearchBtn);

		JButton AddBtn = new JButton("Add new");
		AddBtn.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		AddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Here is the runnable code block when you click the add button
				String word = InputWord.getText();
				String meaning = WordMeaning.getText();
				// here to do the check conditions first
				if (word.isEmpty()) {
					// System.out.println("please input a word!");

					ResultShow.setText("please input a word!");
					return; //  return if error
				} else if (meaning.isEmpty()) {
					// System.out.println("please input a word meaning!");

					ResultShow.setText("please input a word meaning!");
					return; // return if error
				}
				System.out.println("Non-empty inputs get!");

				// to do the next action (non-empty inputs)
				try {

					// !Perform preprocessing in case there are separators in word or meaning agin
					word = word.replaceAll("@", "");
					meaning = meaning.replaceAll("@", "");

					// Use @ as a split, and there must be meaning (all lowercase for case-sensitive
					// distinction)
					String command = "ADD" + "@" + word.toLowerCase() + "@" + meaning;

					while (client.reqQ.size() == 0) {

						client.reqQ.put(command);
						System.out.println("command is = " + command);
						break;
					}
				} catch (Exception e1) {
					System.out.println("Error: Exception occur when click the add new button" + e1.getMessage());
				}

				String response = client.getResponse();
				System.out.println("response is = " + response);

				if (response == null) {
					ResultShow.setText("You are disconnected from the server!");
				} else if (response.equals("5")) {
					ResultShow.setText("Duplicated word, can not add it !");
				} else if (response.equals("0")) {
					ResultShow.setText("Added new word successfully!");
				} else {
					ResultShow.setText("Added new word failed!");
				}
			}
		});
		AddBtn.setBounds(104, 211, 107, 29);
		frame.getContentPane().add(AddBtn);

		JButton RemoveBtn = new JButton("Remove");
		RemoveBtn.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		RemoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// : no implementation for remove
				String word = InputWord.getText();
				String meaning = "remove";
				if (word.isEmpty()) {
					ResultShow.setText("please input a word!");
					return;
				}
				//
				word = word.replaceAll("@", "");
				meaning = meaning.replaceAll("@", "");
				try {
					String command = "REMOVE" + "@" + word.toLowerCase() + "@" + meaning;
					while (client.reqQ.size() == 0) {
						//
						client.reqQ.put(command);
						System.out.println("command is = " + command);
						break;
					}
				} catch (Exception e1) {
					System.out.println("Error: Exception occur when click the add new button" + e1.getMessage());
				}

				String response = client.getResponse();
				System.out.println("response is = " + response);

				if (response == null) {
					ResultShow.setText("You are disconnected from the server!");
				} else if (response.equals("5")) {
					ResultShow.setText("word not exist!");
				} else if (response.equals("0")) {
					ResultShow.setText("Remove successfully!");
				} else {
					ResultShow.setText("Remove failed!");
				}
			}
		});
		RemoveBtn.setBounds(212, 211, 107, 29);
		frame.getContentPane().add(RemoveBtn);

		JButton UpdateBtn = new JButton("Update");
		UpdateBtn.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		UpdateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// o implementation for update
				String word = InputWord.getText();
				String meaning = WordMeaning.getText();
				// here to do the check conditions first
				if (word.isEmpty() || meaning.isEmpty()) {
					ResultShow.setText("please input a word and meaning!");
					return;
				}
				System.out.println("Non-empty inputs get!");
				//
				word = word.replaceAll("@", "");
				meaning = meaning.replaceAll("@", "");
				// to do the next action (non-empty inputs)
				try {
					String command = "UPDATE" + "@" + word.toLowerCase() + "@" + meaning;

					while (client.reqQ.size() == 0) {
						//
						client.reqQ.put(command);
						System.out.println("command is = " + command);
						break;
					}
				} catch (Exception e1) {
					System.out.println("Error: Exception occur when click the add new button" + e1.getMessage());
				}

				String response = client.getResponse();
				System.out.println("response is = " + response);

				if (response == null) {
					ResultShow.setText("You are disconnected from the server!");
				} else if (response.equals("5")) {
					ResultShow.setText("word not exist!");
				} else if (response.equals("0")) {
					ResultShow.setText("Update successfully!");
				} else {
					ResultShow.setText("Update failed!");
				}
			}
		});
		UpdateBtn.setBounds(327, 211, 117, 29);
		frame.getContentPane().add(UpdateBtn);

		// 

		InputWord = new JTextField();
		InputWord.setBounds(26, 71, 130, 26);
		frame.getContentPane().add(InputWord);
		InputWord.setColumns(10);

		WordMeaning = new JTextField();
		WordMeaning.setBounds(26, 134, 130, 26);
		frame.getContentPane().add(WordMeaning);
		WordMeaning.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("The Input Word");
		lblNewLabel_1.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		lblNewLabel_1.setBounds(26, 52, 130, 16);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("The Word Meaning");
		lblNewLabel_2.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		lblNewLabel_2.setBounds(26, 116, 147, 16);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel Show = new JLabel("         Result(Status)");
		Show.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 13));
		Show.setBounds(248, 52, 172, 16);
		frame.getContentPane().add(Show);

		JLabel lblNewLabel = new JLabel("(Use & to separate)");
		lblNewLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		lblNewLabel.setBounds(36, 172, 161, 16);
		frame.getContentPane().add(lblNewLabel);
	}
}
