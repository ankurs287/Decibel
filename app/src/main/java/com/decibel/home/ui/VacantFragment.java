package com.decibel.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.decibel.R;
import com.decibel.home.HomeContract;
import com.decibel.home.adapter.RoomsAdapter;
import com.decibel.home.presenter.VacantPresenter;
import com.decibel.model.Room;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VacantFragment extends Fragment implements HomeContract.VacantView {

    private static final String TAG = VacantFragment.class.getName();
    private VacantPresenter mPresenter;
    @BindView(R.id.rv_rooms)
    RecyclerView rvRooms;
    private RoomsAdapter mRoomsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vacant, container, false);
        ButterKnife.bind(this, rootView);

        mPresenter = new VacantPresenter(this);

        setupRV();

        return rootView;
    }

    private void setupRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvRooms.setLayoutManager(llm);
        rvRooms.setHasFixedSize(true);
        mRoomsAdapter = new RoomsAdapter();
        rvRooms.setAdapter(mRoomsAdapter);

        mPresenter.fetchRoomsWithMostVacantSeats(10);
    }

    @Override
    public void fetchRoomsError() {
        Log.d(TAG, "Error fetching rooms");
    }

    @Override
    public void fetchRoomsSuccess(ArrayList<Room> rooms) {
        mRoomsAdapter.setData(rooms);
    }
}
