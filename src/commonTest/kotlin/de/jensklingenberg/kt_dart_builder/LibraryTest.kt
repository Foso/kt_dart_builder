package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.Allocator
import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {


    @Test
    fun `should emit a source file with manual imports`() {

        val LinkedHashMap = refer("LinkedHashMap", "dart:collection")

        val expect = "import 'dart:collection';final test = LinkedHashMap();"
        val c = Library(
            directives = listOf(Directive.import("dart:collection")),
            body = listOf(
                Field(
                    name = "test",
                    modifier = FieldModifier.final_,
                    assignment = LinkedHashMap.newInstance().code
                )
            )
        )


        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }


    @Test
    fun `should emit a source file with a deferred import`() {


        val expect = "import 'package:foo/foo.dart' deferred  as foo;"
        val c = Library(
            directives = listOf(Directive.importDeferredAs("package:foo/foo.dart", "foo"))

        )


        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a source file with a "show" combinator`() {


        val expect = "import 'package:foo/foo.dart' show Foo, Bar;"
        val c = Library(
            directives = listOf(Directive.import(url = "package:foo/foo.dart", show = listOf("Foo", "Bar")))

        )


        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a source file with a "hide" combinator`() {


        val expect = "import 'package:foo/foo.dart' hide Foo, Bar;"
        val c = Library(
            directives = listOf(Directive.import(url = "package:foo/foo.dart", hide = listOf("Foo", "Bar")))

        )


        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a source file with allocation`() {
        val linked = refer("LinkedHashMap", "dart:collection")

        val expect = "import 'dart:collection';final test = LinkedHashMap();"
        val c = Library(
            body = listOf(Field(
                name = "test",
                modifier = FieldModifier.final_,
                assignment = Code.scope { a -> a(linked) + "()" }
            ))


        )


        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter(de.jensklingenberg.kt_dart_builder.main.Allocator()))}")
    }

    @Test
    fun `should emit a source file with allocation + prefixing`() {
        val linked = refer("LinkedHashMap", "dart:collection")

        val expect = "import 'dart:collection' as _i1;final test = _i1.LinkedHashMap();"
        val c = Library(
            body = listOf(Field(
                name = "test",
                modifier = FieldModifier.final_,
                assignment = Code.scope { a -> a(linked) + "()" }
            ))


        )


        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter(de.jensklingenberg.kt_dart_builder.main.Allocator.simplePrefixing()))}")
    }



}