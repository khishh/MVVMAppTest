package com.example.dogsappmvvm.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsappmvvm.R;
import com.example.dogsappmvvm.databinding.FragmentDetailBinding;
import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.util.Util;
import com.example.dogsappmvvm.viewmodel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private DetailViewModel detailViewModel;

//    private DogBreed dog;

    private int dogUuId;

    private FragmentDetailBinding binding;

//    @BindView(R.id.dogImage)
//    ImageView dogImage;
//
//    @BindView(R.id.dogName)
//    TextView dogName;
//
//    @BindView(R.id.dogPurpose)
//    TextView dogPurpose;
//
//    @BindView(R.id.dogTemperament)
//    TextView dogTemperament;
//
//    @BindView(R.id.dogLifeSpan)
//    TextView dogLifeSpan;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_detail, container, false);
//        ButterKnife.bind(this,view);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);


        observeViewModel();

        if(getArguments() != null){
            // get the uuid from ListFragment
            dogUuId = DetailFragmentArgs.fromBundle(getArguments()).getDogUuId();
        }

        detailViewModel.fetch(dogUuId);

    }

    private void observeViewModel(){

        detailViewModel.dog.observe(this, a_dog -> {

            // before DataBinding
//            if(a_dog != null && getContext() != null){
//                if(a_dog.imageUrl != null) {
//                    Util.loadImages(dogImage, a_dog.imageUrl, new CircularProgressDrawable(getContext()));
//                }
//                dogName.setText(a_dog.dogBreed);
//                dogPurpose.setText(a_dog.bredFor);
//                dogTemperament.setText(a_dog.temperament);
//                dogLifeSpan.setText(a_dog.lifeSpan);
//                dog = a_dog;
//            }

            // after DataBinding
            if(a_dog != null && getContext() != null) {
                binding.setDog(a_dog);
            }
        });

    }

}
