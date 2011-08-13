package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class AModuleThatIncorrectlyInstallsAnotherModuleUsingAnInterface extends AbstractModule {

    @Override
    protected void configure() {
        Module module = new SubModule();
        module.configure(binder());
    }
}