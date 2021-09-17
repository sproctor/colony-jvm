package com.colonymud.colony.server

import java.net.Socket

class Client(private val socket: Socket) {
    private var player: String? = null
}