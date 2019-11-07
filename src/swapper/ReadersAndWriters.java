package swapper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/*
 *	implementation of the algorithm from smurf.mimuw.edu.pl/node/1259 version 2
 */
public class ReadersAndWriters
{
	private enum Room {readingRoom, waitingRoom}
	private AtomicInteger readerCount = new AtomicInteger(0);
	private Swapper<Room> openRooms;
	
	// just R
	private static final ArrayList<Room> justRead = new ArrayList<>();
	// just W
	private static final ArrayList<Room> justWait = new ArrayList<>();
	// R + W
	private static final ArrayList<Room> both = new ArrayList<>();
	
	static {
		justRead.add(Room.readingRoom);
		justWait.add(Room.waitingRoom);
		both.add(Room.readingRoom);
		both.add(Room.waitingRoom);
	}
	
	private class Reader implements Runnable
	{
		@Override
		public void run()
		{
			while (true) {
				try {
					openRooms.swap(justWait, both);
					readerCount.incrementAndGet();
					read();
					if (readerCount.getAndDecrement() == 0) {
						readerCount.incrementAndGet();
						openRooms.swap(justWait, new ArrayList<>());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void read()
		{}
	}
}
