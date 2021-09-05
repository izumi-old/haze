package org.izumi.haze;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {
    public static final String DEFAULT_SAVING_PATH_AS_STRING = System.getProperty("user.dir") + "/obfuscated/";
    public static final Path DEFAULT_SAVING_PATH = Paths.get(DEFAULT_SAVING_PATH_AS_STRING);
}
