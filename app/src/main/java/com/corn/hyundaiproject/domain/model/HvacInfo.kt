package com.corn.hyundaiproject.domain.model

data class HvacInfo(
    val temperature: Float,
    val warningMessage: String? = null,
    val isDoorLocked: Boolean
)