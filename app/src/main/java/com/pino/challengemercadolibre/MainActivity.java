package com.pino.challengemercadolibre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pino.challengemercadolibre.adapters.AdaptadorProductos;
import com.pino.challengemercadolibre.presenters.PresenterMain;
import com.pino.challengemercadolibre.dto.ResultDTO;
import com.pino.challengemercadolibre.interfaces.IMain;
import com.pino.challengemercadolibre.util.Constantes;
import com.pino.challengemercadolibre.util.MyCallBack;
import com.pino.challengemercadolibre.util.Camaleon;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMain.View {

    private IMain.Presenter presenter;
    private ProgressDialog miProgressDialog; //Lo uso para validar conexion

    private EditText etBuscador;
    private Button btnBuscar;

    //Componentes Listar productos
    private List<ResultDTO> productos;
    private RecyclerView rvListaProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activarProgresdialog(getResources().getString(R.string.validando_conexion));
        validarConexion();

    }

    private void validarConexion() {
        pintarLog("Validando Conexion");
        if(Camaleon.hayInternet(this) || Camaleon.isOnlineNet()){
            pintarLog("¡Conexion OK!");
            instanciarComponentes();
        }else{
            pintarLog("¡Error de conexion!");
            cerrarApp();
        }
    }

    public void cerrarApp() {
        Camaleon.mostrarDialogoInfo(this,
                getResources().getString(R.string.error_conexion),
                getResources().getString(R.string.descripcion_error_conexion), new MyCallBack<String>() {
                    @Override
                    public void onResponse(String obj) {
                        finish();
                    }
                });

    }

    private void instanciarComponentes() {
        pintarLog("Instanciando componentes");
        etBuscador = findViewById(R.id.principalEtBuscador);
        btnBuscar = findViewById(R.id.principalBtnBuscar);
        btnBuscar.setOnClickListener(this);
        instanciarVariables();
    }

    private void instanciarVariables() {
        pintarLog("Instannciando Variables");
        presenter = new PresenterMain(this);
        productos = new ArrayList<>();
        rvListaProductos = findViewById(R.id.principalRvProductos);
        rvListaProductos.setLayoutManager(new LinearLayoutManager(this));
        cerrarProgresDialog();
    }

    private void pintarLog(String mensaje) {
        Log.i(getClass().getName(),"<" + Camaleon.darFechaActual("dd/MM/yyyy HH:MM:SS") + ">" + mensaje);
    }

    public void activarProgresdialog(String titulo){
        pintarLog("Se activa progressDialog titulo: "+titulo);
        if(miProgressDialog!=null){
            cerrarProgresDialog();
        }
        miProgressDialog = new ProgressDialog(this);
        miProgressDialog.setTitle(titulo);
        miProgressDialog.setMessage("Por favor espere...");
        miProgressDialog.setCancelable(false);
        miProgressDialog.setCanceledOnTouchOutside(false);
        miProgressDialog.show();
    }

    public void cerrarProgresDialog(){
        pintarLog("Cerrando progresdialog");
        miProgressDialog.hide();
        miProgressDialog.dismiss();
        miProgressDialog=null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.principalBtnBuscar:
                buscarProductos();
                break;
        }
    }

    private void buscarProductos() {
        pintarLog("Obteniendo texto del buscador");
        String text = etBuscador.getText()+"";
        if(text!=null && !text.equals("") && !text.equals("null") && text.length()>4){
            pintarLog("Se va a buscar el texto "+text);
            activarProgresdialog(getResources().getString(R.string.buscando)+text);
            presenter.buscarProductos(text);
        }else{
            pintarLog("Texto no valido para consultar "+text);
            mostrarToas(getResources().getString(R.string.mensaje_buscador_vacio));
        }

    }




    @Override
    protected void onResume() {
        super.onResume();
        if(etBuscador!=null){
            String text = etBuscador.getText()+"";
            if(text!=null && !text.equals("") && !text.equals("null") && text.length()>4){
                buscarProductos();
            }
        }
    }

    @Override
    public Activity getActivityMain() {
        return this;
    }

    @Override
    public void setearProductos(List<ResultDTO> productos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actualizarProductos(productos);
            }
        });
    }

    private void actualizarProductos(List<ResultDTO> productos) {
        pintarLog("Se va a actualizar los productos");
        this.productos = productos;
        rvListaProductos.setAdapter(new AdaptadorProductos(this.productos, presenter));
        cerrarProgresDialog();
    }

    @Override
    public void mostrarToas(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void verProductoSeleccionado(int posicion) {
        String idProducto = productos.get(posicion).getId();
        String nombreProducto = productos.get(posicion).getTitle();
        Intent intent = new Intent(MainActivity.this, ProductoActivity.class);
        intent.putExtra(Constantes.ID_PRODUCTO, idProducto);
        intent.putExtra(Constantes.NOMBRE_PRODUCTO, nombreProducto);
        startActivity(intent);
    }
}