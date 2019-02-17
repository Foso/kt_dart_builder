package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalConstList
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalConstMap
import kotlin.test.Test
import kotlin.test.assertEquals

class ConstTest {

    val constMap = literalConstMap(
        mapOf(
            "list" to literalConstList(),
            "duration" to refer("Duration").constInstance()
        )
    )


    @Test
    fun `expression`() {
        val expect = """|const {'list': [], 'duration': Duration()}""".trimMargin()

        assertEquals(expect, "${constMap.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `assignConst`() {
        val expect = """|const constField = {'list': [], 'duration': Duration()}""".trimMargin()

        assertEquals(expect, "${constMap.assignConst("constField").accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter.scoped())}")
    }

    @Test
    fun `should emit a source file with imports in defined order`() {
        val expect = """|const val1 = ConstClass();const val2 = ConstClass.other();""".trimMargin()
        val library = Library(
            body = listOf(
                Field(
                    name = "val1",
                    modifier = FieldModifier.constant_,
                    assignment = refer("ConstClass").constInstance().code
                ),
                Field(
                    name = "val2",
                    modifier = FieldModifier.constant_,
                    assignment = refer("ConstClass").constInstanceNamed("other").code
                )
            )
        )

        assertEquals(expect, "${library.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter.scoped())}")
    }

}