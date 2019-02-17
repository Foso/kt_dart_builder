package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalNull
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalTrue
import kotlin.test.Test
import kotlin.test.assertEquals


class MethodTest {


    @Test
    fun `should create a method`() {

        val expect = """|foo();""".trimMargin()

        val block = Method(name = "foo")

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create an async method`() {

        val expect = """|foo() async => null""".trimMargin()

        val block = Method(
            name = "foo",
            modifier = MethodModifier.async,
            body = literalNull.code
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create an async* method`() {

        val expect = """|foo() async* => null""".trimMargin()

        val block = Method(
            name = "foo",
            modifier = MethodModifier.asyncStar,
            body = literalNull.code
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create an sync* method`() {

        val expect = """|foo() sync* => null""".trimMargin()

        val block = Method(
            name = "foo",
            modifier = MethodModifier.syncStar,
            body = literalNull.code
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }


    @Test
    fun `should create a lambda method implicitly`() {

        val expect = """|bool returnsTrue() => true""".trimMargin()

        val block = Method(
            name = "returnsTrue",
            returns = refer(BOOL),
            body = literalTrue.code
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a normal method implicitly`() {

        val expect = """|void assignTrue() { topLevelFoo = true; } """.trimMargin()

        val block = Method(
            returns = refer("void"),
            name = "assignTrue",
            body = refer("topLevelFoo").assign(literalTrue).statement
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a getter`() {

        val expect = """|external get foo;""".trimMargin()

        val block = Method(
           name = "foo",
            external = true,
            type = MethodType.gettter

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a setter`() {

        val expect = """|external set foo(foo);""".trimMargin()

        val block = Method(
            name = "foo",
            external = true,
            type = MethodType.setter,
            requiredParameters = listOf(Parameter(name = "foo"))

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a return type`() {

        val expect = """|String foo();""".trimMargin()

        val block = Method(
            name = "foo",
           returns = refer("String")


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a void return type`() {

        val expect = """|void foo();""".trimMargin()

        val block = Method(
            name = "foo",
            returns = refer("void")
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a function type return type`() {

        val expect = """|String Function(int) foo();""".trimMargin()

        val block = Method(
            name = "foo",
            returns = FunctionType(
                returnType = refer("String"),
                requiredParameters = listOf(refer("int"))
            )
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a nested function type return type`() {

        val expect = """|String Function(String) Function(int) foo();""".trimMargin()

        val block = Method(
            name = "foo",
            returns = FunctionType(
                returnType = FunctionType(
                    returnType = refer("String"),
                    requiredParameters = listOf(refer("String"))
                ),
                requiredParameters = listOf(refer("int"))
            )
        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a function type argument`() {

        val expect = """|foo(String Function(int) argument);""".trimMargin()

        val block = Method(
            name = "foo",
            requiredParameters = listOf(
                Parameter(
                name="argument",
                type = FunctionType(
                    returnType = refer("String"),
                    requiredParameters = listOf(refer("int"))
                )
            )
            )

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a nested function type argument`() {

        val expect = """|foo(String Function(String) Function(int) argument);""".trimMargin()

        val block = Method(
            name = "foo",
            requiredParameters = listOf(
                Parameter(
                name="argument",
                type = FunctionType(
                    returnType = FunctionType(
                        returnType = refer("String"),
                        requiredParameters = listOf(refer("String"))
                    ),
                    requiredParameters = listOf(refer("int"))
                )
            )
            )

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with generic types`() {

        val expect = """|foo<T>();""".trimMargin()

        val block = Method(
            name = "foo",
            types = listOf(refer("T"))

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create an external method`() {

        val expect = """|external foo();""".trimMargin()

        val block = Method(
            name = "foo",
            external = true

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a body`() {

        val expect = """|foo() { return 1+ 2; } """.trimMargin()

        val block = Method(
            name = "foo",
           body = StaticCode("return 1+ 2;")

        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a lambda method (explicitly)`() {

        val expect = """|foo() => 1 + 2""".trimMargin()

        val block = Method(
            name = "foo",
           lambda = true,
            body = StaticCode("1 + 2")


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a body with references`() {

        val linkedHashMap = refer("LinkedHashMap","dart:collection")

        val expect = """|foo() { return LinkedHashMap(); } """.trimMargin()

        val block = Method(
            name = "foo",
            body = StaticCode("return ${linkedHashMap.symbol}();")


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a parameter`() {

        val expect = """|fib(i);""".trimMargin()

        val block = Method(
            name = "fib",
           requiredParameters = listOf(Parameter(name = "i"))


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with an annotated parameter`() {

        val expect = """|fib(@deprecated i);""".trimMargin()

        val block = Method(
            name = "fib",
            requiredParameters = listOf(
                Parameter(
                name = "i",
                annotations = listOf(refer("deprecated"))
            )
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a parameter with a type`() {

        val expect = """|fib(int i);""".trimMargin()

        val block = Method(
            name = "fib",
            requiredParameters = listOf(
                Parameter(
                name = "i",
               type = refer("int").type
            )
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a parameter with a generic type`() {

        val expect = """|foo<T extends Iterable>(T t, X<T> x);""".trimMargin()

        val block = Method(
            name = "foo",
            types = listOf(TypeReference(symbol = "T",bound = refer("Iterable"))),
            requiredParameters = listOf(
                Parameter(
                    name = "t",
                    type = refer("T")
                ),
                Parameter(
                    name = "x",
                    type = TypeReference(
                        symbol = "X",
                        types = listOf(refer("T"))
                    )
                )
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with an optional parameter`() {

        val expect = """|fib([i]);""".trimMargin()

        val block = Method(
            name = "fib",
            optionalParameters = listOf(
                Parameter(
                name = "i"
            )
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }


    @Test
    fun `should create a method with multiple optional parameters`() {

        val expect = """|foo([a, b]);""".trimMargin()

        val block = Method(
            name = "foo",
            optionalParameters = listOf(
                Parameter(name = "a"),
                Parameter(name = "b")
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with an optional parameter with a value`() {

        val expect = """|fib([i = 0]);""".trimMargin()

        val block = Method(
            name = "fib",
            optionalParameters = listOf(
                Parameter(name = "i",defaultTo = StaticCode("0"))
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a named optional parameter`() {

        val expect = """|fib({i});""".trimMargin()

        val block = Method(
            name = "fib",
            optionalParameters = listOf(
                Parameter(name = "i",named = true)
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a named optional parameter with value`() {

        val expect = """|fib({i = 0});""".trimMargin()

        val block = Method(
            name = "fib",
            optionalParameters = listOf(
                Parameter(
                    name = "i",
                    named = true,
                    defaultTo = StaticCode("0")
                )
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }

    @Test
    fun `should create a method with a mix of parameters`() {

        val expect = """|foo(a, {b});""".trimMargin()

        val block = Method(
            name = "foo",
            requiredParameters = listOf(Parameter(name="a")),
            optionalParameters = listOf(
                Parameter(
                    name = "b",
                    named = true)
            )


        )

        assertEquals(expect, "${block.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
    }



}