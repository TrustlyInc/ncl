package com.paywithmybank.ncl.navigator.internal;

import com.paywithmybank.ncl.navigator.NCLHorizontalNavigator;
import com.paywithmybank.ncl.model.Node;
import com.paywithmybank.ncl.navigator.NCLNodePointer;

/**
 * A pointer (that is, an object that identifies uniquely a node in an NCL
 * document) that can be used to transverse sideways only (that is, among
 * sibling nodes).
 * <p />
 * Most of the methods of this class have the same behavior as those of the
 * {@linkplain NCLNodeFrame} class.
 */
public interface NCLSidewaysNavigablePointer extends NCLHorizontalNavigator {

    Node getCurrentNode();
    NCLNodePointer getCurrentPointer();
    boolean hasChildren();
    NCLSidewaysNavigablePointer navigateChildren(int index);
}
