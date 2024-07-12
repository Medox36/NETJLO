package ch.giuntini.netjlo_timed.sockets;

import ch.giuntini.netjlo_core.socket.Send;
import ch.giuntini.netjlo_timed.packages.TimedPackage;

import java.util.Date;

public interface TimedSend<P extends TimedPackage<?>> extends Send<P> {

    default void send(P pack, int delay) {
        throw new UnsupportedOperationException("");
    }

    default void send(P pack, Date timestamp) {
        throw new UnsupportedOperationException("");
    }

    default void sendAll(P p, int delay) {
        throw new UnsupportedOperationException("");
    }

    default void sendAll(P p, Date timestamp) {
        throw new UnsupportedOperationException("");
    }
}
