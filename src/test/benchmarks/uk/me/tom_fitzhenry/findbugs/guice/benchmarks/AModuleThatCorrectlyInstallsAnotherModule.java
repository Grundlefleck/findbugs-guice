package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class AModuleThatCorrectlyInstallsAnotherModule extends AbstractModule implements Module {

    @Override
    protected void configure() {
        install(new SubModule());
    }
}