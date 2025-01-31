package com.mckernant1.jda.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Command(val name: String, val description: String)
