package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.mixins.BuiltList
import de.jensklingenberg.kt_dart_builder.main.mixins.HasAnnotations
import de.jensklingenberg.kt_dart_builder.main.mixins.HasDartDocs
import de.jensklingenberg.kt_dart_builder.main.mixins.bool


class Constructor(
    val body: Code? = null,
    val external: bool = false,
    val constant: bool = false,
    val factory: bool = false,
    override val docs: BuiltList<String> = emptyList(),
    override val annotations: List<Expression> = emptyList(),
    val lambda: bool? = null,
    val name: String? = null,
    val redirect: Reference? = null,
    val requiredParameters: BuiltList<Parameter> = emptyList(),
    val optionalParameters: BuiltList<Parameter> = emptyList(),
    val initializers: BuiltList<Code> = emptyList()
) : HasAnnotations, HasDartDocs {


}