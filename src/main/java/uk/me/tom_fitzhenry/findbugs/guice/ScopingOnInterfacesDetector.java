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
    
    private final static String SCOPE_ANNOTATION = "com.google.inject.ScopeAnnotation";
    
    static {
        log("Registered");
    }
    
    private final BugReporter bugReporter;

    public ScopingOnInterfacesDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClass(ClassDescriptor classDescriptor) {
        final XClass classInfo = classDescriptionToXClass(classDescriptor);

        if (classInfo.isInterface()) {
            visitInterface(classInfo);
        }
    }
    
    private void visitInterface(XClass classInfo) {
        for (AnnotationValue annotation : classInfo.getAnnotations()) {
            final XClass annotationInfo = classDescriptionToXClass(annotation.getAnnotationClass());
            for (AnnotationValue annotationAnnotation : annotationInfo.getAnnotations()) {
                if (annotationAnnotation.getAnnotationClass().getDottedClassName().equals(SCOPE_ANNOTATION)) {
                    bugReporter.reportBug(new BugInstance(this, "GUICE_SCOPE_ON_ANNOTATION",
                            NORMAL_PRIORITY).addClass(classInfo.getClassDescriptor()));
                }
            }
        }
    }
    
    @Override
    public void finishPass() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getDetectorClassName() {
        return getClass().getName();
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
    
    private static void log(String string) {
        System.out.println("[ScopingOnInterfacesDetector] " + string);
    }
}
