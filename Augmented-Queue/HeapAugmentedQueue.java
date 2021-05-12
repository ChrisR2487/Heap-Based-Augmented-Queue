import java.util.NoSuchElementException;

public class HeapAugmentedQueue implements IAugmentedQueue {
    private int[] queue = new int[1];
    private int front = 0;
    private int size = 0;
    private AugmentedHeap minHeapAllValues = new AugmentedHeap(true);
    private AugmentedHeap maxHeapAllValues = new AugmentedHeap(false);
    private AugmentedHeap maxHeapSmallValues = new AugmentedHeap(false);
    private AugmentedHeap minHeapBigValues = new AugmentedHeap(true);
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public void enqueue(int value) {
        if (size == queue.length) resize();
        queue[getIndex(front + size)] = value;
        minHeapAllValues.add(value);
        maxHeapAllValues.add(value);
        
        if (maxHeapSmallValues.size() == 0 && minHeapBigValues.size() == 0) 
            maxHeapSmallValues.add(value);
        else if (maxHeapSmallValues.size() == 0 || value > maxHeapSmallValues.top()) 
            minHeapBigValues.add(value);
        else 
            maxHeapSmallValues.add(value);
        size++;
    }

    @Override
    public int dequeue() {
        if (size == 0) throw new NoSuchElementException();
        int retVal = queue[front];
        front = getIndex(front+1);
        size--;
        
        minHeapAllValues.remove(retVal);
        maxHeapAllValues.remove(retVal);
        if (maxHeapSmallValues.size() > 0 && retVal <= maxHeapSmallValues.top())
            maxHeapSmallValues.remove(retVal);
        else
            minHeapBigValues.remove(retVal);
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

        while (maxHeapSmallValues.size() > minHeapBigValues.size()) {
            minHeapBigValues.add(maxHeapSmallValues.deleteTop());
        }

        while (maxHeapSmallValues.size() + 1 < minHeapBigValues.size()) {
            maxHeapSmallValues.add(minHeapBigValues.deleteTop());
        }
        
        if (minHeapBigValues.size() == maxHeapSmallValues.size()) {
            return (minHeapBigValues.top() + maxHeapSmallValues.top()) / 2;
        }
        else {
            return minHeapBigValues.top();
        }
    }

    @Override
    public int min() {
        if (size == 0)
            throw new NoSuchElementException();

        return minHeapAllValues.top();
    }

    @Override
    public int max() {
        if (size == 0)
            throw new NoSuchElementException();

        return maxHeapAllValues.top();
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
