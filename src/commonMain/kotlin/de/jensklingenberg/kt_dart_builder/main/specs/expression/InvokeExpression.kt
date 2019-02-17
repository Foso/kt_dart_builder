package de.jensklingenberg.kt_dart_builder.main.specs.expression

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.Expression
import de.jensklingenberg.kt_dart_builder.main.specs.ExpressionVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.Reference

class InvokeExpression(
    val target: Expression,
    val positionalArguments: List<Expression>,
    val namedArguments: Map<String, Expression>,
    val typeArguments: List<Reference>,
    val name: String,
    val type: InvokeExpressionType? = null
) : Expression {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor

        return visitor.visitInvokeExpression(this, context)

    }

    fun constOf(): Expression =
        create(InvokeExpressionType.constInstance)

    fun newOf(): Expression =
        create(InvokeExpressionType.newInstance)

    private fun create(type: InvokeExpressionType): Expression{
        return InvokeExpression(
            this.target,
            this.positionalArguments,
            this.namedArguments,
            this.typeArguments,
            this.name,
            type
        )
    }

}

enum class InvokeExpressionType {
    newInstance,
    constInstance
}

