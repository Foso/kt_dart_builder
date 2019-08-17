package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.mixins.*

import de.jensklingenberg.kt_dart_builder.main.specs.expression.ClosureExpression


data class Method(
    override val annotations: BuiltList<Expression> = emptyList(),
    override val types: BuiltList<Reference> = emptyList(),
    override val docs: BuiltList<String> = emptyList(),
    val body: Code? = null,
    val external: bool = false,
    val static: bool = false,
    val returns: Reference? = null,
    val type: MethodType? = null,
    val name: String = "",
    val requiredParameters: BuiltList<Parameter> = emptyList(),
    val optionalParameters: BuiltList<Parameter> = emptyList(),
    val modifier: MethodModifier? = null,
    val lambda: bool? = null
) : HasAnnotations, HasGenerics, HasDartDocs, Spec {
    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        return visitor.visitMethod(this, context)
    }

    val closure: Expression
        get() = toClosure(this)


    fun toClosure(method: Method): Expression {
        val withoutTypes = method.copy(returns = null, types = emptyList())
        return ClosureExpression(withoutTypes)
    }
}


enum class MethodType {
    gettter, setter
}

enum class MethodModifier {
    async,
    asyncStar,
    syncStar,
}

class Parameter(
    val type: Reference? = null,
    val toThis: bool = false,
    val name: String = "",
    val defaultTo: Code? = null,
    val named: bool = false,
    override val annotations: List<Expression> = emptyList(),
    override val types: BuiltList<Reference> = emptyList(),
    override val docs: BuiltList<String> = emptyList()
) : HasAnnotations, HasGenerics, HasDartDocs {
}