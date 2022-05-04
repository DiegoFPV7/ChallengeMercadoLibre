package com.pino.challengemercadolibre.model;

import com.pino.challengemercadolibre.interfaces.IProducto;
import com.pino.challengemercadolibre.util.Camaleon;
import com.pino.challengemercadolibre.util.MyCallBack;

public class ModeloProducto implements IProducto.Model {
    private static final String ENDPOINT_ITEMS = "https://api.mercadolibre.com/items/";
    private IProducto.Presenter presenter;

    public ModeloProducto (IProducto.Presenter presenter){
        this.presenter = presenter;
    }
    @Override
    public void consumirRestGet(String idProducto) {
        Camaleon.consumirRestGet(ENDPOINT_ITEMS+idProducto, new MyCallBack<String>() {
            @Override
            public void onResponse(String responseJson) {
                presenter.responseConsultaProducto(responseJson);

            }
        });
    }
}
