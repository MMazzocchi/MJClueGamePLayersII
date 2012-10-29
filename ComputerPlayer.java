package clueGame;

import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	private char lastRoom;
	private ArrayList<Card> seen;
	private String currentRoom;
	
	public ComputerPlayer() {
		super();
	}
	
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
		for(BoardCell b : targets) {
		//	Board.p("Cell "+b+" is doorway: "+b.isDoorway());
			if(b.isDoorway() && ((RoomCell)b).getInitial()!=lastRoom) {
				return b;
			}
		}
		
		Object[] bc = targets.toArray();
		
		int i = (int)(Math.random()*bc.length);
		
		BoardCell b = (BoardCell)bc[i];
		return b;
	}

	public char getLastRoom() {
		return lastRoom;
	}

	public void setLastRoom(char lastRoom) {
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
