package clueGame;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameSetupTest {
	
	public static Board b;
	public static Card green;
	public static Card white;
	public static Card plum;
	public static Card study;
	public static Card rope;
	public static Card kitchen;
	public static Card knife;
	
	@BeforeClass public static void initialize() {
		//Initialize fields
		b = new Board();
		green = new Card("Mr. Green", Card.Type.PERSON);
		white = new Card("Mrs. White", Card.Type.PERSON);
		plum = new Card("Prof. Plum", Card.Type.PERSON);
		study = new Card("Study", Card.Type.ROOM);
		kitchen = new Card("Kitchen", Card.Type.ROOM);
		rope = new Card("Rope", Card.Type.WEAPON);
		knife = new Card("Knife", Card.Type.WEAPON);
	}
	
	@Test public void loadPeople() {		
		ArrayList<Player> cPlayers = b.getCompPlayers();
		HumanPlayer h = b.getHuman();
						
		//Test the human player
		Assert.assertEquals("Mr. Green", h.getName());
		Assert.assertEquals(Color.GREEN, h.getColor());
		Assert.assertEquals(276, h.getLocation());
		
		//Test Computer Player 1
		Player c1 = cPlayers.get(0);
		Assert.assertEquals("Mrs. White", c1.getName());
		Assert.assertEquals(Color.WHITE, c1.getColor());
		Assert.assertEquals(6, c1.getLocation());
		
		//Test Computer Player 2
		Player c2 = cPlayers.get(cPlayers.size()-1);
		Assert.assertEquals("Prof. Plum", c2.getName());
		Assert.assertEquals(new Color(255,0,255), c2.getColor());
		Assert.assertEquals(284, c2.getLocation());
		
	}
	
	@Test public void loadCards() {
		ArrayList<Card> d = b.getDeck();
		
		//Test the deck size
		Assert.assertEquals(21, d.size());
		
		//Test the amount of each type of cards
		int w = 0;
		int s = 0;
		int r = 0;
		for(Card c : d) {
			switch(c.getType()) {
			case WEAPON:
				w++;
				break;
			case PERSON:
				s++;
				break;
			case ROOM:
				r++;
				break;
			}
		}
		Assert.assertEquals(6, w);
		Assert.assertEquals(6, s);
		Assert.assertEquals(9, r);
		
		//Test contains cards
		Card pCard = green;
		Assert.assertTrue(d.contains(pCard));
		Card rCard = study;
		
		Assert.assertTrue(d.contains(rCard));
		Card wCard = rope;
		Assert.assertTrue(d.contains(wCard));
	}
	
	@Test public void testDeal() {
		Player h = b.getHuman();
		ArrayList<Card> copy = (ArrayList<Card>)b.getDeck().clone();
		b.deal();
		ArrayList<Card> d = b.getDeck();
		
		//Test that all cards are dealt
		Assert.assertEquals(0, d.size());
		
		//Test player's number of cards
		int n = b.getHuman().getCards().size();
		for(Player c : b.getCompPlayers()) {
			//Each player must have within one card of the human's amount
			Assert.assertTrue((Math.abs(n-c.getCards().size())<=1));
		}
		
		//Test that each card was dealt only once or less
		for(Card c : copy) {
			int count = 0;
			if(h.getCards().contains(c)) {
				count++;
			}
			for(Player cp : b.getCompPlayers()) {
				if(cp.getCards().contains(c)) {
					count++;
				}
			}
			
			//Count can be 1, if dealt, or 0, if it is the solution
			Assert.assertTrue(count<=1);
		}
	}
	
	@Test public void testAccusation() {
		//Set Solution
		Card pCard = green;
		b.setSolPerson(pCard);
		Card rCard = study;
		b.setSolRoom(rCard);
		Card wCard = rope;
		b.setSolWeapon(wCard);
		
		//Test true accusation
		Assert.assertTrue(b.accuse(pCard, rCard, wCard));
		
		//Test wrong person
		Card pCard2 = white;
		Assert.assertFalse(b.accuse(pCard2, rCard, wCard));
		
		//Test wrong room
		Card rCard2 = kitchen;
		Assert.assertFalse(b.accuse(pCard, rCard2, wCard));
		
		//Test wrong weapon
		Card wCard2 = knife;
		Assert.assertFalse(b.accuse(pCard, rCard, wCard2));
		
		//Test all wrong accusation
		Assert.assertFalse(b.accuse(pCard2, rCard2, wCard2));
	}
	
	@Test public void targetLocationTest() {
		ComputerPlayer p = new ComputerPlayer();

		//Test that a room will always be selected if possible
		b.calcTargets(b.calcIndex(4, 4), 1);
		for(int i=0; i<100; i++) {
			BoardCell selection = p.pickLocation(b.getTargets());
			if(!selection.equals(b.getCellAt(b.calcIndex(4,3)))) {
				fail("Cell with coordinates row: "+selection.row+", col: "+selection.col +" selected.");
			}
		}
		
		//Test that random spaces will be selected if no rooms available
		b.calcTargets(b.calcIndex(14, 0), 1);
		int l1 = 0;
		int l2 = 0;
		int l3 = 0;
		for(int i=0; i<100; i++) {
			//Ensure that a valid space is chosen
			BoardCell selection = p.pickLocation(b.getTargets());
			if(selection.equals(b.getCellAt(b.calcIndex(13,0)))) {
				l1++;
			} else if(selection.equals(b.getCellAt(b.calcIndex(14,1)))) {
				l2++;
			} else if(selection.equals(b.getCellAt(b.calcIndex(15,0)))) {
				l3++;
			} else {
				fail("Cell with coordinates row: "+selection.row+", col: "+selection.col +" selected.");
			}
		}
		
		//Ensure that every valid space was chosen at least once
		Assert.assertTrue(l1>1);
		Assert.assertTrue(l2>1);
		Assert.assertTrue(l3>1);
		
		//Test that a target will be selected randomly if a room is available but that room has already been entered.
		b.calcTargets(b.calcIndex(4, 4), 1);
		l1 = 0;
		l2 = 0;
		l3 = 0;
		p.setLastRoom('C');
		for(int i=0; i<100; i++) {
			//Ensure that a valid space is chosen
			BoardCell selection = p.pickLocation(b.getTargets());
			if(selection.equals(b.getCellAt(b.calcIndex(4,3)))) {
				l1++;
			} else if(selection.equals(b.getCellAt(b.calcIndex(4,5)))) {
				l2++;
			} else if(selection.equals(b.getCellAt(b.calcIndex(5,4)))) {
				l3++;
			} else {
				fail("Cell with coordinates row: "+selection.row+", col: "+selection.col +" selected.");
			}
		}
		
		//Ensure that every valid space was chosen at least once
		Assert.assertTrue(l1>1);
		Assert.assertTrue(l2>1);
		Assert.assertTrue(l3>1);
	}

	
	@Test public void suggestionTest() {
		//Initialize a computer player
		ArrayList<Player> p = new ArrayList<Player>();
		ComputerPlayer c = new ComputerPlayer();
		ArrayList<Card> cards = new ArrayList<Card>();
		Card pCard = green;
		cards.add(pCard);
		Card rCard = study;
		cards.add(rCard);
		Card wCard = rope;
		cards.add(wCard);
		c.setCards(cards);
		c.setName("KERFAIL");
		p.add(c);
		b.setCompPlayers(p);
		b.setHuman(null);
		
		//Test a suggestion with a known person
		Card x = b.suggest(pCard, kitchen, knife);
		Assert.assertTrue(x.equals(pCard));
		//Test a suggestion with a known room
		x = b.suggest(white, rCard, knife);
		Assert.assertTrue(x.equals(rCard));
		//Test a suggestion with a known weapon
		x = b.suggest(white, kitchen, wCard);
		Assert.assertTrue(x.equals(wCard));
		//Test a suggestion with no known cards
		x = b.suggest(white, kitchen, knife);
		Assert.assertTrue(x == null);
		
		//Test that a random card is returned if there are multiple knowns
		int c_1 = 0;
		int c_2 = 0;
		int c_3 = 0;
		for(int i=0; i<100; i++) {
			x = b.suggest(pCard, rCard, wCard);
			if(x.equals(pCard)) {
				c_1++;
			} else if(x.equals(rCard)) {
				c_2++;
			} else if(x.equals(wCard)) {
				c_3++;
			} else {
				fail("Invalid card returned.");
			}
		}
		
		//Ensure that every card was returned at least once
		Assert.assertTrue(c_1>1);
		Assert.assertTrue(c_2>1);
		Assert.assertTrue(c_3>1);
		
		//Test that all players are queried
		ArrayList<Player> players = new ArrayList<Player>();
		
		ArrayList<Card> pCards = new ArrayList<Card>();
		pCards.add(pCard);
		HumanPlayer h = new HumanPlayer();
		h.setCards(pCards);
		players.add(h);
		
		ArrayList<Card> rCards = new ArrayList<Card>();
		rCards.add(rCard);
		ComputerPlayer c1 = new ComputerPlayer();
		c1.setCards(rCards);
		players.add(c1);

		ArrayList<Card> wCards = new ArrayList<Card>();
		wCards.add(wCard);
		ComputerPlayer c2 = new ComputerPlayer();
		c2.setCards(wCards);
		players.add(c2);
		
		b.setCompPlayers(players);
		
		//Try a suggestion that we know the human can disprove. If the right card is returned, the human was queried
		x = b.suggest(pCard, kitchen, knife);
		Assert.assertTrue(x.equals(pCard));
		//Try a suggestion that we know the computer can disprove. If the right card is returned, the computer was queried
		x = b.suggest(white, rCard, knife);
		Assert.assertTrue(x.equals(rCard));
		//Try a suggestion that we know the computer can disprove. If the right card is returned, the computer was queried
		x = b.suggest(white, kitchen, wCard);
		Assert.assertTrue(x.equals(wCard));
		//Test a suggestion no one can disprove
		x = b.suggest(white, kitchen, knife);
		Assert.assertTrue(x == null);
		
		//Set the current player to the human
		b.setCurrent(h);
		
		//Try a suggestion that we know the human can disprove. Because current is human, null should be returned
		x = b.suggest(pCard, kitchen, knife);
		Assert.assertTrue(x == null);
		
		//Set the current player to the computer
		b.setCurrent(c1);
		
		//Try a suggestion that we know the computer can disprove.  Because current is human, null should be returned
		x = b.suggest(white, rCard, knife);
		Assert.assertTrue(x == null);
		
		c_1 = 0;
		c_2 = 0;
		
		//Ensure that players are queried in a random order
		for(int i=0; i<100; i++) {
			//Both the human and the computer can disprove this
			x = b.suggest(pCard, kitchen, wCard);
			if(x.equals(pCard)) {
				c_1++;
			} else if(x.equals(wCard)) {
				c_2++;
			} else {
				fail("Invalid card returned.");
			}
		}
		
		//Test that both players returned a card more than once
		Assert.assertTrue(c_1>1);
		Assert.assertTrue(c_2>1);
	}
	
	@Test public void ComputerSuggestion() {
		ArrayList<Card> deck = b.loadDeck();
		
		
		ComputerPlayer c = new ComputerPlayer(deck);
		c.setCurrentRoom("Study");
				
		Card rCard = study;
		Card pCard = green;
		Card wCard = rope;
		
		//Test that the current room is suggested
		Card x = c.suggestRoom();
		Assert.assertEquals(rCard, x);
		
		//Test that only cards that are not known are suggested
		ArrayList<Card> d = b.loadDeck();
		d.remove(pCard);
		d.remove(wCard);
				
		//Give the computer player every card in the deck besides one person and one weapon
		c.setCards(d);
		c.setSeen(d);
		
		//Ensure that those missing cards are suggested
		x = c.suggestPerson();
		Assert.assertEquals(pCard, x);
		x = c.suggestWeapon();
		Assert.assertEquals(wCard, x);
	}
}
