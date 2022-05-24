package com.gmail.orlandroyd.testepratico.util

data class DataState<out T>(val status: Status, val data: T?, val message: String?) {

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