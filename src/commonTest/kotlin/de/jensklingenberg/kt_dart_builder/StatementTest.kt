package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.specs.Block
import de.jensklingenberg.kt_dart_builder.main.specs.Method
import de.jensklingenberg.kt_dart_builder.main.specs.StaticCode
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalTrue
import de.jensklingenberg.kt_dart_builder.main.specs.refer
import kotlin.test.Test
import kotlin.test.assertEquals

class StatementTest{


    @Test
    fun `should emit a block of code`() {

        val expect = """|if (foo) {
                                |  print(true);
                                |}""".trimMargin()

     val block=   Block.of(listOf(
            StaticCode("if (foo) {"),
            StaticCode("  print(true);"),
            StaticCode("}")
        ))

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a block of code including expressions`() {

        val expect = """|if (foo) {
                                |print(true);
                                |}""".trimMargin()

        val block=   Block.of(listOf(
            StaticCode("if (foo) {"),
            refer("print").newInstance(positionalArguments = listOf(literalTrue)).statement,
            StaticCode("}")
        ))

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a block of code with lazyily invoked generators`() {

        val expect = """|main() { if (foo) {print(true);} } """.trimMargin()

        val m = Method(
            name = "main",
            body = Block.of(listOf(
                StaticCode("if ("),
                refer("foo").code,
                StaticCode(") {"),
                refer("print").newInstance(positionalArguments = listOf(literalTrue)).statement,
                StaticCode("}")
            ))
        )


        assertEquals(expect, "${m.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

}