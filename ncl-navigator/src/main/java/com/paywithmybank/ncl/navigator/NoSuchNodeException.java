package com.paywithmybank.ncl.navigator;

public class NoSuchNodeException extends IllegalArgumentException {

    private NoSuchNodeException(String message) {
        super(message);
    }

    public static NoSuchNodeException create(NCLNodePointer pointer, Relationship relationship, int index) {
       StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("There is no ");
        if (relationship != null) {
            switch (relationship) {
                case PREVIOUS_SIBLING:
                    messageBuilder.append("previous sibling of ");
                    break;
                case NEXT_SIBLING:
                    messageBuilder.append("next sibling of ");
                    break;
                case PARENT:
                    messageBuilder.append("parent of ");
                    break;
                case CHILD:
                    messageBuilder.append("child at index ").append(index).append(" of ");
                    break;
            }
        }
        messageBuilder.append("node ");
        if (pointer.getDepth() > 0) {
            messageBuilder.append("at index ");
            for (int depth = 0; depth < pointer.getDepth(); depth ++) {
                messageBuilder.append('[').append(pointer.getIndexAtDepth(depth)).append(']');
            }
            messageBuilder.append(" of node ");
        }
        messageBuilder.append(pointer.getSelector()).append(" of module with ");
        if (pointer.getModuleName() != null) {
            messageBuilder.append("name ").append(pointer.getModuleName());
        } else {
            messageBuilder.append("no name");
        }
        return new NoSuchNodeException(messageBuilder.toString());
    }

    public static NoSuchNodeException noSuchChild(NCLNodePointer basePointer, int index) {
        return create(basePointer, Relationship.CHILD, index);
    }

    public static NoSuchNodeException noSuchParent(NCLNodePointer basePointer) {
        return create(basePointer, Relationship.PARENT, 0);
    }

    public static NoSuchNodeException noSuchNextSibling(NCLNodePointer basePointer) {
        return create(basePointer, Relationship.NEXT_SIBLING, 0);
    }

    public static NoSuchNodeException noSuchPreviousSibling(NCLNodePointer basePointer) {
        return create(basePointer, Relationship.PREVIOUS_SIBLING, 0);
    }

    private enum Relationship {
        PREVIOUS_SIBLING,
        NEXT_SIBLING,
        PARENT,
        CHILD
    }
}
