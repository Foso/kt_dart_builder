package de.jensklingenberg.kt_dart_builder

import de.jensklingenberg.kt_dart_builder.main.Allocator
import de.jensklingenberg.kt_dart_builder.main.DartEmitter
import de.jensklingenberg.kt_dart_builder.main.specs.*
import kotlin.test.Test
import kotlin.test.assertEquals

class InjectionTest{


    @Test
    fun `should generate a complex generated file`() {
        val expect= "class Injector implements App {Injector(this._module);final Module _module;@override Thing getThing() => Thing(_module.get1(), _module.get2()); }"
        val simpleExpect= "class Injector implements _i1.App {Injector(this._module);final _i2.Module _module;@override _i3.Thing getThing() => _i3.Thing(_module.get1(), _module.get2()); }"

        val App = refer("App","package:app/app.dart")
        val Module = refer("Module","package:app/module.dart")
        val Thing = refer("Thing","package:app/thing.dart")


        val c = Class(
            name = "Injector",
            implements = listOf(App),
            fields = listOf(Field(
                modifier = FieldModifier.final_,
                name = "_module",
                type = Module.type
            )),
            constructors = listOf(Constructor(
                requiredParameters = listOf(Parameter(
                    name = "_module",
                    toThis = true
                ))
            )),
            methods = listOf(
                Method(
                    name = "getThing",
                    body = Thing.newInstance(positionalArguments =  listOf(
                        refer("_module").property("get1").call(),
                        refer("_module").property("get2").call()
                    )).code,
                    returns = Thing,
                    annotations = listOf(refer("override"))
                )
            )
        )

        assertEquals(expect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}")
        assertEquals(simpleExpect, "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter(de.jensklingenberg.kt_dart_builder.main.Allocator.simplePrefixing()))}")

    }



}