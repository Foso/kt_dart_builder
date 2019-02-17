package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.expression.*

interface ExpressionVisitor<T> : SpecVisitor<T> {
    fun visitBinaryExpression(expression: BinaryExpression, context: T?): T
    fun visitClosureExpression(expression: ClosureExpression, context: T?): T
    fun visitCodeExpression(expression: CodeExpression, context: T?): T
    fun visitInvokeExpression(expression: InvokeExpression, context: T?): T
    fun visitToCodeExpression(expression: ToCodeExpression, context: T?): T
    fun visitLiteralExpression(expression: LiteralExpression, context: T?): T
    fun visitLiteralListExpression(expression: LiteralListExpression, context: T?): T
    fun visitLiteralMapExpression(expression: LiteralMapExpression, context: T?): T

}