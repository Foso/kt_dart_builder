package de.jensklingenberg.kt_dart_builder.main.specs

import de.jensklingenberg.kt_dart_builder.main.Allocator
import de.jensklingenberg.kt_dart_builder.main.MyStringBuffer
import de.jensklingenberg.kt_dart_builder.main.Spec
import de.jensklingenberg.kt_dart_builder.main.mixins.bool

import de.jensklingenberg.kt_dart_builder.main.specs.expression.*
import de.jensklingenberg.kt_dart_builder.main.visitAll

class ExpressionEmitter : ExpressionVisitor<StringSink> {
    override fun visitBinaryExpression(expression: BinaryExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitClosureExpression(expression: ClosureExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitCodeExpression(expression: CodeExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitInvokeExpression(expression: InvokeExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitToCodeExpression(expression: ToCodeExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitLiteralExpression(expression: LiteralExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitLiteralListExpression(expression: LiteralListExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitLiteralMapExpression(expression: LiteralMapExpression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitAnnotation(spec: Expression, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitClass(spec: Class, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitConstructor(spec: Constructor, clazz: String, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDirective(spec: Directive, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitField(spec: Field, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitLibrary(spec: Library, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitFunctionType(spec: FunctionType, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitMethod(spec: Method, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitReference(spec: Reference, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitSpec(spec: Spec, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitType(spec: TypeReference, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitTypeParameters(spec: List<Reference>, context: StringSink?): StringSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


abstract class CodeAndExpressionEmitter : CodeVisitor<StringSink>, ExpressionVisitor<StringSink> {

    open lateinit var allocator: de.jensklingenberg.kt_dart_builder.main.Allocator

    override fun visitBlock(block: Block, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()
        return visitAll<Code>(block.statements.toSet(), output, { statement -> statement.accept(this, output) }, "\n")

    }

    override fun visitStaticCode(code: StaticCode, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        return output.apply {
            write(code.code)
        }

    }

    override fun visitScopedCode(code: ScopedCode, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        return output.apply {

            write(code.code(allocator.allocate))
        }
    }


    override fun visitToCodeExpression(expression: ToCodeExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        expression.code.accept(this, output)
        if (expression.isStatement) {
            output.write(";")
        }
        return output
    }


    override fun visitBinaryExpression(expression: BinaryExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()
        expression.left.accept(this, output)
        if (expression.addSpace) {
            output.write(SPACE)
        }
        output.write(expression.operator)
        if (expression.addSpace) {
            output.write(SPACE)
        }
        startConstCode(expression.isConst) {
            expression.right.accept(this, output)

        }
        return output

    }

    override fun visitClosureExpression(expression: ClosureExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        return expression.method.accept(this, output)

    }

    override fun visitCodeExpression(expression: CodeExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        val visitor = this as CodeVisitor<StringSink>
        return expression.code.accept(visitor, output)
    }


    override fun visitInvokeExpression(expression: InvokeExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        return _writeConstExpression(output, expression.type == InvokeExpressionType.constInstance) {
            expression.target.accept(this, output)
            if (expression.name.isNotEmpty()) {
                output.apply {
                    write(".")
                    write(expression.name)
                }
            }
            if (expression.typeArguments.isNotEmpty()) {
                output.write("<")
                visitAll(expression.typeArguments.toSet(), output, { type ->
                    type.accept(this, output)
                })
                output.write(">")

            }
            output.write("(")
            visitAll<Spec>(expression.positionalArguments.toSet(), output, { spec ->
                spec.accept(this, output)
            })
            if (expression.positionalArguments.isNotEmpty() &&
                expression.namedArguments.isNotEmpty()
            ) {
                output.write(", ")
            }
            visitAll<String>(expression.namedArguments.keys, output, { name ->
                output.apply {
                    write(name)
                    write(": ")
                }
                expression.namedArguments.getValue(name).accept(this, output)
            })

            output.apply {
                write(")")
            }
        }

    }


    override fun visitLiteralExpression(expression: LiteralExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        return output.apply {
            write(expression.literal)
        }
    }

    var _withInConstExpression: bool = false


    fun _acceptLiteral(literalOrSpec: Any, output: StringSink) {
        if (literalOrSpec is Spec) {
            literalOrSpec.accept(this, output)
            return
        }
        literal(literalOrSpec).accept(this, output)

    }

    override fun visitLiteralListExpression(expression: LiteralListExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()

        return _writeConstExpression(output, expression.isConst) {
            expression.type?.let {
                output.write("<")
                it.accept(this, output)
                output.write(">")
            }
            output.write("[")
            visitAll<Any>(expression.values.toSet(), output, { value ->
                _acceptLiteral(value, output)
            })
            output.apply {
                write("]")
            }
        }
    }

    override fun visitLiteralMapExpression(expression: LiteralMapExpression, output: StringSink?): StringSink {
        val output = output ?: MyStringBuffer()


        return _writeConstExpression(output, expression.isConst) {
            if (expression.keyType != null) {
                output.write("<")
                expression.keyType.accept(this, output)
                output.write(", ")
                if (expression.valueType == null) {
                    refer("dynamic", "dart:core").accept(this, output)
                } else {
                    expression.valueType.accept(this, output)
                }
                output.write(">")
            }
            output.write("{")
            visitAll<Any>(expression.values.keys, output, { key ->
                val value = expression.values.getValue(key)
                _acceptLiteral(key, output)
                output.write(": ")
                _acceptLiteral(value, output)
            })


            output.apply {
                write("}")
            }
        }


    }


    fun startConstCode(isConst: bool, visit: () -> Unit) {
        val previousConstContext = _withInConstExpression
        if (isConst) {
            _withInConstExpression = true
        }
        visit()
        _withInConstExpression = previousConstContext

    }

    fun _writeConstExpression(sink: StringSink, isConst: bool, visitExpression: () -> StringSink): StringSink {
        val previousConstContext = _withInConstExpression
        if (isConst) {
            if (!_withInConstExpression) {
                sink.write(CONST + " ")
            }
            _withInConstExpression = true
        }

        val returnedSink = visitExpression()


        _withInConstExpression = previousConstContext

        return sink

    }

}