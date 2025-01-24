package ui.gui.component;

import java.util.Stack;

import javax.swing.JComponent;

public interface StackableComponent {
    /*
     * EFFECTS: returns component stack
     */
    Stack<JComponent> componentStack();
    
    /*
     * EFFECTS: removes frame from parent
     */
    void removeFromParent(JComponent component);

    /*
     * EFFECTS: removes frame from parent
     */
    void addToParent(JComponent component);

    /*
     * EFFECTS: repaints the parent
     */
    void repaintParent();

    /*
     * EFFECTS: revalidates the parent
     */
    void revalidateParent();

    /*
     * 
     * EFFECTS: removes top of component stack from frame (if any)
     *          then pushes new component onto top of stack,
     *          adds it to the frame before redraw and revalidating
     */
    default void pushComponent(JComponent component) {
        if (!componentStack().isEmpty()) {
            JComponent previous = componentStack().peek();

            if (previous.equals(component)) {
                return;
            }

            if (previous != null) {
                removeFromParent(previous);
            }
        }

        if (component != null) {
            componentStack().push(component);
            addToParent(component);
            repaintParent();
            revalidateParent();
        }
    } 

    /*
     * 
     * EFFECTS: if there are more than 1 elemetn in stack
     *          remove top
     *          draw new top
     *          return true
     */
    default boolean popComponent() {
        if (componentStack().size() > 1) {
            JComponent top = componentStack().pop();

            if (top != null) {
                removeFromParent(top);
                repaintParent();
                revalidateParent();
            }

            top = componentStack().peek();

            if (top != null) {
                addToParent(top);
                repaintParent();
                revalidateParent();
            }

            return true;
        }

        return false;
    }
}
