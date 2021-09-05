package org.izumi.haze.modules.impl;

import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.Files;
import org.izumi.haze.modules.Module;
import org.springframework.stereotype.Component;

@Component
public class JavaModule implements Module {

    @Override
    public boolean canHandle(Extension extension) {
        return extension != null && extension.is("java");
    }

    @Override
    public void handle(Files files) {
        throw new UnsupportedOperationException(); //TODO: implement
    }
}
