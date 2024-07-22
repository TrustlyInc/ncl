package com.paywithmybank.ncl.navigator;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A value-based class that represents a navigation path in a NCL document.
 * <p />
 * During NCL navigation, a given software might need to start new downward
 * paths multiple times, while keeping track to where it was before each new
 * one. A NCL node path is the backtracking of this navigation.
 * <p />
 * A node path is analogous to a call stack.
 * <p />
 * The NCL node path can be represented as a series of NCL node pointers
 * separated by forward slashes ({@code /}).
 */
public class NCLNodePath {

    private final NCLNodePointer[] pointers;

    public NCLNodePath(NCLNodePointer... pointers) {
        if (pointers == null) {
            throw new NullPointerException("The node pointers must be provided");
        }
        for (int i = 0; i < pointers.length; i ++) {
            if (pointers[i] == null) {
                throw new NullPointerException("The node pointer at index " + i + " must not be null");
            }
        }

        this.pointers = pointers.clone();
    }

    /**
     * Gets the length of this node path.
     * @return The length of this node path
     */
    public int getLength() {
        return pointers.length;
    }

    public NCLNodePointer get(int index) {
        return pointers[index];
    }

    public boolean isEmpty() {
        return pointers.length == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NCLNodePath)) {
            return false;
        }
        NCLNodePath that = (NCLNodePath) o;
        return Objects.deepEquals(pointers, that.pointers);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pointers);
    }

    @Override
    public String toString() {
        return Arrays.stream(pointers)
            .map(NCLNodePointer::stringify)
            .collect(Collectors.joining("/"));
    }

    public static NCLNodePath parse(String string) {
        String[] split = string.split("/");
        NCLNodePointer[] pointers = Arrays.stream(split)
            .map(NCLNodePointer::parse)
            .toArray(NCLNodePointer[]::new);
        return new NCLNodePath(pointers);
    }

    public static String stringify(NCLNodePath nodePath) {
        return nodePath.toString();
    }
}
