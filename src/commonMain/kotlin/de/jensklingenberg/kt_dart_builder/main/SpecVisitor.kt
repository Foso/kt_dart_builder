package de.jensklingenberg.kt_dart_builder.main

import de.jensklingenberg.kt_dart_builder.main.specs.*

interface SpecVisitor<T> {

    fun visitAnnotation(spec: Expression, context: T?): T
    fun visitClass(spec: Class, context: T?): T
    fun visitConstructor(spec: Constructor, clazz: String, context: T?): T
    fun visitDirective(spec: Directive, context: T?): T
    fun visitField(spec: Field, context: T?): T
    fun visitLibrary(spec: Library, context: T?): T
    fun visitFunctionType(spec: FunctionType, context: T?): T
    fun visitMethod(spec: Method, context: T?): T
    fun visitReference(spec: Reference, context: T?): T
    fun visitSpec(spec: Spec, context: T?): T
    fun visitType(spec: TypeReference, context: T?): T
    fun visitTypeParameters(spec: List<Reference>, context: T?): T


}
