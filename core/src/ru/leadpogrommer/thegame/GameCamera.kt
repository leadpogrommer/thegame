package ru.leadpogrommer.thegame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2

class GameCamera(private val mapWidth: Int, private val mapHeight: Int): OrthographicCamera(){
    private var pos = Vector2()
    private var playerPos = Vector2()
    private var playerOnScreenPos = Vector2()
    fun position(pp: Vector2){
        playerPos = pp
        this.setToOrtho(false, viewportWidth, viewportHeight)
        val rawPos = Vector2(playerPos.x - (this.viewportWidth / 2f), playerPos.y - (this.viewportHeight / 2f))
        pos.x = rawPos.x.coerceIn(0f, mapWidth - viewportWidth)
        pos.y = rawPos.y.coerceIn(0f, mapHeight - viewportHeight)
        playerOnScreenPos = Vector2(viewportWidth / 2f + (rawPos.x - pos.x), viewportHeight / 2f + (rawPos.y - pos.y))
        translate(pos)
    }

    fun getFacingAngle(mouseX: Float, mouseY: Float): Float{
        val facingVect = Vector2((mouseX / Gdx.graphics.width) * viewportWidth, (mouseY / Gdx.graphics.height) * viewportHeight)
        facingVect.sub(playerOnScreenPos)
        return facingVect.angleRad()
    }
}