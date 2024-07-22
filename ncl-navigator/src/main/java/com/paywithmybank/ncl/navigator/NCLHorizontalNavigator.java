package com.paywithmybank.ncl.navigator;

/**
 * An interface defining operations for navigating a NCL document horizontally
 * (that is, between siblings nodes).
 */
public interface NCLHorizontalNavigator {

    /**
     * Check if the current node has a next sibling.
     * @return {@code true} if the current node has a next sibling.
     */
    boolean hasNextSibling();

    /**
     * Moves this navigator to the next sibling of the current node.
     * @throws NoSuchNodeException If the current node has no next sibling.
     */
    void goToNextSibling();

    /**
     * Check if the current node has a previous sibling.
     * @return {@code true} if the current node has a previous sibling.
     */
    boolean hasPreviousSibling();

    /**
     * Moves this navigator to the previous sibling of the current node.
     * @throws NoSuchNodeException If the current node has no previous sibling.
     */
    void goToPreviousSibling();

}
