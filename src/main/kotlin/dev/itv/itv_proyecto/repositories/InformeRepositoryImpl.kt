package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import dev.itv.itv_proyecto.errors.InformeErrors
import dev.itv.itv_proyecto.errors.TrabajadorErrors
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Connection
import java.sql.Date

class InformeRepositoryImpl : InformeRepository, KoinComponent {
    private val logger = KotlinLogging.logger {  }
    val manager : DatabaseManager by inject()
    var database = manager.bd

    /**
     * @return Informe guardado o los Posibles errores al guardarlo en la base de datos con Result
     * @param informe El informe que guardaremos en la base de datos
     */
    override fun saveInforme(informe: Informe): Result<Informe, InformeErrors> {

        logger.debug { " InformeRepositoryImpl -- SaveInforme($informe) " }

        val sql = """
            INSERT INTO tInforme VALUES (null,?,?,?,?,?,?,?,?,?)
        """.trimIndent()
        var respuesta = 0
        database.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1 ,if (informe.apto == true) 1 else 0)
            preparedStatement.setDouble(2, informe.frenado!!)
            preparedStatement.setDouble(3, informe.contaminacion!!)
            preparedStatement.setInt(4 ,if (informe.interior == true) 1 else 0)
            preparedStatement.setInt(5 ,if (informe.luces == true) 1 else 0)
            preparedStatement.setLong(6, (informe.trabajador.idTrabajador))
            preparedStatement.setString(7, informe.vehiculo.matricula)
            preparedStatement.setString(8, informe.horaCita)
            preparedStatement.setDate(9, Date.valueOf(informe.fechaCita))

            respuesta = preparedStatement.executeUpdate()

            preparedStatement.close()
        }

        if (respuesta != 0) {
            return Ok(informe)
        }
        return Err(InformeErrors.InformeNotFoundError("No se ha guardado el nuevo informe"))

    }
    /**
     * Función que se encarga de borrar el informe de la base de datos según su Id
     *
     * @param id Id del Informe que buscaremos en la base de datos para borarlo
     * @return El informe eliminado o los posibles errores al hacerlo con Result
     * @see findById
     */
    override fun deleteInformeById(id: Long): Result<Informe, InformeErrors> {
        logger.debug { " InformeRepositoryImpl -- DeleteInformeById ($id) " }
        val old = findById(id).onFailure {
            return Err(it)
        }.component1()!!

        val sql = """
            DELETE FROM tInforme WHERE nId_informe = ?;
        """.trimIndent()

        database.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setLong(1,id)
            preparedStatement.executeUpdate()
            preparedStatement.close()
        }

        return Ok(old)
    }

    /**
     * Función que actualiza un informe con base a su id. Sí existe los datos serán cambiados en la base de datos
     *
     * @param id Id del Informe sobre el que se realizara la consulta a la base de datos para su actualización
     * @param informe Los nuevos datos del informe que se guardaran en la base de datos
     * @return El informe actualizado o los Posibles errores al actualizar con Result
     * @see findById
     *
     */
    override fun updateInformeById(id: Long, informe: Informe): Result<Informe, InformeErrors> {
        logger.debug { " InformeRepositoryImpl -- updateInformeById ($id , $informe)" }
        findById(id).onFailure {
            return Err(it)
        }.component1()

        val sql = """
            UPDATE tInforme 
             SET bFavorable = ?,
             nFrenado = ?,
             nContaminacion = ?,
             bInterior = ?,
             bLuces = ?,
             nId_Trabajador = ?,
             cMatricula = ?,
             cHora = ?,
             dFecha_Cita = ?
             WHERE nId_informe = ?
        """.trimIndent()
        database.prepareStatement(sql).use { statement ->

            statement.setInt(1 ,if (informe.apto == true) 1 else 0)
            statement.setDouble(2, informe.frenado!!)
            statement.setDouble(3, informe.contaminacion!!)
            statement.setInt(4 ,if (informe.interior == true) 1 else 0)
            statement.setInt(5 ,if (informe.luces == true) 1 else 0)
            statement.setLong(6, (informe.trabajador.idTrabajador))
            statement.setString(7, informe.vehiculo.matricula)
            statement.setString(8, informe.horaCita)
            statement.setDate(9, Date.valueOf(informe.fechaCita))
            statement.setLong(10, id)
            statement.executeUpdate()
            statement.close()

        }

        findById(id).onFailure {
            return Err(it)
        }.run {
            return Ok(this.component1()!!)
        }
    }

    /**
     * Función que filtra todos los informes de la base de datos y devuelve si existe uno con la Id
     *
     *  @param id Id del informe sobre el que filtraremos los resultados
     *  @return El informe con la Id deseada o un Error al no encontrar ninguna
     *  @see loadAll
     */
    override fun findById(id: Long): Result<Informe, InformeErrors> {
        logger.debug { " InformeRepositoryImpl -- FindById ($id) " }
        loadAll().onFailure {
            return Err(it)
        }.component1()!!.find { it.idInforme == id }?.let {
            return Ok(it)
        }
        return Err(InformeErrors.InformeNotFoundError("No existe un informe con la id $id"))
    }

    /**
     * Función que se encarga de buscar todos los trabajadores y todos los vehiculos de la base de datos y luego se le asignan
     * a su informe asignado. Esta función llama a otras funciones para sacar los datos restantes
     *
     * @return Lista de los Informes o los posibles errores al buscarlos en la Base de Datos con Result
     * @see selectAllTrabajadores
     * @see selectAllVehiculos
     *
     */
    override fun loadAll(): Result<List<Informe>, InformeErrors> {
        logger.debug { " InformesRepositoryImpl -- LoadAll() " }
        val informes = mutableListOf<Informe>()
        val trabajadores = selectAllTrabajadores(database).onFailure {
            return Err(InformeErrors.InformeQueryErrors(it.message!!))
        }.component1()!!
        val vehiculos = selectAllVehiculos(database).onFailure {
            return Err(InformeErrors.InformeQueryErrors(it.message!!))
        }.component1()!!
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tInforme").let { resultSet ->
            while (resultSet.next()) {
                informes.add(
                    Informe(
                        idInforme = resultSet.getLong("nId_informe"),
                        apto = resultSet.getInt("bFavorable") == 1,
                        frenado = resultSet.getDouble("nFrenado"),
                        contaminacion = resultSet.getDouble("nContaminacion"),
                        interior = resultSet.getInt("bInterior") == 1,
                        luces = resultSet.getInt("bLuces") == 1,
                        trabajador = trabajadores.find { it.idTrabajador == resultSet.getLong("nId_trabajador") }!!,
                        vehiculo = vehiculos.find { it.matricula == resultSet.getString("cMatricula")}!!,
                        horaCita = resultSet.getString("cHora"),
                        fechaCita = resultSet.getDate("dFecha_cita").toLocalDate()
                    )
                )
            }
            resultSet.close()
            statement.close()

        }
        return Ok(informes)
    }

    /**
     * Función que selecciona todos los trabajadores de la Base de datos utilizando funciones auxiliares para formarlos.
     *
     * @param database Conexion a la Base de datos sobre la que se seleccionaran los trabajadores
     * @return Lista de todos los trabajadores de la base de datos o los posibles errores al hacerlo con Result
     * @see Utils.selectAllFromTable
     * @see Utils.sacarEspecialidad
     */
    private fun selectAllTrabajadores(database: Connection): Result<List<Trabajador>, TrabajadorErrors> {
        Utils.logger.debug { " Utils : selectAllTrabajadores () " }
        val trabajadores = mutableListOf<Trabajador>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tTrabajador").let { resultSet ->
            while (resultSet.next()) {
                trabajadores.add(
                    Trabajador(
                        idTrabajador = resultSet.getLong("nId_Trabajador"),
                        nombreTrabajador = resultSet.getString("cNombre"),
                        telefonoTrabajador = resultSet.getInt("nTelefono"),
                        email = resultSet.getString("cEmail"),
                        username = resultSet.getString("cUsuario"),
                        password = resultSet.getString("cContrasena"),
                        fechaContratacion = resultSet.getDate("dFecha_contratacion").toLocalDate(),
                        especialidad = Utils.sacarEspecialidad(resultSet.getString("cEspecialidad")),
                        isResponsable = resultSet.getInt("nEs_jefe") == 1
                    )
                )
            }
            resultSet.close()
            statement.close()

        }
        return Ok(trabajadores)
    }

    /**
     * Función que selecciona todos los Vehículos de la Base de datos utilizando funciones auxiliares para formarlos.
     *
     * @param database Conexion a la Base de datos sobre la que se seleccionaran los vehículos
     * @return Lista de todos los Vehiculos de la base de datos o los posibles errores al hacerlo con Result
     * @see Utils.selectAllFromTable
     * @see Utils.findPropietarioByDni
     * @see Utils.generarVehiculo
     */
    private fun selectAllVehiculos(database: Connection): Result<List<Vehiculo>, VehiculosErrors> {
        Utils.logger.debug { " Utils : selectAllVehiculos () " }
        val vehiculos = mutableListOf<Vehiculo>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tVehiculo").let { resultSet ->
            while (resultSet.next()) {
                val propietario = Utils.findPropietarioByDni(database, resultSet.getString(8)).onFailure {
                    return Err(VehiculosErrors.VehiculoNotFoundError(it.message!!))
                }

                vehiculos.add(
                    Utils.generarVehiculo(resultSet, propietario.component1()!!)
                )
            }
            resultSet.close()
            statement.close()

        }
        return Ok(vehiculos)
    }

    fun saveCita(informe: Informe): Result<Informe, InformeErrors> {

        logger.debug { " InformeRepositoryImpl -- SaveInforme($informe) " }

        val sql = """
            INSERT INTO tInforme VALUES (null,?,?,?,?,?,?,?,?,?)
        """.trimIndent()
        var respuesta = 0
        database.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setNull(1 ,java.sql.Types.VARCHAR)
            preparedStatement.setNull(2, java.sql.Types.VARCHAR)
            preparedStatement.setNull(3, java.sql.Types.VARCHAR)
            preparedStatement.setNull(4 ,java.sql.Types.VARCHAR)
            preparedStatement.setNull(5 ,java.sql.Types.VARCHAR)
            preparedStatement.setLong(6, (informe.trabajador.idTrabajador))
            preparedStatement.setString(7, informe.vehiculo.matricula)
            preparedStatement.setString(8, informe.horaCita)
            preparedStatement.setDate(9, Date.valueOf(informe.fechaCita))

            respuesta = preparedStatement.executeUpdate()

            preparedStatement.close()
        }

        if (respuesta != 0) {
            return Ok(informe)
        }
        return Err(InformeErrors.InformeNotFoundError("No se ha guardado el nuevo informe"))

    }

    fun updatCitaById(id: Long, informe: Informe): Result<Informe, InformeErrors> {
        logger.debug { " InformeRepositoryImpl -- updateInformeById ($id , $informe)" }
        findById(id).onFailure {
            return Err(it)
        }.component1()

        val sql = """
            UPDATE tInforme 
             SET bFavorable = ?,
             nFrenado = ?,
             nContaminacion = ?,
             bInterior = ?,
             bLuces = ?,
             nId_Trabajador = ?,
             cMatricula = ?,
             cHora = ?,
             dFecha_Cita = ?
             WHERE nId_informe = ?
        """.trimIndent()
        database.prepareStatement(sql).use { preparedStatement ->

            preparedStatement.setNull(1 ,java.sql.Types.VARCHAR)
            preparedStatement.setNull(2, java.sql.Types.VARCHAR)
            preparedStatement.setNull(3, java.sql.Types.VARCHAR)
            preparedStatement.setNull(4 ,java.sql.Types.VARCHAR)
            preparedStatement.setNull(5 ,java.sql.Types.VARCHAR)
            preparedStatement.setLong(6, (informe.trabajador.idTrabajador))
            preparedStatement.setString(7, informe.vehiculo.matricula)
            preparedStatement.setString(8, informe.horaCita)
            preparedStatement.setDate(9, Date.valueOf(informe.fechaCita))
            preparedStatement.setLong(10, informe.idInforme)

            preparedStatement.executeUpdate()

            preparedStatement.close()

        }

        findById(id).onFailure {
            return Err(it)
        }.run {
            return Ok(this.component1()!!)
        }
    }


}