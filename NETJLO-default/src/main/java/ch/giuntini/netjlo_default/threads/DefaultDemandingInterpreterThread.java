package ch.giuntini.netjlo_default.threads;

import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.threads.DemandingInterpreterThread;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DefaultDemandingInterpreterThread<I extends Interpretable<DefaultPackage>>
        extends DemandingInterpreterThread<DefaultPackage, I> {

    public DefaultDemandingInterpreterThread(ConcurrentLinkedQueue<DefaultPackage> packages, I interpreter) {
        super(packages, interpreter);
        setName("Default-Interpreter-Thread");
    }
}
