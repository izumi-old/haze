package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.File;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.ModuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@RequiredArgsConstructor
public class Obfuscation {
    private static final Logger log = LoggerFactory.getLogger(Obfuscation.class);
    private final ModuleFactory moduleFactory;
    private final Collection<String> paths;

    public void launch() {
        Parsing parsing = new Parsing(paths);
        parsing.parse();
        Wishes wishes = parsing.getWishes();

        Collection<Extension> extensions = wishes.getDistinctExtensions();
        for (Extension extension : extensions) {
            try {
                Module module = moduleFactory.getAbleToHandle(extension);
                for (Wish wish : wishes) {
                    String content = module.handle(wish.source.getContent());
                    File target = wish.target;
                    target.changeContent(content);
                    target.save();
                }
            } catch (HazeException ex) {
                log.warn("Unable to handle files with extension: " + extension);
            }
        }
    }
}
