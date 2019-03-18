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

package jp.nephy.glados.clients.discord.listener.websocket.events.category.update

import jp.nephy.glados.clients.discord.listener.websocket.DiscordWebsocketEventSubscription
import jp.nephy.glados.clients.discord.listener.websocket.events.DiscordWebsocketEventBase
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent

data class DiscordCategoryUpdatePermissionsEvent(
    override val subscription: DiscordWebsocketEventSubscription,
    override val jdaEvent: CategoryUpdatePermissionsEvent
): DiscordWebsocketEventBase<CategoryUpdatePermissionsEvent>

val DiscordCategoryUpdatePermissionsEvent.changedPermissionHolders: List<IPermissionHolder>
    get() = jdaEvent.changedMembers

val DiscordCategoryUpdatePermissionsEvent.changedMembers: List<Member>
    get() = jdaEvent.changedMembers

val DiscordCategoryUpdatePermissionsEvent.changedRoles: List<Role>
    get() = jdaEvent.changedRoles
