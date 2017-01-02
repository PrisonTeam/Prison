package tech.mcprison.prison.cells;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by DMP9 on 01/01/2017.
 */
public class CellsList implements List<Cell> {
    // Base list
    List<Cell> cells;

    // Inherited methods -- don't know why I make things so difficult
    public int size() {
        return cells.size();
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    public boolean contains(Object o) {
        return cells.contains(o);
    }

    public Iterator<Cell> iterator() {
        return cells.iterator();
    }

    public Cell[] toArray() {
        return (Cell[]) cells.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return cells.toArray(a);
    }

    public boolean add(Cell c) {
        if (contains(c)) {
            return false;
        } else {
            return cells.add(c);
        }
    }

    public boolean remove(Object c) {
        if (!contains(c)) {
            return false;
        } else {
            return cells.remove(c);
        }
    }

    public boolean containsAll(Collection c) {
        return cells.containsAll(c);
    }

    public boolean addAll(Collection<? extends Cell> c) {
        return cells.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Cell> c) {
        return cells.addAll(index, c);
    }

    public boolean removeAll(Collection c) {
        return cells.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return cells.retainAll(c);
    }

    public void clear() {
        cells.clear();
    }

    public Cell get(int index) {
        return cells.get(index);
    }

    public Cell set(int index, Cell element) {
        return cells.set(index, element);
    }

    public void add(int index, Cell c) {
        cells.add(index, c);
    }

    public Cell remove(int index) {
        return cells.remove(index);
    }

    public int indexOf(Object c) {
        return cells.indexOf(c);
    }

    public int lastIndexOf(Object c) {
        return cells.lastIndexOf(c);
    }

    public ListIterator<Cell> listIterator() {
        return cells.listIterator();
    }

    public ListIterator<Cell> listIterator(int index) {
        return cells.listIterator(index);
    }

    public CellsList subList(int fromIndex, int toIndex) {
        return (CellsList) cells.subList(fromIndex, toIndex);
    }

    // Chain LINQ-style methods
    public CellsList select(CellsFilter filter) {
        CellsList out = new CellsList();
        for (Cell c : this) {
            if (filter.accept(c)) {
                out.add(c);
            }
        }
        return out;
    }
}
