package uk.me.tom_fitzhenry.findbugs.guice;

import java.util.Arrays;
import java.util.List;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.Global;

public class SuspiciousSubmoduleConfigurationDetector extends BytecodeScanningDetector {
    
    private final static String MODULE_NAME = "com.google.inject.Module";

    private final BugReporter bugReporter;
    private boolean isModule = false;

    public SuspiciousSubmoduleConfigurationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }
    
    @Override
    public void visitClassContext(ClassContext classContext) {
        if (isModule(classContext)) {
            isModule = true;
        }
        super.visitClassContext(classContext);
    }
    
    @Override
    public void sawOpcode(int seen) {
        if(isModule) {
            switch(seen) {
            case INVOKEVIRTUAL:
            case INVOKEINTERFACE:
                if(isModule(getClassDescriptorOperand())) {
                    if(isCallingConfigure()) {
                        bugReporter.reportBug(new BugInstance(this, "GUICE_SUSPICIOUS_SUBMODULE_CONFIGURATION",
                                NORMAL_PRIORITY).addClassAndMethod(this));
                    }
                }
                break;
            }
        }
    }
    
    private boolean isCallingConfigure() {
        return getNameConstantOperand().equals("configure");
    }

    private boolean isModule(ClassDescriptor classDescriptor) {
        ClassContext myClass = null;
        try {
            myClass = Global.getAnalysisCache().getClassAnalysis(ClassContext.class, classDescriptor);
        } catch (CheckedAnalysisException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isModule(myClass);
    }

    private static boolean isModule(ClassContext classContext) {
        final XClass classInfo = classContext.getXClass();
        
        // This only returns interfaces directly implemented by classContext.
        // Interfaces of superclasses need to be considered too.
        List<ClassDescriptor> interfaces = Arrays.asList(classInfo.getInterfaceDescriptorList());
        
        for(ClassDescriptor interfaceDescriptor : interfaces) {
            if (interfaceDescriptor.getDottedClassName().equals(MODULE_NAME)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void report() {
    }
    
}
