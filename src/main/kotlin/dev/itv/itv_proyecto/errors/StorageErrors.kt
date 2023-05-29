package dev.itv.itv_proyecto.errors

sealed class StorageErrors (message: String) : Error(message) {
    class JsonStorageError(message: String) : StorageErrors(message)
    class CsvStorageError(message: String) : StorageErrors(message)
    class HtmlStorageError(message: String) : StorageErrors(message)
}