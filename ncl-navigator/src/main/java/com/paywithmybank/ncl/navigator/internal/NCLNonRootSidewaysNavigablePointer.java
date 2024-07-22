package com.paywithmybank.ncl.navigator.internal;

import com.paywithmybank.ncl.navigator.NCLNodePointer;
import com.paywithmybank.ncl.navigator.NoSuchNodeException;
import com.paywithmybank.ncl.model.Node;

import java.util.List;

/**
 * A {@linkplain NCLSidewaysNavigablePointer} that represents a non-root node
 * in a NCL document (either an internal node or a leaf node).
 */
public class NCLNonRootSidewaysNavigablePointer implements NCLSidewaysNavigablePointer {

    private final NCLNodePointer parentPath;
    private final List<Node> siblings;

    private NCLNodePointer currentPath;
    private int currentIndex;

    public NCLNonRootSidewaysNavigablePointer(NCLNodePointer parentPath, List<Node> siblings, int index) {
        this.parentPath = parentPath;
        this.siblings = siblings;
        currentIndex = index;
    }

    @Override
    public Node getCurrentNode() {
        return siblings.get(currentIndex);
    }

    @Override
    public NCLNodePointer getCurrentPointer() {
        if (currentPath == null) {
            currentPath = parentPath.childPointer(currentIndex);
        }
        return currentPath;
    }

    @Override
    public boolean hasNextSibling() {
        return currentIndex + 1 < siblings.size();
    }

    @Override
    public void goToNextSibling() {
        if (!hasNextSibling()) {
            throw NoSuchNodeException.noSuchNextSibling(getCurrentPointer());
        }
        currentIndex ++;
        currentPath = null;
    }

    @Override
    public boolean hasPreviousSibling() {
        return currentIndex > 0;
    }

    @Override
    public void goToPreviousSibling() {
        if (!hasPreviousSibling()) {
            throw NoSuchNodeException.noSuchPreviousSibling(getCurrentPointer());
        }
        currentIndex --;
        currentPath = null;
    }

    @Override
    public boolean hasChildren() {
        List<Node> children = getCurrentNode().getChildren();
        return children != null && !children.isEmpty();
    }

    @Override
    public NCLSidewaysNavigablePointer navigateChildren(int index) {
        List<Node> children = getCurrentNode().getChildren();
        if (children == null || children.isEmpty() || 0 > index || index >= children.size()) {
            throw NoSuchNodeException.noSuchChild(getCurrentPointer(), index);
        }
        return new NCLNonRootSidewaysNavigablePointer(getCurrentPointer(), children, index);
    }
}
