package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.specs.expression.*
import kotlin.reflect.KClass


open class Reference : Expression {

    fun newInstance(
        positionalArguments: List<Expression> = emptyList(),
        namedArguments: Map<String, Expression> = emptyMap(),
        typeArguments: List<Reference> = emptyList()
    ): Expression {
        return InvokeExpression(
            this,
            positionalArguments,
            namedArguments,
            typeArguments,
            "",
            InvokeExpressionType.newInstance
        )
    }

    fun newInstanceNamed(
        name: String,
        positionalArguments: List<Expression> = emptyList(),
        namedArguments: Map<String, Expression> = emptyMap(),
        typeArguments: List<Reference> = emptyList()
    ): Expression {
        return InvokeExpression(
            this,
            positionalArguments,
            namedArguments,
            typeArguments,
            name,
            InvokeExpressionType.newInstance
        )
    }

    open var symbol: String = ""
    open var url: String = ""

    constructor()


    constructor(symbol: String, url: String = "") {
        this.symbol = symbol
        this.url = url
    }

    override val expression: Expression
        get() {
            return CodeExpression(Code.scope { a -> a(this) })
        }

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T = visitor.visitReference(this, context)


    open val type: Reference
        get() {
            if (this is TypeReference) {
                return TypeReference(this.url, this.symbol, bound = this.bound, types = this.types)

            }
            return TypeReference(this.url, this.symbol)
        }


}


fun refer(symbol: String, url: String = ""): Reference = Reference(symbol, url)


