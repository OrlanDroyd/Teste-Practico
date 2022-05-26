package com.gmail.orlandroyd.testepratico.util

data class DataState<out T>(val status: Status = Status.LOADING, val data: T? = null, val message: String? = null) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        FAILURE
    }

    companion object {

        fun <T> success(data: T): DataState<T> {
            return DataState(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): DataState<T> {
            return DataState(Status.ERROR, data, message)
        }

        fun <T> loading(data: T?): DataState<T> {
            return DataState(Status.LOADING, data, null)
        }

        fun <T> failed(message: String, data: T? = null): DataState<T> {
            return DataState(Status.FAILURE, data, message)
        }

    }


}