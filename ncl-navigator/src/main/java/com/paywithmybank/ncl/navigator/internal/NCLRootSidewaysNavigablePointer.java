package com.paywithmybank.ncl.navigator.internal;

import com.paywithmybank.ncl.navigator.NCLNodePointer;
import com.paywithmybank.ncl.navigator.NoSuchNodeException;
import com.paywithmybank.ncl.model.Node;

import java.util.List;

/**
 * A {@linkplain NCLSidewaysNavigablePointer} that represents a root node in a
 * NCL document.
 */
public class NCLRootSidewaysNavigablePointer implements NCLSidewaysNavigablePointer {

    private final NCLNodePointer rootPointer;
    private final Node node;

    public NCLRootSidewaysNavigablePointer(Node node, String moduleName, String selector) {
        this.node = node;
        this.rootPointer = new NCLNodePointer(moduleName, selector);
    }

    @Override
    public Node getCurrentNode() {
        return node;
    }

    @Override
    public NCLNodePointer getCurrentPointer() {
        return rootPointer;
    }

    @Override
    public boolean hasNextSibling() {
        return false;
    }

    @Override
    public void goToNextSibling() {
        throw NoSuchNodeException.noSuchNextSibling(rootPointer);
    }

    @Override
    public boolean hasPreviousSibling() {
        return false;
    }

    @Override
    public void goToPreviousSibling() {
        throw NoSuchNodeException.noSuchPreviousSibling(rootPointer);
    }

    @Override
    public boolean hasChildren() {
        List<Node> children = node.getChildren();
        return children != null && !children.isEmpty();
    }

    @Override
    public NCLSidewaysNavigablePointer navigateChildren(int index) {
        List<Node> children = node.getChildren();
        if (children == null || children.isEmpty() || 0 > index || index >= children.size()) {
            throw NoSuchNodeException.noSuchChild(rootPointer, index);
        }
        return new NCLNonRootSidewaysNavigablePointer(rootPointer, children, index);
    }
}
