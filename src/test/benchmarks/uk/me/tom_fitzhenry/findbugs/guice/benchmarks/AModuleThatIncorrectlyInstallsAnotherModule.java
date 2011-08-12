package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;

import com.google.inject.AbstractModule;

public class AModuleThatIncorrectlyInstallsAnotherModule extends AbstractModule {

    @Override
    protected void configure() {
        new SubModule().configure(binder());
    }
}