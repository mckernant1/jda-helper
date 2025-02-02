package com.mckernant1.jda.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.mckernant1.jda.annotations.Command
import com.mckernant1.jda.annotations.Option
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import java.io.OutputStreamWriter

class CommandDataProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.info("CommandDataProcessorProvider is being created!")
        return CommandProcessor(environment.codeGenerator, environment.logger)
    }
}


class CommandProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.mckernant1.jda.annotations.Command")
        val ret = symbols.filter { !it.validate() }.toList()

        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(CommandDataVisitor(), Unit) }

        return ret
    }

    inner class CommandDataVisitor : KSVisitorVoid() {
        @OptIn(KspExperimental::class)
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.containingFile?.packageName?.asString()!!
            val className = classDeclaration.simpleName.asString()
            val classType = classDeclaration.toClassName()

            val command = classDeclaration.getAnnotationsByType(Command::class).firstOrNull()

            val functionSpec = FunSpec.builder("toCommandData")
                .receiver(ClassName(packageName, className).nestedClass("Companion"))
                .returns(CommandData::class)
            functionSpec.addStatement("val c = Commands.slash(\"${command?.name}\",\"${command?.description}\")")

            val sb = StringBuilder()
            val l = mutableListOf<String>()
            classDeclaration.getAllProperties()
                .forEach { property: KSPropertyDeclaration ->
                    val optionAnnotation = property.getAnnotationsByType(Option::class)
                        .firstOrNull() ?: return@forEach
                    functionSpec.addStatement(
                        "c.addOption(%T.%L, %S, %S, %L)",
                        ClassName("net.dv8tion.jda.api.interactions.commands", "OptionType"),
                        optionAnnotation.type,
                        optionAnnotation.name,
                        optionAnnotation.description,
                        optionAnnotation.required
                    )
                    val s = when (optionAnnotation.type) {
                        OptionType.STRING -> "getOption(%S)?.asString"
                        OptionType.INTEGER -> "getOption(%S)?.asInt"
                        OptionType.BOOLEAN -> "getOption(%S)?.asBoolean"
                        OptionType.USER -> "getOption(%S)?.asUser"
                        OptionType.CHANNEL -> "getOption(%S)?.asChannel"
                        OptionType.ROLE -> "getOption(%S)?.asRole"
                        OptionType.MENTIONABLE -> "getOption(%S)?.asMentionable"
                        OptionType.NUMBER -> "getOption(%S)?.asDouble"
                        OptionType.ATTACHMENT -> "getOption(%S)?.asAttachment"
                        else -> throw IllegalArgumentException("Unknown option type ${optionAnnotation.type}")
                    }
                    sb.append("\t%L = $s")
                    if (!property.type.resolve().isMarkedNullable) {
                        sb.append("\n\t\t ?: throw IllegalStateException(\"${property.simpleName.asString()} is a required field\")")
                    }
                    sb.appendLine(",")
                    l.add(property.simpleName.asString())
                    l.add(optionAnnotation.name)
                }

            functionSpec.addStatement("return c")

            val toCommandDataFuncSpec = FunSpec.builder("to${className}")
                .receiver(SlashCommandInteractionEvent::class)
                .returns(ClassName(packageName, className))

            toCommandDataFuncSpec.addStatement("return %T(\n$sb)", classType, *l.toTypedArray())

            val fileSpec = FileSpec.builder(packageName, "${className}Generated")
                .addImport("net.dv8tion.jda.api.interactions.commands.build", "Commands")
                .addFunction(functionSpec.build())
                .addFunction(toCommandDataFuncSpec.build())

            if (classType.enclosingClassName() != null) {
                fileSpec.addImport(classType.enclosingClassName()!!, className)
            }

            // Write the generated file
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(false, classDeclaration.containingFile!!),
                packageName = packageName,
                fileName = "${className}Generated"
            )

            OutputStreamWriter(file).use { writer ->
                fileSpec.build().writeTo(writer)
            }
        }
    }
}
