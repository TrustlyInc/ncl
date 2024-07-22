package com.paywithmybank.ncl.navigator;

import com.paywithmybank.ncl.NCL;
import com.paywithmybank.ncl.model.Node;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NCLNodePointerTest {

    @ParameterizedTest(name =
        "GIVEN {0}\n" +
        "WHEN new is called\n" +
        "THEN a new NCL node pointer is created"
    )
    @MethodSource("successfulTestCasesOfNew")
    void new_createsInstance_whenValidInput(
        @SuppressWarnings("unused") // Used in the name of the ParameterizedTest
        String testDescription,
        String moduleName, String selector, int[] indices) {
        NCLNodePointer pointer = new NCLNodePointer(moduleName, selector, indices);
        assertEquals(moduleName, pointer.getModuleName());
        assertEquals(selector, pointer.getSelector());
        assertEquals(indices.length, pointer.getDepth());
        for (int i = 0; i < indices.length; i++) {
            assertEquals(indices[i], pointer.getIndexAtDepth(i));
        }
        // check if changes on the input array are NOT reflected.
        for (int i = 0; i < indices.length; i++) {
            indices[i] += 1;
            assertEquals(indices[i] - 1, pointer.getIndexAtDepth(i));
        }
    }

    @ParameterizedTest(name =
        "GIVEN {0}\n" +
        "WHEN new is called\n" +
        "THEN an exception is thrown"
    )
    @MethodSource("failedTestCasesOfNew")
    void new_throwsException_whenInvalidInput(
        @SuppressWarnings("unused") // Used in the name of the ParameterizedTest
        String testDescription,
        String moduleName, String selector, int[] indices, String errorMessage) {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new NCLNodePointer(moduleName, selector, indices);
        });
        assertTrue(exception.getMessage().contains(errorMessage));
    }

    static List<Arguments> successfulTestCasesOfNew() {
        return Arrays.asList(
            successfulTestCaseOfNew(
                "all arguments provided with valid values",
                "module", "selector", new int[]{ 1, 2, 3 }),
            successfulTestCaseOfNew(
                "an empty index list",
                "module", "selector", new int[]{}),
            successfulTestCaseOfNew(
                "no module provided",
                null, "selector", new int[]{ 4, 5, 6 })
        );
    }

    static Arguments successfulTestCaseOfNew(
        String testDescription, String moduleName, String selector, int[] indices) {
        return Arguments.of(testDescription, moduleName, selector, indices);
    }

    static List<Arguments> failedTestCasesOfNew() {
        return Arrays.asList(
            failedTestCaseOfNew(
                "a null selector",
                "module", null, new int[]{ 1, 2, 3 },
                "selector must be provided"),
            failedTestCaseOfNew(
                "a null index list",
                "module", "selector", null,
                "child indices must be provided"),
            failedTestCaseOfNew(
                "a negative index",
                "module", "selector", new int[]{ 1, -1, 3 },
                "child index must greater than or equal to 0")
        );
    }

    static Arguments failedTestCaseOfNew(
        String testDescription, String moduleName, String selector, int[] indices, String errorMessage) {
        return Arguments.of(testDescription, moduleName, selector, indices, errorMessage);
    }

    @DisplayName(
        "GIVEN a valid NCL node pointer string\n" +
        "WHEN parse is called\n" +
        "THEN the module name, selector and child indices are correctly parsed"
    )
    @Test
    void parse_returnsPointer_whenFullFormatIsValid() {
        NCLNodePointer pointer = NCLNodePointer.parse("\"module\".\"selector\"[1][2][3]");
        assertEquals("module", pointer.getModuleName());
        assertEquals("selector", pointer.getSelector());
        assertEquals(3, pointer.getDepth());
        assertEquals(1, pointer.getIndexAtDepth(0));
        assertEquals(2, pointer.getIndexAtDepth(1));
        assertEquals(3, pointer.getIndexAtDepth(2));
    }

    @DisplayName(
        "GIVEN a valid NCL node pointer string without module name\n" +
        "WHEN parse is called\n" +
        "THEN the selector and child indices are correctly parsed"
    )
    @Test
    void parse_returnsPointer_whenFormatWithoutModuleIsValid() {
        NCLNodePointer pointer = NCLNodePointer.parse("\"selector\"[1][2][3]");
        assertNull(pointer.getModuleName());
        assertEquals("selector", pointer.getSelector());
        assertEquals(3, pointer.getDepth());
        assertEquals(1, pointer.getIndexAtDepth(0));
        assertEquals(2, pointer.getIndexAtDepth(1));
        assertEquals(3, pointer.getIndexAtDepth(2));
    }

    @DisplayName(
        "GIVEN a valid NCL node pointer string without child indices\n" +
        "WHEN parse is called\n" +
        "THEN the module name and selector are correctly parsed"
    )
    @Test
    void parse_returnsPointer_whenFormatWithoutIndicesIsValid() {
        NCLNodePointer pointer = NCLNodePointer.parse("\"module\".\"selector\"");
        assertEquals("module", pointer.getModuleName());
        assertEquals("selector", pointer.getSelector());
        assertEquals(0, pointer.getDepth());
    }

    @DisplayName(
        "GIVEN an empty string\n" +
        "WHEN parse is called\n" +
        "THEN an IllegalArgumentException is thrown"
    )
    @Test
    void parse_throwsException_whenStringIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            NCLNodePointer.parse("");
        });
    }

    @DisplayName(
        "GIVEN an invalid NCL node pointer string\n" +
        "WHEN parse is called\n" +
        "THEN an IllegalArgumentException is thrown"
    )
    @Test
    void parse_throwsException_whenStringIsWrongFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            NCLNodePointer.parse("\"invalid\"format\"");
        });
    }

    @DisplayName(
        "GIVEN an invalid NCL node pointer string with wrong index format\n" +
        "WHEN parse is called\n" +
        "THEN an IllegalArgumentException is thrown"
    )
    @Test
    void parse_throwsException_whenStringIsWrongFormatWithWrongIndices() {
        assertThrows(IllegalArgumentException.class, () -> {
            NCLNodePointer.parse("\"selector\"[1][a][3]");
        });
    }

    @DisplayName(
        "GIVEN a valid NCL node pointer\n" +
        "WHEN stringify is called\n" +
        "THEN the pointer is correctly converted to a string"
    )
    @Test
    void stringify_returnsString_whenPointerIsValid() {
        NCLNodePointer pointer = new NCLNodePointer("module", "selector", 1, 2, 3);
        assertEquals("\"module\".\"selector\"[1][2][3]", NCLNodePointer.stringify(pointer));
    }

    @DisplayName(
        "GIVEN a valid NCL node pointer without module name\n" +
        "WHEN stringify is called\n" +
        "THEN the pointer is correctly converted to a string"
    )
    @Test
    void stringify_returnsString_whenPointerHasNoModule() {
        NCLNodePointer pointer = new NCLNodePointer(null, "selector", 1, 2, 3);
        assertEquals("\"selector\"[1][2][3]", NCLNodePointer.stringify(pointer));
    }

    @DisplayName(
        "GIVEN a valid NCL node pointer without child indices\n" +
        "WHEN stringify is called\n" +
        "THEN the pointer is correctly converted to a string"
    )
    @Test
    void stringify_returnsString_whenPointerHasNoIndices() {
        NCLNodePointer pointer = new NCLNodePointer("module", "selector");
        assertEquals("\"module\".\"selector\"", NCLNodePointer.stringify(pointer));
    }

    @DisplayName(
        "GIVEN two equal NCL node pointers\n" +
        "WHEN equals is called\n" +
        "THEN true is returned"
    )
    @Test
    void equals_returnsTrue_whenPointersAreEqual() {
        NCLNodePointer pointer1 = new NCLNodePointer("module", "selector", 1, 2, 3);
        NCLNodePointer pointer2 = new NCLNodePointer("module", "selector", 1, 2, 3);
        assertTrue(pointer1.equals(pointer2));
    }

    @DisplayName(
        "GIVEN two NCL node pointers with different modules\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenModulesAreNotEqual() {
        NCLNodePointer pointer1 = new NCLNodePointer("module1", "selector", 1, 2, 3);
        NCLNodePointer pointer2 = new NCLNodePointer("module2", "selector", 1, 2, 3);
        assertFalse(pointer1.equals(pointer2));
    }

    @DisplayName(
        "GIVEN two NCL node pointers with different selectors\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenSelectorsAreNotEqual() {
        NCLNodePointer pointer1 = new NCLNodePointer("module", "selector1", 1, 2, 3);
        NCLNodePointer pointer2 = new NCLNodePointer("module", "selector2", 1, 2, 3);
        assertFalse(pointer1.equals(pointer2));
    }

    @DisplayName(
        "GIVEN two NCL node pointers with different depths\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenDepthsAreNotEqual() {
        NCLNodePointer pointer1 = new NCLNodePointer("module", "selector", 1, 2, 3);
        NCLNodePointer pointer2 = new NCLNodePointer("module", "selector", 1, 2);
        assertFalse(pointer1.equals(pointer2));
    }

    @DisplayName(
        "GIVEN two NCL node pointers with different indices\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenIndicesAreNotEqual() {
        NCLNodePointer pointer1 = new NCLNodePointer("module", "selector", 1, 2, 3);
        NCLNodePointer pointer2 = new NCLNodePointer("module", "selector", 1, 2, 4);
        assertFalse(pointer1.equals(pointer2));
    }

    @DisplayName(
        "GIVEN a NCL node pointer and null\n" +
        "WHEN equals is called\n" +
        "THEN true is returned"
    )
    @Test
    void equals_returnsFalse_whenOtherIsNull() {
        NCLNodePointer pointer = new NCLNodePointer("module", "selector", 1, 2, 3);
        assertFalse(pointer.equals(null));
    }

    @DisplayName(
        "GIVEN a NCL node pointer and a non-pointer object\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenOtherIsNotPointer() {
        NCLNodePointer pointer = new NCLNodePointer("module", "selector", 1, 2, 3);
        assertFalse(pointer.equals("SOMETHING"));
    }

    @DisplayName(
        "GIVEN a NCL node pointer\n" +
        "WHEN hashCode is called\n" +
        "THEN no exception is thrown"
    )
    @Test
    void hashCode_doesNotThrow_whenCalled() {
        NCLNodePointer pointer = new NCLNodePointer("module", "selector", 1, 2, 3);
        assertDoesNotThrow(() -> pointer.hashCode());
    }

    @DisplayName(
        "GIVEN a NCL node pointer\n" +
        "WHEN childPointer is called\n" +
        "THEN a new pointer with the child index is returned"
    )
    @Test
    void childPointer_returnsChildPointer() {
        NCLNodePointer pointer = new NCLNodePointer("module", "selector", 1, 2, 3);
        NCLNodePointer childPointer = pointer.childPointer(4);
        assertEquals("module", childPointer.getModuleName());
        assertEquals("selector", childPointer.getSelector());
        assertEquals(4, childPointer.getDepth());
        assertEquals(1, childPointer.getIndexAtDepth(0));
        assertEquals(2, childPointer.getIndexAtDepth(1));
        assertEquals(3, childPointer.getIndexAtDepth(2));
        assertEquals(4, childPointer.getIndexAtDepth(3));
    }

    @DisplayName(
        "GIVEN a NCL node pointer without indices\n" +
        "WHEN isRoot is called\n" +
        "THEN true is returned"
    )
    @Test
    void isRoot_returnsTrue_whenPointerHasNoIndices() {
        NCLNodePointer pointer = new NCLNodePointer(null, "selector");
        assertTrue(pointer.isRoot());
    }

    @DisplayName(
        "GIVEN a NCL node pointer with indices\n" +
        "WHEN isRoot is called\n" +
        "THEN false is returned"
    )
    @Test
    void isRoot_returnsFalse_whenPointerHasIndices() {
        NCLNodePointer pointer = new NCLNodePointer(null, "selector", 1, 2, 3);
        assertFalse(pointer.isRoot());
    }

    @ParameterizedTest(name =
        "GIVEN {0}\n" +
        "AND a NCL code where that node can be found\n" +
        "WHEN findNode is called\n" +
        "THEN the node with the given path is returned"
    )
    @MethodSource("successfulTestCasesOfFindNode")
    void findNode_returnsNode_whenNodeExists(
        @SuppressWarnings("unused") // Used in the name of the ParameterizedTest
        String testDescription,
        String nclCode, NCLNodePointer nclPointer, String expectedNodeName) {
        NCL ncl = new NCL();
        ncl.loadModule(nclCode.trim());
        Node node = nclPointer.findNode(ncl);

        assertNotNull(node);
        assertEquals(expectedNodeName, node.getNodeName());
    }

    @ParameterizedTest(name =
        "GIVEN {0}\n" +
        "AND a NCL code where that node can't be found\n" +
        "WHEN findNode is called\n" +
        "THEN an exception is thrown"
    )
    @MethodSource("failedTestCasesOfFindNode")
    void findNode_throwsException_whenNodeDoesNotExist(
        @SuppressWarnings("unused") // Used in the name of the ParameterizedTest
        String testDescription,
        String nclCode, NCLNodePointer nclPointer) {
        NCL ncl = new NCL();
        ncl.loadModule(nclCode.trim());
        assertThrows(RuntimeException.class, () -> nclPointer.findNode(ncl));
    }

    static List<Arguments> successfulTestCasesOfFindNode() {
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
            successfulTestCaseOfFindNode(
                "a root node path",
                nclCode,
                new NCLNodePointer(null, "node1"),
                "node1"),
            successfulTestCaseOfFindNode(
                "a child node path",
                nclCode,
                new NCLNodePointer(null, "node4", 1),
                "node7"),
            successfulTestCaseOfFindNode(
                "a child of a child node path",
                nclCode,
                new NCLNodePointer(null, "node4", 0, 0),
                "node6"),
            successfulTestCaseOfFindNode(
                "an root node that has not been explicitly declared",
                nclCode,
                new NCLNodePointer(null, "nodeX"),
                "nodeX")
        );
    }

    static Arguments successfulTestCaseOfFindNode(String testDescription, String nclCode, NCLNodePointer nclPointer, String expectedNodeName) {
        return Arguments.of(testDescription, nclCode, nclPointer, expectedNodeName);
    }

    static List<Arguments> failedTestCasesOfFindNode() {
        String nclCode =
            "node1 [\n" +
            "    node2\n" +
            "    node3\n" +
            "]";
        return Arrays.asList(
            failedTestCaseOfFindNode(
                "an out-of-bounds child node",
                nclCode,
                new NCLNodePointer(null, "node1", 2)),
            failedTestCaseOfFindNode(
                "a child node of a node with no children",
                nclCode,
                new NCLNodePointer(null, "node1", 0, 0)),
            failedTestCaseOfFindNode(
                "a non-existing module",
                nclCode,
                new NCLNodePointer("module", "selector"))
        );
    }

    static Arguments failedTestCaseOfFindNode(String testDescription, String nclCode, NCLNodePointer nclPointer) {
        return Arguments.of(testDescription, nclCode, nclPointer);
    }
}
