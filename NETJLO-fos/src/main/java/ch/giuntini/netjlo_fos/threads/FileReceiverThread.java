package ch.giuntini.netjlo_fos.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.threads.ThreadCommons;
import ch.giuntini.netjlo_fos.connections.client.FileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.packages.FilePartPackage;
import ch.giuntini.netjlo_fos.streams.FilePartPackageObjectInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReceiverThread<S extends BaseSocket, I extends Interpretable>
        extends Thread implements AutoCloseable {

    protected final String PATH;
    protected final FileConnection<S, I> connection;
    protected final S socket;
    protected final FilePartPackageObjectInputStream ois;
    protected final I interpreter;
    protected volatile boolean stop;

    public FileReceiverThread(FileConnection<S, I> connection, S socket, String rootPathForFiles, Class<I> interpreterC) {
        super("Receiving-Thread");
        this.connection = connection;
        this.socket = socket;
        this.PATH = rootPathForFiles;
        try {
            ois = new FilePartPackageObjectInputStream(new ObjectInputStream(new BufferedInputStream(socket.getInputStream())));
            interpreter = interpreterC.getConstructor().newInstance();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                cycle();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitIn(socket, ois, connection, stop);
    }

    protected void cycle() throws IOException, ClassNotFoundException {
        final String filename = ((FilePartPackage) ois.readObject()).getInformation();
        File file = receiveFile(filename, PATH, ois);
        interpreter.interpret(file);
    }

    public static File receiveFile(String filename, String path, FilePartPackageObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        Path target = Path.of(path + filename);
        File file = Files.createFile(target).toFile();
        BufferedWriter bw = Files.newBufferedWriter(target);
        FilePartPackage partPackage;
        while (!(partPackage = (FilePartPackage) ois.readObject()).isEOF()) {
            bw.write(partPackage.getInformation());
        }
        bw.flush();
        bw.close();
        return file;
    }

    @Override
    public void close() {
        stop = true;
    }
}
