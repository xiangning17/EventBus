package xiangning.coroutines.eventbus.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import xiangning.coroutines.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // launch in main thread, may miss some event at early
        EventBus.eventFlow()
            .onEach { Log.e(TAG, "main event: $it") }
            .onCompletion { Log.e(TAG, "main event finish!") }
            .launchIn(mainScope)

        // launch in io thread, receive all event
        EventBus.eventFlow()
            .onEach { Log.e(TAG, "io event: $it") }
            .onCompletion { Log.e(TAG, "io event finish!") }
            .launchIn(mainScope + Dispatchers.IO)

        // send event from default thread, waiting 200ms for collector
        mainScope.launch(Dispatchers.Default) {
            delay(200)
            EventBus.send(1)
            delay(300)
            EventBus.send(2)
            delay(300)
            EventBus.send("Hello")
            EventBus.send(3)
            delay(1000)
            EventBus.send("Event Bus")
        }

        // send event from main thread with no suspend, waiting 500ms for collector
        runBlocking { delay(500) }
        EventBus.trySend("no block event")

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}