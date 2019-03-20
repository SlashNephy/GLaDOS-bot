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

package jp.nephy.glados.clients.discord.listener.websocket.events.channel.private

import jp.nephy.glados.clients.discord.listener.websocket.DiscordWebsocketEventSubscription
import jp.nephy.glados.clients.discord.listener.websocket.events.DiscordWebsocketEventBase
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.channel.priv.PrivateChannelDeleteEvent

data class DiscordPrivateChannelDeleteEvent(
    override val subscription: DiscordWebsocketEventSubscription,
    override val jdaEvent: PrivateChannelDeleteEvent
): DiscordWebsocketEventBase<PrivateChannelDeleteEvent>

val DiscordPrivateChannelDeleteEvent.channel: PrivateChannel
    get() = jdaEvent.channel

val DiscordPrivateChannelDeleteEvent.user: User
    get() = jdaEvent.user