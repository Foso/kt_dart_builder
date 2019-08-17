package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.mixins.BuiltList
import de.jensklingenberg.kt_dart_builder.main.mixins.HasAnnotations
import de.jensklingenberg.kt_dart_builder.main.mixins.HasDartDocs
import de.jensklingenberg.kt_dart_builder.main.mixins.bool

class Field(
    override val annotations: List<Expression> = emptyList(),
    override val docs: BuiltList<String> = emptyList(),
    val static: bool = false,
    val modifier: FieldModifier = FieldModifier.var_,
    val type: Reference? = null,
    val name: String = "",
    val assignment: Code? = null
) : Spec, HasAnnotations, HasDartDocs {

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T = visitor.visitField(this, context)
}

enum class FieldModifier {
    var_,
    final_,
    constant_,
}
