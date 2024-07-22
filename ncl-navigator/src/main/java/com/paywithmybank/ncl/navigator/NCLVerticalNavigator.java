package com.paywithmybank.ncl.navigator;

/**
 * An interface defining operations for navigating a NCL document vertically
 * (that is, between child and parent nodes).
 */
public interface NCLVerticalNavigator {

    /**
     * Check if the current node has children.
     * @return {@code true} if the current node has children.
     */
    boolean hasChildren();

    /**
     * Moves this navigator to a child node of the current node.
     * @param index The index of the child node to move to.
     * @throws NoSuchNodeException If the current node has no children or the
     * given index is out of bounds.
     */
    void goToChild(int index);

    /**
     * Check if the current node has a parent.
     * @return {@code true} if the current node has a parent.
     */
    boolean hasParent();

    /**
     * Moves this navigator to the parent node.
     * @throws UnsupportedOperationException If the current node has no parent.
     */
    void goToParent();
}
