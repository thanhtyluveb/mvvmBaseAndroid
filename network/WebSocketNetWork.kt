package com.example.thefirstprojecttdtdemo.network

import android.util.Log
import com.example.thefirstprojecttdtdemo.model.BitcoinTicker
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class WebSocketNetWork {
    private lateinit var webSocketClient: WebSocketClient
    private var mCallBack: ((BitcoinTicker?) -> Unit)? = null

    init {
        initWebSocket()
    }

    fun initWebSocket() {
        val coinBaseUri = URI(WEB_SOCKET_URL)
        createWebSocketClient(coinBaseUri)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
    }

    private fun createWebSocketClient(coinBaseUri: URI) {
        webSocketClient = object : WebSocketClient(coinBaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                message?.let {
                    val moShi = Moshi.Builder().build()
                    val adapter: JsonAdapter<BitcoinTicker> =
                        moShi.adapter(BitcoinTicker::class.java)
                    val bitcoin = adapter.fromJson(message)
                    mCallBack?.invoke(bitcoin)

                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
                unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }
        }

    }

    fun setUpCallBack(callback: ((BitcoinTicker?) -> Unit)?) {
        mCallBack = callback
    }

    fun connect() {
        webSocketClient.connect()
    }

    private fun subscribe() {
        webSocketClient.send(
            "{\n" +
                    "    \"type\": \"subscribe\",\n" +
                    "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [\"BTC-USD\"] }]\n" +
                    "}"
        )
    }

    private fun unsubscribe() {
        webSocketClient.send(
            "{\n" +
                    "    \"type\": \"unsubscribe\",\n" +
                    "    \"channels\": [\"ticker\"]\n" +
                    "}"
        )
    }

    fun close() {
        webSocketClient.close()
    }

    companion object {
        private const val WEB_SOCKET_URL = "wss://ws-feed.pro.coinbase.com"
        private const val TAG = "WebSocketNetWork"
    }

}