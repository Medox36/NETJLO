package ch.giuntini.netjlo_core.interpreter;

import ch.giuntini.netjlo_core.packages.BasePackage;

public interface Interpretable<P extends BasePackage<?>> {

    void interpret(P p);
}
