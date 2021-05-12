import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AugmentedHeap {
	PriorityQueue<Integer> heap; // The values in the heap, including values marked for deletion
	HashMap<Integer,Integer> deleteCounts = new HashMap<>(); // A map of keys to the number of
															 // times the key has been deleted
	int totalDeleted = 0; // The total number of values in the heap marked for deletion

	/**
	 * Constructor
	 * 
	 * @param minHeap if true a minheap is created, otherwise a maxheap will be created.
	 */
	public AugmentedHeap(boolean minHeap) {
		if (minHeap) {
			heap = new PriorityQueue<>();
		}
		else {
			heap = new PriorityQueue<>(Collections.reverseOrder());
		}
	}
	/**
	 * Add a key to the heap
	 * 
	 * @param key the key to add
	 */
	public void add(int key) {
		if (deleteCounts.containsKey(key)) {
			// If this key has been deleted, it already exists in the heap,
			// so we simply decrement the number of times it has been deleted
			deleteCounts.put(key, deleteCounts.get(key) - 1);
			if (deleteCounts.get(key) == 0) deleteCounts.remove(key);
			totalDeleted--;
		}
		else {
			heap.add(key);
		}
	}

	/** 
	 * Remove the top of the heap
	 * 
	 * @param key the key that was removed
	 */
	public void remove(int key) {
		// Increase the number of times this key has been deleted, but don't actually
		// try to remove it from the heap
		deleteCounts.put(key, deleteCounts.getOrDefault(key,0) + 1);
		totalDeleted++;
	}

	/**
	 * Return the top of the heap
	 * 
	 * @return the top of the heap, i.e. the min value in a min-heap or max value in a max heap
	 */
	public int top() {
		// remove any deleted nodes from the top of the heap
		while (deleteCounts.containsKey(heap.peek())) {
			// The key at the top of the heap has been deleted, so we now
			// remove it from the heap
			int key = heap.remove();
			deleteCounts.put(key, deleteCounts.get(key) - 1);
			if (deleteCounts.get(key) == 0) deleteCounts.remove(key);
			totalDeleted--;
		}
		return heap.peek();
	}
	
	/**
	 * Remove and return the top of the heap
	 * 
	 * @return the top of the heap, i.e. the min value in a min-heap or max value in a max heap
	 */
	public int deleteTop() {
		// remove any deleted nodes from the top of the heap
		while (deleteCounts.containsKey(heap.peek())) {
			// The key at the top of the heap has been deleted, so we now
			// remove it from the heap
			int key = heap.remove();
			deleteCounts.put(key, deleteCounts.get(key) - 1);
			if (deleteCounts.get(key) == 0) deleteCounts.remove(key);
			totalDeleted--;
		}
		return heap.remove();
	}

	public int size() {
		return heap.size() - totalDeleted;
	}
}
