package ch.giuntini.netjlo_fos.socket;

import java.io.File;

public interface Send {

    default void send(File file) {
        throw new UnsupportedOperationException("");
    }

    default void sendToAll(File file) {
        throw new UnsupportedOperationException("");
    }
}
