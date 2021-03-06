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

package jp.nephy.glados.core

import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import jp.nephy.glados.InternalCoroutineScope
import jp.nephy.glados.api.FileSystemEventListener
import jp.nephy.glados.api.FileSystemWatcher
import jp.nephy.glados.api.Logger
import jp.nephy.glados.api.of
import kotlinx.coroutines.launch
import java.nio.file.FileSystemException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CopyOnWriteArraySet

// TODO: replace with java.nio
internal object FileSystemWatcherImpl: FileSystemWatcher {
    private val logger = Logger.of("GLaDOS.FileSystemWatcher")

    private lateinit var watcher: DirectoryWatcher
    private val listeners = CopyOnWriteArraySet<FileSystemEventListener>()
    
    override fun addListener(listener: FileSystemEventListener) {
        listeners += listener
    }

    override fun removeListener(listener: FileSystemEventListener) {
        listeners -= listener
    }
    
    private fun handleCreateEvent(path: Path) {
        for (listener in listeners) {
            InternalCoroutineScope.launch {
                try {
                    listener.onCreated(path)
                } catch (e: FileSystemException) {
                }
            }
        }
    }
    
    private fun handleModifyEvent(path: Path) {
        for (listener in listeners) {
            InternalCoroutineScope.launch {
                try {
                    listener.onModified(path)
                } catch (e: FileSystemException) {
                }
            }
        }
    }
    
    private fun handleDeleteEvent(path: Path) {
        for (listener in listeners) {
            InternalCoroutineScope.launch {
                try {
                    listener.onDeleted(path)
                } catch (e: FileSystemException) {
                }
            }
        }
    }

    fun start() {
        watcher = DirectoryWatcher.builder().path(Paths.get("")).listener { event ->
            val path = event.path()
            if (path.startsWith("logs")) {
                return@listener
            }
            
            if (!Files.exists(path)) {
                return@listener
            }
            
            when (event.eventType()!!) {
                DirectoryChangeEvent.EventType.CREATE -> {
                    handleCreateEvent(path)

                    logger.trace { "ファイル作成: $path" }
                }
                DirectoryChangeEvent.EventType.MODIFY -> {
                    handleModifyEvent(path)

                    logger.trace { "ファイル編集: $path" }
                }
                DirectoryChangeEvent.EventType.DELETE -> {
                    handleDeleteEvent(path)

                    logger.trace { "ファイル削除: $path" }
                }
                else -> {}
            }
        }.build()
        
        watcher.watch()
    }

    override fun close() {
        watcher.close()
    }
}
