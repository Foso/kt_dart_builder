package de.jensklingenberg.kt_dart_builder.main.specs

interface StringSink {
    fun write(any: Any = "")
    fun writeLn(obj: Any = "")

    fun writeAll(objects: List<Any>, seperator: String = "")


}