package com.example.dogsappmvvm.view;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dogsappmvvm.R;
import com.example.dogsappmvvm.model.DogBreed;
import com.example.dogsappmvvm.view.DogListAdapter;
import com.example.dogsappmvvm.viewmodel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private DogListAdapter dogsListAdapter = new DogListAdapter(new ArrayList<>());

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.dogList)
    RecyclerView dogList;

    @BindView(R.id.listErrorView)
    TextView listErrorView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();


        dogList.setLayoutManager(new LinearLayoutManager(getContext()));
        dogList.setAdapter(dogsListAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            dogList.setVisibility(View.GONE);
            listErrorView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.refreshBypassCache();
            swipeRefreshLayout.setRefreshing(false);
        });

        observeViewModel();

    }

    private void observeViewModel(){

        viewModel.dogList.observe(this, dogs -> {
            if(dogs != null){
                dogList.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogs);
            }
        });

        viewModel.dogLoadError.observe(this, isError -> {
            if(isError != null)
                listErrorView.setVisibility(isError ? View.VISIBLE : View.GONE);
        });

        viewModel.loading.observe(this, isLoading -> {
            if(isLoading != null){
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if(isLoading){
                    dogList.setVisibility(View.GONE);
                    listErrorView.setVisibility(View.GONE);
                }
            }
        });
    }



}
