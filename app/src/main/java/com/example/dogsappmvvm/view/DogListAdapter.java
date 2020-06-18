package com.example.dogsappmvvm.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsappmvvm.R;
import com.example.dogsappmvvm.databinding.ItemDogBinding;
import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.DogViewHolder> implements DogItemOnClickListener{

    private ArrayList<DogBreed> dogList;

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

        // ---- before DataBinding -------
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent,false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        holder.itemView.setDog(dogList.get(position));
        holder.itemView.setListener(this);

        // ---- before DataBinding -------
//        ImageView imageView = holder.itemView.findViewById(R.id.imageView);
//        TextView name = holder.itemView.findViewById(R.id.dogName);
//        TextView lifespan = holder.itemView.findViewById(R.id.lifeSpan);
//
//        // use glide for image
//        Util.loadImages(imageView, dogList.get(position).imageUrl, Util.getCircularProgressDrawable(imageView.getContext()));
//
//        name.setText(dogList.get(position).dogBreed);
//        lifespan.setText(dogList.get(position).lifeSpan);
//
//        LinearLayout linearLayout = holder.itemView.findViewById(R.id.dog_item_linearLayout);
//        linearLayout.setOnClickListener(v -> {
//            ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
//            action.setDogUuId(dogList.get(position).uuid);
//            Navigation.findNavController(linearLayout).navigate(action);
//        });

    }

    @Override
    public void onDogItemClicked(View view) {
        String uuid_str = ((TextView)view.findViewById(R.id.uuid)).getText().toString();
        int uuid = Integer.parseInt(uuid_str);
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDogUuId(uuid);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }



    class DogViewHolder extends RecyclerView.ViewHolder{

        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

}
