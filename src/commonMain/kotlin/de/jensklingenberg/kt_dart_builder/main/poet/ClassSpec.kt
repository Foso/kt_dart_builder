package de.jensklingenberg.kt_dart_builder.main.poet

import de.jensklingenberg.kt_dart_builder.main.specs.Class
import de.jensklingenberg.kt_dart_builder.main.specs.Method

class ClassSpec private constructor(builder: Builder) {



    class Builder(val name: String) {


        fun build(): Class {
            return Class(name = name)
        }

    }

    companion object {
        fun builder(name:String)=Builder(name)
    }

}