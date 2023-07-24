package flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.zipWithNext(): Flow<Pair<T, T>> = flow {
    var prev: T? = null
    collect {
        if (prev != null) {
            emit(prev!! to it)
        }
        prev = it
    }
}
