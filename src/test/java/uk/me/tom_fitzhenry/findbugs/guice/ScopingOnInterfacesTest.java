package uk.me.tom_fitzhenry.findbugs.guice;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AnInterfaceWithAScopingAnnotation;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ScopingOnInterfacesTest {
    
    @Test
    public void explicitBindingsFailUponInjectionConstruction() {
        
        Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(AnInterfaceWithAScopingAnnotation.class);
            }
        };
        
        try {
            @SuppressWarnings("unused")
            Injector injector = Guice.createInjector(module);
            fail();
        } catch (CreationException e) {
            assertTrue(e.getMessage().contains("scope annotations are not supported for abstract types"));
        }
    }

    @Test
    public void justInTimeBindingsFailJustInTime() {
        Injector injector = Guice.createInjector();
        
        try {
            injector.getInstance(AnInterfaceWithAScopingAnnotation.class);
            fail();
        } catch (ConfigurationException e) {
            assertTrue(e.getMessage().contains("scope annotations are not supported for abstract types"));
        }
    }
    
}
