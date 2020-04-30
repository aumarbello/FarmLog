package com.aumarbello.farmlog

@Target(AnnotationTarget.CLASS)
annotation class OpenClass

@OpenClass
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting