package ch.giuntini.netjlo_fos.threads.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.client.zip.ZipFileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.packages.FilePartPackage;
import ch.giuntini.netjlo_fos.threads.FileSenderThread;
import ch.giuntini.netjlo_fos.zip.ZipUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ZipFileSenderThread<S extends BaseSocket, I extends Interpretable> extends FileSenderThread<S, I> {

    private final long zipThreshold;

    /**
     * creates a ZipFileSenderThread with the zipThreshold of half a Gibibyte (in Windows equivalent of a Gigabyte)
     *
     * @param socket
     */
    public ZipFileSenderThread(ZipFileConnection<S, I> connection, S socket) {
        this(connection, socket, 536870912);
    }

    /**
     * creates a ZipFileSenderThread which zips files before sending them when the filesize in bytes is above the threshold
     *
     * @param socket
     * @param zipThreshold for filesize in bytes over wich files get zipped
     */
    public ZipFileSenderThread(ZipFileConnection<S, I> connection,S socket, long zipThreshold) {
        super(connection, socket);
        this.zipThreshold = zipThreshold;
    }

    /**
     * creates .zip files when the given file is a directory or bigger then the zipThreshold
     *
     * @param file file to send
     */
    @Override
    protected void cycle(File file) throws IOException {
        if (file != null) {
            final boolean isDir = file.isDirectory();
            if (!file.getName().endsWith(".zip")) {
                file = zipFile(file, isDir);
            }
            objectOutputStream.writeObject(new FilePartPackage(file.getName()));
            objectOutputStream.writeObject(new FilePartPackage(String.valueOf(isDir)));
            objectOutputStream.flush();
            char[] buff = new char[8192];
            FileReader fr = new FileReader(file);
            while (fr.read(buff) > 0) {
                objectOutputStream.writeObject(new FilePartPackage(String.valueOf(buff)));
                objectOutputStream.flush();
            }
            fr.close();
            objectOutputStream.writeObject(new FilePartPackage(true));
            objectOutputStream.flush();
        }
    }

    /**
     * zips the given file if its length is bigger than the threshold
     *
     * @param file file to zip
     * @param isDir true if the file is a directory, otherwise false
     * @return the (zipped) file
     */
    private File zipFile(File file, final boolean isDir) {
        if (file.length() > zipThreshold) {
            try {
                if (isDir) {
                    file = ZipUtil.zipDirAndSubDirs(file);
                } else {
                    file = ZipUtil.zipSingleFile(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public void addFileToSendStack(File file) {
        if (file == null || !file.exists() || !file.canRead())
            throw new IllegalArgumentException("The file to send can't be null, can't not exist and can't be unreadable");
        files.add(file);
    }
}