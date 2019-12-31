package net.berry64.libs64.nms.internal;

import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.*;

public class VersionTransformer extends ClassVisitor implements Opcodes {

    public VersionTransformer(ClassVisitor classVisitor, String from, String to) {
        super(ASM7, classVisitor);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return null;
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return null;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return null;
    }

    public static String edit(String edit, String from, String to) {
        return null;
    }

    public static Type editType(Type type, String from, String to) {
        return null;
    }
}
