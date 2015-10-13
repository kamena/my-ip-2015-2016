package org.elsysbg.ip.java;

public class ClassExample {
	public static void main(String[] args) {
		// for local variable - Ctrl+2 -> let go then L
		final Room room = new Room();
		
		room.setHeight(12);
		room.setWidth(10);

		final int height = room.getHeight();
		
		System.out.println(height);
		System.out.println(room.getWidth());
		System.out.println(room.calculateArea());
	}

}
