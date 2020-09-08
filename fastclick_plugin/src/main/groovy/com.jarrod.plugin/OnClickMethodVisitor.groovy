package com.jarrod.plugin

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.TypePath

class OnClickMethodVisitor extends MethodVisitor {
    private  Project project;
    private boolean isAllowFastClick = false;
    private int access;
    private String name;
    private String descriptor;
    private HashMap<String, Boolean> hasAnnotation = new HashMap<>()

    OnClickMethodVisitor(int access, String name, String descriptor, MethodVisitor methodVisitor, Project project) {
        super(Opcodes.ASM7, methodVisitor)
        this.project = project
        this.access = access
        this.name = name
        this.descriptor = descriptor
    }

    @Override
    AnnotationVisitor visitAnnotation(String annotation, boolean visible) {
        FastClickExtension extension = (FastClickExtension) this.project.getExtensions().findByName("FastClickExtension");
        if (extension.allowFastClickAnnotation.equals(annotation)) {
            hasAnnotation.put(name, new Boolean(true))
        }
        return super.visitAnnotation(annotation, visible)
    }

    @Override
    AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    @Override
    AnnotationVisitor visitAnnotationDefault() {
        return super.visitAnnotationDefault()
    }

    @Override
    void visitCode() {
        super.visitCode()
        if (hasAnnotation.get(name) != null && hasAnnotation.get(name)) {
//            println("FastClickPlugin : skip method ----> " + name);
            return
        }
        if (Utils.isViewOnclickMethod(access, name, descriptor)) {
//            println("FastClickPlugin : change method ----> " + name);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getId", "()I", false);
            FastClickExtension extension = (FastClickExtension) this.project.getExtensions().findByName("FastClickExtension");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, extension.fastClickUtilClass,
                    "isFastDoubleClick", "(I)Z",
                    false);
            Label label = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, label);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitLabel(label);
        }
    }

}