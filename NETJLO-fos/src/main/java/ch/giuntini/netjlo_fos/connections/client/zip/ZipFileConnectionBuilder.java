package ch.giuntini.netjlo_fos.connections.client.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.client.FileConnectionBuilder;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class ZipFileConnectionBuilder<S extends BaseSocket, I extends Interpretable>
        extends FileConnectionBuilder<S, I> {

    protected long zipThreshold = 536870912;
    protected boolean unzipDirs = true;

    public ZipFileConnectionBuilder() {
    }

    public ZipFileConnectionBuilder<S, I> zipThreshold(long zipThreshold) {
        this.zipThreshold = zipThreshold;
        return this;
    }

    public ZipFileConnectionBuilder<S, I> unzipDirs(boolean unzipDirs) {
        this.unzipDirs = unzipDirs;
        return this;
    }

    @Override
    public ZipFileConnection<S, I> build() {
        checkState();
        return new ZipFileConnection<>(socket, rootPathForFiles, zipThreshold, unzipDirs, interpreterC);
    }
}
