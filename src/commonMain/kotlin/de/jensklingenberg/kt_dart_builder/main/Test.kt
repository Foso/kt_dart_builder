package de.jensklingenberg.kt_dart_builder.main

import de.jensklingenberg.kt_dart_builder.main.specs.*
import de.jensklingenberg.kt_dart_builder.main.specs.expression.literalString

fun main(args: Array<String>) {
   // println(scopedLibrary())
print(aClass())
}

fun aClass(): String {

    val docs = listOf("/// My favorite class.")

    val expect = "/// My favorite class. class Foo {}"
    val c = Class(name = "Foo",docs = docs)

    return "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}"


}


/// Outputs:
///
/// ```dart
/// import 'package:a/a.dart' as _i1;
/// import 'package:b/b.dart' as _i2;
///
/// _i1.Thing doThing() {}
/// _i2.Other doOther() {}
/// ```

fun scopedLibrary(): String {
    val list = mutableListOf<Method>()

    list.add(
            Method(

                    body = StaticCode(""),
                    name = "doThing",

                    returns = refer("Thing", "package:a/a.dart")
            )


    )

    list.add(
            Method(

                    body = StaticCode(""),
                    name = "doOther",

                    returns = refer("Other", "package:b/b.dart")
            )


    )

    val lib = Library(body = list)

    return "${lib.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter.scoped())}"
}


fun animalClass(): String {


    /*

String animalClass() {
  final animal = Class((b) => b
    ..name = 'Animal'
    ..extend = refer('Organism')
    ..methods.add(Method.returnsVoid((b) => b
      ..name = 'eat'
      ..body = refer('print').call([literalString('Yum!')]).code)));



  return '${animal.accept(DartEmitter())}';
     */
    val list = mutableListOf<Method>()

    list.add(Method(name = "eat", body = refer("print").call(listOf(literalString("Yum!"))).code))


    val c = Class(
            name = "Hallo",
            //extend = Reference("Orgnism"),
            methods = list
    )


    return "${c.accept(de.jensklingenberg.kt_dart_builder.main.DartEmitter())}"

}