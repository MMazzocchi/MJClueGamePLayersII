package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import clueGame.RoomCell.DoorDirection;

public class Board {
	ArrayList<BoardCell> cells ;
	Map<Character, String> rooms;
	int numRows = 0, numCols = 0;

	Map<Integer, LinkedList<Integer>> adjMtx;
	Set<BoardCell> targets;
	
	ArrayList<Player> compPlayers;
	HumanPlayer human;
	Player current;
	ArrayList<Card> deck;
	
	Card solWeapon;
	Card solPerson;
	Card solRoom;
	
	Random randomGenerator = new Random();
	ArrayList<Player> totalPlayers = new ArrayList<Player>();
	
	
	public Board() {
				
		this.cells = new ArrayList<BoardCell>();
		this.rooms = new TreeMap<Character, String>();
		this.adjMtx = new TreeMap<Integer, LinkedList<Integer>>();
		this.targets = new HashSet<BoardCell>();
		
		loadConfigFiles();
		calcAdjacencies();
		
		this.compPlayers = loadPlayers();
		this.deck = loadDeck();
	}
	
	private ArrayList<Player> loadPlayers() {
		ArrayList<Player> newPlayers = new ArrayList<Player>();

		try {
			calcNumCols();
			FileReader reader = new FileReader("PlayerStats.txt");
			Scanner in = new Scanner(reader);
			String delimiter = ",";
			int count = 0;

			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] temp = line.split(delimiter);
				Player p = null;
				Color c = null;
				String cName = temp[1].toLowerCase().trim();

				if(cName.equals("purple")) {
					c = new Color(255,0,255);
				} else if (cName.equals("green")) {
					c = Color.GREEN;
				} else if (cName.equals("red")) {
					c = Color.RED;
				} else if (cName.equals("blue")) {
					c = Color.BLUE;
				} else if (cName.equals("white")) {
					c = Color.WHITE;
				} else if (cName.equals("green")) {
					c = Color.YELLOW;
				}

				if(count==0) {
					p = new HumanPlayer();
				} else {
					p = new ComputerPlayer();
				}

				p.setName(temp[0]);
				p.setColor(c);
				p.setLocation(Integer.valueOf(temp[2].trim()));

				if(count==0) {
					this.human = (HumanPlayer)p;
				} else {
					newPlayers.add(p);
				}

				count++;
			}
			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("PlayerStats.txt cannot be found. Please add PlayerStats.txt to the list and try again.");
		}

		return newPlayers;
	}
	public Card suggest(Card p, Card r, Card w) {
		ArrayList<Player> tempPlayers = (ArrayList<Player>) totalPlayers.clone();
		while (tempPlayers.size() > 0){
			int index = randomGenerator.nextInt(totalPlayers.size());
			Player player = tempPlayers.get(index);
			if (player.getCards().contains(p) || player.getCards().contains(r) || player.getCards().contains(w)){
				if (player.getCards().contains(p))
					return p;
				else if (player.getCards().contains(w))
					return w;
				else
					return r;
			}
			tempPlayers.remove(index);
		}
		return null;
	}
	public boolean accuse(Card p, Card r, Card w) {
		if (p == getSolPerson() && r == getSolRoom() && w == getSolWeapon())
			return true;
		return false;
	}
	public void deal() {
		boolean weapon = false, room = false, person = false;
		// Setting the solutions
		while (weapon == false && room == false && person == false){
			int index = randomGenerator.nextInt(deck.size());
			Card item = deck.get(index);
			if (item.getType() == Card.Type.WEAPON && weapon == false){
				setSolWeapon(item);
				deck.remove(index);
				weapon = true;
			}
			else if(item.getType() == Card.Type.PERSON && person == false){
				setSolPerson(item);
				deck.remove(index);
				person = true;
			}
			else if(item.getType() == Card.Type.ROOM && room == false){
				setSolRoom(item);
				deck.remove(index);
				room = true;
			}
		}
		// Dealing the cards, yo!
		totalPlayers.addAll(compPlayers); // making an arrayList of all the players (thought it would be easier to cycle through players this way)
		totalPlayers.add(human);
		int arrayCounter = 0;
		while (deck.size() > 0){
			int index = randomGenerator.nextInt(deck.size());
			Card item = deck.get(index);
			if (arrayCounter == totalPlayers.size())
				arrayCounter = 0;
			totalPlayers.get(arrayCounter).setCard(item);
			deck.remove(index);
			arrayCounter++;
		}	   
	}
	
	public ArrayList<Card> loadDeck() {
		
		ArrayList<Card> newDeck = new ArrayList<Card>();
		
		try {
			calcNumCols();
			FileReader reader = new FileReader("DeckInit.txt");
			Scanner in = new Scanner(reader);
			String delimiter = ",";
			
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] temp = line.split(delimiter);
				Card c = null;
				
				if(temp.equals("W")) {
					c = new Card(temp[1], Card.Type.WEAPON);
				} else if(temp.equals("P")) {
					c = new Card(temp[1], Card.Type.PERSON);
				} else if(temp.equals("R")) {
					c = new Card(temp[1], Card.Type.ROOM);
				}
				
				newDeck.add(c);
			}
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("DeckInit.txt cannot be found. Please add DeckInit.txt to the list and try again.");
		}
		
		return newDeck;
	}

	public void loadConfigFiles() {
		loadLegend();
		fileToString();
	}
	
	public void fileToString() {
		try {
			calcNumCols();
			FileReader reader = new FileReader("ProfRaderConfigFile.txt");
			Scanner in = new Scanner(reader);
			String delimiter = ",";
			
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] temp = line.split(delimiter);
				for(int i = 0; i < temp.length; i++) {
					if(temp[i].equalsIgnoreCase("W")) {
						WalkwayCell walkway = new WalkwayCell();
						walkway.row = numRows;
						walkway.col = i;
						cells.add(walkway);
					} else {
						RoomCell room = new RoomCell();
						room.roomInitial = temp[i].charAt(0);
						if(temp[i].length() >= 2) {
							room.doorway = true;
							
							if(temp[i].endsWith("R")) {
								room.doorDirection = DoorDirection.RIGHT;
							} else if(temp[i].endsWith("L")) {
								room.doorDirection = DoorDirection.LEFT;
							} else if(temp[i].endsWith("U")) {
								room.doorDirection = DoorDirection.UP;
							} else if(temp[i].endsWith("D")) {
								room.doorDirection = DoorDirection.DOWN;
							}
						} else {
							room.doorDirection = DoorDirection.NONE;
							room.doorway = false;
						}
						room.row = numRows;
						room.col = i;
						cells.add(room);
					}
				}
				numRows++;
			}
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("ClueBoard3.csv cannot be found. Please add ClueBoard3.csv to the list and try again.");
		}
	}

	public void calcNumCols() {
		try {
			FileReader readerCols = new FileReader("ProfRaderConfigFile.txt");
			Scanner in = new Scanner(readerCols);
			String delimiter = ",";
			
			String line = in.nextLine();
			String[] temp = line.split(delimiter);
			numCols = temp.length;
			
		} catch (FileNotFoundException e) {
			System.out.println("ClueBoard3.csv cannot be found. Please add ClueBoard3.csv to the list and try again.");
		}
	}
	
	public void loadLegend() {
		try {
			FileReader reader = new FileReader("ProfRaderRoomLegend.txt");
			Scanner in = new Scanner(reader);
			String delimiter = ", ";
			String[] temp = null;
			
			char roomKey = 0;
			String roomName = null;
			
			while(in.hasNextLine()) {
				String line = in.nextLine();
				temp = line.split(delimiter);
				for(int i = 0; i < temp.length; i++) {
					if(i % 2 == 0) {
						char tempKey = temp[i].charAt(0);
						roomKey = tempKey;
					} else {
						roomName = temp[i];
					}
				}
				rooms.put(roomKey, roomName);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("ClueBoardKey.docx cannot be found. Please add ClueBoardKey.docx to the list and try again.");
		}
	}
	
	public int calcIndex(int row, int col) {
		return (col + (row * numCols));
	}
	
	public RoomCell getRoomCellAt(int row, int col) {
		int index = calcIndex(row, col);
		RoomCell room = new RoomCell();
		if(cells.get(index).isRoom() == true) {
			room = (RoomCell) cells.get(index);
		}
		return room;
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows - 1;
	}

	public int getNumCols() {
		return numCols - 1;
	}

	public BoardCell getCellAt(int i) {
		return cells.get(i);
	}

	public void calcAdjacencies() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				int key = calcIndex(i, j);
				LinkedList<Integer> values = new LinkedList<Integer>();
				if(getCellAt(key).isWalkway()) {
					if(i > 0) {
						if(getCellAt(calcIndex((i-1), j)).isWalkway() == true || getRoomCellAt((i - 1), j).getDoorDirection() == DoorDirection.DOWN) 
							values.add(calcIndex((i-1), j));
					}
					if(j > 0) {
						if(getCellAt(calcIndex(i, (j-1))).isWalkway() == true || getRoomCellAt(i, (j-1)).getDoorDirection() == DoorDirection.RIGHT)
							values.add(calcIndex(i, (j-1)));
					}
					if(i < (numRows - 1)) {
						if(getCellAt(calcIndex((i+1), j)).isWalkway() == true || getRoomCellAt((i+1), j).getDoorDirection() == DoorDirection.UP)
							values.add(calcIndex((i+1), j));
					}
					if(j < (numCols - 1)) {
						if(getCellAt(calcIndex(i, (j+1))).isWalkway() == true || getRoomCellAt(i, (j+1)).getDoorDirection() == DoorDirection.LEFT)
							values.add(calcIndex(i, (j+1)));
					}
				} else if(getRoomCellAt(i, j).isDoorway() == true) {
					if(getRoomCellAt(i, j).getDoorDirection() == DoorDirection.UP) {
						values.add(calcIndex((i - 1), j));
					}else if(getRoomCellAt(i, j).getDoorDirection() == DoorDirection.DOWN) {
						values.add(calcIndex((i + 1), j));
					}else if(getRoomCellAt(i, j).getDoorDirection() == DoorDirection.LEFT) {
						values.add(calcIndex(i, (j - 1)));
					}else if(getRoomCellAt(i, j).getDoorDirection() == DoorDirection.RIGHT) {
						values.add(calcIndex(i, (j + 1)));
					}
				}
				adjMtx.put(key, values);
			}
		}
	}
		
	
	public void calcTargets(int start, int steps) {
		//Clear the targets before beginning
		targets.clear();
		
		//Start calclating the targets with no visited rooms so far
		calcTargets2(start, steps, new ArrayList<Integer>());
	}
	
 
	public void calcTargets2(int start, int steps, ArrayList<Integer> visited) {

		//Add this room to the visited list so that we don't hit it again
		visited.add(start);
		if(steps > 1) {
			for(int i : adjMtx.get(start)) {
				
				//If we've seen this room before, don't consider it
				if(!visited.contains(i)) {

					//If this is a doorway, add it to the targets and don't mess with its adjMtx
					if(getCellAt(i).isDoorway()) {
						targets.add(getCellAt(i));
						continue;
					}
					
					/*
					 * Ok, so this is the really important/cool part. We make a NEW visited list.
					 * The new visited list is unique to ONLY THIS PATH. So it doesn't bother with
					 * any other paths that have been taken.
					 */
					ArrayList<Integer> newVisited = (ArrayList<Integer>) visited.clone();
					
					//Redo this process with this room, the new visited list, and a decremented counter
					calcTargets2(i, steps - 1, newVisited);
				}
			}
		}
		else if(steps == 1) {
			for(int i : adjMtx.get(start)) {
				BoardCell boardCell = getCellAt(i);
				
				//If this room has been visited, skip it
				if(!visited.contains(i))
					targets.add(boardCell);
			}
		}
	}
	
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public LinkedList<Integer> getAdjList(int cell) {
		LinkedList<Integer> temp = adjMtx.get(cell);
		return temp;
	}

	public ArrayList<Player> getCompPlayers() {
		return compPlayers;
	}

	public void setCompPlayers(ArrayList<Player> compPlayers) {
		this.compPlayers = compPlayers;
	}

	public HumanPlayer getHuman() {
		return human;
	}

	public void setHuman(HumanPlayer human) {
		this.human = human;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}

	public Card getSolWeapon() {
		return solWeapon;
	}

	public void setSolWeapon(Card solWeapon) {
		this.solWeapon = solWeapon;
	}

	public Card getSolPerson() {
		return solPerson;
	}

	public void setSolPerson(Card solPerson) {
		this.solPerson = solPerson;
	}

	public Card getSolRoom() {
		return solRoom;
	}

	public void setSolRoom(Card solRoom) {
		this.solRoom = solRoom;
	}

	public Player getCurrent() {
		return current;
	}

	public void setCurrent(Player current) {
		this.current = current;
	}
	
	public static void p(Object o) {
		System.out.println(o);
	}
}
