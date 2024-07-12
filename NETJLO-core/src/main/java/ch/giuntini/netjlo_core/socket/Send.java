package ch.giuntini.netjlo_core.socket;

import ch.giuntini.netjlo_core.packages.BasePackage;

public interface Send<P extends BasePackage<?>> {

    default void send(P p) {
        throw new UnsupportedOperationException("");
    }

    default void sendToAll(P p) {
        throw new UnsupportedOperationException("");
    }
}
