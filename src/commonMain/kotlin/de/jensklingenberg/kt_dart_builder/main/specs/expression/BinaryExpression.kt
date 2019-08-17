package de.jensklingenberg.kt_dart_builder.main.specs.expression

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.mixins.bool

import de.jensklingenberg.kt_dart_builder.main.specs.Expression
import de.jensklingenberg.kt_dart_builder.main.specs.ExpressionVisitor

class BinaryExpression(
    val left: Expression,
    val right: Expression,
    val operator: String,
    val addSpace: bool = true,
    val isConst: bool = false
) : Expression {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor
        return visitor.visitBinaryExpression(this, context)
    }


}
