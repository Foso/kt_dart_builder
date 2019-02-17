package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor

class Library(
    val directives: List<Directive> = emptyList(),
    val body: List<Spec> = emptyList()
) : Spec {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T = visitor.visitLibrary(this, context)
}
