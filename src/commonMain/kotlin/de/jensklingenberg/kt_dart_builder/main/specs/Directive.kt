package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.SpecVisitor
import de.jensklingenberg.kt_dart_builder.main.mixins.bool


class Directive(
    val type: DirectiveType? = null,
    val url: String = "",
    val _as: String = "",
    val defered: bool = false,
    val show: List<String> = emptyList(),
    val hide: List<String> = emptyList()
) : Spec {

    companion object {
        fun import(
            url: String = "",
            _as: String = "",
            show: List<String> = emptyList(),
            hide: List<String> = emptyList()
        ): Directive {
            return Directive(
                url = url,
                type = DirectiveType.import,
                _as = _as,
                show = show,
                hide = hide
            )
        }

        fun importDeferredAs(
            url: String = "",
            _as: String = "",
            show: List<String> = emptyList(),
            hide: List<String> = emptyList()
        ): Directive {
            return Directive(
                url = url,
                type = DirectiveType.import,
                _as = _as,
                show = show,
                hide = hide,
                defered = true
            )
        }

        fun export(
            url: String = "",
            _as: String = "",
            show: List<String> = emptyList(),
            hide: List<String> = emptyList()
        ): Directive {
            return Directive(
                url = url,
                type = DirectiveType.export,
                _as = _as,
                show = show,
                hide = hide
            )
        }
    }

    override fun <T> accept(visitor: SpecVisitor<T>, context: T?): T {
        return visitor.visitDirective(this, context)
    }

}


enum class DirectiveType {
    import,
    export,
}
