package jp.nephy.glados.core.config

import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import jp.nephy.glados.GLaDOS
import jp.nephy.glados.core.logger.SlackLogger
import java.io.Closeable
import java.nio.file.Files
import java.nio.file.Paths

object ConfigFileWatcher: Closeable {
    private val logger = SlackLogger("GLaDOS.ConfigFileWatcher")

    private val watcher = DirectoryWatcher.builder().path(Paths.get(".")).listener { event ->
        when (event.eventType()) {
            DirectoryChangeEvent.EventType.CREATE -> {
                handleConfigFile(event)

                logger.trace { "ファイル作成: ${event.path()}" }
            }
            DirectoryChangeEvent.EventType.MODIFY -> {
                handleConfigFile(event)

                logger.trace { "ファイル編集: ${event.path()}" }
            }
            DirectoryChangeEvent.EventType.DELETE -> {
                logger.trace { "ファイル削除: ${event.path()}" }
            }
            else -> return@listener
        }
    }.build()

    private fun handleConfigFile(event: DirectoryChangeEvent) {
        val path = event.path()
        when {
            !GLaDOS.isDebugMode && Files.isSameFile(path, GLaDOSConfig.productionConfigPath) -> {
                GLaDOS.config = GLaDOSConfig.load(GLaDOS.isDebugMode)
            }
            GLaDOS.isDebugMode && Files.isSameFile(path, GLaDOSConfig.developmentConfigPath) -> {
                GLaDOS.config = GLaDOSConfig.load(GLaDOS.isDebugMode)
            }
            Files.isSameFile(path, SecretConfig.secretConfigPath) -> {
                GLaDOS.secret = SecretConfig.load()
            }
        }
    }

    fun block() {
        watcher.watch()
    }

    override fun close() {
        watcher.close()
    }
}
