package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.specs.AS
import de.jensklingenberg.kt_dart_builder.main.specs.refer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AllocatorTest {


    @Test
    fun `should emit a simple expression`() {

        val expect = "Foo"
        val actual = de.jensklingenberg.kt_dart_builder.main.Allocator()


        assertEquals(actual.allocate(refer("Foo", "package")), expect)
    }


    @Test
    fun `should collect import URLs`() {

        //TODO: Implement test

    }


    @Test
    fun `none should do nothing`() {

        val allocator = de.jensklingenberg.kt_dart_builder.main.Allocator.none

        assertEquals(allocator.allocate(refer("Foo", "package:Foo")), "Foo")
        assertTrue(allocator.imports.isEmpty())

    }

    @Test
    fun `simplePrefixing should add import prefixes`() {

        val allocator = de.jensklingenberg.kt_dart_builder.main.Allocator.simplePrefixing()

        assertEquals(allocator.allocate(refer("List", "dart:core")), "List")
        assertEquals(allocator.allocate(refer("LinkedHashMap", "dart:collection")), "_i1.LinkedHashMap")

        assertEquals("dart:collection as _i1",allocator.imports.map { "${it.url} "+ AS + " ${it._as}" }.first())


    }


}