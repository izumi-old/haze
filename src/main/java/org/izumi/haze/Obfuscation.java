package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@RequiredArgsConstructor
public class Obfuscation {
    private static final Logger log = LoggerFactory.getLogger(Obfuscation.class);
    private final Collection<String> paths;

    public void launch() {
        Request request = new Request(paths);
        request.handlePaths();
        Collection<File> files = request.getFiles();
        int debug = 4;
    }
}
