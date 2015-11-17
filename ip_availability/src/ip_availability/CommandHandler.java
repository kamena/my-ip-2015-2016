package ip_availability;

import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	private String login(String[] split, ArrayList<String> arraylist, Date dateNow, DateFormat dateFormat) {
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
	}
	
	private String logout(String[] split, ArrayList<String> arraylist, Date dateNow, DateFormat dateFormat) {
		arraylist = hashmap.get(split[0]);

		if (hashmap.get(split[0]) == null) 
			return "error:notlogged";
		else if (arraylist.size() % 2 != 0) {
		    arraylist.add(dateFormat.format(dateNow));
		    hashmap.put(split[0], arraylist);
		    return "ok";
		} return "error:notlogged";
	}
	
	private String info(String[] split, ArrayList<String> arraylist) {	
		Object value = hashmap.get(split[2]);
		ArrayList<String> isOnline = hashmap.get(split[0]);
		if ((value != null) && (isOnline != null) && (isOnline.size() % 2 != 0)) {
			arraylist = hashmap.get(split[2]);
			String str = "";
			for (String line : arraylist) str += ":" + line;
	        if (arraylist.size() % 2 != 0)
	        	return "ok:" + split[2] + ":true:" + ((arraylist.size()+1)/2) + str;
	        else return "ok:" + split[2] + ":false:" + ((arraylist.size())/2) + str;
		} else return "error:notlogged";
	}
	
	private String listavailable(ArrayList<String> arraylist) {	
		String listavailable = "ok";
		for (Object name : hashmap.keySet()) {
			arraylist = hashmap.get(name);
			if (arraylist.size() % 2 != 0) {
				listavailable += ":" + name ;
			}
		}
		return listavailable;
	}
	
	private String listabsen(ArrayList<String> arraylist) {	
		String listabsen = "ok";
		for (Object name : hashmap.keySet()) {
			arraylist = hashmap.get(name);
			if (arraylist.size() % 2 == 0) {
				listabsen+= ":" + name ;
			}
		}	
		return listabsen;
	}	
	
	public String executeCommands(String[] split, EchoServer echoServer) throws IllegalArgumentException, IOException {	  
		ArrayList<String> arraylist = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH_mm_ss.SSSZ");
		Date dateNow = new Date();
		
		if(split.length < 2) {
			switch(split[0]) {
				case "listavailable": return listavailable(arraylist);
				case "listabsen": return listabsen(arraylist);
				case "shutdown": echoServer.stopServer();
				default: return "error:unknowncommand";
			}
		} else {
			switch(split[1]) {
				case "login": return login(split, arraylist, dateNow, dateFormat);
				case "logout": return logout(split, arraylist, dateNow, dateFormat);
				case "info": return info(split, arraylist);
				case "listavailable": return listavailable(arraylist);
				case "listabsen": return listabsen(arraylist);
				case "shutdown": echoServer.stopServer();
				default: return "error:unknowncommand";
			}
		}
	}
}
