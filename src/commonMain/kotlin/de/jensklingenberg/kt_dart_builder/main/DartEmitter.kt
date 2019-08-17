package de.jensklingenberg.kt_dart_builder.main

import de.jensklingenberg.kt_dart_builder.main.mixins.bool
import de.jensklingenberg.kt_dart_builder.main.specs.*


class DartEmitter : CodeAndExpressionEmitter {

    override var allocator: de.jensklingenberg.kt_dart_builder.main.Allocator
    val orderDirectives: bool

    constructor(allocator: de.jensklingenberg.kt_dart_builder.main.Allocator = de.jensklingenberg.kt_dart_builder.main.Allocator.Companion.none, orderDirectives: bool = false) : super() {
        this.allocator = allocator
        this.orderDirectives = orderDirectives

    }

    companion object {
        fun scoped(orderDirectives : bool =false): de.jensklingenberg.kt_dart_builder.main.DartEmitter {
            return de.jensklingenberg.kt_dart_builder.main.DartEmitter(
                de.jensklingenberg.kt_dart_builder.main.Allocator.Companion.simplePrefixing(),
                orderDirectives
            )
        }
    }

    override fun visitAnnotation(spec: Expression, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        output.write(AT)
        spec.accept(this, output)
        output.write(SPACE)
        return output

    }

    override fun visitClass(spec: Class, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()


        spec.docs.forEach { output.writeLn(it) }
        spec.annotations.forEach { a -> visitAnnotation(a, output) }
        if (spec.abstract) {
            output.write(ABSTRACT + " ")
        }
        output.write(CLASS + " ${spec.name}")
        visitTypeParameters(spec.types.map { r -> r.type!! }, output)
        if (spec.extend != null) {
            output.write(" $EXTENDS ")
            spec.extend.type!!.accept(this, output)
        }
        if (spec.mixins.isNotEmpty()) {
            output.apply {
                write(" $WITH ")
                writeAll(spec.mixins.map { m -> m.type!!.accept(this@DartEmitter) }, ",")
            }
        }
        if (spec.implements.isNotEmpty()) {
            output.apply {
                write(" $IMPLEMENTS ")
                writeAll(spec.implements.map { m -> m.type!!.accept(this@DartEmitter) }, ",")
            }
        }
        output.write(" {")
        spec.constructors.forEach { c ->
            visitConstructor(c, spec.name, output)
            output.writeLn()
        }
        spec.fields.forEach { f ->
            visitField(f, output)
            output.writeLn()
        }
        spec.methods.forEach { m ->
            visitMethod(m, output)
            if (_isLambdaMethod(m)) {
                output.write(";")
            }
            output.writeLn()

        }
        output.writeLn(" }")
        return output

    }


    fun _isLambdaMethod(method: Method): bool = method.lambda ?: _isLambdaBody(method.body)

    fun _isLambdaBody(code: Code?): bool {
        return code is ToCodeExpression && !code.isStatement
    }

    fun _isLambdaConstructor(constructor: Constructor): bool {

        when (constructor.lambda) {
            true -> {
                return true
            }
            null -> {
                return constructor.lambda ?: constructor.factory && _isLambdaBody(constructor.body)

            }
            false -> {
                return false
            }
        }
    }

    override fun visitConstructor(spec: Constructor, clazz: String, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        spec.docs.forEach { output.writeLn(it) }
        spec.annotations.forEach { a -> visitAnnotation(a, output) }

        if (spec.external) {
            output.write("$EXTERNAL ")
        }
        if (spec.factory) {
            output.write("$FACTORY ")
        }
        if (spec.constant) {
            output.write("$CONST ")
        }
        output.write(clazz)
        spec.name?.let {
            output.apply {
                write(".")
                write(it)
            }
        }
        output.write("(")
        if (spec.requiredParameters.isNotEmpty()) {
            var count = 0
            spec.requiredParameters.forEach {
                count++
                _visitParameter(it, output)
                if (spec.requiredParameters.size != count || spec.optionalParameters.isNotEmpty()) {
                    output.write(", ")
                }
            }
        }
        if (spec.optionalParameters.isNotEmpty()) {
            val named = spec.optionalParameters.any { p -> p.named }
            if (named) {
                output.write("{")
            } else {
                output.write("[")
            }
            var count = 0
            spec.optionalParameters.forEach { p ->
                count++
                _visitParameter(p, output, optional = true, named = named)
                if (spec.optionalParameters.size != count) {
                    output.write(", ")
                }
            }
            if (named) {
                output.write("}")
            } else {
                output.write("]")
            }
        }
        output.write(")")
        if (spec.initializers.isNotEmpty()) {
            output.write(" : ")
            var count = 0
            spec.initializers.forEach {
                count++
                it.accept(this, output)
                if (count != spec.initializers.size) {
                    output.write(", ")
                }
            }
        }
        if (spec.redirect != null) {
            output.write(" = ")
            spec.redirect.type!!.accept(this, output)
            output.write(";")

        } else if (spec.body != null) {
            if (_isLambdaConstructor(spec)) {
                output.write(" => ")
                spec.body.accept(this, output)
                output.write(";")
            } else {
                output.write(" { ")
                spec.body.accept(this, output)
                output.write(" }")
            }
        } else {
            output.write(";")
        }
        output.writeLn()
        return output
    }

    private fun _visitParameter(spec: Parameter, output: StringSink, optional: bool = false, named: bool = false) {
        spec.docs.forEach { output.writeLn(it) }
        spec.annotations.forEach { a -> visitAnnotation(a, output) }
        spec.type?.let {
            it.type!!.accept(this, output)
            output.write(SPACE)
        }
        if (spec.toThis) {
            output.write(THIS + ".")
        }
        output.write(spec.name)
        if (optional && spec.defaultTo != null) {
            output.write(" $EQUAL ")
            spec.defaultTo.accept(this, output)
        }

    }

    override fun visitReference(spec: Reference, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        return output.apply {
            write(allocator.allocate(spec))
        }
    }

    override fun visitSpec(spec: de.jensklingenberg.kt_dart_builder.main.Spec, context: StringSink?): StringSink {
        return spec.accept(this, context)

    }


    override fun visitType(spec: TypeReference, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        visitReference(spec, output)
        spec.bound?.let {
            output.write(" $EXTENDS ")
            it.type!!.accept(this, output)
        }
        visitTypeParameters(spec.types.map { r -> r.type!! }, output)
        return output
    }

    override fun visitDirective(spec: Directive, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        if (spec.type == DirectiveType.import) {
            output.write(IMPORT + " ")
        } else {
            output.write(EXPORT + " ")
        }
        output.write("'${spec.url}'")
        if(spec._as.isNotEmpty()){
            if (spec.defered) {
                output.write(" $DEFERRED ")
            }
            output.write(" $AS ${spec._as}")
        }

        if (spec.show.isNotEmpty()) {
            output.apply {
                write(" $SHOW ")
                writeAll(spec.show, ", ")
            }
        } else if (spec.hide.isNotEmpty()) {
            output.apply {
                write(" $HIDE ")
                writeAll(spec.hide, ", ")

            }
        }
        output.write(";")
        return output

    }

    override fun visitMethod(spec: Method, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        spec.docs.forEach { output.writeLn(it) }
        spec.annotations.forEach { a -> visitAnnotation(a, output) }
        if (spec.external) {
            output.write(EXTERNAL + " ")
        }
        if (spec.static) {
            output.write(STATIC + " ")
        }
        spec.returns?.let { ref ->
            ref.accept(this, output)
            output.write(SPACE)
        }
        if (spec.type == MethodType.gettter) {
            output.apply {
                write(GET + " ")
                write(spec.name)
            }
        } else {
            if (spec.type == MethodType.setter) {
                output.write(SET + " ")
            }

            if (spec.name.isNotEmpty()) {
                output.write(spec.name)
            }
            visitTypeParameters(spec.types.map { r -> r.type!! }, output)
            output.write("(")
            if (spec.requiredParameters.isNotEmpty()) {
                var count = 0
                spec.requiredParameters.forEach {
                    count++
                    _visitParameter(it, output)
                    if (spec.requiredParameters.size != count || spec.optionalParameters.isNotEmpty()) {
                        output.write(", ")
                    }
                }
            }
            if (spec.optionalParameters.isNotEmpty()) {
                val named = spec.optionalParameters.any { p -> p.named }
                if (named) {
                    output.write("{")
                } else {
                    output.write("[")
                }
                var count = 0
                spec.optionalParameters.forEach { p ->
                    count++
                    _visitParameter(p, output, optional = true, named = named)
                    if (spec.optionalParameters.size != count) {
                        output.write(", ")
                    }
                }
                if (named) {
                    output.write("}")
                } else {
                    output.write("]")
                }
            }
            output.write(")")

        }

        if (spec.body != null) {

            when (spec.modifier) {

                MethodModifier.async -> {
                    output.write(" " + ASYNC)
                }
                MethodModifier.asyncStar -> {
                    output.write(" " + ASYNCStar)
                }
                MethodModifier.syncStar -> {
                    output.write(" " + SYNCStar)

                }
                else -> {
                }
            }
            if (_isLambdaMethod(spec)) {
                output.write(" => ")
            } else {
                output.write(" { ")
            }
            spec.body.accept(this, output)
            if (!_isLambdaMethod(spec)) {
                output.write(" } ")
            }
        } else {
            output.write(";")
        }
        return output
    }

    override fun visitField(spec: Field, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        spec.docs.forEach { output.writeLn(it) }
        spec.annotations.forEach { a -> visitAnnotation(a, output) }
        if (spec.static) {
            output.write("$STATIC ")
        }
        when (spec.modifier) {

            FieldModifier.var_ -> {
                if (spec.type == null) {
                    output.write(VAR + " ")
                }
            }
            FieldModifier.final_ -> {
                output.write(FINAL + " ")
            }
            FieldModifier.constant_ -> {
                output.write(CONST + " ")
            }
        }
        spec.type?.let {
            it.type!!.accept(this, output)
            output.write(SPACE)
        }
        output.write(spec.name)
        spec.assignment?.let {
            output.write(" = ")
            startConstCode(spec.modifier == FieldModifier.constant_){
                it.accept(this, output)

            }
        }
        output.write(";")
        return output
    }

    override fun visitLibrary(spec: Library, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        val body = de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()
        spec.body.forEach {
            it.accept(this, body)
            if (it is Method && _isLambdaMethod(it)) {
                body.write(";")
            }
        }
        spec.directives.forEach {
            it.accept(this, output)
        }
        allocator.imports.forEach {
            it.accept(this, output)
        }
        output.write(body)
        return output

    }

    override fun visitFunctionType(spec: FunctionType, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()
        spec.returnType?.let {
            it.accept(this, output)
            output.write(SPACE)
        }
        output.write(FUNCTION)
        if (spec.types.isNotEmpty()) {
            output.write("<")
            de.jensklingenberg.kt_dart_builder.main.visitAll<Reference>(spec.types.toSet(), output, { spec ->
                spec.accept(this, output)
            })
            output.write(GREATER)
        }
        output.write("(")
        de.jensklingenberg.kt_dart_builder.main.visitAll(spec.requiredParameters.toSet(), output, { spec ->
            spec.accept(this, output)
        })
        if (spec.optionalParameters.isNotEmpty() ||
            spec.namedParameters.isNotEmpty() && spec.requiredParameters.isNotEmpty()
        ) {
            output.write(", ")
        }
        if (spec.optionalParameters.isNotEmpty()) {
            output.write("[")
            de.jensklingenberg.kt_dart_builder.main.visitAll(spec.optionalParameters.toSet(), output, { spec ->
                spec.accept(this, output)
            })
            output.write("]")
        } else if (spec.namedParameters.isNotEmpty()) {
            output.write("{")
            de.jensklingenberg.kt_dart_builder.main.visitAll<String>(spec.namedParameters.keys, output, { name ->
                spec.namedParameters[name]!!.accept(this, output)
                output.apply {
                    write(SPACE)
                    write(name)
                }
            })
            output.write("}")
        }
        return output.apply {
            write(")")
        }

    }


    override fun visitTypeParameters(specs: List<Reference>, output: StringSink?): StringSink {
        val output = output ?: de.jensklingenberg.kt_dart_builder.main.MyStringBuffer()

        if (specs.isNotEmpty()) {
            output.apply {
                write("<")
                writeAll(specs.map { s -> s.accept(this@DartEmitter) }, ",")
                write(">")
            }
        }
        return output
    }

}


