package clueGame;

public class RoomCell extends BoardCell{
	enum DoorDirection { LEFT, RIGHT, UP, DOWN, NONE }
	DoorDirection doorDirection;
	char roomInitial;
	public Boolean doorway = false;

	Boolean isRoom() {
		return true;
	}
	
	Boolean isDoorway() {
		return doorway;
	}
	
	public char getInitial() {
		return roomInitial;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
}
