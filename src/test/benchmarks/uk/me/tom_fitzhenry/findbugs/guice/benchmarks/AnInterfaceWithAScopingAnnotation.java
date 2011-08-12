package uk.me.tom_fitzhenry.findbugs.guice.benchmarks;


import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

@Singleton
@ImplementedBy(ClassThatImplementsInterfaceWithAScopingAnnotation.class)
public interface AnInterfaceWithAScopingAnnotation {
}
