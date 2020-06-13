package com.example.dogsappmvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.viewmodel.DetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private DetailViewModel detailViewModel;

    private DogBreed dog;

    private int dogUuId;

    @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;

    @BindView(R.id.dogPurpose)
    TextView dogPurpose;

    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifeSpan)
    TextView dogLifeSpan;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.refresh();

        observeViewModel();

        if(getArguments() != null){
            dogUuId = DetailFragmentArgs.fromBundle(getArguments()).getDogUuId();
        }

    }

    private void observeViewModel(){

        detailViewModel.dog.observe(this, a_dog -> {
            if(a_dog != null){
                dogImage.setImageResource(R.mipmap.ic_launcher_round);
                dogName.setText(a_dog.dogBreed);
                dogPurpose.setText(a_dog.bredFor);
                dogTemperament.setText(a_dog.temperament);
                dogLifeSpan.setText(a_dog.lifeSpan);
                dog = a_dog;
            }
        });

    }

}
