import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

class SeqAccessE extends Exception {}

//
// Just like in A1 the general case will look like this:
//
// |-------------------------|
// | 4 5 6 _ _ _ _ _ _ 1 2 3 |
// |-------------------------|
//         /\        /\      /\
//        left      right  capacity
//
//
// There are two main extensions to A1:
//    - the two ends of the array are connected as if the array was circular.
//      Technically all arithmetic is modulo capacity. We will always maintain
//      that the elements are stored at locations:
//      right+1, right+2, ... left-2, left-1
//    - when the array is full, it is resized; the default strategy is to
//      double the size
//

class LRFiniteSequence<E> {
    private Optional<E>[] elements;
    private int capacity;
    private int left;
    private int right;
    private int size;
    private Function<Integer, Integer> growthStrategy;

    @SuppressWarnings("unchecked")
    LRFiniteSequence(int capacity) {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
        this.capacity = capacity;
        left = 0;
        right = capacity - 1;
        size = 0;
        this.growthStrategy = n -> n * 2;
    }

    LRFiniteSequence(int capacity, Function<Integer, Integer> growthStrategy) {
        this(capacity);
        this.growthStrategy = growthStrategy;
    }

    public int getCapacity() {
        return capacity;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public E get(int index) throws SeqAccessE {
        if (capacity - 1 < index | index < 0) {
            throw new SeqAccessE();
        } else if (elements[index] == Optional.empty()) {
            throw new SeqAccessE();
        } else {
            return elements[index].get();
        }
    }

    public void insertLeft(E elem) {
        if (isFull()) {
            resize();
            elements[left] = Optional.of(elem);
            left += 1;
            size += 1;
        } else {
            if (left > capacity - 1) {
                left = 0;
            }
            if (elements[left] == Optional.empty()) {
                elements[left] = Optional.of(elem);
                left += 1;
                size += 1;
            } else {
                left = Math.floorMod(right, capacity);
                elements[left] = Optional.of(elem);
                left += 1;
                size += 1;
            }
        }
    }

    public void insertRight(E elem) {
        if (isFull()) {
            resize();
            if (elements[right] == Optional.empty()) {
                elements[right] = Optional.of(elem);
                right -= 1;
                size += 1;
            }
        } else {
            if (right < 0) {
                right = capacity - 1;
            }
            if (elements[right] == Optional.empty()) {
                elements[right] = Optional.of(elem);
                right -= 1;
                size += 1;
            }
        }
    }

    public E removeLeft() throws SeqAccessE {
        Optional<E> removedElement = null;
        if (left > capacity - 1 | left - 1 < 0) {
            left = capacity - 1;
            removedElement = elements[left];
            if (removedElement == Optional.empty()) {
                throw new SeqAccessE();
            }
            elements[left] = Optional.empty();
            size -= 1;
        } else if (elements[left - 1] == Optional.empty()) {
            throw new SeqAccessE();
        } else {
            removedElement = elements[left - 1];
            elements[left - 1] = Optional.empty();
            left -= 1;
            size -= 1;
        }
        return removedElement.get();
    }

    public E removeRight() throws SeqAccessE {
        Optional<E> removedElement = null;
        if (right + 1 < 0 | right + 1 > capacity - 1) {
            right = 0;
            removedElement = elements[right];
            if (removedElement == Optional.empty()) {
                throw new SeqAccessE();
            }
            elements[right] = Optional.empty();
            size -= 1;
        } else if (elements[right + 1] == Optional.empty()) {
            throw new SeqAccessE();
        } else {
            removedElement = elements[right + 1];
            elements[right + 1] = Optional.empty();
            right += 1;
            size -= 1;
        }
        return removedElement.get();
    }

    @SuppressWarnings("unchecked")
    void resize() {
        int oldCapacity = this.capacity;
        this.capacity = growthStrategy.apply(capacity);
        Optional<E> newElements[] = (Optional<E>[]) Array.newInstance(Optional.class, this.capacity);
        Arrays.fill(newElements, Optional.empty());
        int count = 0;
        int newLeft = 0;
        for (int i = 0; i < elements.length; i++) {
            if ((right + 1) < oldCapacity) {
                newElements[i] = elements[right + 1];
                count++;
                right++;
            } else if ((count < oldCapacity)) {
                newElements[i] = elements[newLeft];
                newLeft++;
                count++;
            }
        }
        this.left = size;
        this.right = capacity - 1;
        this.elements = newElements;
    }

    public String toString() {
        return Arrays.toString(elements);
    }
}
