package de.jensklingenberg.kt_dart_builder.main.specs.expression

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.mixins.bool
import de.jensklingenberg.kt_dart_builder.main.mixins.num
import de.jensklingenberg.kt_dart_builder.main.specs.*

val errorStub: (Any) -> Expression = {
    literalString("No error function")
}

inline fun <reified T> literal(literal: T?): Expression {

    if (literal is bool || literal is Boolean) {
        return literalBool(literal)
    }

    if (literal is num || literal is Int) {
        return literalNum(literal)
    }

    if (literal is String) {
        return literalString(literal)
    }

    if (literal is List<*>) {
        literal as List<Any>
        return literalList(literal)
    }

    if (literal is Map<*, *>) {
        literal as Map<Any, Any>
        return literalMap(literal)
    }
    throw Exception("Not a supported literal type: $literal.")
}


fun literalList(values: List<Any>, type: Reference? = null): Expression = LiteralListExpression(false, values, type)

fun literalConstList(values: List<Any> = emptyList(), type: Reference? = null): LiteralListExpression =
    LiteralListExpression(true, values, type)

fun literalConstMap(
    values: Map<Any, Any>,
    keyType: Reference? = null,
    valueType: Reference? = null
): LiteralMapExpression = LiteralMapExpression(true, values, keyType, valueType)


fun literalBool(value: bool): Expression {
    return if (value) {
        literalTrue
    } else {
        literalFalse
    }
}

val literalTrue: Expression = LiteralExpression("true")
val literalFalse: Expression = LiteralExpression("false")
val literalNull: Expression = LiteralExpression("null")
fun literalNum(value: num): Expression = LiteralExpression("$value")
val literalArray: Expression = LiteralExpression("[]")

/// Create a literal expression from a string [value].
///
/// **NOTE**: The string is always formatted `'<value>'`.
///
/// If [raw] is `true`, creates a raw String formatted `r'<value>'` and the
/// value may not contain a single quote.
/// If [raw] is `false` escapes single quotes in the value.
fun literalString(value: String, raw: Boolean = false): Expression {
    if (raw && value.contains("'")) {
        throw Exception("Cannot include a single quote in a raw string")
    }
    val escaped = value.replace("'", "\\" + "'")

    val str: String = when (raw) {
        true -> "r"
        else -> ""
    }


    return LiteralExpression(str + "'${escaped}'")
}


class LiteralExpression(val literal: String) : Expression {


    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor
        return visitor.visitLiteralExpression(this, context)

    }

    override fun toString() = literal
}

class LiteralListExpression(
    val isConst: Boolean,
    val values: List<Any>,
    val type: Reference? = null
) : Expression {


    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor

        return visitor.visitLiteralListExpression(this, context)

    }

    override fun toString(): String {
        var constString = ""

        if (isConst) {
            constString = "$CONST "
        }
        return constString + "[${values.map { literal(it) }.joinToString(", ")}]"
    }

}


fun literalMap(values: Map<Any, Any>, keyType: Reference? = null, valueType: Reference? = null): LiteralMapExpression =
    LiteralMapExpression(false, values, keyType, valueType)


class LiteralMapExpression(
    val isConst: Boolean,
    val values: Map<Any, Any>,
    val keyType: Reference? = null,
    val valueType: Reference? = null
) :
    Expression {


    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as ExpressionVisitor

        return visitor.visitLiteralMapExpression(this, context)

    }

    override fun toString() = "{$values}"

}