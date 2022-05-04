package com.pino.challengemercadolibre.presenters;


import com.google.gson.Gson;
import com.pino.challengemercadolibre.MainActivity;
import com.pino.challengemercadolibre.R;
import com.pino.challengemercadolibre.dto.SearchDTO;
import com.pino.challengemercadolibre.interfaces.IMain;
import com.pino.challengemercadolibre.model.ModeloMain;
import com.pino.challengemercadolibre.util.Camaleon;
import com.pino.challengemercadolibre.util.MyCallBack;


public class PresenterMain implements IMain.Presenter {


    private IMain.View mainActivity;
    private IMain.Model modelMain;

    public PresenterMain(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.modelMain = new ModeloMain(this);
    }


    @Override
    public void buscarProductos(String text) {
        modelMain.consumirRestGet(text);
    }

    @Override
    public void responseConsumoRest(String responseJson) {
        if(responseJson!=null && !responseJson.equals("")){
            SearchDTO searchDTO = new Gson().fromJson(responseJson, SearchDTO.class);
            mainActivity.setearProductos(searchDTO.getResults());
        }else{
            mainActivity.mostrarToas(mainActivity.getActivityMain().getResources().getString(R.string.error_cargando_productos));
        }
    }

    @Override
    public void verProductoSelecionado(int posicion) {
        mainActivity.verProductoSeleccionado(posicion);
    }
}
