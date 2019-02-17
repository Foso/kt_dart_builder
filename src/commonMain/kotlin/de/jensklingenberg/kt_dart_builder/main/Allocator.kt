package de.jensklingenberg.kt_dart_builder.main

import de.jensklingenberg.kt_dart_builder.main.specs.Directive
import de.jensklingenberg.kt_dart_builder.main.specs.Reference


open class Allocator {

  private  val _imports = mutableListOf<String>()


    companion object {
        val none = de.jensklingenberg.kt_dart_builder.main.NullAllocator()
        fun simplePrefixing(): de.jensklingenberg.kt_dart_builder.main.PrefixedAllocator {
            return de.jensklingenberg.kt_dart_builder.main.PrefixedAllocator()
        }
    }

  open  val allocate: (Reference) -> String
        get() = { ref ->
            allocate(ref)
        }


    open fun allocate(ref: Reference): String {

        if (ref.url.isNotEmpty() ) {
            _imports.add(ref.url)
        }
        return ref.symbol

    }



    //fun allocate(ref: Reference): String


    open val imports: List<Directive>
        get() = _imports.map { u -> Directive.import(u)}



}

class NullAllocator() : de.jensklingenberg.kt_dart_builder.main.Allocator() {
    override val allocate: (Reference) -> String
        get() = {
            it.symbol
        }


    override val imports: List<Directive> = listOf()


}

class PrefixedAllocator() : de.jensklingenberg.kt_dart_builder.main.Allocator() {


    val imports_ = mutableMapOf<String, Int>()
    val _doNotPrefix = listOf("dart:core")
    var _keys = 1

    override val imports: List<Directive>
        get() = imports_.keys.map { u -> Directive.import(u, _as = "_i${imports_[u]}") }

    override val allocate: (Reference) -> String
        get() = { ref ->
            allocate(ref)
        }


    override fun allocate(ref: Reference): String {
        val symbol = ref.symbol
        if (ref.url.isEmpty() || _doNotPrefix.contains(ref.url)) {
            return symbol
        }
        return "_i${imports_.getOrPut(ref.url, { _nextKey() })}.$symbol"

    }

    fun _nextKey(): Int = _keys++


}
