package com.unosoft.ecomercialapp.ui.pedidos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.databinding.ActivityAddPedidoBinding
import com.unosoft.ecomercialapp.entity.Pedidos.Detalle
import com.unosoft.ecomercialapp.entity.Pedidos.EnviarPedido
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActivityAddPedido : AppCompatActivity() {
    private lateinit var binding: ActivityAddPedidoBinding

    var apiInterface: PedidoApi? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //*********************************************************
        apiInterface = APIClient.client?.create(PedidoApi::class.java)

        inicialDatos()
        eventsHandlers()
    }

    private fun eventsHandlers() {
        binding.ivDatosClientAddPedido.setOnClickListener { editDateClient() }
        binding.ivProductoAddPedido.setOnClickListener { addressCartQuotation() }
        binding.icObsAddPedido.setOnClickListener { observacion() }


        //** CONSULTAR **
        val btn_savePedido = findViewById<Button>(R.id.btn_savePedido)
        btn_savePedido.setOnClickListener { enviarPedido() }
    }

    private fun inicialDatos() {

        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        binding.tvNumAddPedido.text = "Numero: "
        binding.tvFechaCreacionAddPedido.text = "Fecha y hora: "
        binding.tvClienteADdPedido.text = "Nombre Cliente "
        binding.tvRucAddPedido.text = "RUC: "
        binding.tvMonedaAddPedido.text = "Moneda: "
        binding.tvCondicionPagoAddPedido.text = "Condici??n de Pago: "

        //**********  CORREGIR ************

        CoroutineScope(Dispatchers.IO).launch {

            if (database.daoTblBasica().isExistsEntityDataCabezera()) {

                val nombreCliente = database.daoTblBasica().getAllDataCabezera()[0].nombreCliente
                val rucCliente = database.daoTblBasica().getAllDataCabezera()[0].rucCliente
                val tipoMoneda = database.daoTblBasica().getAllDataCabezera()[0].tipoMoneda
                val condicionPago = database.daoTblBasica().getAllDataCabezera()[0].condicionPago

                runOnUiThread {

                    binding.tvFechaCreacionAddPedido.text =
                        "Fecha y hora: ${formatter.format(date)}"
                    binding.tvNumAddPedido.text = "Numero: "
                    binding.tvClienteADdPedido.text = "Nombre Cliente ${nombreCliente}"
                    binding.tvRucAddPedido.text = "RUC: ${rucCliente}"
                    binding.tvMonedaAddPedido.text = "Moneda: ${tipoMoneda}"
                    binding.tvCondicionPagoAddPedido.text = "Condici??n de Pago ${condicionPago}"

                }

            }

            if (database.daoTblBasica().isExistsEntityProductList()) {

                val montoSubTotal = database.daoTblBasica().getAllListProct()[0].montoSubTotal
                val montoTotalIGV = database.daoTblBasica().getAllListProct()[0].montoTotalIGV
                val montoTotal = database.daoTblBasica().getAllListProct()[0].montoTotal

                runOnUiThread {

                    binding.tvSubTotalAddPedido.text = utils().pricetostringformat(montoSubTotal)
                    binding.tvValorVentaAddPedido.text = utils().pricetostringformat(montoSubTotal)
                    binding.tvValorIGVAddPedido.text = utils().pricetostringformat(montoTotalIGV)
                    binding.tvImporteTotalAddPedido.text = utils().pricetostringformat(montoTotal)

                }
            }
        }

    }

    private fun editDateClient() {
        val intent = Intent(this, EditCabezeraPedido::class.java)
        startActivity(intent)
        finish()
    }

    private fun addressCartQuotation() {

        CoroutineScope(Dispatchers.IO).launch {
            val valor = database.daoTblBasica().isExistsEntityDataCabezera()
            runOnUiThread {
                if (valor) {
                    val intent = Intent(this@ActivityAddPedido, ActivityCartPedido::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ActivityAddPedido,"Rellenar datos clientes",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun enviarPedido() {

        CoroutineScope(Dispatchers.IO).launch {
            var validarCabezera = database.daoTblBasica().isExistsEntityDataCabezera()
            var validarListProct = database.daoTblBasica().isExistsEntityListProct()
            val listaPedido = ArrayList<Detalle>()

            runOnUiThread {
                if (validarCabezera) {
                    if (validarListProct) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val datosLista = database.daoTblBasica().getAllListProct()
                            val datosCabezera = database.daoTblBasica().getAllDataCabezera()[0]
                            val datoslogin = database.daoTblBasica().getAllDataLogin()[0]

                            println("*********** TABLA DATA CABEZERA ***********")
                            println(database.daoTblBasica().getAllDataCabezera())

                            datosLista.forEach {
                                listaPedido.add(
                                    Detalle(
                                        0,
                                        it.id_Producto,
                                        it.cantidad,
                                        it.nombre,
                                        it.precioUnidad,
                                        0,
                                        it.precioUnidad * 0.18,
                                        it.precioUnidad * it.cantidad,
                                        0,
                                        0,
                                        "",
                                        it.id,
                                        0.0,
                                        "1",
                                        0,
                                        "1",
                                        0,
                                        0,
                                        "",
                                        "0",
                                        "",
                                        "0",
                                        it.unidad,
                                        "2022-07-19T23:38:40.713Z",
                                        "${datoslogin.usuario}",
                                        "0001", //******
                                        "",
                                        0,
                                        0,
                                        0,
                                        "",
                                        "2022-07-19T23:38:40.713Z",
                                        1,
                                        "",
                                        "S",
                                        "",
                                        0,
                                        "N",
                                        "0",
                                        "N",
                                        "",
                                        0,
                                        0,
                                        0,
                                        "",
                                        "2022-07-19T23:38:40.713Z",
                                        0,
                                        0,
                                        0
                                    )
                                )
                            }

                            val datosPedido = EnviarPedido(
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                datosCabezera.codCondicionPago!!,
                                datosCabezera.codMoneda!!,
                                "2022-07-19T23:38:40.713Z",
                                "",
                                database.daoTblBasica().getAllListProct()[0].montoSubTotal,
                                database.daoTblBasica().getAllListProct()[0].montoTotalIGV,
                                0,
                                database.daoTblBasica().getAllListProct()[0].montoTotal,
                                0,
                                0,
                                "",
                                "",
                                "0001",
                                datosCabezera.idCliente!!.toInt(),
                                0,
                                datoslogin.usuariocreacion,
                                datoslogin.usuarioautoriza,
                                "2022-07-19T23:38:40.713Z",
                                "2022-07-19T23:38:40.713Z",
                                datoslogin.codigO_EMPRESA,
                                datoslogin.sucursal,
                                0,
                                0,
                                "",
                                "2022-07-19T23:38:40.713Z",
                                "",
                                "",
                                "",
                                "",
                                0,
                                0,
                                "",
                                datoslogin.redondeo,
                                "",
                                "",
                                "",
                                "",
                                prefs.getTipoCambio().toDouble(),
                                datoslogin.sucursal,
                                "",
                                "",
                                listaPedido
                            )

                            val response = apiInterface!!.postCreatePedido(datosPedido)

                            runOnUiThread {

                                if (response.isSuccessful) {
                                    println("**********************************")
                                    println("********      EXITO        *******")
                                    println("**********************************")
                                    Toast.makeText(this@ActivityAddPedido,"Exito",Toast.LENGTH_SHORT).show()
                                } else {
                                    println("**********************************")
                                    println("********      ERROR        *******")
                                    println("**********************************")
                                    Toast.makeText(this@ActivityAddPedido,"ERROR",Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }else{
                        Toast.makeText(this@ActivityAddPedido, "Falta ingresar productos", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ActivityAddPedido, "Falta datos cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun observacion() {
        //***********  Alerta de Dialogo  ***********
        val dialogue = Dialog(this@ActivityAddPedido)
        dialogue.setContentView(R.layout.dialogue_observacion)
        dialogue.window!!.setGravity(Gravity.TOP)
        dialogue.show()

        //***********Declara elementos *****************
        var et_detalle = dialogue.findViewById<EditText>(R.id.et_detalle)
        val bt_guardarDetalle = dialogue.findViewById<Button>(R.id.bt_guardarDetalle)

        //*********** BOTON GUARDAR DEL DIALOGO ********
        bt_guardarDetalle.setOnClickListener {
            var detalle: String = et_detalle.text.toString()
            binding.tvObsAddPedido.text = detalle
            dialogue.hide()
        }
    }
    override fun onBackPressed() {
        clearTable()
        super.onBackPressed()
    }
    private fun clearTable() {

        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()

            database.daoTblBasica().deleteTableDataCabezera()
            database.daoTblBasica().clearPrimaryKeyDataCabezera()

            println(database.daoTblBasica().getAllListProct())
        }

    }



}


