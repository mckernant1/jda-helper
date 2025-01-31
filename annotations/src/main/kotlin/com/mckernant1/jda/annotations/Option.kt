package com.mckernant1.jda.annotations

import net.dv8tion.jda.api.interactions.commands.OptionType

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Option(
    val type: OptionType,
    val name: String,
    val description: String,
    val required: Boolean = false,
)
