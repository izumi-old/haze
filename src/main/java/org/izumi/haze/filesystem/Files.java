package org.izumi.haze.filesystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Files extends LinkedList<File> {
    public Files() {
    }

    public Files(Collection<? extends File> c) {
        super(c);
        for (File file : c) {
            if (file == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public Collection<Extension> getDistinctExtensions() {
        Set<Extension> extensions = new HashSet<>();
        for (File file : this) {
            extensions.add(file.getExtension());
        }

        return extensions;
    }
    
    public Files getAllByExtension(Extension extension) {
        Files result = new Files();
        for (File file : this) {
            if (file.getExtension().equals(extension)) {
                result.add(file);
            }
        }
        
        return result;
    }
}
