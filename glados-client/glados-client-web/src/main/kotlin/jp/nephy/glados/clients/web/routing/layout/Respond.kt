/*
 * The MIT License (MIT)
 *
 *     Copyright (c) 2017-2019 Nephy Project Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("UNUSED")

package jp.nephy.glados.clients.web.routing.layout

import io.ktor.application.ApplicationCall
import io.ktor.html.Template
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.withCharset
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.bufferedWriter
import io.ktor.utils.io.ByteWriteChannel
import jp.nephy.jsonkt.*
import jp.nephy.jsonkt.delegation.*
import kotlinx.html.*
import kotlinx.html.stream.*

/**
 * Responds with minified html contents.
 */
@UseExperimental(KtorExperimentalAPI::class)
suspend inline fun ApplicationCall.respondMinifiedHtml(status: HttpStatusCode = HttpStatusCode.OK, noinline block: HTML.() -> Unit) {
    respond(status, object: OutgoingContent.WriteChannelContent() {
        override val contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)

        override suspend fun writeTo(channel: ByteWriteChannel) {
            channel.bufferedWriter().use {
                it.append("<!DOCTYPE html>")
                it.appendHTML(prettyPrint = false).html(block = block)
            }
        }
    })
}

/**
 * Responds with minified html template.
 */
suspend inline fun <T: Template<HTML>> ApplicationCall.respondMinifiedHtmlTemplate(template: T, status: HttpStatusCode = HttpStatusCode.OK, body: T.() -> Unit) {
    template.body()
    
    respondMinifiedHtml(status) { 
        with(template) { 
            apply()
        }
    }
}

/**
 * Responds with [JsonModel].
 */
suspend inline fun ApplicationCall.respondJsonModel(model: JsonModel, status: HttpStatusCode = HttpStatusCode.OK) {
    respondJsonObject(status) { model.json }
}

/**
 * Responds with [JsonObject].
 */
suspend inline fun ApplicationCall.respondJsonObject(status: HttpStatusCode = HttpStatusCode.OK, block: () -> Map<String, Any?>) {
    respondText(block().toJsonObject().stringify(), ContentType.Application.Json, status)
}

/**
 * Responds with [JsonArray].
 */
suspend inline fun ApplicationCall.respondJsonArray(status: HttpStatusCode = HttpStatusCode.OK, block: () -> Iterable<Map<String, Any?>>) {
    respondText(block().toJsonArray().stringify(), ContentType.Application.Json, status)
}
