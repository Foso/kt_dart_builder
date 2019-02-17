package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.Allocator
import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import de.jensklingenberg.kt_dart_builder.main.specs.expression.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpresionTest {


    @Test
    fun `should emit a simple expression`() {

        val expect = "null"
        val actual = literalNull


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a String`() {

        val expect = "monkey"
        val actual = literalString("monkey")

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a raw String`() {

        val expect = "monkey"
        val actual = literalString("'monkey'", raw = true)

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }


    @Test
    fun `should escape single quotes in a String`() {

        val expect = "'don\\'t'"
        val actual = literalString("don't")

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a && expression`() {

        val expect = "true && false"
        val actual = literalTrue.and(literalFalse)

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a list`() {

        val expect = "[]"
        val actual = literalList(emptyList())

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a const list`() {

        val expect = "const []"
        val actual = literalConstList(emptyList())

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit an explicitly typed list`() {

        val expect = "<int>[]"
        val actual = literalList(emptyList(), refer("int"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a map`() {

        val expect = "{}"
        val actual = literalMap(emptyMap())

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a const map`() {

        val expect = "const {}"
        val actual = literalConstMap(emptyMap())

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a map of other literals and expressions`() {

        val expect = "{1: 'one', 2: two, three: 3, Map(): null}"
        val actual = literalMap(
            mapOf(
                1 to "one",
                2 to refer("two"),
                refer("three") to 3,
                refer("Map").newInstance() to literalNull
            )
        )


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a list of other literals and expressions`() {

        val expect = "[[], true, null, Map()]"
        val actual = literalList(listOf(literalArray, true, literalNull, refer("Map").newInstance()))


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a type as an expression`() {

        val expect = "Map"
        val actual = refer("Map")


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a scoped type as an expression`() {

        val expect = "_i1.Foo"
        val actual = refer("Foo", "package:foo/foo.dart")


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter(de.jensklingenberg.kt_dart_builder.main.Allocator.simplePrefixing()))}")
    }

    @Test
    fun `should emit invoking Type()`() {

        val expect = "Map()"
        val actual = refer("Map").newInstance()


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking named constructor`() {

        val expect = "Foo.bar()"
        val actual = refer("Foo").newInstanceNamed("bar")


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking const Type()`() {

        val expect = "const Object()"
        val actual = refer("Object").constInstance()


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a property accessor`() {

        val expect = "foo.bar"
        val actual = refer("foo").property("bar")


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a null safe property accessor`() {

        val expect = "foo?.bar"
        val actual = refer("foo").nullSafeProperty("bar")


        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with a single positional argument`() {

        val expect = "foo(1)"
        val actual = refer("foo").call(
            listOf(
                literal(1)
            )

        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with positional arguments`() {

        val expect = "foo(1, 2, 3)"
        val actual = refer("foo").call(
            listOf(
                literal(1),
                literal(2),
                literal(3)
            )

        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with a single named argument`() {

        val expect = "foo(bar: 1)"
        val actual = refer("foo").call(
            emptyList(), mapOf(
                "bar" to literal(1)

            )
        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with named arguments`() {

        val expect = "foo(bar: 1, baz: 2)"
        val actual = refer("foo").call(
            emptyList(), mapOf(
                "bar" to literal(1),
                "baz" to literal(2)
            )
        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with positional and named arguments`() {

        val expect = "foo(1, bar: 2, baz: 3)"
        val actual = refer("foo").call(
            listOf(literal(1)), mapOf(
                "bar" to literal(2),
                "baz" to literal(3)
            )
        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with a single type argument`() {

        val expect = "foo<String>()"
        val actual = refer("foo").call(typeArguments = listOf(refer("String")))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit invoking a method with type arguments`() {

        val expect = "foo<String, int>()"
        val actual = refer("foo").call(typeArguments = listOf(refer("String"), refer("int")))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should emit a function type`() {

        val expect = "void Function()"
        val actual = FunctionType(returnType = refer("void"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }


    @Test
    fun `should emit a typedef statement`() {

        val expect = "typedef Void0 = void Function();"
        val actual = FunctionType(returnType = refer("void")).toTypeDef("Void0")

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }


    @Test
    fun `should emit a function type with type parameters`() {

        val expect = "T Function<T>()"
        val actual = FunctionType(
            returnType = refer("T"),
            types = listOf(refer("T"))
        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a function type a single parameter`() {

        val expect = "Function(String)"
        val actual = FunctionType(
            requiredParameters = listOf(refer("String"))
        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }


    @Test
    fun `should emit a function type with parameters`() {

        val expect = "Function(String, [int])"
        val actual = FunctionType(
            requiredParameters = listOf(refer("String")),
            optionalParameters = listOf(refer("int"))
        )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a function type with named parameters`() {

        val expect = "Function({int x, int y})"
        val actual = FunctionType(namedParameters = mapOf("x" to refer("int"), "y" to refer("int")))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }


    @Test
    fun `should emit a closure`() {
        val expect = "map.putIfAbsent('foo', () => true)"
        val actual = refer("map")
            .property("putIfAbsent")
            .call(
                positionalArguments = listOf(
                    literalString("foo"),
                    Method(body = literalTrue.code).closure
                )
            )

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a assignment`() {
        val expect = "foo = true"
        val actual = refer("foo")
            .assign(literalTrue)

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a null-aware assignment`() {
        val expect = "foo ??= true"
        val actual = refer("foo")
            .assignNullAware(literalTrue)

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an index operator`() {
//TODO: Find out why it isnt "bar['key']"
        val expect = "var foo = bar  ['key'];"
        val actual = refer("bar")
            .index(literalString("key"))
            .assignVar("foo").statement

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an index operator set`() {

        val expect = "var foo = bar  ['key'] = false;"
        val actual = refer("bar")
            .index(literalString("key"))
            .assign(literalFalse)
            .assignVar("foo").statement

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a null-aware index operator set`() {

        val expect = "var foo = bar  [true] ??= false;"
        val actual = refer("bar").index(literalTrue).assignNullAware(literalFalse).assignVar("foo").statement

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))

    }

    @Test
    fun `should emit assigning to a var`() {

        val expect = "var foo = true"
        val actual = literalTrue.assignVar("foo")

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit assigning to a type`() {

        val expect = "bool  foo = true"
        val actual = literalTrue.assignVar("foo", refer("bool"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit assigning to a final`() {

        val expect = "final foo = true"
        val actual = literalTrue.assignFinal("foo")

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))

    }

    @Test
    fun `should emit assigning to a const`() {

        val expect = "const foo = true"
        val actual = literalTrue.assignConst("foo")

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit await`() {

        val expect = "await  future"
        val actual = refer("future").awaited

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit return`() {

        val expect = "return  null"
        val actual = literalNull.returned

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }


    @Test
    fun `should emit an explicit cast`() {

        val expect = """(
                |foo as String.length
                |)""".trimMargin()
        val actual = refer("foo").asA(refer("String").property("length"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }


    @Test
    fun `should emit an is check`() {

        val expect = "foo is String"
        val actual = refer("foo").isA(refer("String"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}".replace("\n", ""))

    }

    @Test
    fun `should emit an is! check`() {

        val expect = "foo is! String"
        val actual = refer("foo").isNotA(refer("String"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an equality check`() {

        val expect = "foo == 'bar'"
        val actual = refer("foo").equalTo(literalString("bar"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an inequality check`() {

        val expect = "foo != 'bar'"
        val actual = refer("foo").notEqualTo(literalString("bar"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }


    @Test
    fun `should emit an greater than check`() {

        val expect = "foo > 'bar'"
        val actual = refer("foo").greaterThan(literalString("bar"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an less than check`() {

        val expect = "foo < 'bar'"
        val actual = refer("foo").lessThan(literalString("bar"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an greater or equals check`() {

        val expect = "foo >= 'bar'"
        val actual = refer("foo").greaterOrEqualTo(literalString("bar"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an less or equals check`() {

        val expect = "foo <= 'bar'"
        val actual = refer("foo").lessOrEqualTo(literalString("bar"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit a conditional`() {

        val expect = "foo ? 1 : 2"
        val actual = refer("foo").conditional(literal(1), literal(2))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an operator add call`() {

        val expect = "foo + foo2"
        val actual = refer("foo").operatorAdd(refer("foo2"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an operator substract call`() {

        val expect = "foo - foo2"
        val actual = refer("foo").operatorSubstract(refer("foo2"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an operator multiply call`() {

        val expect = "foo * foo2"
        val actual = refer("foo").operatorMultiply(refer("foo2"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an operator divide call`() {

        val expect = "foo / foo2"
        val actual = refer("foo").operatorDivide(refer("foo2"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }

    @Test
    fun `should emit an euclidean modulo operator call`() {

        val expect = "foo % foo2"
        val actual = refer("foo").operatorEuclideanModulo(refer("foo2"))

        assertEquals(expect, "${actual.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")

    }


}
