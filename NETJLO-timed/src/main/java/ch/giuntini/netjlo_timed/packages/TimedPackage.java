package ch.giuntini.netjlo_timed.packages;

import ch.giuntini.netjlo_core.packages.BasePackage;

import java.util.Date;

public class TimedPackage<T> extends BasePackage<T> {

    /**
     * the timestamp at which the package should be interpreted by the maschine the socket is connected to
     */
    public Date timeStamp;

    public TimedPackage(T information) {
        super(information);
    }
}