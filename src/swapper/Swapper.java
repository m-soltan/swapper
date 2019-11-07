package swapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Swapper<E>
{
	public void swap(Collection<E> removed, Collection<E> added) 
		throws InterruptedException
	{
		HashSet<E> remaining = findNotIn(removed);
		l.lock();
		try {
			while (!contents.containsAll(remaining)) ready.await();
			contents.removeAll(removed);
			contents.addAll(added);
			ready.signalAll();
		} finally {
			l.unlock();
		}
	}
	
	//	private attributes
	private final HashSet<E> contents = new HashSet<>();
	private final ReentrantLock l = new ReentrantLock();
	private final Condition ready = l.newCondition();
	
	//	private methods
	private HashSet<E> findNotIn(Collection<E> removed)
	{
		HashSet<E> ans = new HashSet<>();
		for (E elem: removed) {
			if (!contents.contains(elem)) {
				ans.add(elem);
			}
		}
		return ans;
	}
}
