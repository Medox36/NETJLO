package ch.giuntini.netjlo_timed.streams;

import ch.giuntini.netjlo_timed.packages.TimedPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;


public class TimedPackageObjectInputStream<P extends TimedPackage<?>> extends ObjectInputStream {

    private final Class<P> pack;

    public TimedPackageObjectInputStream(InputStream in, Class<P> pack) throws IOException {
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
