package com.example.quiz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.Models.SetModel;
import com.example.quiz.R;
import com.example.quiz.databinding.ItemSetsBinding;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SetModel> list;
    private OnSetClickListener clickListener;

    public SetAdapter(Context context, ArrayList<SetModel> list, OnSetClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SetModel model = list.get(position);
        holder.binding.setName.setText(model.getSetName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifiez le type de l'ensemble et appelez la méthode appropriée de l'interface
                clickListener.onSetClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSetsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSetsBinding.bind(itemView);
        }
    }

    // Interface pour gérer les clics sur les ensembles
    public interface OnSetClickListener {
        void onSetClick(SetModel setModel);
    }
}
