package ru.leadpogrommer.thegame.physicalEngine

enum class MapLayer(val impassableLayers: Array<String>) {
    GROUND(arrayOf("solid", "flyable")),
    AIR(arrayOf("solid"))
}