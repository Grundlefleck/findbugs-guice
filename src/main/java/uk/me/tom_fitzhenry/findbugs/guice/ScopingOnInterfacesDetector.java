package uk.me.tom_fitzhenry.findbugs.guice;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

public class ScopingOnInterfacesDetector implements Detector2 {
    private final BugReporter bugReporter;

    public ScopingOnInterfacesDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClass(ClassDescriptor classDescriptor) {
        final XClass classInfo = classDescriptionToXClass(classDescriptor);

        if (classInfo.isInterface()) {
            for (AnnotationValue annotation : classInfo.getAnnotations()) {
                if (annotation.getAnnotationClass().getClassName().contains("Singleton")) {
                    bugReporter.reportBug(new BugInstance(this, "GUICE_SCOPE_ON_ANNOTATION",
                            NORMAL_PRIORITY).addClass(classDescriptor));
                }
            }
        }
    }

    private XClass classDescriptionToXClass(ClassDescriptor classDescriptor) {
        final IAnalysisCache analysisCache = Global.getAnalysisCache();

        XClass classInfo = null;
        try {
            classInfo = analysisCache.getClassAnalysis(XClass.class, classDescriptor);
        } catch (CheckedAnalysisException e) {
            bugReporter.logError("Error when converting a classDescriptor, " + classDescriptor
                    + ", " + "to an XClass.", e);
        }
        return classInfo;
    }

    @Override
    public void finishPass() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getDetectorClassName() {
        return getClass().getName();
    }
}
