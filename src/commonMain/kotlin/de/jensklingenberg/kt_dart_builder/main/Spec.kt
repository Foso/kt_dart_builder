package de.jensklingenberg.kt_dart_builder.main

interface Spec {
    fun <T> accept(visitor: SpecVisitor<T>, context: T? = null): T

}


