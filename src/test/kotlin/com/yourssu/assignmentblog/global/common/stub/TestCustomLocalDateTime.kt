package com.yourssu.assignmentblog.global.common.stub

import com.yourssu.assignmentblog.global.common.localDateTImeHolder.ICustomLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TestCustomLocalDateTime: ICustomLocalDateTime {
    override fun plusHour(hour: Long): Instant {
        return LocalDateTime.of(
            2023,
            9,
            13,
            21,
            5,
            0,
            0)
            .atZone(ZoneId.of("Asia/Seoul"))
            .toInstant()
    }

}