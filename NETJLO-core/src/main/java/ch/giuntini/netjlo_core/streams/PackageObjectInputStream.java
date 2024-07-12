package ch.giuntini.netjlo_core.streams;

import ch.giuntini.netjlo_core.packages.BasePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class PackageObjectInputStream<P extends BasePackage<?>> extends ObjectInputStream {

    private final Class<P> pack;

    public PackageObjectInputStream(InputStream in, Class<P> pack) throws IOException {
        super(in);
        this.pack = pack;
        setObjectInputFilter(filterInfo -> {
            if (filterInfo.serialClass().equals(pack))
                return ObjectInputFilter.Status.ALLOWED;
            else
                return ObjectInputFilter.Status.REJECTED;
        });
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass desc = super.readClassDescriptor();
        if (desc.getName().equals(pack.getName())) {
            return ObjectStreamClass.lookup(pack);
        }
        return desc;
    }
}
