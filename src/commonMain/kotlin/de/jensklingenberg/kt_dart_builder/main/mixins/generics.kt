package de.jensklingenberg.kt_dart_builder.main.mixins

import de.jensklingenberg.kt_dart_builder.main.specs.Reference


interface HasGenerics {
    /// Generic type parameters.
    val types: BuiltList<Reference>
}

interface HasGenericsBuilder {
    /// Generic type parameters.
    val types: ListBuilder<Reference>
}
