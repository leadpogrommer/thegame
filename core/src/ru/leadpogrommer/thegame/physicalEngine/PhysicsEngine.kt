package ru.leadpogrommer.thegame.physicalEngine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import java.util.*
import kotlin.math.*

class PhysicsEngine(tiledMap: TiledMap, private val entities: MutableCollection<Entity>){
    val map = EnumMap<MapLayer, Array<BooleanArray>>(MapLayer::class.java)
    val mapHeight = tiledMap.properties["height"] as Int
    val mapWidth = tiledMap.properties["width"] as Int
    init {
        for (layerType in MapLayer.values()){
            map[layerType] = Array(tiledMap.properties["width"] as Int) { y -> BooleanArray(tiledMap.properties["height"] as Int) {x ->
                var out = false;
                for (layerName in layerType.impassableLayers){
                    out = out or ((tiledMap.layers[layerName] as TiledMapTileLayer).getCell(x, y) != null)
                }
                return@BooleanArray out
            } }
        }
        Gdx.app.log(TAG, "loaded map")
    }

    fun tick(delta: Float){
        for (entity in entities){
            entity.pos.x += entity.speed.x * delta
            entity.pos.y += entity.speed.y * delta
            val currentCell = GridPoint2(round(entity.pos.x).toInt(), round(entity.pos.y).toInt())
            for(y in max(0, currentCell.y - ceil(entity.radius).toInt()) .. min(mapHeight - 1, currentCell.y + ceil(entity.radius).toInt())){
                for(x in max(0, currentCell.x - ceil(entity.radius).toInt()) .. min(mapWidth - 1, currentCell.x + ceil(entity.radius).toInt())){
                    if(!map[entity.layer]!![y][x])continue
                    val cx = x+0.5f
                    val cy = y+0.5f
                    val dst = distance(entity.pos.x, entity.pos.y, cx, cy)
                    if (dst < entity.radius + CELL_RADIUS){
                        // I don't know how it works, just copied it from EKW
                        val b1 = Vector2(entity.pos.x - cx, entity.pos.y - cy).nor().scl((entity.radius - dst + CELL_RADIUS) / 2f)
                        entity.pos.add(b1)

                    }
                }
            }
        }
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return (sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2)))
    }


    companion object{
        const val TAG = "PHYSICS"
        const val CELL_RADIUS = 0.5f
    }
}