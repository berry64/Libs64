/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.nms.internal;

import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.*;

public class VersionTransformer extends ClassVisitor implements Opcodes {
    String from, to;
    public VersionTransformer(ClassVisitor classVisitor, String from, String to) {
        super(ASM7, classVisitor);
        this.from = from;
        this.to = to;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, edit(descriptor, from, to), signature, value);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, edit(descriptor, from, to));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(edit(descriptor, from, to), visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, edit(descriptor, from, to), visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, edit(descriptor, from, to), signature, exceptions);
        if(mv != null)
            mv = new VersionMethodTransformer(mv, from, to);
        return mv;
    }

    public static String edit(String edit, String from, String to){
        if(edit.contains(from)) {
            String edt = edit.replace(from, to);
            System.out.println("Transforming from " + edit + " to " + edt + " [" + from + "->" + to + "]");
            return edt;
        } else
            return edit;
    }

    public static Type editType(Type type, String from, String to){
        String tmp = type.getDescriptor();
        if(tmp.contains(from)) {
            String edt = tmp.replace(from, to);
            System.out.println("Transforming from " + tmp + " to " + edt + " [" + from + "->" + to + "]");
            return Type.getType(edt);
        }
        else
            return type;
    }
}
class VersionMethodTransformer extends InstructionAdapter implements Opcodes{
    String from, to;
    public VersionMethodTransformer(MethodVisitor methodVisitor, String from, String to) {
        super(ASM7, methodVisitor);
        this.from = from;
        this.to = to;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(VersionTransformer.edit(descriptor, from, to), visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, VersionTransformer.edit(descriptor, from, to), visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return super.visitParameterAnnotation(parameter, VersionTransformer.edit(descriptor, from, to), visible);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        super.visitMethodInsn(opcode, owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, VersionTransformer.edit(descriptor, from, to), isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, VersionTransformer.edit(descriptor, from, to), bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitInsnAnnotation(typeRef, typePath, VersionTransformer.edit(descriptor, from, to), visible);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTryCatchAnnotation(typeRef, typePath, VersionTransformer.edit(descriptor, from, to), visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, VersionTransformer.edit(descriptor, from, to), signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, VersionTransformer.edit(descriptor, from, to), visible);
    }

    @Override
    public void getstatic(String owner, String name, String descriptor) {
        super.getstatic(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void putstatic(String owner, String name, String descriptor) {
        super.putstatic(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void getfield(String owner, String name, String descriptor) {
        super.getfield(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void putfield(String owner, String name, String descriptor) {
        super.putfield(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void invokevirtual(String owner, String name, String descriptor) {
        super.invokevirtual(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void invokevirtual(String owner, String name, String descriptor, boolean isInterface) {
        super.invokevirtual(owner, name, VersionTransformer.edit(descriptor, from, to), isInterface);
    }

    @Override
    public void invokespecial(String owner, String name, String descriptor) {
        super.invokespecial(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void invokespecial(String owner, String name, String descriptor, boolean isInterface) {
        super.invokespecial(owner, name, VersionTransformer.edit(descriptor, from, to), isInterface);
    }

    @Override
    public void invokestatic(String owner, String name, String descriptor) {
        super.invokestatic(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void invokestatic(String owner, String name, String descriptor, boolean isInterface) {
        super.invokestatic(owner, name, VersionTransformer.edit(descriptor, from, to), isInterface);
    }

    @Override
    public void invokeinterface(String owner, String name, String descriptor) {
        super.invokeinterface(owner, name, VersionTransformer.edit(descriptor, from, to));
    }

    @Override
    public void invokedynamic(String name, String descriptor, Handle bootstrapMethodHandle, Object[] bootstrapMethodArguments) {
        super.invokedynamic(name, VersionTransformer.edit(descriptor, from, to), bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void checkcast(Type type) {
        super.checkcast(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void add(Type type) {
        super.add(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void sub(Type type) {
        super.sub(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void mul(Type type) {
        super.mul(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void div(Type type) {
        super.div(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void rem(Type type) {
        super.rem(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void neg(Type type) {
        super.neg(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void shl(Type type) {
        super.shl(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void shr(Type type) {
        super.shr(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void ushr(Type type) {
        super.ushr(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void and(Type type) {
        super.and(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void or(Type type) {
        super.or(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void xor(Type type) {
        super.xor(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void cast(Type a, Type b) {
        super.cast(VersionTransformer.editType(a, from, to), VersionTransformer.editType(b, from, to));
    }

    @Override
    public void cmpl(Type type) {
        super.cmpl(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void cmpg(Type type) {
        super.cmpg(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void areturn(Type type) {
        super.areturn(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void anew(Type type) {
        super.anew(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void newarray(Type type) {
        super.newarray(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void instanceOf(Type type) {
        super.instanceOf(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, VersionTransformer.edit(type, from, to));
    }

    @Override
    public void tconst(Type type) {
        super.tconst(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void load(int var, Type type) {
        super.load(var, VersionTransformer.editType(type, from, to));
    }

    @Override
    public void aload(Type type) {
        super.aload(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void store(int var, Type type) {
        super.store(var, VersionTransformer.editType(type, from, to));
    }

    @Override
    public void astore(Type type) {
        super.astore(VersionTransformer.editType(type, from, to));
    }

    @Override
    public void multianewarray(String descriptor, int numDimensions) {
        super.multianewarray(VersionTransformer.edit(descriptor, from, to), numDimensions);
    }
}
