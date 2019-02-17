package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor

class ToCodeExpression(
    val code: Expression,
    val isStatement: Boolean = false
) : Code {
    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor<T>
        return (visitor).visitToCodeExpression(this, context)

    }


    override fun toString() = code.toString()

}