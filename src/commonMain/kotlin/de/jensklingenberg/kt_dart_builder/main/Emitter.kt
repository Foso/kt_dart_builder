package de.jensklingenberg.kt_dart_builder.main

import de.jensklingenberg.kt_dart_builder.main.specs.StringSink

fun <T> visitAll(elements: Set<T>, output: StringSink, visit: (element: T) -> Unit, seperator: String = ", "): StringSink {
    // Basically, this whole method is an improvement on
    //   output.writeAll(specs.map((s) => s.accept(visitor));
    //
    // ... which would allocate more StringBuffer(s) for a one-time use.
    if (elements.isEmpty()) {
        return output
    }

    elements.forEachIndexed { index, element ->
        if (index == 0) {
            visit(element)
        }
        else {
            output.write(seperator)
            visit(element)
        }
    }

    return output;

}