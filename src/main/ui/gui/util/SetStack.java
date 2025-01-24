package ui.gui.util;

import java.util.Stack;

// Represents a stack with no duplicate items
public class SetStack<T> extends Stack<T> {
    /*
     * MODIFIES: this
     * EFFECTS: if doesnt contain item, pushes to stack like normal
     *          if contains item, removes the item and then pushes it
     */
    @Override
    public T push(T item) {
        if (!this.contains(item)) {
            return super.push(item);
        }

        remove(item);

        return super.push(item);
    }
}
