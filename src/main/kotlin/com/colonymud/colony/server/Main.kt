package com.colonymud.colony.server

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.net.ServerSocket

const val CRITTER_TICKER_MILLIS = 1_000L
const val MASTER_TICKER_MILLIS = 45_000L

@OptIn(DelicateCoroutinesApi::class)
fun main(args: Array<String>) {
    println("args $args")
    val server = ServerSocket(9000)

    val scope = CoroutineScope(Dispatchers.Default)

    tickerFlow(delayMillis = CRITTER_TICKER_MILLIS)
        .onEach {
            println("critter pulse")
        }
        .launchIn(scope)

    tickerFlow(delayMillis = MASTER_TICKER_MILLIS)
        .onEach { println("master pulse") }
        .launchIn(scope)

    while (true) {
        val socket = server.accept()
        println("Client connected ${socket.inetAddress.hostAddress}")

        scope.launch {
            val client = Client(socket)
        }
    }
}

fun tickerFlow(delayMillis: Long): Flow<Unit> =
    flow {
        while (true) {
            delay(delayMillis)
            emit(Unit)
        }
    }