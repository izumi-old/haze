package org.izumi.haze.filesystem;

import java.nio.file.Path;

public class LazyFile extends File {
    private boolean fieldsInitiated = false;
    private boolean contentLoaded = false;

    public LazyFile(Path path) {
        super(path);
    }

    @Override
    public Extension getExtension() {
        initiateFieldsIfNecessary();
        return super.getExtension();
    }

    @Override
    public String getFilename() {
        initiateFieldsIfNecessary();
        return super.getFilename();
    }

    @Override
    public String getFullName() {
        initiateFieldsIfNecessary();
        return super.getFullName();
    }

    @Override
    public String getContent() {
        loadContentIfNecessary();
        return super.getContent();
    }

    @Override
    public boolean isSupported() {
        initiateFieldsIfNecessary();
        return super.isSupported();
    }

    @Override
    public void save() {
        loadContentIfNecessary();
        super.save();
    }

    private void initiateFieldsIfNecessary() {
        if (!fieldsInitiated) {
            initiateFields();
            fieldsInitiated = true;
        }
    }

    private void loadContentIfNecessary() {
        initiateFieldsIfNecessary();
        if (!contentLoaded) {
            loadContent();
            contentLoaded = true;
        }
    }
}
