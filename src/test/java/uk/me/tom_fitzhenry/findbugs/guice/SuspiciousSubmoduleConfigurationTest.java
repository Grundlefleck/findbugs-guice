package uk.me.tom_fitzhenry.findbugs.guice;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AModuleThatCorrectlyInstallsAnotherModule;
import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AModuleThatIncorrectlyInstallsAnotherModule;
import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.SubModule.Foo;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class SuspiciousSubmoduleConfigurationTest {

    @Test
    public void configuringASubModuleMissesProviderBindings() {
        Injector injector = Guice.createInjector(new AModuleThatIncorrectlyInstallsAnotherModule());
        
        try {
            @SuppressWarnings("unused")
            Foo result = injector.getInstance(Foo.class);
            fail();
        } catch (ConfigurationException e) {
            assertTrue(e.getMessage().contains("No implementation for " + Foo.class.getName() + " was bound."));
        }
    }
    
    @Test
    public void installingASubModuleDoesNotMissProviderBindings() {
        Injector injector = Guice.createInjector(new AModuleThatCorrectlyInstallsAnotherModule());
        injector.getInstance(Foo.class);
    }
    
}
