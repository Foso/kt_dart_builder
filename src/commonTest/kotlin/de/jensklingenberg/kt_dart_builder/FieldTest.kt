package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.Field
import de.jensklingenberg.kt_dart_builder.main.specs.FieldModifier
import de.jensklingenberg.kt_dart_builder.main.specs.StaticCode
import de.jensklingenberg.kt_dart_builder.main.specs.refer
import kotlin.test.Test
import kotlin.test.assertEquals


class FieldTest {
    @Test
    fun `should create a field`() {
        val expect = "var foo;"
        val f = Field(name = "foo")

        assertEquals(expect, "${f.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))
    }

    @Test
    fun `should create a typed field`() {
        val expect = "String foo;"
        val f = Field(name = "foo",
            type = refer("String")
        )

        assertEquals(expect, "${f.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))
    }

    @Test
    fun `should create a final field`() {
        val expect = "final foo;"
        val f = Field(name = "foo",
           modifier = FieldModifier.final_)

        assertEquals(expect, "${f.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))
    }

    @Test
    fun `should create a constant field`() {
        val expect = "const foo;"
        val f = Field(name = "foo",
            modifier = FieldModifier.constant_)

        assertEquals(expect, "${f.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))
    }

    @Test
    fun `should create a field with an assignment`() {
        val expect = "var foo = 1;"
        val f = Field(name = "foo",
            assignment = StaticCode("1")
           )

        assertEquals(expect, "${f.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))
    }
}