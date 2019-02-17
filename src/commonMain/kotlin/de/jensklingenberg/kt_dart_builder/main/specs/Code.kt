package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.poet.Allocate


interface Code : Spec {
    companion object {
        fun scope(code: (Allocate) -> String): Code = ScopedCode(code)
    }

}


interface CodeVisitor<T> : SpecVisitor<T> {
    fun visitBlock(code: Block, output: T?): T
    fun visitStaticCode(code: StaticCode, context: StringSink?): T
    fun visitScopedCode(code: ScopedCode, output: StringSink?): T
}

class Block(val statements: List<Code> = emptyList()) : Code, Spec {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as CodeVisitor
        return visitor.visitBlock(this, context)

    }

    companion object {
        fun of(statements: List<Code>): Block = Block(statements)

    }
}


/// Represents a simple, literal code block to be inserted as-is.
class StaticCode(val code: String) : Code {


    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as CodeVisitor
        return visitor.visitStaticCode(this, context as StringSink)
    }

    override fun toString() = code
}


/// Represents a code block that may require scoping.
class ScopedCode(val code: (Allocate) -> String) : Code {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        visitor as CodeVisitor
        return visitor.visitScopedCode(this, context as StringSink)
    }

    override fun toString() = code { ref -> ref.symbol }

}


