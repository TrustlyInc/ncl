package com.paywithmybank.ncl.navigator;

import com.paywithmybank.ncl.NCL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class NCLNodeNavigatorTest {

    @DisplayName(
        "GIVEN a node navigator that is out of any node\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasNextSibling_returnsTrue_whenOut() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());

        assertFalse(navigator.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a root node\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasNextSibling_returnsFalse_whenInRootNode() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");

        assertFalse(navigator.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a child node\n" +
        "AND that child node has a next sibling\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasNextSibling_returnsTrue_whenInChildNodeAndItHasNextSibling() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.goToChild(1);

        assertTrue(navigator.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a child node\n" +
        "AND that child node does not have a next sibling\n" +
        "WHEN hasNextSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasNextSibling_returnsTrue_whenInChildNodeAndItDoesNotHaveNextSibling() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.goToChild(2);

        assertFalse(navigator.hasNextSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is out of any node\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasPreviousSibling_returnsTrue_whenOut() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());

        assertFalse(navigator.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a root node\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasPreviousSibling_returnsFalse_whenInRootNode() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");

        assertFalse(navigator.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a child node\n" +
        "AND that child node has a next sibling\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasPreviousSibling_returnsTrue_whenInChildNodeAndItHasPreviousSibling() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.goToChild(1);

        assertTrue(navigator.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a child node\n" +
        "AND that child node does not have a next sibling\n" +
        "WHEN hasPreviousSibling is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasPreviousSibling_returnsTrue_whenInChildNodeAndItDoesNotHavePreviousSibling() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.goToChild(0);

        assertFalse(navigator.hasPreviousSibling());
    }

    @DisplayName(
        "GIVEN a node navigator that is out of any node\n" +
        "WHEN hasChildren is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasChildren_returnsFalse_whenOut() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());

        assertFalse(navigator.hasChildren());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node with children\n" +
        "WHEN hasChildren is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasChildren_returnsTrue_whenCurrentNodeHasChildren() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");

        assertTrue(navigator.hasChildren());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node without children\n" +
        "WHEN hasChildren is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasChildren_returnsFalse_whenCurrentNodeDoesNotHaveChildren() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.goToChild(0);

        assertFalse(navigator.hasChildren());
    }

    @DisplayName(
        "GIVEN a node navigator that is out of any node\n" +
        "WHEN hasParent is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasParent_returnsFalse_whenOut() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());

        assertFalse(navigator.hasParent());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node without a parent\n" +
        "WHEN hasParent is called\n" +
        "THEN false is returned"
        )
    @Test
    void hasParent_returnsFalse_whenCurrentNodeDoesNotHaveParent() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");

        assertFalse(navigator.hasParent());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node with a parent\n" +
        "WHEN hasParent is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasParent_returnsTrue_whenCurrentNodeHasParent() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.goToChild(0);

        assertTrue(navigator.hasParent());
    }

    @DisplayName(
        "GIVEN a node navigator that is out of any node\n" +
        "WHEN hasPredecessor is called\n" +
        "THEN false is returned"
    )
    @Test
    void hasPredecessor_returnsFalse_whenOut() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());

        assertFalse(navigator.hasPredecessor());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node\n" +
        "AND the node has no predecessor\n" +
        "WHEN hasPredecessor is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasPredecessor_returnsFalse_whenInAndNodeDoesNotHavePredecessor() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");

        assertFalse(navigator.hasPredecessor());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node\n" +
        "AND the node has a predecessor\n" +
        "WHEN hasPredecessor is called\n" +
        "THEN true is returned"
    )
    @Test
    void hasPredecessor_returnsTrue_whenInAndNodeHasPredecessor() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");
        navigator.enterNode(null, "call1");

        assertTrue(navigator.hasPredecessor());
    }

    @DisplayName(
        "GIVEN a node navigator that is out of any node\n" +
        "WHEN isOut is called\n" +
        "THEN true is returned"
    )
    @Test
    void isOut_returnsTrue_whenOut() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());

        assertTrue(navigator.isOut());
    }

    @DisplayName(
        "GIVEN a node navigator that is in a node\n" +
        "WHEN isOut is called\n" +
        "THEN false is returned"
    )
    @Test
    void isOut_returnsFalse_whenInNode() {
        NCLNodeNavigator navigator = new NCLNodeNavigator(loadSimpleNCL());
        navigator.enterNode(null, "call1");

        assertFalse(navigator.isOut());
    }

    @ParameterizedTest(name =
        "GIVEN {0}\n" +
        "WHEN navigation is performed\n" +
        "THEN the desired node is located"
    )
    @MethodSource("successfulTestCasesOfNavigation")
    void navigationSucceeds_whenSequenceOfStepsCorrect(
        @SuppressWarnings("unused") // Used in the name of the ParameterizedTest
        String testDescription,
        String nclCode, String expectedNodeName,
        Consumer<NCLNodeNavigator> operation,
        NCLNodePath expectedNodePath) {
        NCL ncl = new NCL();
        ncl.loadModule(nclCode.trim());

        NCLNodeNavigator navigator = new NCLNodeNavigator(ncl);
        operation.accept(navigator);

        NCLNodePath nodePath = navigator.exportPath();
        assertEquals(expectedNodeName, navigator.getNode().getNodeName());
        assertEquals(expectedNodePath, nodePath);
        assertEquals(nodePath.get(nodePath.getLength() - 1), navigator.getPointer());
    }

    @ParameterizedTest(name =
        "GIVEN {0}\n" +
        "WHEN navigation is performed\n" +
        "THEN an exception is thrown"
    )
    @MethodSource("failedTestCasesOfNavigation")
    void navigationFails_whenSequenceOfStepsIncorrect(
        @SuppressWarnings("unused") // Used in the name of the ParameterizedTest
        String testDescription,
        String nclCode,
        Consumer<NCLNodeNavigator> operation,
        Class<? extends Throwable> exceptionClass,
        String exceptionMessage) {

        NCL ncl = new NCL();
        ncl.loadModule(nclCode.trim());

        NCLNodeNavigator navigator = new NCLNodeNavigator(ncl);
        Throwable t = assertThrows(Throwable.class, () -> operation.accept(navigator));
        assertInstanceOf(exceptionClass, t);
        assertTrue(t.getMessage().contains(exceptionMessage));
    }

    static List<Arguments> successfulTestCasesOfNavigation() {
        String nclCode =
            "node1 [\n" +
            "    node2\n" +
            "    node3\n" +
            "]\n" +
            "node4 [\n" +
            "    node5 [\n" +
            "        node6\n" +
            "    ]\n" +
            "    node7\n" +
            "]";
        return Arrays.asList(
            successfulTestCaseOfNavigation(
                "a valid up and down navigation",
                nclCode,
                "node5",
                navigator -> {
                    navigator.enterNode(null, "node4");
                    navigator.goToChild(0);
                    navigator.goToChild(0);
                    navigator.goToParent();
                },
                new NCLNodePath(
                    new NCLNodePointer(null, "node4", 0))),
            successfulTestCaseOfNavigation(
                "a valid left and right navigation",
                nclCode,
                "node2",
                navigator -> {
                    navigator.enterNode(null, "node1");
                    navigator.goToChild(1);
                    navigator.goToPreviousSibling();
                    navigator.goToNextSibling();
                    navigator.goToPreviousSibling();
                },
                new NCLNodePath(
                    new NCLNodePointer(null, "node1", 0))),
            successfulTestCaseOfNavigation(
                "a valid in and out navigation",
                nclCode,
                "node4",
                navigator -> {
                    navigator.enterNode(null, "node1");
                    navigator.goToChild(1);
                    navigator.enterNode(null, "node4");
                    navigator.enterNode(null, "node1");
                    navigator.goToChild(0);
                    navigator.exitNode();
                },
                new NCLNodePath(
                    new NCLNodePointer(null, "node1", 1),
                    new NCLNodePointer(null, "node4"))),
            successfulTestCaseOfNavigation(
                "a valid navigation from a restored path",
                nclCode,
                "node1",
                navigator -> {
                    navigator.restorePath(new NCLNodePath(
                        new NCLNodePointer(null, "node1", 0),
                        new NCLNodePointer(null, "node4", 0, 0)));
                    navigator.exitNode();
                    navigator.goToParent();
                },
                new NCLNodePath(
                    new NCLNodePointer(null, "node1")))
        );
    }

    static Arguments successfulTestCaseOfNavigation(
        String testDescription,
        String nclCode, String expectedNodeName,
        Consumer<NCLNodeNavigator> operation,
        NCLNodePath expectedNodePath) {
        return Arguments.of(
            testDescription, nclCode,
            expectedNodeName, operation, expectedNodePath);
    }

    static List<Arguments> failedTestCasesOfNavigation() {
        String nclCode =
            "node1 [\n" +
            "    node2 [\n" +
            "        node3\n" +
            "    ]\n" +
            "]\n" +
            "node4 [\n" +
            "    node5 [\n" +
            "        node6\n" +
            "    ]\n" +
            "]";
        return Arrays.asList(
            failedTestCaseOfNavigation(
                "going out even if there is no selected node",
                nclCode,
                navigator -> {
                    navigator.exitNode();
                },
                EmptyNavigatorException.class,
                "No node is selected"),
            failedTestCaseOfNavigation(
                "a navigation to a non-existent node",
                nclCode,
                navigator -> {
                    navigator.enterNode(null, "node1");
                    navigator.goToChild(0);
                    navigator.goToNextSibling();
                },
                NoSuchNodeException.class,
                "no next sibling of"),
            failedTestCaseOfNavigation(
                "a navigation to a non-existent module",
                nclCode,
                navigator -> {
                    navigator.enterNode("module", "node1");
                },
                NoSuchModuleException.class,
                "There is no module"),
            failedTestCaseOfNavigation(
                "restoring a path with a non-existent module",
                nclCode,
                navigator -> {
                    navigator.restorePath(new NCLNodePath(
                        new NCLNodePointer("module", "node1")));
                },
                NoSuchModuleException.class,
                "There is no module"),
            failedTestCaseOfNavigation(
                "restoring a path with a non-existent node",
                nclCode,
                navigator -> {
                    navigator.restorePath(new NCLNodePath(
                        new NCLNodePointer(null, "node1", 1)));
                },
                NoSuchNodeException.class,
                "no child at index")
        );
    }

    static Arguments failedTestCaseOfNavigation(
        String testDescription,
        String nclCode,
        Consumer<NCLNodeNavigator> operation,
        Class<? extends Throwable> exceptionClass,
        String exceptionMessage) {
        return Arguments.of(
            testDescription, nclCode,
            operation,
            exceptionClass, exceptionMessage);
    }

    static NCL loadSimpleNCL() {
        NCL ncl = new NCL();
        ncl.loadModule(
            "call1 [\n" +
            "    step1\n" +
            "    step2\n" +
            "    step3\n" +
            "]"
        );
        return ncl;
    }
}
