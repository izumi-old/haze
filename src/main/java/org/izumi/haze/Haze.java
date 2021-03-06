package org.izumi.haze;

import org.izumi.haze.filesystem.Home;
import org.izumi.haze.modules.ModuleFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;
import java.util.List;

public class Haze {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("org.izumi.haze");

        Home home = context.getBean(Home.class);
        home.createIfNotExists();
        home.clear();

        Collection<String> packagesToHandle = List.of("/home/aiden/IdeaProjects/sample");
        /*Collection<String> packagesToHandle = List.of("/home/aiden/IdeaProjects/LifeTraceHelp (copy)",
                System.getProperty("user.dir") + "/src");*/
        Obfuscation obfuscation = new Obfuscation(context.getBean(ModuleFactory.class), packagesToHandle);
        obfuscation.launch();
    }
}
