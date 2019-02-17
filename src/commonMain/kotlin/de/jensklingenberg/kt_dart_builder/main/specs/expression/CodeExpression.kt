package de.jensklingenberg.kt_dart_builder.main.specs.expression

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.Code
import de.jensklingenberg.kt_dart_builder.main.specs.Expression
import de.jensklingenberg.kt_dart_builder.main.specs.ExpressionVisitor

class CodeExpression(override val code: Code) : Expression {


    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor
        return visitor.visitCodeExpression(this, context)

    }

}