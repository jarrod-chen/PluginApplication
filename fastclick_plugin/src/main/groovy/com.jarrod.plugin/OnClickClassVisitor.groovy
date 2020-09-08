package com.jarrod.plugin

import com.sun.xml.internal.ws.org.objectweb.asm.MethodAdapter
import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.TypePath
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.util.ASMifier

class OnClickClassVisitor extends ClassVisitor {
    private String className;
    private Project project;

    OnClickClassVisitor(Project project, ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor)
        this.project = project
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//        println("FastClickPlugin : visitMethod ----------> started: " + this.className);
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        return new OnClickMethodVisitor(access, name, descriptor, mv, this.project);
    }
}