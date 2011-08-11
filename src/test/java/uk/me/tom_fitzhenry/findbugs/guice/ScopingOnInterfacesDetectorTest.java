package uk.me.tom_fitzhenry.findbugs.guice;

import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.ofType;

import org.junit.Before;
import org.junit.Test;

import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AnInterfaceWithAScopingAnnotation;
import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AnInterfaceWithoutAScopingAnnotation;

import com.youdevise.fbplugins.tdd4fb.DetectorAssert;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;

public class ScopingOnInterfacesDetectorTest {

    private BugReporter bugReporter;
    private Detector detector;
    
    @Before public void setUp() {
        bugReporter = DetectorAssert.bugReporterForTesting();
        detector = new ScopingOnInterfacesDetector(bugReporter);
    }
    
    @Test
    public void test() throws Exception {
        assertNoBugsReportedForClass(AnInterfaceWithoutAScopingAnnotation.class);
    }
    
    @Test
    public void bar() throws Exception {
        assertBugReportedAgainstClass(AnInterfaceWithAScopingAnnotation.class);
    }
    
    private void assertBugReportedAgainstClass(Class<?> classToTest) throws Exception {
        DetectorAssert.assertBugReported(classToTest, detector, bugReporter, ofType("GUICE_SCOPE_ON_ANNOTATION"));
    }
    
    private void assertNoBugsReportedForClass(Class<?> classToTest) throws Exception {
        DetectorAssert.assertNoBugsReported(classToTest, detector, bugReporter);
    }

}
