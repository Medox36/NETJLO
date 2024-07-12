package ch.giuntini.netjlo_default.streams;

import ch.giuntini.netjlo_core.streams.PackageObjectInputStream;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

import java.io.IOException;
import java.io.InputStream;

public class DefaultPackageObjectInputStream extends PackageObjectInputStream<DefaultPackage> {

    public DefaultPackageObjectInputStream(InputStream in) throws IOException {
        super(in, DefaultPackage.class);
    }
}
