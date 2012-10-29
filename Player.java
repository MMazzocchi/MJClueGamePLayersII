package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public class Player {
	
	protected ArrayList<Card> deck;
	protected String name;
	protected ArrayList<Card> cards;
	protected int location;
	protected Color color;
	
	public Player() {
		cards = new ArrayList<Card>();
	}
	
	public Player(ArrayList<Card> d) {
		cards = new ArrayList<Card>();
		deck = d;
	}

	public void setCard(Card card){
		cards.add(card);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public String toString() {
		return name;
	}

}
