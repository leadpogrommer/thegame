package ru.leadpogrommer.thegame.net

import java.io.Serializable
import java.util.*

class Request(val uuid: UUID, val endpoint: String, val args: Array<Any>): Serializable