package xiangning.coroutines.eventbus

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 * Created by xiangning on 2021/6/20.
 *
 * a event bus base on MutableSharedFlow.
 */
object EventBus {

    private val bus: MutableSharedFlow<Any> = MutableSharedFlow(0, Int.MAX_VALUE, BufferOverflow.SUSPEND)

    /**
     * send an event no suspend, result indicate if success.
     */
    fun trySend(event: Any): Boolean = bus.tryEmit(event)

    /**
     * send an event may suspend, but it hardly happens for the event buffer length are Int.MAX_VALUE.
     */
    suspend fun send(event: Any) = bus.emit(event)

    /**
     * get the event flow.
     */
    fun eventFlow() : Flow<Any> = bus.asSharedFlow()

    /**
     * get the event flow with only T type event.
     */
    inline fun <reified T> eventFlowOnType() = eventFlow().filterIsInstance<T>()

}

