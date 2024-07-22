package com.paywithmybank.ncl.navigator.internal;

import com.paywithmybank.ncl.model.Node;
import com.paywithmybank.ncl.navigator.NCLNodePointer;
import com.paywithmybank.ncl.navigator.NoSuchNodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class NCLNodeFrameTest {

    private static final String ROOT_NODE_NAME = "Root";
    private static final String CHILD_NODE_NAME = "Child";

    Node node;
    NCLNodeFrame subject;

    @BeforeEach
    void setUp() {
        node = new Node();
        node.setNodeName(ROOT_NODE_NAME);
        subject = new NCLNodeFrame(node, null, node.getNodeName());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasNextSibling_returnsFalse_whenRootIsSelected() {
        assertFalse(subject.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with a next sibling\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasNextSibling_returnsTrue_whenChildWithNextSiblingIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        assertTrue(subject.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with no next sibling\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasNextSibling_returnsTrue_whenChildWithNoNextSiblingIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(2);

        assertFalse(subject.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN goToNextSibling is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoNextSibling_throwsException_whenRootIsSelected() {
        assertThrows(NoSuchNodeException.class, subject::goToNextSibling);
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with a next sibling\n" +
        "WHEN goToNextSibling is called\n" +
        "THEN the frame moves to the next sibling"
    )
    @Test
    void gotoNextSibling_movesToNextSibling_whenChildWithNoNextSiblingIsSelected() {
        List<Node> childNodes = addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        subject.goToNextSibling();

        assertEquals(childNodes.get(2), subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with no next sibling\n" +
        "WHEN goToNextSibling is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoNextSibling_throwsException_whenChildWithNoNextSiblingIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(2);

        assertThrows(NoSuchNodeException.class, subject::goToNextSibling);
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasPreviousSibling_returnsFalse_whenRootIsSelected() {
        assertFalse(subject.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with a previous sibling\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasPreviousSibling_returnsTrue_whenChildWithPreviousSiblingIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        assertTrue(subject.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN goToPreviousSibling is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoPreviousSibling_throwsException_whenRootIsSelected() {
        assertThrows(NoSuchNodeException.class, subject::goToPreviousSibling);
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with a previous sibling\n" +
        "WHEN goToPreviousSibling is called\n" +
        "THEN the frame moves to the previous sibling"
    )
    @Test
    void gotoPreviousSibling_movesToPreviousSibling_whenChildWithPreviousSiblingIsSelected() {
        List<Node> childNodes = addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        subject.goToPreviousSibling();

        assertEquals(childNodes.get(0), subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with no previous sibling\n" +
        "WHEN goToPreviousSibling is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoPreviousSibling_throwsException_whenChildWithNoPreviousSiblingIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(0);

        assertThrows(NoSuchNodeException.class, subject::goToPreviousSibling);
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with no previous sibling\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasPreviousSibling_returnsFalse_whenChildWithNoPreviousSiblingIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(0);

        assertFalse(subject.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node frame pointing to the root node without children\n" +
        "WHEN hasChildren is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasChildren_returnsFalse_whenRootNodeHasNoChildren() {
        assertFalse(subject.hasChildren());
    }

    @DisplayName(
        "GIVEN a node frame pointing to the root node with children\n" +
        "WHEN hasChildren is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasChildren_returnsTrue_whenRootNodeHasChildren() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);

        assertTrue(subject.hasChildren());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node without children\n" +
        "WHEN hasChildren is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasChildren_returnsFalse_whenChildNodeHasNoChildren() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        assertFalse(subject.hasChildren());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with children\n" +
        "WHEN hasChildren is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasChildren_returnsTrue_whenChildNodeHasChildren() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);
        addChildrenToNode(subject.getCurrentNode(), CHILD_NODE_NAME, 2);

        assertTrue(subject.hasChildren());
    }

    @DisplayName(
        "GIVEN a node frame pointing to the root node without children\n" +
        "WHEN goToChild is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoChild_throwsException_whenRootNodeHasNoChildren() {
        assertThrows(NoSuchNodeException.class, () -> subject.goToChild(0));
    }

    @DisplayName(
        "GIVEN a node frame pointing to the root node with children\n" +
        "AND an existing child index\n" +
        "WHEN goToChild is called\n" +
        "THEN the frame moves to the child"
    )
    @Test
    void gotoChild_movesToChild_whenRootNodeHasChild() {
        List<Node> childNodes = addChildrenToNode(node, CHILD_NODE_NAME, 3);

        subject.goToChild(1);

        assertEquals(childNodes.get(1), subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to the root node with children\n" +
        "AND a non-existing child index\n" +
        "WHEN goToChild is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoChild_throwsException_whenRootAndIndexIsOutOfBounds() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);

        assertThrows(NoSuchNodeException.class, () -> subject.goToChild(3));
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node without children\n" +
        "WHEN goToChild is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoChild_throwsException_whenChildNodeHasNoChildren() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        assertThrows(NoSuchNodeException.class, () -> subject.goToChild(0));
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with children\n" +
        "AND an existing child index\n" +
        "WHEN goToChild is called\n" +
        "THEN the frame moves to the child"
    )
    @Test
    void gotoChild_movesToChild_whenChildNodeHasChildren() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);
        List<Node> childNodes = addChildrenToNode(subject.getCurrentNode(), CHILD_NODE_NAME, 2);

        subject.goToChild(1);

        assertEquals(childNodes.get(1), subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node with children\n" +
        "AND a non-existing child index\n" +
        "WHEN goToChild is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoChild_throwsException_whenChildAndIndexIsOutOfBounds() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);
        addChildrenToNode(subject.getCurrentNode(), CHILD_NODE_NAME, 2);

        assertThrows(NoSuchNodeException.class, () -> subject.goToChild(2));
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN hasParent is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasParent_returnsFalse_whenRootIsSelected() {
        assertFalse(subject.hasParent());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node\n" +
        "WHEN hasParent is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasParent_returnsTrue_whenChildIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        assertTrue(subject.hasParent());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN goToParent is called\n" +
        "THEN an exception is thrown"
    )
    @Test
    void gotoParent_throwsException_whenRootIsSelected() {
        assertThrows(NoSuchNodeException.class, subject::goToParent);
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node\n" +
        "WHEN goToParent is called\n" +
        "THEN the frame moves to the parent"
    )
    @Test
    void gotoParent_movesToParent_whenChildIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        subject.goToParent();

        assertEquals(node, subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN getCurrentNode is called\n" +
        "THEN the root node is returned"
    )
    @Test
    void getCurrentNode_returnsRootNode_whenRootIsSelected() {
        assertEquals(node, subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node\n" +
        "WHEN getCurrentNode is called\n" +
        "THEN the child node is returned"
    )
    @Test
    void getCurrentNode_returnsChildNode_whenChildIsSelected() {
        List<Node> childNodes = addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);

        assertEquals(childNodes.get(1), subject.getCurrentNode());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a root node\n" +
        "WHEN getCurrentPointer is called\n" +
        "THEN the root node pointer is returned"
    )
    @Test
    void getCurrentPointer_returnsRootPointer_whenRootIsSelected() {
        NCLNodePointer rootPointer = new NCLNodePointer(null, "Root");
        assertEquals(rootPointer, subject.getCurrentPointer());
    }

    @DisplayName(
        "GIVEN a node frame pointing to a child node\n" +
        "WHEN getCurrentPointer is called\n" +
        "THEN the child node pointer is returned"
    )
    @Test
    void getCurrentPointer_returnsChildPointer_whenChildIsSelected() {
        addChildrenToNode(node, CHILD_NODE_NAME, 3);
        subject.goToChild(1);
        NCLNodePointer childPointer = new NCLNodePointer(null, ROOT_NODE_NAME, 1);

        assertEquals(childPointer, subject.getCurrentPointer());
    }

    static List<Node> addChildrenToNode(Node node, String nodeName, int amount) {
        List<Node> childNodes = IntStream.range(0, amount).mapToObj(i -> {
            Node childNode = new Node();
            childNode.setNodeName(nodeName);
            return childNode;
        }).collect(Collectors.toList());
        childNodes.forEach(node::add);
        return childNodes;
    }
}
