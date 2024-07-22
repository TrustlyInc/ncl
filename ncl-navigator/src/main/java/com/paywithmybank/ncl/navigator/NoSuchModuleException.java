package com.paywithmybank.ncl.navigator;

public class NoSuchModuleException extends IllegalArgumentException {

    public NoSuchModuleException(String moduleName) {
        super("There is no module with " + (moduleName != null ? "name " + moduleName : "no name") + "!");
    }
}
