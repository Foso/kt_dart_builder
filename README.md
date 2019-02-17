# kt_dart_builder
A fluent, builder-based kotlin library for generating valid Dart code.
This is a Kotlin Port of https://github.com/dart-lang/code_builder v3.2.0

## Usage

`code_builder` has a narrow and user-friendly API.

See the `example` and `test` folders for additional examples.

For example creating a class with a method:

```kotlin

fun main() {
    val animal = Class(
        name = "Animal",
        extend = refer("Organism"),
        methods = listOf(Method(
            name ="eat",
            returns = refer("void"),
            body = refer("print('Yum')").code
        ))
    )

    print("${animal.accept(DartEmitter())}")
}
```

Outputs:
```dart
class Animal extends Organism {
  void eat() => print('Yum!');
}
```
