package com.paywithmybank.ncl.navigator;

import com.paywithmybank.ncl.navigator.internal.NCLNodeFrame;
import com.paywithmybank.ncl.Module;
import com.paywithmybank.ncl.NCL;
import com.paywithmybank.ncl.model.Node;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.StreamSupport;

/**
 * A class that abstracts the navigation in a NCL document.
 * <p />
 * This class allows navigating through a NCL document, while also allowing
 * the current navigation path to be saved and restored.
 */
public class NCLNodeNavigator implements NCLHorizontalNavigator, NCLVerticalNavigator {

    private NCL ncl;
    private Deque<NCLNodeFrame> frameStack = new LinkedList<>();

    public NCLNodeNavigator(NCL ncl) {
        this.ncl = ncl;
    }

    private NCLNodeFrame getTopFrame() {
        if (frameStack.isEmpty()) {
            throw new EmptyNavigatorException();
        }
        return frameStack.peek();
    }

    @Override
    public boolean hasNextSibling() {
        if (frameStack.isEmpty()) {
            return false;
        }
        return frameStack.peek().hasNextSibling();
    }

    @Override
    public void goToNextSibling() {
        getTopFrame().goToNextSibling();
    }

    @Override
    public boolean hasPreviousSibling() {
        if (frameStack.isEmpty()) {
            return false;
        }
        return frameStack.peek().hasPreviousSibling();
    }

    @Override
    public void goToPreviousSibling() {
        getTopFrame().goToPreviousSibling();
    }

    @Override
    public boolean hasChildren() {
        if (frameStack.isEmpty()) {
            return false;
        }
        return frameStack.peek().hasChildren();
    }

    @Override
    public void goToChild(int index) {
        getTopFrame().goToChild(index);
    }

    @Override
    public boolean hasParent() {
        if (frameStack.isEmpty()) {
            return false;
        }
        return frameStack.peek().hasParent();
    }

    @Override
    public void goToParent() {
        getTopFrame().goToParent();
    }

    /**
     * Enters a new node in the navigation path, while keeping track of the
     * previous location.
     * @param moduleName The name of the module containing the node.
     * @param selector The selector of the node.
     * @throws NoSuchModuleException If the module does not exist.
     * @throws NoSuchNodeException If the node does not exist.
     */
    public void enterNode(String moduleName, String selector) {
        Module module;
        try {
            module = ncl.getModule(moduleName);
        } catch (RuntimeException e) {
            throw new NoSuchModuleException(moduleName);
        }
        Node node = module.getNode(selector);
        NCLNodeFrame frame = new NCLNodeFrame(node, moduleName, selector);
        frameStack.push(frame);
    }

    /**
     * Checks if the current node has a predecessor, that is, if it is possible
     * to invoke {@linkplain #exitNode()} at this point, and if after invoking
     * it, the navigation will still be pointing to a node.
     * @return {@code true} if the current node has a predecessor.
     */
    public boolean hasPredecessor() {
        return frameStack.size() > 1;
    }

    /**
     * Checks if the current navigator is out of the NCL document, that is,
     * there is no selected node.
     * @return {@code true} if the navigator is out of the NCL document.
     */
    public boolean isOut() {
        return frameStack.isEmpty();
    }

    /**
     * Exits the current node in the navigation path, returning to the original
     * location before {@linkplain #enterNode(String, String) enterNode} was
     * called.
     * @throws EmptyNavigatorException If the navigator is empty.
     */
    public void exitNode() {
        getTopFrame();
        frameStack.pop();
    }

    public Node getNode() {
        return getTopFrame().getCurrentNode();
    }

    public NCLNodePointer getPointer() {
        return getTopFrame().getCurrentPointer();
    }

    public NCLNodePath exportPath() {
        Iterator<NCLNodeFrame> descendingIterator = frameStack.descendingIterator();
        Iterable<NCLNodeFrame> iterable = () -> descendingIterator;
        NCLNodePointer[] pointers = StreamSupport.stream(iterable.spliterator(), false)
            .map(NCLNodeFrame::getCurrentPointer)
            .toArray(NCLNodePointer[]::new);
        return new NCLNodePath(pointers);
    }

    public void restorePath(NCLNodePath path) {
        ArrayList<NCLNodeFrame> frames = new ArrayList<>(frameStack);

        boolean succeeded = false;
        try {
            frameStack.clear();
            for (int i = 0; i < path.getLength(); i ++) {
                NCLNodePointer pointer = path.get(i);
                enterNode(pointer.getModuleName(), pointer.getSelector());
                for (int depth = 0; depth < pointer.getDepth(); depth ++) {
                    int childIndex = pointer.getIndexAtDepth(depth);
                    goToChild(childIndex);
                }
            }
            succeeded = true;
        } finally {
            if (!succeeded) {
                frameStack.clear();
                frameStack.addAll(frames);
            }
            frames.clear();
        }
    }
}
