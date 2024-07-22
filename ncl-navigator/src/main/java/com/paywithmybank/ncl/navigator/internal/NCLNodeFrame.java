package com.paywithmybank.ncl.navigator.internal;

import com.paywithmybank.ncl.navigator.NCLHorizontalNavigator;
import com.paywithmybank.ncl.navigator.NCLNodePointer;
import com.paywithmybank.ncl.navigator.NCLVerticalNavigator;
import com.paywithmybank.ncl.navigator.NoSuchNodeException;
import com.paywithmybank.ncl.model.Node;

import java.util.Deque;
import java.util.LinkedList;

/**
 * A node frame represents location in a NCL document, keeping track of the
 * navigation path that lead to it.
 * <p />
 * During NCL navigation, a given software might need to start a new downward
 * path from some node while keeping track to where it was before. Each of
 * these individual navigations is a NCL node frame.
 * <p />
 * A node frame is analogous to a stack frame in a call stack.
 */
public class NCLNodeFrame implements NCLHorizontalNavigator, NCLVerticalNavigator {

    private Deque<NCLSidewaysNavigablePointer> nodeStack = new LinkedList<>();

    public NCLNodeFrame(Node node, String moduleName, String selector) {
        nodeStack.push(new NCLRootSidewaysNavigablePointer(node, moduleName, selector));
    }

    /**
     * Get the sideways navigable pointer representing the current node in this
     * frame.
     * @return The current node in the frame.
     */
    private NCLSidewaysNavigablePointer getTopNavigablePointer() {
        return nodeStack.peek();
    }

    @Override
    public boolean hasNextSibling() {
        return getTopNavigablePointer().hasNextSibling();
    }

    @Override
    public void goToNextSibling() {
        getTopNavigablePointer().goToNextSibling();
    }

    @Override
    public boolean hasPreviousSibling() {
        return getTopNavigablePointer().hasPreviousSibling();
    }

    @Override
    public void goToPreviousSibling() {
        getTopNavigablePointer().goToPreviousSibling();
    }

    @Override
    public boolean hasChildren() {
        return getTopNavigablePointer().hasChildren();
    }

    @Override
    public void goToChild(int index) {
        NCLSidewaysNavigablePointer pointer = nodeStack.peek().navigateChildren(index);
        nodeStack.push(pointer);
    }

    /**
     * Check if the current node has a parent.
     * @return {@code true} if the current node has a parent.
     */
    public boolean hasParent() {
        return nodeStack.size() > 1;
    }

    @Override
    public void goToParent() {
        if (!hasParent()) {
            throw NoSuchNodeException.noSuchParent(getCurrentPointer());
        }
        nodeStack.pop();
    }

    /**
     * Get the current node in this frame.
     * @return The current node in the frame.
     */
    public Node getCurrentNode() {
        return getTopNavigablePointer().getCurrentNode();
    }

    /**
     * Get the pointer to the current node in this frame.
     * @return The pointer to the current node in the frame.
     */
    public NCLNodePointer getCurrentPointer() {
        return getTopNavigablePointer().getCurrentPointer();
    }
}
