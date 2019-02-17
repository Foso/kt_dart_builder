package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.specs.expression.*

interface Expression : Spec {


    /**
    /// Returns the result of `this` `is` [other].
     */

    val code: Code
        get() = ToCodeExpression(this, false)

    val statement: Code
        get() = ToCodeExpression(this, true)

    fun createTypeDef(name: String, type: FunctionType): Code {
        return BinaryExpression(LiteralExpression("typedef $name"), (type as Expression), "=").statement
    }

    val expression: Expression
        get() = this

}


fun Expression.call(
    positionalArguments: List<Expression> = emptyList(),
    namedArguments: Map<String, Expression> = emptyMap(),
    typeArguments: List<Reference> = emptyList(),
    name: String = "",
    type: InvokeExpressionType? = null
): Expression {
    return InvokeExpression(this, positionalArguments, namedArguments, typeArguments, name, type)
}


fun Expression.assign(other: Expression): Expression = BinaryExpression(this, other, "=")
fun Expression.assignNullAware(other: Expression): Expression = BinaryExpression(this, other, "??=")

fun Expression.property(name: String): BinaryExpression = BinaryExpression(this, LiteralExpression(name), ".", false)

fun Expression.and(other: Expression): Expression = BinaryExpression(this, other, "&&")

fun Expression.operatorEuclideanModulo(other: Expression): Expression = BinaryExpression(this, other, "%")

fun Expression.operatorMultiply(other: Expression): Expression = BinaryExpression(this, other, "*")

fun Expression.operatorDivide(other: Expression): Expression = BinaryExpression(this, other, "/")

fun Expression.operatorSubstract(other: Expression): Expression = BinaryExpression(this, other, "-")

fun Expression.operatorAdd(other: Expression): Expression = BinaryExpression(this, other, "+")

fun Expression.conditional(whenTrue: Expression, whenFalse: Expression): Expression =
    BinaryExpression(this, BinaryExpression(whenTrue, whenFalse, ":"), "?")

fun Expression.lessOrEqualTo(other: Expression): Expression = BinaryExpression(this, other, "<=")

fun Expression.greaterOrEqualTo(other: Expression): Expression = BinaryExpression(this, other, ">=")

fun Expression.lessThan(other: Expression): Expression = BinaryExpression(this, other, "<")

fun Expression.greaterThan(other: Expression): Expression = BinaryExpression(this, other, ">")

fun Expression.notEqualTo(other: Expression): Expression = BinaryExpression(this, other, "!=")

fun Expression.equalTo(other: Expression): Expression = BinaryExpression(this, other, "==")

fun Expression.isNotA(other: Expression): Expression = BinaryExpression(this, other, "is!")

fun Expression.isA(other: Expression): Expression = BinaryExpression(this, other, "is")

fun Expression.asA(other: Expression): Expression {
    return CodeExpression(
        Block.of(
            listOf(
                StaticCode("("),
                BinaryExpression((this as Expression), other, "as").code,
                StaticCode(")")

            )
        )
    )
}

fun Expression.index(index: Expression): Expression {
    return BinaryExpression(
        this, CodeExpression(
            Block.of(
                listOf(
                    StaticCode("["),
                    index.code,
                    StaticCode("]")
                )
            )
        ), ""
    )
}


val Expression.returned: Expression
    get() = BinaryExpression(LiteralExpression(RETURN), this, "")

val Expression.expression: Expression
    get() = this

val Expression.awaited: Expression
    get() = BinaryExpression(LiteralExpression(AWAIT), this, "")


fun Expression.assignVar(name: String, type: Reference? = null): Expression {

    var exp: Expression
    if (type == null) {
        exp = LiteralExpression("$VAR $name")
    } else {
        exp = BinaryExpression(type.expression, LiteralExpression(name), "")
    }

    return BinaryExpression(exp, this, "=")
}

fun Expression.assignConst(name: String, type: Reference? = null): Expression {

    var exp: Expression
    if (type == null) {
        exp = LiteralExpression(CONST)
    } else {
        exp = BinaryExpression(LiteralExpression(CONST), type.expression, "")
    }

    return BinaryExpression(exp, this, "$name =", isConst = true)
}

fun Expression.assignFinal(name: String, type: Reference? = null): Expression {

    var exp: Expression
    if (type == null) {
        exp = LiteralExpression(FINAL)
    } else {
        exp = BinaryExpression(LiteralExpression(FINAL), type.expression, "")
    }

    return BinaryExpression(exp, this, "$name =")
}

fun Expression.nullSafeProperty(name: String): Expression = BinaryExpression(this, LiteralExpression(name), "?.", false)

fun Expression.constInstance(
    positionalArguments: List<Expression> = emptyList(),
    namedArguments: Map<String, Expression> = emptyMap(),
    typeArguments: List<Reference> = emptyList()
): Expression = InvokeExpression(this, positionalArguments, namedArguments, typeArguments, "").constOf()

fun Expression.constInstanceNamed(
    name: String,
    positionalArguments: List<Expression> = emptyList(),
    namedArguments: Map<String, Expression> = emptyMap(),
    typeArguments: List<Reference> = emptyList()
): Expression = InvokeExpression(this, positionalArguments, namedArguments, typeArguments, name).newOf()


