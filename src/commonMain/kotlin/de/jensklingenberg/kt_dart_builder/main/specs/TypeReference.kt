package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.mixins.HasGenerics
import de.jensklingenberg.kt_dart_builder.main.poet.BuiltList

class TypeReference(
    override var url: String = "", override var symbol: String = "", val bound: Reference? = null,
    override val types: BuiltList<Reference> = emptyList()
) : Expression, Spec, HasGenerics, Reference() {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        return visitor.visitType(this, context)

    }


}