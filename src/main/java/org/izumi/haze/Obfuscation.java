package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.File;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.ModuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                Map<UUID, Wish> wishesMap = new HashMap<>();
                Map<UUID, String> contents = new HashMap<>();
                for (Wish wish : wishes) {
                    UUID id = UUID.randomUUID();
                    contents.put(id, wish.source.getContent());
                    wishesMap.put(id, wish);
                }

                Map<UUID, String> handled = module.handle(contents);

                for (Map.Entry<UUID, String> entry : handled.entrySet()) {
                    Wish wish = wishesMap.get(entry.getKey());
                    File target = wish.target;
                    target.changeContent(entry.getValue());
                    target.save();
                }
            } catch (HazeException ex) {
                log.warn("Unable to handle files with extension: " + extension);
            }
        }
    }
}
