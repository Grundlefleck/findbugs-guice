package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;

import com.google.inject.AbstractModule;

public class AModuleThatCorrectlyInstallsAnotherModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new SubModule());
    }
}