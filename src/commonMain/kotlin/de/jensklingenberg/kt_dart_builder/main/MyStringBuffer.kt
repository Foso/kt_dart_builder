package de.jensklingenberg.kt_dart_builder.main

import de.jensklingenberg.kt_dart_builder.main.specs.StringSink

class MyStringBuffer : StringSink {
    val buffer = StringBuilder()


    override fun writeLn(obj: Any) {
        buffer.append(obj)
        //  buffer.append("\n");

        //  println(obj)
    }

    override fun write(seperator: Any) {
        buffer.append(seperator)


    }

    override fun writeAll(objects: List<Any>, seperator: String) {
        buffer.append(objects.joinToString(seperator))


        // buffer.append(objects)
        //  println(objects)

    }

    override fun toString(): String {
        return buffer.toString()
    }


}