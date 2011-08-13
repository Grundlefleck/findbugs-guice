package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class SubModule extends AbstractModule {

    @Override
    protected void configure() {
    }
    
    @Provides
    Foo getFoo() {
        return new ConcreteFoo();
    }
    
    public static interface Foo {}
    public static class ConcreteFoo implements Foo {}
}