import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

class SeqAccessE extends Exception {}
class SeqFullE extends Exception {}

// The general picture to keep in mind is the following:
//
// |-------------------------|
// | 4 5 6 _ _ _ _ _ _ 1 2 3 |
// |-------------------------|
//         /\        /\      /\
//        left      right  capacity
//
// We maintain two pointers: left and right. The left
// pointer starts at 0 and is incremented for insertions
// and decremented for deletions. The right pointer
// starts at the other end and behaves symmetrically.

/**
 * General rules:
 *   - please do not change any method signatures
 *   - please do not change the declarations of the instance variables
 *   - please do not change any of the given methods and do
 *     remove the parts of the methods that are given to you
 *   - only edit the parts marked with TODO
 */

class LRFiniteSequence<E> {
    private final Optional<E>[] elements;
    private final int capacity;
    private int left;
    private int right;
    private int size;

    @SuppressWarnings("unchecked")
    LRFiniteSequence(int capacity) {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
        this.capacity = capacity;
        left = 0;
        right = capacity - 1;
        size = 0;
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

    private boolean sizeConsistent() {
        return size == left + (capacity - 1) - right;
    }

    /**
     * The method returns the element at the given index.
     * If the index is out of bounds, the SeqAccessE exception
     * is thrown.
     * If the index is in bounds but the element at the given
     * index is Optional.empty the SeqAccessE exception
     * is also thrown.
     */

    public E get(int index) throws SeqAccessE {
        if(capacity - 1 < index | index < 0){
            throw new SeqAccessE();
        }
        else if (elements[index] == Optional.empty()){
            throw new SeqAccessE();
        }
        else{
            return elements[index].get(); // TODO
        }
    }

    /**
     * If the sequence is full, the method throws the SeqFullE exception.
     * Otherwise adds the element to the sequence adjusting all pointers
     * appropriately.
     */
    public void insertLeft(E elem) throws SeqFullE {
        // TODO
        if (isFull()) {
            throw new SeqFullE();
        }
        else{
            if (elements[left] == Optional.empty()) {
                elements[left] = Optional.of(elem);
                left += 1;
                size += 1;
            }
        }
        assert sizeConsistent(); // do not remove this line
    }

    /**
     * Same contract as insertLeft
     */
    public void insertRight(E elem) throws SeqFullE {
        // TODO
        if (isFull()){
           throw new SeqFullE();
        }
        else {
            if (elements[right] == Optional.empty()) {
                elements[right] = Optional.of(elem);
                right -= 1;
                size += 1;
            }
        }
        assert sizeConsistent();
    }

    /**
     * If the left pointer is out of bounds, throw the SeqAccessE exception
     * If the left pointer is in bounds but points to an element that
     * is Optional.empty, also throw the SeqAccessE exception
     * Otherwise return the element that left points to adjusting all
     * instance variables appropriately
     */
    public E removeLeft() throws SeqAccessE {
        // TODO
        Optional<E> removedElement = null;
        if (left - 1 > capacity - 1 | left - 1 < 0){
            throw new SeqAccessE();
        }
        else if(elements[left - 1] == Optional.empty()){
            throw new SeqAccessE();
        }
        else {
            removedElement = elements[left - 1];
            elements[left - 1] = Optional.empty();
            left -= 1;
            size -= 1;
        }
        assert sizeConsistent(); // keep this line immediately before the return
        return removedElement.get(); // TODO
    }

    /**
     * The contract is similar to removeLeft
     */
    public E removeRight() throws SeqAccessE {
        // TODO
        Optional<E> removedElement = null;
        if (right + 1 < 0 | right + 1 > capacity - 1){
            throw new SeqAccessE();
        }
        else if (elements[right + 1] == Optional.empty()){
            throw new SeqAccessE();
        }
        else {
            removedElement = elements[right + 1];
            elements[right + 1] = Optional.empty();
            right += 1;
            size -= 1;
        }
        assert sizeConsistent(); // keep this line immediately before the return
        return removedElement.get(); // TODO
    }

    void insertLeftIfNotFull(E elem) {
        if (isFull()) return;
        try {
            insertLeft(elem);
        } catch (SeqFullE e) {
            throw new Error("Internal bug!");
        }
    }

    void insertRightIfNotFull(E elem) {
        if (isFull()) return;
        try {
            insertRight(elem);
        } catch (SeqFullE e) {
            throw new Error("Internal bug!");
        }
    }

    void removeLeftIfNotEmpty() {
        if (left == 0) return;
        try {
            removeLeft();
        } catch (SeqAccessE e) {
            throw new Error("Internal bug!");
        }
    }

    void removeRightIfNotEmpty() {
        if (right == capacity - 1) return;
        try {
            removeRight();
        } catch (SeqAccessE e) {
            throw new Error("Internal bug!");
        }
    }
}
