import java.util.*;

public class InOutCommand {
	static int i = 0;
	final static HashMap<String, ArrayList<String>> hashmap = 
	        new HashMap<String, ArrayList<String>>();
	//static ArrayList<String> arraylist = new ArrayList<String>();
	public static void main(String[] args) {
		final Scanner in = new Scanner(System.in);
		
		while(i != 3) {
			System.out.print("Enter command: ");
			
			final String command = in.next();			
			final String result = execute(command);

			System.out.println(result);	
		}
		in.close();
	}

	private static String execute(String command) throws IllegalArgumentException {
		final String[] split = command.split(":");	  
		ArrayList<String> arraylist = new ArrayList<String>();
		if ("login".equals(split[1])){
		    if (hashmap.get(split[0]) == null) {
		    	arraylist = new ArrayList<String>();
			    arraylist.add("1");
			    hashmap.put(split[0], arraylist);
		    } else {
				arraylist = hashmap.get(split[0]);

		    	arraylist.add("1");
			    hashmap.put(split[0], arraylist);
		    }
		    return "ok";
		} else
		if ("logout".equals(split[1])){	
			arraylist = hashmap.get(split[0]);

			if (hashmap.get(split[0]) == null) {
				return "error:notlogged";
			} else if (arraylist.get(arraylist.size()-1) == "1") {
			    arraylist.add("0");
			    hashmap.put(split[0], arraylist);
			    return "ok";
			} return "error:notlogged";

		} else
		
		if("info".equals(split[1])) {
			Object value = hashmap.get(split[2]);
			ArrayList<String> isOnline = hashmap.get(split[0]);
			if ((value != null) && (isOnline != null) && (isOnline.get(isOnline.size()-1) == "1")) {
				arraylist = hashmap.get(split[2]);
//		        System.out.println(arraylist);
		        if (arraylist.get(arraylist.size()-1) == "1") {
		        	return "ok:" + split[2] + ":true:" + ((arraylist.size()+1)/2);
		        } else return "ok:" + split[2] + ":false:" + ((arraylist.size())/2);
			} else return "error:notlogged";
		} else
		
		if ("listavailable".equals(split[1])){
			for (Object key : hashmap.keySet()) {
				arraylist = hashmap.get(key);
				if (arraylist.get(arraylist.size()-1) == "1") {
					System.out.println(key);
				}
			}
		} else 
		
		if ("shutdown".equals(split[1])) {
			i = 3;
		} else return "error:unknowncommand";
		return "ok";
	}
}