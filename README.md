<h1 align="center">kt_dart_builder </h1>

[![jCenter](https://img.shields.io/badge/Apache-2.0-green.svg
)](https://github.com/Foso/KotlinReactNativeMpp/blob/master/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![jCenter](https://img.shields.io/badge/v-1.0.0-green.svg
)]()

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


License
-------

The original project is licensed under  [BSD-3 License](https://github.com/dart-lang/code_builder/blob/master/LICENSE)

This project is licensed under Apache License, Version 2.0

    Copyright 2019 Jens Klingenberg

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

