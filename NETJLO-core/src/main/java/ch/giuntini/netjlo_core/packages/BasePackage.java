package ch.giuntini.netjlo_core.packages;

import java.io.Serializable;

public class BasePackage<T> implements Serializable {

    private final T information;

    public BasePackage(T information) {
        this.information = information;
    }

    public T getInformation() {
        return information;
    }
}
