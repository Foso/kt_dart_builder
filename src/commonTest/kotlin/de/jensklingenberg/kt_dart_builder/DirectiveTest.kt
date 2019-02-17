package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DirectiveTest {

    @Test
    fun `should emit a block of code`() {

        val expect = """|if (foo) {  print(true);}""".trimMargin()

        val LinkedHashMap = refer("LinkedHashMap", "dart:collection");


        val block = Library(
            directives = listOf(
                Directive.export("../relative.dart"),
                Directive.export("package:foo/foo.dart")
            ),
            body = listOf(
                Field(
                    name = "relativeRef",
                    modifier = FieldModifier.final_,
                    assignment = refer("Relative", "../relative.dart").newInstance().code
                ),
                Field(
                    name = "pkgRefFoo",
                    modifier = FieldModifier.final_,
                    assignment = refer("Foo", "package:foo/foo.dart").newInstance().code
                ),
                Field(
                    name = "pkgRefBar",
                    modifier = FieldModifier.final_,
                    assignment = refer("Bar","package:foo/bar.dart").newInstance().code
                ),
                Field(
                    name = "collectionRef",
                    modifier = FieldModifier.final_,
                    assignment = LinkedHashMap.newInstance().code
                )
            )
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter.scoped())}")
    }

}