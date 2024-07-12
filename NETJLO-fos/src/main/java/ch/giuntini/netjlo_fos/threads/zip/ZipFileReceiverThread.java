package ch.giuntini.netjlo_fos.threads.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.client.zip.ZipFileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.packages.FilePartPackage;
import ch.giuntini.netjlo_fos.threads.FileReceiverThread;
import ch.giuntini.netjlo_fos.zip.ZipUtil;

import java.io.File;
import java.io.IOException;

public class ZipFileReceiverThread<S extends BaseSocket, I extends Interpretable> extends FileReceiverThread<S, I> {

    private final boolean unzipDirs;

    public ZipFileReceiverThread(
            ZipFileConnection<S, I> connection, S socket,
            String rootPathForFiles,
            Class<I> interpreterC
    ) {
        this(connection, socket, rootPathForFiles, interpreterC, true);
    }

    public ZipFileReceiverThread(
            ZipFileConnection<S, I> connection,
            S socket,
            String rootPathForFiles,
            Class<I> interpreterC,
            boolean unzipDirs
    ) {
        super(connection, socket, rootPathForFiles, interpreterC);
        this.unzipDirs = unzipDirs;
    }

    @Override
    protected void cycle() throws IOException, ClassNotFoundException {
        final String filename = ((FilePartPackage)ois.readObject()).getInformation();
        final boolean wasDir = Boolean.parseBoolean(((FilePartPackage)ois.readObject()).getInformation());
        File file = FileReceiverThread.receiveFile(filename, PATH, ois);
        if (unzipDirs && file.getName().endsWith(".zip")) {
            if (wasDir) {
                file = ZipUtil.unzipDirAndSubDirs(file, PATH);
            } else {
                file = ZipUtil.unzipSingleFile(file, PATH);
            }
        }
        interpreter.interpret(file);
    }
}