package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import it.arcidiacono.warehouse.domain.DatasourceError
import it.arcidiacono.warehouse.domain.FailureReason
import java.io.File
import java.nio.charset.StandardCharsets

interface Datasource {
    fun read(): Either<FailureReason, String>
    fun write(content: String): Either<FailureReason, Unit>
}

class FileDatasource(
    private val filePath: String
) : Datasource {
    override fun read(): Either<FailureReason, String> =
        try {
            Right(readFile(filePath))
        } catch (e: Exception) {
            Left(DatasourceError(e))
        }

    override fun write(content: String): Either<FailureReason, Unit> =
        try {
            writeToFile(filePath, content)
            Right(Unit)
        } catch (e: Exception) {
            Left(DatasourceError(e))
        }

    private fun writeToFile(path: String, content: String) = File(javaClass.getResource(path).file).writeText(content)
    private fun readFile(path: String): String = javaClass.getResource(path).readText(StandardCharsets.UTF_8)
}