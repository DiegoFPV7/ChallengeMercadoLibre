package com.pino.challengemercadolibre.model;

import com.google.gson.Gson;
import com.pino.challengemercadolibre.R;
import com.pino.challengemercadolibre.dto.SearchDTO;
import com.pino.challengemercadolibre.interfaces.IMain;
import com.pino.challengemercadolibre.util.Camaleon;
import com.pino.challengemercadolibre.util.MyCallBack;

public class ModeloMain implements IMain.Model {
    private static final String ENDPOINT_SERCH = "https://api.mercadolibre.com/sites/MCO/search?q=";
    private IMain.Presenter presenter;

    public ModeloMain(IMain.Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void consumirRestGet(String text) {
        Camaleon.consumirRestGet(ENDPOINT_SERCH+text , new MyCallBack<String>() {
            @Override
            public void onResponse(String responseJson) {
                presenter.responseConsumoRest(responseJson);
            }
        });
    }
}
