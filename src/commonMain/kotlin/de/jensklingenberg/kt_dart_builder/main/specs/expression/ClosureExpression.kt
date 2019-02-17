package de.jensklingenberg.kt_dart_builder.main.specs.expression

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.Expression
import de.jensklingenberg.kt_dart_builder.main.specs.ExpressionVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.Method

class ClosureExpression(val method: Method) : Expression {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor

        return visitor.visitClosureExpression(this, context)
    }
}

