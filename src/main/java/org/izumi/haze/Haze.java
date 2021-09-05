package org.izumi.haze;

import java.util.Collection;
import java.util.List;

public class Haze {
    public static void main(String[] args) {
        Collection<String> packagesToHandle = List.of("/home/aiden/IdeaProjects/LifeTraceHelp (copy)");
        Obfuscation obfuscation = new Obfuscation(packagesToHandle);
        obfuscation.launch();
    }
}
