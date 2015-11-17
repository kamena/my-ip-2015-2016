package ip_availability;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler implements Runnable {

	final static HashMap<String, ArrayList<String>> hashmap = 
	        new HashMap<String, ArrayList<String>>();
	private final Socket socket;

	private final EchoServer echoServer;
	private final CommandHandler CommandHandler;

	public ClientHandler(EchoServer echoServer, Socket socket) {
		this.socket = socket;
		this.echoServer = echoServer;
		this.CommandHandler = new CommandHandler(socket);
	}
	
	@Override
	public void run() {
		try {
			final PrintStream out = 
				new PrintStream(socket.getOutputStream());
			final Scanner scanner =
				new Scanner(socket.getInputStream());
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				out.println(CommandHandler.executeCommands(CommandHandler.SplitCommand(line), echoServer));
			}
			scanner.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			echoServer.onClientStopped(this);
		}
	}
	
	public void stopClient() throws IOException {
		socket.close();
	}
}
