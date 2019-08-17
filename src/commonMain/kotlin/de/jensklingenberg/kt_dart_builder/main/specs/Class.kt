package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor

import de.jensklingenberg.kt_dart_builder.main.mixins.*


class Class(
    val abstract: bool = false,
    override val annotations: BuiltList<Expression> = emptyList(),
    override val docs: BuiltList<String> = emptyList(),
    val extend: Reference? = null,
    val implements: BuiltList<Reference> = emptyList(),
    val mixins: BuiltList<Reference> = emptyList(),
    override val types: BuiltList<Reference> = emptyList(),
    val constructors: BuiltList<Constructor> = emptyList(),
    val methods: BuiltList<Method> = emptyList(),
    val fields: BuiltList<Field> = emptyList(),
    val name: String = ""
) : HasAnnotations, HasDartDocs, HasGenerics, Spec {


    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T = visitor.visitClass(this, context)


}



