package com.pino.challengemercadolibre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pino.challengemercadolibre.interfaces.IProducto;
import com.pino.challengemercadolibre.presenters.PresenterProducto;
import com.pino.challengemercadolibre.dto.ProductoDTO;
import com.pino.challengemercadolibre.util.Camaleon;
import com.pino.challengemercadolibre.util.Constantes;
import com.squareup.picasso.Picasso;

public class ProductoActivity extends AppCompatActivity implements View.OnClickListener, IProducto.View{

    private ProgressDialog miProgressDialog;
    private IProducto.Presenter presenter;

    private TextView tvNombreProducto ;
    private TextView tvGarantiaProducto;
    private TextView tvPrecioProducto;
    private Button btnComprar;
    private ImageView ivImagenProducto;
    private String idProducto;

    private ProductoDTO productoDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        obtenerInfoIntent();
    }

    private void obtenerInfoIntent() {
        idProducto = getIntent().getStringExtra(Constantes.ID_PRODUCTO);
        String nombreProducto = getIntent().getStringExtra(Constantes.NOMBRE_PRODUCTO);
        activarProgresdialog(getResources().getString(R.string.buscando)+ nombreProducto);
        instanciarComponentes();
    }

    private void instanciarComponentes() {
        tvNombreProducto = findViewById(R.id.protvNombreProducto);
        tvGarantiaProducto = findViewById(R.id.protvGarantia);
        tvPrecioProducto = findViewById(R.id.protvPrecio);
        ivImagenProducto = findViewById(R.id.proIVImagenProducto);
        btnComprar = findViewById(R.id.proBtnComprar);
        btnComprar.setOnClickListener(this);
        instanciarVariables();
    }

    private void instanciarVariables() {
        presenter = new PresenterProducto(this);
        presenter.cargarProducto(idProducto);
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

    private void pintarLog(String mensaje) {
        Log.i(getClass().getName(),"<" + Camaleon.darFechaActual("dd/MM/yyyy HH:MM:SS") + ">" + mensaje);
    }

    @Override
    public Activity getActivityProducto() {
        return this;
    }

    @Override
    public void mostrarProducto(ProductoDTO productoDTO) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProductoActivity.this.productoDTO = productoDTO;
                Picasso.get().load(productoDTO.getPictures().get(0).getUrl()).into(ivImagenProducto);
                tvNombreProducto.setText(productoDTO.getTitle()+"");
                if(productoDTO.getWarranty()!=null){
                    tvGarantiaProducto.setText(productoDTO.getWarranty()+"");
                }else{
                    tvGarantiaProducto.setText("No aplica");
                }

                tvPrecioProducto.setText(productoDTO.getPrice()+"");
                cerrarProgresDialog();
            }
        });
    }

    @Override
    public void mostrarToas(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.proBtnComprar:
                enviarACompra();
                break;
        }
    }

    private void enviarACompra() {
        presenter.enviarACompra(productoDTO.getPermalink());

    }
}