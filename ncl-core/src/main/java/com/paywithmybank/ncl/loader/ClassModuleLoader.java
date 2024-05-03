package com.paywithmybank.ncl.loader;

import com.paywithmybank.ncl.model.Source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassModuleLoader implements ModuleLoader {
    Class<?> loader;
    String path;

    public ClassModuleLoader() {
            this.loader = this.getClass();
            this.path = null;
    }

    public ClassModuleLoader(Class<?> loader, String path) {
            this.loader = loader;
            this.path = formatPath(path);
    }

    @Override
    public Source openModule(String filename) {
    try {
        if (path == null || path.trim().isEmpty()) {
            String fileName = "/" + filename + ".ncl";
            return new Source (fileName , loader.getResourceAsStream(fileName));
        } else {
            String fileName = path + filename + ".ncl";
            return new Source(fileName, Files.newInputStream(Paths.get(fileName)));
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Guarantees that the path ends with \ or /,
     * so that we can append the file name after.
     */
        private static String formatPath(String path) {
        char lastChar = path.charAt(path.length() - 1);
        if (lastChar == '\\' || lastChar == '/') {
            return path;
        } else {
            return path + '/';
        }
    }
}
