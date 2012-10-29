package clueGame;

import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	private char lastRoom;
	private ArrayList<Card> seen;
	private String currentRoom;
	
	public ComputerPlayer() {
		super();
		seen = new ArrayList<Card>();
	}
	
	public ComputerPlayer(ArrayList<Card> d) {
		super(d);
		seen = new ArrayList<Card>();
	}

	public void setCard(Card card) {
		super.setCard(card);
		setSeenCard(card);
	}
	
	
	public void setSeenCard(Card card) {
		seen.add(card);
	}
	
	public Card suggestPerson() {
		for(Card card:deck) {
			if (card.getType() == Card.Type.PERSON && seen.contains(card) == false)
				return card;
		}
		return null;
	}
	
	public Card suggestRoom() {
		Card c = new Card(currentRoom, Card.Type.ROOM);
		return c;
	}
	
	public Card suggestWeapon() {
		for(Card card:deck) {
			if (card.getType() == Card.Type.WEAPON && seen.contains(card) == false)
				return card;
		}
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
