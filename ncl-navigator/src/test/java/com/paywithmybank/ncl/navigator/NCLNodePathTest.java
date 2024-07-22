package com.paywithmybank.ncl.navigator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NCLNodePathTest {

    @DisplayName(
        "GIVEN a valid NCL node path\n" +
        "WHEN new is called\n" +
        "THEN the path is correctly created"
    )
    @Test
    void new_createsPath_whenValidInput() {
        NCLNodePointer[] pointers = new NCLNodePointer[] {
            new NCLNodePointer("module1", "selector1", 1, 2, 3),
            new NCLNodePointer("module2", "selector2", 4, 5, 6)
        };
        NCLNodePath path = new NCLNodePath(pointers);
        assertEquals(2, path.getLength());
        assertEquals(pointers[0], path.get(0));
        assertEquals(pointers[1], path.get(1));
        // check if changes on the input array are NOT reflected.
        pointers[0] = new NCLNodePointer("module3", "selector3", 7, 8, 9);
        assertNotEquals(pointers[0], path.get(0));
    }

    @DisplayName(
        "GIVEN a null input\n" +
        "WHEN new is called\n" +
        "THEN a NullPointerException is thrown"
    )
    @Test
    void new_throwException_whenNullInput() {
        assertThrows(NullPointerException.class, () -> new NCLNodePath((NCLNodePointer[]) null));
    }

    @DisplayName(
        "GIVEN a list of node paths with null elements\n" +
        "WHEN new is called\n" +
        "THEN an IllegalArgumentException is thrown"
    )
    @Test
    void new_throwException_whenInputHasNullElements() {
        assertThrows(RuntimeException.class, () -> new NCLNodePath(new NCLNodePointer[1]));
    }

    @DisplayName(
        "GIVEN two paths that are the same object\n" +
        "WHEN equals is called\n" +
        "THEN true is returned"
    )
    @Test
    void equals_returnsTrue_whenPathsAreSame() {
        NCLNodePath path = new NCLNodePath(new NCLNodePointer("module", "selector"));
        assertTrue(path.equals(path));
    }

    @DisplayName(
        "GIVEN two equal paths\n" +
        "WHEN equals is called\n" +
        "THEN true is returned"
    )
    @Test
    void equals_returnsTrue_whenPathsAreEqual() {
        NCLNodePath path1 = new NCLNodePath(new NCLNodePointer("module", "selector"));
        NCLNodePath path2 = new NCLNodePath(new NCLNodePointer("module", "selector"));
        assertTrue(path1.equals(path2));
    }

    @DisplayName(
        "GIVEN a path and null\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenOtherIsNull() {
        NCLNodePath path = new NCLNodePath(new NCLNodePointer("module", "selector"));
        assertFalse(path.equals(null));
    }

    @DisplayName(
        "GIVEN a path and a non-path object\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenOtherIsNotPath() {
        NCLNodePath path = new NCLNodePath(new NCLNodePointer("module", "selector"));
        assertFalse(path.equals("SOMETHING"));
    }

    @DisplayName(
        "GIVEN two paths with different pointers\n" +
        "WHEN equals is called\n" +
        "THEN false is returned"
    )
    @Test
    void equals_returnsFalse_whenPointersAreDifferent() {
        NCLNodePath path1 = new NCLNodePath(new NCLNodePointer("module1", "selector1", 0, 1));
        NCLNodePath path2 = new NCLNodePath(new NCLNodePointer("module2", "selector2", 0, 2, 0));
        assertFalse(path1.equals(path2));
    }

    @DisplayName(
        "GIVEN a path\n" +
        "WHEN hashCode is called\n" +
        "THEN no exception is thrown"
    )
    @Test
    void hashCode_doesNotThrow_whenCalled() {
        NCLNodePath path = new NCLNodePath(new NCLNodePointer("module", "selector"));
        assertDoesNotThrow(() -> path.hashCode());
    }

    @DisplayName(
        "GIVEN an empty path\n" +
        "WHEN getLength is called\n" +
        "THEN 0 is returned"
    )
    @Test
    void isEmpty_returnsTrue_whenPathIsEmpty() {
        assertTrue(new NCLNodePath().isEmpty());
    }

    @DisplayName(
        "GIVEN a non-empty path\n" +
        "WHEN isEmpty is called\n" +
        "THEN false is returned"
    )
    @Test
    void isEmpty_returnsFalse_whenPathIsNotEmpty() {
        assertFalse(new NCLNodePath(new NCLNodePointer("module", "selector")).isEmpty());
    }

    @DisplayName(
        "GIVEN a valid NCL node path string\n" +
        "WHEN parse is called\n" +
        "THEN the path is correctly parsed"
    )
    @Test
    void parse_returnsPath_whenValidFormatIsValid() {
        NCLNodePath path = NCLNodePath.parse("\"module1\".\"selector1\"[1][2][3]/\"module2\".\"selector2\"[4][5][6]");
        assertEquals(2, path.getLength());
        assertEquals("module1", path.get(0).getModuleName());
        assertEquals("selector1", path.get(0).getSelector());
        assertEquals(3, path.get(0).getDepth());
        assertEquals(1, path.get(0).getIndexAtDepth(0));
        assertEquals(2, path.get(0).getIndexAtDepth(1));
        assertEquals(3, path.get(0).getIndexAtDepth(2));
        assertEquals("module2", path.get(1).getModuleName());
        assertEquals("selector2", path.get(1).getSelector());
        assertEquals(3, path.get(1).getDepth());
        assertEquals(4, path.get(1).getIndexAtDepth(0));
        assertEquals(5, path.get(1).getIndexAtDepth(1));
        assertEquals(6, path.get(1).getIndexAtDepth(2));
    }

    @DisplayName(
        "GIVEN a valid NCL node path string with only one node\n" +
        "WHEN parse is called\n" +
        "THEN the path is correctly parsed"
    )
    @Test
    void parse_returnsPath_whenValidFormatWithOneNodeIsValid() {
        NCLNodePath path = NCLNodePath.parse("\"module\".\"selector\"[1][2][3]");
        assertEquals(1, path.getLength());
        assertEquals("module", path.get(0).getModuleName());
        assertEquals("selector", path.get(0).getSelector());
        assertEquals(3, path.get(0).getDepth());
        assertEquals(1, path.get(0).getIndexAtDepth(0));
        assertEquals(2, path.get(0).getIndexAtDepth(1));
        assertEquals(3, path.get(0).getIndexAtDepth(2));
    }

    @DisplayName(
        "GIVEN an invalid NCL node path string\n" +
        "WHEN parse is called\n" +
        "THEN an IllegalArgumentException is thrown"
    )
    @Test
    void parse_throwsException_whenInvalidFormatIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> NCLNodePath.parse("\"module\".\"selector\"[1][2][3]\"module2\".\"selector2\"[4][5][6]"));
    }

    @DisplayName(
        "GIVEN a NCL node path\n" +
        "WHEN stringify is called\n" +
        "THEN the path is correctly converted to a string"
    )
    @Test
    void stringify_returnsString_whenPathIsValid() {
        NCLNodePath path = new NCLNodePath(
            new NCLNodePointer("module1", "selector1", 1, 2, 3),
            new NCLNodePointer("module2", "selector2", 4, 5, 6)
        );
        assertEquals("\"module1\".\"selector1\"[1][2][3]/\"module2\".\"selector2\"[4][5][6]", NCLNodePath.stringify(path));
    }
}
