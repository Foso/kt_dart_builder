package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalString
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassTest {
    @Test
    fun `should create a class`() {
        val expect = "class Foo { }"
        val c = Class(name = "Foo")

        assertEquals(expect, "${c.accept(DartEmitter())}".replace("\n", ""))
    }





    @Test
    fun `should create an abstract class`() {
        val expect = "abstract class Foo { }"
        val c = Class(name = "Foo", abstract = true)

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with documentations`() {
        val expect = """|/// My favorite class.
                                |class Foo { }
                                |""".trimMargin()


        val c = Class(
            name = "Foo",
            docs = listOf("/// My favorite class.")
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with annotations`() {
        val expect = """|@deprecated @Deprecated('This is an old class') class Foo { }""".trimMargin()

        val c = Class(
            annotations = listOf(
                refer("deprecated"),
                refer("Deprecated").call(listOf(literalString("This is an old class")))),
            name = "Foo"
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a generic type`() {
        val expect = """|class List<T> { }""".trimMargin()


        val c = Class(
            name = "List",
            types = listOf(refer("T"))
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with multiple generic types`() {
        val expect = """|class Map<K,V> { }""".trimMargin()


        val types = listOf(refer("K"), refer("V"))


        val c = Class(name = "Map", types = types)

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a bound generic type`() {
        val expect = """|class Comparable<T extends Comparable<T>> { }""".trimMargin()

        val c = Class(
            name = "Comparable",
            types = listOf(
                TypeReference(
                    symbol = "T",
                    bound = TypeReference(
                        symbol = "Comparable",
                        types = listOf(refer("T").type)
                    )
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class extending another class`() {
        val expect = """|class Foo extends Bar { }""".trimMargin()

        val c = Class(
            name = "Foo",
            extend = refer("Bar").type
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class mixing in another class`() {
        val expect = """|class Foo extends Bar with Foo { }""".trimMargin()

        val c = Class(
            name = "Foo",
            extend = refer("Bar").type,
            mixins = listOf(refer("Foo").type)
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class implementing another class`() {
        val expect = """|class Foo extends Bar implements Foo { }""".trimMargin()

        val c = Class(
            name = "Foo",
            extend = refer("Bar").type,
            implements = listOf(refer("Foo").type)
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a constructor`() {
        val expect = """|class Foo {Foo(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(Constructor())
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a constructor with initializers`() {
        val expect = """|class Foo {Foo() : a = 5, super(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(initializers = listOf(
                StaticCode("a = 5"),
                StaticCode("super()")
            ))
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a annotated constructor`() {
        val expect = """class Foo {@deprecated Foo(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(Constructor(annotations = listOf(refer("deprecated"))))
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a named constructor`() {
        val expect = """|class Foo {Foo.named(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(Constructor(name = "named"))
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }


    @Test
    fun `should create a class with a const constructor`() {
        val expect = """|class Foo {const Foo(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(Constructor(constant = true))
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with an external constructor`() {
        val expect = """|class Foo {external Foo(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(Constructor(external = true))
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }


    @Test
    fun `should create a class with a factory constructor`() {
        val expect = """|class Foo {factory Foo() = _Foo; }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(
                    factory = true,
                    redirect = refer("_Foo")
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a factory lambda constructor`() {
        val expect = """|class Foo {factory Foo() => _Foo(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(
                    factory = true,
                    lambda = true,
                    body = StaticCode("_Foo()")
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }


    @Test
    fun `should create a class with an implicit factory lambda constructor`() {
        val expect = """|class Foo {factory Foo() => _Foo(); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(
                    factory = true,
                    body = refer("_Foo").newInstance().code
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }


    @Test
    fun `should create a class with a constructor with a body`() {
        val expect = """|class Foo {factory Foo() { return _Foo(); } }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(
                    factory = true,
                    body = StaticCode("return _Foo();")
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with method parameters`() {
        val expect = """|class Foo {Foo(a, b, {c}); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(
                   requiredParameters = listOf(
                       Parameter(name="a"),
                       Parameter(name="b")
                   ),
                    optionalParameters = listOf(
                        Parameter(
                        name = "c",
                        named = true
                    )
                    )
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }

    @Test
    fun `should create a class with a constructor+field-formal parameters`() {
        val expect = """|class Foo {Foo(this.a, this.b, {this.c}); }""".trimMargin()

        val c = Class(
            name = "Foo",
            constructors = listOf(
                Constructor(
                    requiredParameters = listOf(
                        Parameter(name="a",toThis = true),
                        Parameter(name="b",toThis = true)
                    ),
                    optionalParameters = listOf(
                        Parameter(
                        name = "c",
                        named = true,
                        toThis = true
                    )
                    )
                )
            )
        )

        assertEquals(expect, "${c.accept(DartEmitter())}")
    }


}
