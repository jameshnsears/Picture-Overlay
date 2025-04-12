package io.github.luiisca.floating.views.helpers

import io.github.luiisca.floating.views.data.OverlayConfigData
import java.util.UUID

object ConfigHelper {
    private val configs = mutableMapOf<String, OverlayConfigData>()

    fun addConfig(config: OverlayConfigData): String {
        val id = UUID.randomUUID().toString()
        configs[id] = config

        return id
    }

    fun getConfig(id: String): OverlayConfigData? = configs[id]
}