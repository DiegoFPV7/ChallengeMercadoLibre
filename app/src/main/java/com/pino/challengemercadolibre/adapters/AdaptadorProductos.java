package com.pino.challengemercadolibre.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pino.challengemercadolibre.MainActivity;
import com.pino.challengemercadolibre.R;
import com.pino.challengemercadolibre.dto.ResultDTO;
import com.pino.challengemercadolibre.interfaces.IMain;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolder>{

    private List<ResultDTO> productos;
    private IMain.Presenter presenterMain;

    public AdaptadorProductos(List<ResultDTO> productos, IMain.Presenter presenterMain) {
        this.productos = productos;
        this.presenterMain = presenterMain;
    }

    @NonNull
    @NotNull
    @Override
    public AdaptadorProductos.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lista_productos, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorProductos.ViewHolder viewHolder, int position) {
        viewHolder.asignarDatos(productos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private ImageView ivImagenProducto;
        private int posicion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.itemProTvNombreProducto);
            tvNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenterMain.verProductoSelecionado(posicion);
                }
            });
            ivImagenProducto = itemView.findViewById(R.id.itemProIBImagenProducto);
            ivImagenProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenterMain.verProductoSelecionado(posicion);
                }
            });

        }

        public void asignarDatos(ResultDTO producto, int posicion){
            tvNombre.setText(producto.getTitle());
            Picasso.get().load(producto.getThumbnail()).into(ivImagenProducto);
            this.posicion = posicion;

        }

    }
}
