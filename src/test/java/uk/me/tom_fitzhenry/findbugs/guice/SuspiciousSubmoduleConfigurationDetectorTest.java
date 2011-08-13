package uk.me.tom_fitzhenry.findbugs.guice;

import static com.youdevise.fbplugins.tdd4fb.DetectorAssert.ofType;

import org.junit.Before;
import org.junit.Test;

import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AModuleThatCorrectlyInstallsAnotherModule;
import uk.me.tom_fitzhenry.findbugs.guice.benchmarks.AModuleThatIncorrectlyInstallsAnotherModule;

import com.youdevise.fbplugins.tdd4fb.DetectorAssert;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;

public class SuspiciousSubmoduleConfigurationDetectorTest {

    private BugReporter bugReporter;
    private Detector detector;
    
    @Before public void setUp() {
        bugReporter = DetectorAssert.bugReporterForTesting();
        detector = new SuspiciousSubmoduleConfigurationDetector(bugReporter);
    }
    
    @Test
    public void modulesThatCorrectlyInstallModulesAreNotReported() throws Exception {
        assertNoBugsReportedForClass(AModuleThatCorrectlyInstallsAnotherModule.class);
    }
    
    @Test
    public void modulesThatIncorrectlyInstallModulesAreReported() throws Exception {
        assertBugReportedAgainstClass(AModuleThatIncorrectlyInstallsAnotherModule.class);
    }
    
    private void assertBugReportedAgainstClass(Class<?> classToTest) throws Exception {
        DetectorAssert.assertBugReported(classToTest, detector, bugReporter, ofType("GUICE_SUSPICIOUS_SUBMODULE_CONFIGURATION"));
    }
    
    private void assertNoBugsReportedForClass(Class<?> classToTest) throws Exception {
        DetectorAssert.assertNoBugsReported(classToTest, detector, bugReporter);
    }

}
