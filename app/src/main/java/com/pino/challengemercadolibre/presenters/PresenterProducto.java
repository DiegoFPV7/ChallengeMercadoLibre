package com.pino.challengemercadolibre.presenters;

import com.google.gson.Gson;
import com.pino.challengemercadolibre.R;
import com.pino.challengemercadolibre.dto.ProductoDTO;
import com.pino.challengemercadolibre.interfaces.IProducto;
import com.pino.challengemercadolibre.model.ModeloProducto;
import com.pino.challengemercadolibre.util.Camaleon;

public class PresenterProducto implements IProducto.Presenter {

    private IProducto.View productoActivity;
    private IProducto.Model modeloProducto;

    public PresenterProducto(IProducto.View productoActivity) {
        this.productoActivity = productoActivity;
        this.modeloProducto = new ModeloProducto(this);
    }

    @Override
    public void cargarProducto(String idProducto) {
        modeloProducto.consumirRestGet(idProducto);
    }

    @Override
    public void enviarACompra(String urlMercadoLibre) {
        Camaleon.abrirLink(productoActivity.getActivityProducto(),urlMercadoLibre);
    }

    @Override
    public void responseConsultaProducto(String responseJson) {
        if(responseJson!=null && !responseJson.equals("")) {
            ProductoDTO productoDTO = new Gson().fromJson(responseJson, ProductoDTO.class);
            productoActivity.mostrarProducto(productoDTO);
        }else{
            productoActivity.mostrarToas(productoActivity.getActivityProducto().getResources().getString(R.string.error_cargando_productos));
        }
    }
}
