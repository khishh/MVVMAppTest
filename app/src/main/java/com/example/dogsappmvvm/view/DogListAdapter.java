package com.example.dogsappmvvm.view;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsappmvvm.R;
import com.example.dogsappmvvm.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.DogViewHolder> {

    private ArrayList<DogBreed> dogList = new ArrayList<>();

    public DogListAdapter(ArrayList<DogBreed> dogList){
        this.dogList = dogList;
    }

    public void updateDogsList(List<DogBreed> newDogList){
        dogList.clear();
        dogList.addAll(newDogList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.dogName);
        TextView lifespan = holder.itemView.findViewById(R.id.lifeSpan);

        // use glide for image

        name.setText(dogList.get(position).dogBreed);
        lifespan.setText(dogList.get(position).lifeSpan);
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder{

        public View itemView;

        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
