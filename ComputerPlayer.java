package clueGame;

import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	private String lastRoom;
	private ArrayList<Card> seen;
	private String currentRoom;
	
	public Card suggestPerson() {
		return null;
	}
	
	public Card suggestRoom() {
		return null;
	}
	
	public Card suggestWeapon() {
		return null;
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		return null;
	}

	public String getLastRoom() {
		return lastRoom;
	}

	public void setLastRoom(String lastRoom) {
		this.lastRoom = lastRoom;
	}

	public ArrayList<Card> getSeen() {
		return seen;
	}

	public void setSeen(ArrayList<Card> seen) {
		this.seen = seen;
	}

	public String getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(String currentRoom) {
		this.currentRoom = currentRoom;
	}

}
