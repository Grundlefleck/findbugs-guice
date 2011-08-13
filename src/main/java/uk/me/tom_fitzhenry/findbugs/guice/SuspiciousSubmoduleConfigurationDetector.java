package uk.me.tom_fitzhenry.findbugs.guice;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.ch.Subtypes2;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

public class SuspiciousSubmoduleConfigurationDetector extends BytecodeScanningDetector {
    
    private final static String MODULE_NAME = "com.google.inject.Module";

    private final BugReporter bugReporter;
    private boolean isModule = false;

    public SuspiciousSubmoduleConfigurationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }
    
    @Override
    public void visitClassContext(ClassContext classContext) {
        if (isModule(classContext.getClassDescriptor())) {
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
        return Subtypes2.instanceOf(classDescriptor, MODULE_NAME);
    }

    @Override
    public void report() {
    }
    
}
