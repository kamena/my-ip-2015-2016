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
	
	private String login(String[] split, ArrayList<String> arraylist, Date dateNow, SimpleDateFormat dateFormat) {
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
	
	private String logout(String[] split, ArrayList<String> arraylist, Date dateNow, SimpleDateFormat dateFormat) {
		arraylist = hashmap.get(split[0]);

		if (hashmap.get(split[0]) == null) {
			return "error:notlogged";
		} else if (arraylist.size() % 2 != 0) {
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
			for (String line : arraylist) { str += ":" + line; }
	        if (arraylist.size() != 0) {
	        	return "ok:" + split[2] + ":true:" + ((arraylist.size()+1)/2) + str;
	        } else return "ok:" + split[2] + ":false:" + ((arraylist.size())/2) + str;
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
		
		SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyyMMdd'T'HH'_'mm'_'ss.SSSZ");
		Date dateNow = new Date(0);
		
		if ("login".equals(split[1]))
			return login(split, arraylist, dateNow, dateFormat);
		else if ("logout".equals(split[1]))	
			return logout(split, arraylist, dateNow, dateFormat);
		else if("info".equals(split[1])) 
			return info(split, arraylist);
		else if ("listavailable".equals(split[1]))
			return listavailable(arraylist);
		else if ("listabsen".equals(split[1]))
			return listabsen(arraylist);
		else if ("shutdown".equals(split[1])) 
			echoServer.stopServer();
		else return "error:unknowncommand";
//		return "ok";
		return null;
	}
}
