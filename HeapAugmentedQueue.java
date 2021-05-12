import java.util.HashMap;
import java.util.NoSuchElementException;

public class HeapAugmentedQueue implements IAugmentedQueue {
	private int[] queue = new int[1];
	private final AugmentedHeap minHeap = new AugmentedHeap(true);
	private final AugmentedHeap maxHeap = new AugmentedHeap(false);
	private int front = 0;
	private int size = 0;

	@Override
	public int size() {
		return size;
	}

	@Override
	public void enqueue(int value) {
		if (size == queue.length) resize();
		queue[getIndex(front + size)] = value;
		minHeap.add(value);
		maxHeap.add(value);
		size++;
	}

	@Override
	public int dequeue() {
		if (size == 0) throw new NoSuchElementException();
		int retVal = queue[front];
		front = getIndex(front+1);
		size--;
		minHeap.deleteTop();
		maxHeap.deleteTop();
		return retVal;
	}

	@Override
	public int front() {
		if (size == 0)
			throw new NoSuchElementException();
		return queue[front];
	}

	@Override
	public int back() {
		if (size == 0)
			throw new NoSuchElementException();

		return queue[getIndex(front + size - 1)];
	}

	@Override
	public int median() {
		if (size == 0)
			throw new NoSuchElementException();

		AugmentedHeap minTopValue = new AugmentedHeap(false);
		AugmentedHeap maxTopValue = new AugmentedHeap(true);
		int maxSize = maxTopValue.size();
		int minSize = minTopValue.size();
		for(int i = 0; i < size; i++){
			minHeap.add(queue[i]);
		}
		for (int i = 0; i<size; i++){
			if(minHeap.top() > queue[i]){
				minTopValue.add(queue[i]);
			}
		}
		for(int i = 0; i < size; i++){
			maxHeap.add(queue[i]);
		}
		for (int i = 0; i<size; i++){
			if(maxHeap.top() < queue[i]){
				maxTopValue.add(queue[i]);
			}
		}

		if (minSize > maxSize) {
			return minTopValue.top();
		}
		if (minSize < maxSize){
			return maxTopValue.top();
		}

		return (minTopValue.top() + maxTopValue.top())/2;
	}

	@Override
	public int min() {
		if (size == 0)
			throw new NoSuchElementException();
		AugmentedHeap minHeap = new AugmentedHeap(true);
		for(int i = 0; i < size; i++){
			minHeap.add(queue[i]);
		}
		return minHeap.top();
	}

	@Override
	public int max() {
		if (size == 0)
			throw new NoSuchElementException();
		AugmentedHeap maxHeap = new AugmentedHeap(false);
		for(int i = 0; i < size; i++){
			maxHeap.add(queue[i]);
		}
		return maxHeap.top();
	}

	private void resize() {
		int[] biggerQueue = new int[queue.length * 2];
		
		for (int i = 0; i < size; i++) {
			biggerQueue[i] = queue[getIndex(front + i)];
		}
		queue = biggerQueue;
		front = 0;
	}
	
	private int getIndex(int x) {
		x = x % queue.length;
		if (x < 0) x += queue.length;
		return x;
	}
}
