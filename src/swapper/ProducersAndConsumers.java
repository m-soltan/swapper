package swapper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducersAndConsumers
{
	private Swapper<Integer> produced = new Swapper<>();
	private Swapper<Integer> freed = new Swapper<>();
	
	private final int bufferSize = 100;
	private AtomicInteger madeIndex = new AtomicInteger(0);
	private AtomicInteger consumedIndex = new AtomicInteger(0);
	
	private class Producer implements Runnable
	{
		@Override
		public void run()
		{
			ArrayList<Integer> a;
			int thisNum;
			while (true) {
				thisNum = madeIndex.getAndIncrement();
				a = new ArrayList<>();
				a.add(thisNum % bufferSize);
				try {
					freed.swap(a, new ArrayList<>());
					produced.swap(new ArrayList<>(), a);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(thisNum + " produced");
			}
		}
	}
	
	private class Consumer implements Runnable
	{
		@Override
		public void run()
		{
			ArrayList<Integer> a;
			int thisNum;
			while (true) {
				thisNum = consumedIndex.getAndIncrement();
				a = new ArrayList<>();
				a.add(thisNum);
				try {
					produced.swap(a, new ArrayList<>());
					freed.swap(new ArrayList<>(), a);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(thisNum + " consumed");
			}
		}
	}
}
