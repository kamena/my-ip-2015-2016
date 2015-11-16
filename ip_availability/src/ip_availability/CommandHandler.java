package ip_availability;

import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandHandler {
	final static HashMap<String, ArrayList<String>> hashmap = 
			new HashMap<String, ArrayList<String>>();
	
	private Socket socket;
	
	public CommandHandler(Socket socket) {
		this.socket = socket;
	}
	
	public String[] SplitCommand(String string) {
		String[] split = string.split(":");
		return split;
	}
	
	public String executeCommands(String[] split, EchoServer echoServer) throws IllegalArgumentException, IOException {	  
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
		    return "ok";
		} else
		if ("logout".equals(split[1])){	
			arraylist = hashmap.get(split[0]);

			if (hashmap.get(split[0]) == null) {
				return "error:notlogged";
			} else if (arraylist.size() % 2 != 0) {
			    arraylist.add(dateFormat.format(dateNow));
			    hashmap.put(split[0], arraylist);
			    return "ok";
			} return "error:notlogged";

		} else
		
		if("info".equals(split[1])) {
			Object value = hashmap.get(split[2]);
			ArrayList<String> isOnline = hashmap.get(split[0]);
			if ((value != null) && (isOnline != null) && (isOnline.size() % 2 != 0)) {
				arraylist = hashmap.get(split[2]);
				String str = "";
				for (String line : arraylist) { str = str + line + ":"; }
		        if (arraylist.size() != 0) {
		        	return "ok:" + split[2] + ":true:" + ((arraylist.size()+1)/2) + str;
		        } else return "ok:" + split[2] + ":false:" + ((arraylist.size())/2) + str;
			} else return "error:notlogged";
		} else
		
		if ("listavailable".equals(split[1])){
			for (Object key : hashmap.keySet()) {
				arraylist = hashmap.get(key);
				if (arraylist.size() % 2 != 0) {
					System.out.println(key);
				}
			}
		} else 
		if ("listabsen".equals(split[1])){
			System.out.print("ok:");
			for (Object key : hashmap.keySet()) {
				arraylist = hashmap.get(key);
				if (arraylist.size() % 2 == 0) {
					System.out.print(key);
					System.out.print(":");
				}
			}	
			System.out.println();
		} else
		
		if ("shutdown".equals(split[1])) {
			echoServer.stopServer();
		} else return "error:unknowncommand";
		return "ok";
	}
}
