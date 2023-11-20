package com.yourssu.assignmentblog.global.common.localDateTImeHolder

import java.time.Instant

interface ICustomLocalDateTime {
    fun plusHour(hour: Long): Instant
}
