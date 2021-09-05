package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.Files;
import org.izumi.haze.modules.ModuleFactory;

import java.util.Collection;

@RequiredArgsConstructor
public class Obfuscation {
    private final ModuleFactory moduleFactory;
    private final Collection<String> paths;

    public void launch() {
        Request request = new Request(paths);
        request.handlePaths();
        Files files = request.getFiles();

        Collection<Extension> extensions = files.getDistinctExtensions();
        for (Extension extension : extensions) {
            moduleFactory.getAbleToHandle(extension).handle(files.getAllByExtension(extension));
        }
    }
}
