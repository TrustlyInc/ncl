package com.paywithmybank.ncl.navigator;

import com.paywithmybank.ncl.Module;
import com.paywithmybank.ncl.NCL;
import com.paywithmybank.ncl.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A value-based class that uniquely identifies a node in a NCL document.
 * <p />
 * The NCL node pointer can be represented as a string in the following formats:
 * <ul>
 * <li>{@code "module"."selector"[index1][index2]...[indexN]}</li>
 * <li>{@code "selector"[index1][index2]...[indexN]}</li>
 * <li>{@code "module"."selector"}</li>
 * <li>{@code "selector"}</li>
 * </ul>
 */
public class NCLNodePointer {

    // important: using *+ instead of * in regex to make it non-greedy.
    private static final Pattern NODE_POINTER_PATTERN =
        Pattern.compile("^(?:(\"[^\"]+\")\\.)?(\"[^\"]+\")((?:\\[\\d+\\])*+)$");

    private static final Pattern INDEX_PATTERN = Pattern.compile("\\[(\\d+)\\]");

    private final String moduleName;

    private final String selector;

    private final int[] childIndices;

    public NCLNodePointer(String moduleName, String selector, int... childIndices) {
        if (selector == null) {
            throw new NullPointerException("The selector must be provided");
        }
        if (childIndices == null) {
            throw new NullPointerException("The child indices must be provided");
        }
        for (int childIndex : childIndices) {
            if (childIndex < 0) {
                throw new IllegalArgumentException("The child index must greater than or equal to 0");
            }
        }

        this.moduleName = moduleName;
        this.selector = selector;
        this.childIndices = childIndices.clone();
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getSelector() {
        return selector;
    }

    /**
     * Gets the total depth of the node referenced in this node pointer.
     * <p />
     * The depth refers to the number of child nodes that must be traversed to
     * get to the referenced node.
     * @return The total depth of the node referenced in this node pointer.
     */
    public int getDepth() {
        return childIndices.length;
    }

    /**
     * Gets the index of the child node at the specified depth.
     * @param depth The depth of the child node.
     * @return The index of the child node at the specified depth.
     * @throws IndexOutOfBoundsException If the depth is out of bounds.
     */
    public int getIndexAtDepth(int depth) {
        return childIndices[depth];
    }

    /**
     * Returns a node pointer pointing to a child node of the current node.
     * @param index The index of the child node.
     * @return A new node pointer pointing to the child node.
     */
    public NCLNodePointer childPointer(int index) {
        int[] subChildIndices = new int[childIndices.length  + 1];
        System.arraycopy(childIndices, 0, subChildIndices, 0, childIndices.length);
        subChildIndices[subChildIndices.length - 1] = index;
        return new NCLNodePointer(moduleName, selector, subChildIndices);
    }

    /**
     * Returns {@code true} if this node pointer points to a root node.
     * @return {@code true} if this node pointer points to a root node.
     */
    public boolean isRoot() {
        return childIndices.length == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NCLNodePointer)) {
            return false;
        }
        NCLNodePointer that = (NCLNodePointer) o;
        return (
                Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(selector, that.selector) &&
                Objects.deepEquals(childIndices, that.childIndices)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, selector, Arrays.hashCode(childIndices));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (moduleName != null) {
            builder.append('"').append(moduleName).append('"').append('.');
        }

        builder.append('"').append(selector).append('"');

        for (int index : childIndices) {
            builder.append('[').append(index).append(']');
        }

        return builder.toString();
    }

    /**
     * Finds the node referenced by this node pointer in a NCL document.
     * @param ncl The NCL document.
     * @return The node referenced by this node pointer.
     * @throws NoSuchModuleException If the module does not exist.
     * @throws NoSuchNodeException If the node does not exist.
     */
    public Node findNode(NCL ncl) {
        Module module;
        try {
            module = ncl.getModule(moduleName);
        } catch (RuntimeException e) {
            throw new NoSuchModuleException(moduleName);
        }

        Node node = module.getNode(selector);
        for (int i = 0; i < childIndices.length; i ++) {
            List<Node> children = node.getChildren();
            int childIndex = childIndices[i];
            if (children == null || childIndex >= children.size()) {
                int[] childIndicesForError = new int[i];
                System.arraycopy(this.childIndices, 0, childIndicesForError, 0, i);
                throw NoSuchNodeException.noSuchChild(new NCLNodePointer(moduleName, selector, childIndicesForError), childIndices[i]);
            }
            node = children.get(childIndex);
        }

        return node;
    }

    public static String stringify(NCLNodePointer nodePointer) {
        return nodePointer.toString();
    }

    public static NCLNodePointer parse(String string) {
        Matcher matcher = NODE_POINTER_PATTERN.matcher(string);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a valid format!");
        }

        String moduleName = matcher.group(1);
        String selector = matcher.group(2);
        String strIndices = matcher.group(3);

        if (moduleName != null) {
            moduleName = moduleName.substring(1, moduleName.length() - 1);
        }
        selector = selector.substring(1, selector.length() - 1);
        int[] indices = parseIndices(strIndices);

        return new NCLNodePointer(moduleName, selector, indices);
    }

    private static int[] parseIndices(String strIndices) {
        if (strIndices.isEmpty()) {
            return new int[0];
        }

        List<Integer> numbers = new ArrayList<>();

        Matcher matcher = INDEX_PATTERN.matcher(strIndices);

        while (matcher.find()) {
            String numberString = matcher.group(1);
            int number = Integer.parseInt(numberString);
            numbers.add(number);
        }

        int[] result = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            result[i] = numbers.get(i);
        }

        return result;
    }
}
