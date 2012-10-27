package clueGame;

public abstract class BoardCell {
	
	
	Boolean isWalkway() {
		return false;
	}
	
	Boolean isDoorway() {
		return false;
	}
	
	Boolean isRoom() {
		return false;
	}
	
	Integer row;
	Integer col;
	
	public String toString() {
		return ("("+row+","+col+")");
	}
	
	public boolean equals(BoardCell b) {
		return ((b.row==row) && (b.col==col));
	}
	
}
