package de.jensklingenberg.kt_dart_builder.main.mixins

import de.jensklingenberg.kt_dart_builder.main.poet.BuiltList
import de.jensklingenberg.kt_dart_builder.main.poet.ListBuilder

interface HasDartDocs {
    val docs: BuiltList<String>
}

interface HasDartDocsBuilder {
    var docs: ListBuilder<String>
}