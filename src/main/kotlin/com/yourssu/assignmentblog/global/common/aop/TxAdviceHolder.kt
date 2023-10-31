package com.yourssu.assignmentblog.global.common.aop

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TxAdviceHolder(
    _txAdvice: TxAdvice
) {

    init {
        txAdvice = _txAdvice
    }

    companion object {
        private lateinit var txAdvice: TxAdvice

        fun <T> writeable(function: () -> T): T {
            return txAdvice.writeable(function)
        }

        fun <T> readOnly(function: () -> T): T {
            return txAdvice.readOnly(function)
        }
    }

    @Component
    class TxAdvice {

        @Transactional
        fun <T> writeable(function: () -> T): T {
            return function.invoke()
        }

        @Transactional(readOnly = true)
        fun <T> readOnly(function: () -> T): T {
            return function.invoke()
        }
    }
}