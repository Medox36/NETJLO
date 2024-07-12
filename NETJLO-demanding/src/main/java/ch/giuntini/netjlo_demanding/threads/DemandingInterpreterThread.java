package ch.giuntini.netjlo_demanding.threads;

import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.interpreter.Interpretable;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DemandingInterpreterThread<P extends BasePackage<?>, I extends Interpretable<P>>
        extends Thread implements AutoCloseable {

    protected final ConcurrentLinkedQueue<P> packages;
    protected final I interpreter;
    protected volatile boolean stop;

    public DemandingInterpreterThread(ConcurrentLinkedQueue<P> packages, I interpreter) {
        super("ReceiverInterpret-Thread");
        this.packages = packages;
        this.interpreter = interpreter;
    }

    @Override
    public void run() {
        while (!stop) {
            while (!packages.isEmpty()) {
                P p = packages.poll();
                if (p != null) {
                    interpreter.interpret(p);
                }
            }
            Thread.onSpinWait();
        }
    }

    @Override
    public void close() {
        stop = true;
    }
}
