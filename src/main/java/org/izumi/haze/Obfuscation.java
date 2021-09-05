package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.File;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.ModuleFactory;

import java.util.Collection;

@RequiredArgsConstructor
public class Obfuscation {
    private final ModuleFactory moduleFactory;
    private final Collection<String> paths;

    public void launch() {
        Parsing parsing = new Parsing(paths);
        parsing.parse();
        Wishes wishes = parsing.getWishes();

        Collection<Extension> extensions = wishes.getDistinctExtensions();
        for (Extension extension : extensions) {
            Module module = moduleFactory.getAbleToHandle(extension);
            for (Wish wish : wishes) {
                File target = wish.target;
                String content = module.handle(wish.source);
                target.changeContent(content);
                target.save();
            }
        }
    }
}
