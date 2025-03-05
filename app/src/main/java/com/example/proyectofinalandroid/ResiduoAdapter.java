package com.example.proyectofinalandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ResiduoAdapter extends RecyclerView.Adapter<ResiduoAdapter.ViewHolder> {

    private List<Residuo> residuos;
    private int selectedPosition = RecyclerView.NO_POSITION; // 🔹 Almacena la fila seleccionada
    private OnResiduoClickListener onResiduoClickListener;

    private Context context;



    public ResiduoAdapter(Context context, List<Residuo> residuos, OnResiduoClickListener listener) {
        this.residuos = residuos;
        this.context = context;
        this.onResiduoClickListener = listener;


    }


    public interface OnResiduoClickListener {
        void onResiduoSelected(boolean isSelected);
    }


    public void actualizarLista(List<Residuo> nuevaLista) {
        if (nuevaLista != null) {
            residuos.clear();
            residuos.addAll(nuevaLista);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residuo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Residuo residuo = residuos.get(position);
        holder.tvId.setText(String.valueOf(residuo.getId()));
        holder.tvUsuario.setText(residuo.getNombreUsuario());
        holder.tvTipo.setText(residuo.getTipo());
        holder.tvPeso.setText(residuo.getPeso() + " kg");
        holder.tvFecha.setText(residuo.getFecha());


        // 🔹 Cambiar color de fondo y texto según selección
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.BLUE);
            holder.tvId.setTextColor(Color.WHITE);
            holder.tvUsuario.setTextColor(Color.WHITE);
            holder.tvTipo.setTextColor(Color.WHITE);
            holder.tvPeso.setTextColor(Color.WHITE);
            holder.tvFecha.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.tvId.setTextColor(Color.BLACK);
            holder.tvUsuario.setTextColor(Color.BLACK);
            holder.tvTipo.setTextColor(Color.BLACK);
            holder.tvPeso.setTextColor(Color.BLACK);
            holder.tvFecha.setTextColor(Color.BLACK);
        }


        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = (selectedPosition == position) ? RecyclerView.NO_POSITION : position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);


            onResiduoClickListener.onResiduoSelected(selectedPosition != RecyclerView.NO_POSITION);

            // Pasar datos a `datos.java`
            Intent intent = new Intent(context, datos.class);
            intent.putExtra("id", residuo.getId());
            intent.putExtra("usuario", residuo.getNombreUsuario());
            intent.putExtra("tipo", residuo.getTipo());
            intent.putExtra("peso", residuo.getPeso());
            intent.putExtra("fecha", residuo.getFecha());
            context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return residuos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvUsuario, tvTipo, tvPeso, tvFecha;

        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdtable);
            tvUsuario = itemView.findViewById(R.id.tvUsuariotable);
            tvTipo = itemView.findViewById(R.id.tvTipotable);
            tvPeso = itemView.findViewById(R.id.tvPesotable);
            tvFecha = itemView.findViewById(R.id.tvFechatable);
        }
    }








}
