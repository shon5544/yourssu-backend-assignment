package com.yourssu.assignmentblog.global.common.stub

import java.io.PrintWriter
import java.util.*
import javax.servlet.ServletOutputStream
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

class StubHttpServletResponse: HttpServletResponse {
    override fun getCharacterEncoding(): String {
        TODO("Not yet implemented")
    }

    override fun getContentType(): String {
        TODO("Not yet implemented")
    }

    override fun getOutputStream(): ServletOutputStream {
        TODO("Not yet implemented")
    }

    override fun getWriter(): PrintWriter {
        TODO("Not yet implemented")
    }

    override fun setCharacterEncoding(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun setContentLength(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun setContentLengthLong(p0: Long) {
        TODO("Not yet implemented")
    }

    override fun setContentType(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun setBufferSize(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun getBufferSize(): Int {
        TODO("Not yet implemented")
    }

    override fun flushBuffer() {
        TODO("Not yet implemented")
    }

    override fun resetBuffer() {
        TODO("Not yet implemented")
    }

    override fun isCommitted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun setLocale(p0: Locale?) {
        TODO("Not yet implemented")
    }

    override fun getLocale(): Locale {
        TODO("Not yet implemented")
    }

    override fun addCookie(p0: Cookie?) {
        TODO("Not yet implemented")
    }

    override fun containsHeader(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun encodeURL(p0: String?): String {
        TODO("Not yet implemented")
    }

    override fun encodeRedirectURL(p0: String?): String {
        TODO("Not yet implemented")
    }

    override fun encodeUrl(p0: String?): String {
        TODO("Not yet implemented")
    }

    override fun encodeRedirectUrl(p0: String?): String {
        TODO("Not yet implemented")
    }

    override fun sendError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun sendError(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun sendRedirect(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun setDateHeader(p0: String?, p1: Long) {
        TODO("Not yet implemented")
    }

    override fun addDateHeader(p0: String?, p1: Long) {
        TODO("Not yet implemented")
    }

    override fun setHeader(p0: String?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun addHeader(p0: String?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun setIntHeader(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun addIntHeader(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun setStatus(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun setStatus(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun getStatus(): Int {
        TODO("Not yet implemented")
    }

    override fun getHeader(p0: String?): String {
        TODO("Not yet implemented")
    }

    override fun getHeaders(p0: String?): MutableCollection<String> {
        TODO("Not yet implemented")
    }

    override fun getHeaderNames(): MutableCollection<String> {
        TODO("Not yet implemented")
    }
}