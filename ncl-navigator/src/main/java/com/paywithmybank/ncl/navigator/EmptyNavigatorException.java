package com.paywithmybank.ncl.navigator;

public class EmptyNavigatorException extends RuntimeException {
    public EmptyNavigatorException() {
        super("No node is selected in the navigator");
    }
}
