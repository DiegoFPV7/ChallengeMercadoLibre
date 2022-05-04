package com.pino.challengemercadolibre.interfaces;

import android.app.Activity;

import com.pino.challengemercadolibre.dto.ResultDTO;
import com.pino.challengemercadolibre.util.MyCallBack;

import java.util.List;

public interface IMain {
    public interface Model{
        public void consumirRestGet(String text);
    }

    public interface View{
        public Activity getActivityMain();
        public void setearProductos(List<ResultDTO> productos);
        public void mostrarToas(String mensaje);
        public void verProductoSeleccionado(int posicion);
    }
    public interface Presenter{

        public void buscarProductos(String text);
        public void responseConsumoRest(String responseJson);
        public void verProductoSelecionado(int posicion);

    }
}
