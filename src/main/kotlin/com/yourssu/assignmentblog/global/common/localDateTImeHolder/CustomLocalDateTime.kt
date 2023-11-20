package com.yourssu.assignmentblog.global.common.localDateTImeHolder

import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class CustomLocalDateTime : ICustomLocalDateTime {
    override fun plusHour(hour: Long): Instant {
        return LocalDateTime.now()
            .plusHours(hour)
            .atZone(ZoneId.of("Asia/Seoul"))
            .toInstant()
    }
}
