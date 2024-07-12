package ch.giuntini.netjlo_default.interpreter;

import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultInterpreter implements Interpretable<DefaultPackage> {

    @Override
    public void interpret(DefaultPackage p) {
        System.out.println(p.getInformation());
    }
}
