package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.poet.BuiltList
import de.jensklingenberg.kt_dart_builder.main.poet.BuiltMap

class FunctionType(
    val returnType: Reference? = null,
    val types: BuiltList<Reference> = emptyList(),
    val optionalParameters: BuiltList<Reference> = emptyList(),
    val namedParameters: BuiltMap<String, Reference> = emptyMap(),
    val requiredParameters: BuiltList<Reference> = emptyList()
) : Reference(), Spec, Expression {

    override var url: String = ""
    override var symbol: String = ""
    override val type: Reference = this

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        return visitor.visitFunctionType(this, context)
    }

    fun toTypeDef(name: String): Code = createTypeDef(name, this)


}

