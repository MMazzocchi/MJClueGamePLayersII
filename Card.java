package clueGame;

public class Card {
	
	public enum Type {PERSON, WEAPON, ROOM};
	
	private String name;
	private Type type;
	
	public Card() {
		
	}
	
	public Card(String n, Type t) {
		name = n;
		type = t;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean equals(Object o) {
		return equals((Card)o);
	}
	
	public boolean equals(Card c) {
		return ((c.getName().equals(name)) && (c.getType() == type));
	}
	
	public String toString() {
		return name +", " + type;
	}
}
