# EventBus
a simple event bus based on kotin coroutines.
一个简单的基于Kotlin协程的EventBus。

[![](https://www.jitpack.io/v/xiangning17/eventbus.svg)](https://www.jitpack.io/#xiangning17/eventbus)


## Getting started

1. add maven repositories in project build.gradle:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. add dependency for this library in module build.gradle：
```groovy
dependencies {
    implementation 'com.github.xiangning17:eventbus:1.0.1'
}
```

## Usage

```kotlin
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
```
