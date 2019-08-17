package de.jensklingenberg.kt_dart_builder.main.mixins


interface HasDartDocs {
    val docs: BuiltList<String>
}

interface HasDartDocsBuilder {
    var docs: ListBuilder<String>
}