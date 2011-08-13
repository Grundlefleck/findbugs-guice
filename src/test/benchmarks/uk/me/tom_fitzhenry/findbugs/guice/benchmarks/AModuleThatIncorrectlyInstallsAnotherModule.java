package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class AModuleThatIncorrectlyInstallsAnotherModule extends AbstractModule implements Module {

    @Override
    protected void configure() {
        new SubModule().configure(binder());
    }
}