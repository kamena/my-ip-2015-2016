package ip_availability;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler implements Runnable {

	final static HashMap<String, ArrayList<String>> hashmap = 
	        new HashMap<String, ArrayList<String>>();
	private final Socket socket;

	private final EchoServer echoServer;

	public ClientHandler(EchoServer echoServer, Socket socket) {
		this.socket = socket;
		this.echoServer = echoServer;
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
				final String[] split = line.split(":");	  
				ArrayList<String> arraylist = new ArrayList<String>();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyyMMdd'T'HH'_'mm'_'ss.SSSZ");
				Date dateNow = new Date(0 );
				
				if ("login".equals(split[1])){
				    if (hashmap.get(split[0]) == null) {
				    	arraylist = new ArrayList<String>();
					    arraylist.add(dateFormat.format(dateNow));
					    hashmap.put(split[0], arraylist);
				    } else {
						arraylist = hashmap.get(split[0]);

				    	arraylist.add(dateFormat.format(dateNow));
					    hashmap.put(split[0], arraylist);
				    }
				    out.println("ok");
				} else
				if ("logout".equals(split[1])){	
					arraylist = hashmap.get(split[0]);

					if (hashmap.get(split[0]) == null) {
						out.println("error:notlogged:logout:1");
					} else if (arraylist.size() % 2 != 0) {
					    arraylist.add(dateFormat.format(dateNow));
					    hashmap.put(split[0], arraylist);
					    out.println("ok");
					} else out.println("error:notlogged:logout:2");

				} else
				
				if("info".equals(split[1])) {
					Object value = hashmap.get(split[2]);
					ArrayList<String> isOnline = hashmap.get(split[0]);
					if ((value != null) && (isOnline != null) && (isOnline.size() % 2 != 0)) {
						arraylist = hashmap.get(split[2]);
						String str = "";
						for (String date : arraylist) { str = str + date + ":"; }
				        if (arraylist.size() % 2 != 0) {
				        	out.println("ok:" + split[2] + ":true:" + ((arraylist.size()+1)/2) + str);
				        } else out.println("ok:" + split[2] + ":false:" + ((arraylist.size())/2) + str);
					} else out.println("error:notlogged:info");
				} else
				
				if ("shutdown".equals(split[1])) {
					echoServer.stopServer();
				} else out.println("error:unknowncommand:shutdown");
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
