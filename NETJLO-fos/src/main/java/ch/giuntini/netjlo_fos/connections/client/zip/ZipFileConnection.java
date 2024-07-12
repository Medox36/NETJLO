package ch.giuntini.netjlo_fos.connections.client.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.client.FileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.threads.zip.ZipFileReceiverThread;
import ch.giuntini.netjlo_fos.threads.zip.ZipFileSenderThread;

public class ZipFileConnection <S extends BaseSocket, I extends Interpretable> extends FileConnection<S, I> {

    private ZipFileConnection() {
    }

    public ZipFileConnection(S socket, String rootPathForFiles, Class<I> interpreterC) {
        this.socket = socket;
        senderThread = new ZipFileSenderThread<>(this, socket);
        receiverThread = new ZipFileReceiverThread<>(this, socket, rootPathForFiles, interpreterC);
    }

    public ZipFileConnection(S socket, String rootPathForFiles, long zipThreshold, Class<I> interpreterC) {
        this.socket = socket;
        senderThread = new ZipFileSenderThread<>(this, socket, zipThreshold);
        receiverThread = new ZipFileReceiverThread<>(this, socket, rootPathForFiles, interpreterC);
    }

    public ZipFileConnection(S socket, String rootPathForFiles, boolean unzipDirs, Class<I> interpreterC) {
        this.socket = socket;
        senderThread = new ZipFileSenderThread<>(this, socket);
        receiverThread = new ZipFileReceiverThread<>(this, socket, rootPathForFiles, interpreterC, unzipDirs);
    }

    public ZipFileConnection(S socket, String rootPathForFiles, long zipThreshold, boolean unzipDirs, Class<I> interpreterC) {
        this.socket = socket;
        senderThread = new ZipFileSenderThread<>(this, socket, zipThreshold);
        receiverThread = new ZipFileReceiverThread<>(this, socket, rootPathForFiles, interpreterC, unzipDirs);
    }
}