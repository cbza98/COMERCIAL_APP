package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse

@Dao
interface DAO {

    //*************   QUERY DE TABLAS    *********************
    @Query("SELECT * FROM EntityCondicionPago")
    fun getAllCondicionPago(): List<EntityCondicionPago>

    @Query("SELECT * FROM EntityDepartamento")
    fun getAllDepartamento(): List<EntityDepartamento>

    @Query("SELECT * FROM EntityDistrito")
    fun getAllDistrito(): List<EntityDistrito>

    @Query("SELECT * FROM EntityDocIdentidad")
    fun getAllDocIdentidad(): List<EntityDocIdentidad>

    @Query("SELECT * FROM EntityFrecuenciaDias")
    fun getAllFrecuenciaDias(): List<EntityFrecuenciaDias>

    @Query("SELECT * FROM EntityMoneda")
    fun getAllMoneda(): List<EntityMoneda>

    @Query("SELECT * FROM EntityProvincia")
    fun getAllProvincia(): List<EntityProvincia>

    @Query("SELECT * FROM EntityUnidadMedida")
    fun getAllUnidadMedida(): List<EntityUnidadMedida>

    @Query("SELECT * FROM EntityPedidoMaster")
    fun getAllPedidoMaster(): List<EntityPedidoMaster>

    @Query("SELECT * FROM EntityListaPrecio")
    fun getAllListaPrecio(): List<EntityListaPrecio>


    //*************   INSERT DE TABLAS    *********************
    @Insert
    fun insertCondicionPago( insertCondicionPago: EntityCondicionPago)

    @Insert
    fun insertDepartamento( insertDepartamento: EntityDepartamento)

    @Insert
    fun insertDistrito( insertDistrito: EntityDistrito)

    @Insert
    fun insertDocIdentidad( insertDocIdentidad: EntityDocIdentidad)

    @Insert
    fun insertFrecuenciaDias( insertFrecuenciaDias: EntityFrecuenciaDias)

    @Insert
    fun insertMoneda( insertMoneda: EntityMoneda)

    @Insert
    fun insertProvincia( insertProvincia: EntityProvincia)

    @Insert
    fun insertUnidadMedida( insertUnidadMedida: EntityUnidadMedida)

    @Insert
    fun insertPedidoMaster( insertPedidoMaster: EntityPedidoMaster)

    @Insert
    fun insertListaPrecio( insertListaPrecio: EntityListaPrecio)



    //*************   Datos    *********************
    @Query("SELECT Nombre,Numero,Referencia1  FROM EntityMoneda WHERE id= :id")
    fun getTipoMoneda(id:Int): MonedaResponse

    @Query("SELECT COUNT(*) from EntityMoneda")
    fun getSizeMoneda(): Int



    //*********  ELIMINAR DATOS DE LAS TABLAS  **************
    @Query("DELETE FROM EntityCondicionPago")
    fun deleteTableCondicionPago()

    @Query("DELETE FROM EntityDepartamento")
    fun deleteTableDepartamento()

    @Query("DELETE FROM EntityDistrito")
    fun deleteTableDistrito()

    @Query("DELETE FROM EntityDocIdentidad")
    fun deleteTableDocIdentidad()

    @Query("DELETE FROM EntityFrecuenciaDias")
    fun deleteTableFrecuenciaDias()

    @Query("DELETE FROM EntityMoneda")
    fun deleteTableMoneda()

    @Query("DELETE FROM EntityProvincia")
    fun deleteTableProvincia()

    @Query("DELETE FROM EntityUnidadMedida")
    fun deleteTableUnidadMedida()

    @Query("DELETE FROM EntityPedidoMaster")
    fun deleteTablePedidoMaster()

    @Query("DELETE FROM EntityListaPrecio")
    fun deleteTableListaPrecio()


    //*********  REINICIAR LOS ID AUTOGENERADOS   **************
    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityCondicionPago'")
    fun clearPrimaryKeyCondicionPago()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityDepartamento'")
    fun clearPrimaryKeyDepartamento()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityDistrito'")
    fun clearPrimaryKeyDistrito()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityDocIdentidad'")
    fun clearPrimaryKeyDocIdentidad()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityFrecuenciaDias'")
    fun clearPrimaryKeyFrecuenciaDias()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityMoneda'")
    fun clearPrimaryKeyMoneda()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityProvincia'")
    fun clearPrimaryKeyProvincia()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityUnidadMedida'")
    fun clearPrimaryKeyUnidadMedida()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityPedidoMaster'")
    fun clearPrimaryKeyPedidoMaster()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'EntityListaPrecio'")
    fun clearPrimaryKeyListaPrecio()


    //*****************  CONSULTA DE LA EXISTENCIA DE DATABASE *******************
    @Query("SELECT EXISTS(SELECT * FROM EntityCondicionPago)")
    fun isExists(): Boolean





    //*****************  CONSULTA TABLAS BASICAS *******************
    @Query("SELECT Nombre FROM EntityCondicionPago WHERE Codigo = :Numero  ")
    fun findnamecategoriapagowithnumero(Numero :String): String
}
