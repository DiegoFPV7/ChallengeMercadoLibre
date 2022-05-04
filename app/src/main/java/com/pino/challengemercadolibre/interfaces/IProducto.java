package com.pino.challengemercadolibre.interfaces;

import android.app.Activity;

import com.pino.challengemercadolibre.dto.ProductoDTO;
import com.pino.challengemercadolibre.dto.ResultDTO;

import java.util.List;

public interface IProducto {
    public interface Model{
        public void consumirRestGet(String idProducto);
    }

    public interface View{
        public Activity getActivityProducto();
        public void mostrarProducto(ProductoDTO productoDTO);
        public void mostrarToas(String mensaje);
    }
    public interface Presenter{
        public void cargarProducto(String idProducto);
        public void enviarACompra(String urlMercadoLibre);
        public void responseConsultaProducto(String responseJson);


    }
}
