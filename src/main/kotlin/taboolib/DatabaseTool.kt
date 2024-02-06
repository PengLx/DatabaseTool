package taboolib

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory
import taboolib.module.database.Host

object DatabaseTool : ModInitializer {
	val logger = LoggerFactory.getLogger("databasetool")

	override fun onInitialize() {
		ServerLifecycleEvents.SERVER_STOPPED.register(this::stop)
	}

	private fun stop(minecraftServer: MinecraftServer) {
		Host.release()
	}
}