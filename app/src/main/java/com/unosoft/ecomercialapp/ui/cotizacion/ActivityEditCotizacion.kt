package com.unosoft.ecomercialapp.ui.cotizacion

import android.app.Dialog
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.CotizacionMaster
import com.unosoft.ecomercialapp.api.PedidoMaster
import com.unosoft.ecomercialapp.databinding.ActivityEditCotizacionBinding
import com.unosoft.ecomercialapp.db.cotizacion.EntityEditQuotationDetail
import com.unosoft.ecomercialapp.db.cotizacion.EntityQuotationMaster
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ActivityEditCotizacion : AppCompatActivity() {

    private lateinit var adapterCotizaciones: listcotizacionesadapter
    private val listacotizaciones = ArrayList<cotizacionesDto>()
    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    var apiInterface: CotizacionMaster? = null

    private lateinit var binding: ActivityEditCotizacionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //**********************************************

        apiInterface = APIClient.client?.create(CotizacionMaster::class.java) as CotizacionMaster

        getData(DATAGLOBAL.prefs.getIdPedido())
        iniciarData()
        eventsHandlers()


    }

    override fun onBackPressed() {
        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()
        }
        super.onBackPressed()
    }


    private fun eventsHandlers() {
        binding.ivProductosCot.setOnClickListener { showactivitydetail() }
        //binding.ivPersonCot.setOnClickListener { editDetailQuotation() }
    }

    //**************   EVENTS HANDLERS   ****************
    private fun editDetailQuotation() {
        //******************** EXTRAR DATA BASE ********************//
        val listaspinner = ArrayList<String>()
        CoroutineScope(Dispatchers.IO).launch {
            val size = database.daoTblBasica().getSizeMoneda()
            listaTipoMoneda.clear()
            for (i in 1..size) {
                listaTipoMoneda.add(database.daoTblBasica().getTipoMoneda(i))
            }
        }

        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.dialogue_editarinformacion)
        dialogue.show()


        //**************** SPINNER *****************
        listaTipoMoneda.forEach { listaspinner.add(it.Nombre) }

        val sp_filtroMoneda = dialogue.findViewById<Spinner>(R.id.sp_filtroMoneda)
        val Adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaspinner)
        sp_filtroMoneda?.adapter = Adaptador
        sp_filtroMoneda.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                val itemSelect = listaspinner[position]
                Toast.makeText(this@ActivityEditCotizacion,"Lista $itemSelect",Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //*******************************************
        dialogue.show()
    }

    private fun showactivitydetail() {
        val intent = Intent(this, ActivityDetalleCotizacion::class.java)

        /*
        //ENVIAR DATOS
        val bundle = Bundle()
        bundle.putSerializable("DATOSCOTIZACION", dataclassCotizacion)
        intent.putExtras(bundle)*/

        startActivity(intent)
    }

    private fun iniciarData() {

        val datos = intent.getSerializableExtra("DATOSCOTIZACION") as cotizacionesDto

        binding.tvFechaCreacionCot.text = "00/00/00"
        //tv_fechaCreacionCot?.text = "Fecha Creacion: ${LocalDateTime.now()}"
        binding.tvIdCotizacion.text = StringBuilder().append("NUMERO: ").append(datos.id_cotizacion)
        binding.tvNameClientCot.text = StringBuilder().append("NOMBRE CLIENTE: ").append(datos.persona)
        binding.tvRucCot.text = StringBuilder().append("RUC: ").append(datos.ruc)
        binding.tvTipoMonedaCot.text = StringBuilder().append("MONEDA: ").append(datos.mon)
        binding.tvCondPagoCot.text = StringBuilder().append("Consicion Pago: ").append("--------")
        binding.tvSubtotalCot.text = StringBuilder().append(datos.mon).append(datos.importe_total - datos.importe_igv)
        binding.tvValorventaCot.text = StringBuilder().append(datos.mon).append(datos.importe_total - datos.importe_igv)
        binding.tvIgvCot.text = StringBuilder().append(datos.mon).append(datos.importe_igv)
        binding.tvImporte.text = StringBuilder().append(datos.mon).append(datos.importe_total)

    }

    fun getData(IDQUOTATION: String) {
        CoroutineScope(Dispatchers.IO).launch {

            val quotationcabresponse = apiInterface!!.getbyIdQuotationCab("$IDQUOTATION")

            val quotationdetailresponse = apiInterface!!.getbyIdQuotationDetail("$IDQUOTATION")

            if (quotationcabresponse.isSuccessful) {

                var t = quotationcabresponse.body()!!

                //Insert Cabecera ROOM
                with(database) {
                    //Insert Cabecera ROOM
                    daoTblBasica().deleteTableQuotationMaster()

                    t.forEach {
                        daoTblBasica().insertQuotationMaster(
                            EntityQuotationMaster(0,
                                it.iD_COTIZACION,
                                it.numerO_COTIZACION,
                                it.codigO_VENDEDOR,
                                it.codigO_VENDEDOR_ASIGNADO,
                                it.codigO_CPAGO,
                                it.codigO_MONEDA,
                                it.fechA_COTIZACION,
                                it.numerO_OCLIENTE,
                                it.importE_STOT,
                                it.importE_DESCUENTO,
                                it.valoR_VENTA,
                                it.importE_IGV,
                                it.importE_TOTAL,
                                it.porcentajE_DESCUENTO,
                                it.porcentajE_IGV,
                                it.observacion,
                                it.estado,
                                it.iD_CLIENTE,
                                it.iD_CLIENTE_FACTURA,
                                it.importE_ISC,
                                it.contacto,
                                it.emaiL_CONTACTO,
                                it.usuariO_CREACION,
                                it.fechA_CREACION,
                                it.usuariO_MODIFICACION,
                                it.fechA_MODIFICACION,
                                it.codigO_EMPRESA,
                                it.codigO_SUCURSAL,
                                it.tipO_FECHA_ENTREGA,
                                it.diaS_ENTREGA,
                                it.fechA_ENTREGA,
                                it.usuariO_AUTORIZA,
                                it.fechA_AUTORIZACION,
                                it.lugaR_ENTREGA,
                                it.comision,
                                it.redondeo,
                                it.validez,
                                it.motivo,
                                it.correlativo,
                                it.centrO_COSTO,
                                it.tipO_CAMBIO,
                                it.iD_COTIZACION_PARENT,
                                it.telefonos,
                                it.sucursal,
                                it.tipO_ENTREGA,
                                it.diaS_ENTREGA2,
                                it.observacioN2,
                                it.costo,
                                it.iD_OPORTUNIDAD,
                                it.motivO_PERDIDA,
                                it.competencia,
                                it.notA_PERDIDA,
                                it.separaR_STOCK,
                                it.tipO_DSCTO,
                                it.iD_PROYECTO,
                                it.swT_VISADO,
                                it.usuario,
                                it.noM_MONEDA,
                                it.direccion,
                                it.ruc,
                                it.persona,
                                it.tipO_CLIENTE,
                            )
                        )
                    }
                }


                if (quotationdetailresponse.isSuccessful) {

                    var d = quotationdetailresponse.body()!!
                    //Insert Detalle ROOM
                    with(database) {
                        daoTblBasica().deleteTableEntityEditQuotationDetail()
                        d.forEach {
                            daoTblBasica().insertEditQuotatiDetail(
                                EntityEditQuotationDetail(
                                    0,
                                    it.iD_COTIZACION,
                                    it.iD_PRODUCTO,
                                    it.codigo,
                                    it.codigO_BARRA,
                                    it.nombre,
                                    it.cantidad,
                                    it.iD_UNIDAD,
                                    it.unidad,
                                    it.precio,
                                    it.descuento,
                                    it.importe,
                                    it.secuencia,
                                    it.igv,
                                    it.preciO_ORIGINAL,
                                    it.tipo,
                                    it.afectO_IGV,
                                    it.importE_DSCTO,
                                    it.comision,
                                    it.swT_PIGV,
                                    it.noM_UNIDAD
                                )
                            )
                        }
                    }
                }
            }




            CoroutineScope(Dispatchers.IO).launch {
                println("********* TODOS LAS COTIZACIONES ************")
                println(database.daoTblBasica().getAllQuotationMaster())
                println("********** DETALLE DE COTIZACION ************")
                println(database.daoTblBasica().getAllQuotationDetail())
            }
        }
    }
}


