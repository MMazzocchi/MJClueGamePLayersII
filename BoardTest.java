package clueGame;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BoardTest {
	
	private static Board board;
	public static final int NUM_ROOMS = 12;
	public static final int NUM_ROWS = 19;
	public static final int NUM_COLUMNS = 31;

	@BeforeClass
	public static void boardSetUp() {
		board = new Board();
	}
	
	@Test
	public void testRowsCols() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumCols());	
	}
	@Test
	public void testRooms() {
		Map<Character, String> rooms = board.getRooms();
		
		assertEquals(NUM_ROOMS, rooms.size());
		
		assertEquals("Disco Room", rooms.get('I'));
		assertEquals("Master Bedroom", rooms.get('M'));
		assertEquals("NULL SPACE", rooms.get('N'));
		assertEquals("Dining Room", rooms.get('D'));
		assertEquals("Walkway", rooms.get('W'));
	}
	@Test
	public void testDoors() {
		RoomCell room = board.getRoomCellAt(9, 5);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getRoomCellAt(2, 8);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
		
		room = board.getRoomCellAt(21, 5);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.LEFT, room.getDoorDirection());
		
		room = board.getRoomCellAt(15, 6);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
		
		room = board.getRoomCellAt(0, 0);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.NONE, room.getDoorDirection());
		
	}
	@Test
	public void testCorrectRoomInitial() {
		assertEquals('N', board.getRoomCellAt(0, 0).roomInitial);
		assertEquals('K', board.getRoomCellAt(6, 5).roomInitial);
		assertEquals('I', board.getRoomCellAt(15, 8).roomInitial);
		assertEquals('E', board.getRoomCellAt(30, 5).roomInitial);
		assertEquals('D', board.getRoomCellAt(20, 18).roomInitial);
	}
	@Test
	public void testCalcIndex() { 
		int temp = board.calcIndex(1, 2);
		Assert.assertEquals(63, temp);
	}
	@Test
	public void testCalcIndex2() {	
		int temp = board.calcIndex(2, 3);
		Assert.assertEquals(95, temp);
		temp = board.calcIndex(30, 18);
		Assert.assertEquals(588, temp);
	}
}
