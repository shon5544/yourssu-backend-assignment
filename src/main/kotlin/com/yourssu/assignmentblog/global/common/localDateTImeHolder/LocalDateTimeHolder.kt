package com.yourssu.assignmentblog.global.common.localDateTImeHolder

import java.time.Instant

interface LocalDateTimeHolder {
    fun plusHour(hour: Long): Instant
}