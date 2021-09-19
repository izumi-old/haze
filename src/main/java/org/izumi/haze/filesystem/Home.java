package org.izumi.haze.filesystem;

import org.izumi.haze.Settings;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public final class Home extends Directory {
    public Home() {
        super(Path.of(Settings.DEFAULT_SAVING_PATH_AS_STRING));
    }
}
