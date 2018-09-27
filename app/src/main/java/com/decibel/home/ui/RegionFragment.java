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
import com.decibel.home.presenter.RegionPresenter;
import com.decibel.model.Room;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionFragment extends Fragment implements HomeContract.RegionView {

    private static final String TAG = RegionFragment.class.getName();
    private RegionPresenter mPresenter;
    @BindView(R.id.sp_region)
    MaterialSpinner spRegion;
    @BindView(R.id.rv_rooms)
    RecyclerView rvRooms;
    private RoomsAdapter mRoomsAdapter;
    private String region = "Library";
    private ArrayList<String> regionsArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_region, container, false);
        ButterKnife.bind(this, rootView);

        mPresenter = new RegionPresenter(this);

        setupSpinner();
        setupRV();

        return rootView;
    }

    private void setupSpinner() {
        mPresenter.fetchRegions();

        spRegion.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mPresenter.fetchRooms(regionsArrayList.get(position));
            }
        });

//        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
//                    int position, long id) {
//                mPresenter.fetchRooms("", regionsArrayList.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                mRoomsAdapter.setData(null);
//            }
//
//        });
    }

    private void setupRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvRooms.setLayoutManager(llm);
        rvRooms.setHasFixedSize(true);
        mRoomsAdapter = new RoomsAdapter();
        rvRooms.setAdapter(mRoomsAdapter);

        mPresenter.fetchRooms(region);
    }

    @Override
    public void fetchRoomsError() {
        Log.d(TAG, "Error fetching rooms");
    }

    @Override
    public void fetchRoomsSuccess(ArrayList<Room> rooms) {
//        Log.d(TAG, rooms.toString());
        mRoomsAdapter.setData(rooms);
    }

    @Override
    public void fetchRegionsError(String message) {
        Log.d(TAG, "Error fetching regions");
    }

    @Override
    public void fetchRegionsSuccess(ArrayList<String> regions) {
        regionsArrayList = regions;

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext
// ()),
//                android.R.layout.simple_spinner_item, regionsArrayList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spRegion.setAdapter(dataAdapter);
//        spRegion.setSelection(regionsArrayList.indexOf("Library"));


        spRegion.setItems(regions);
        spRegion.setSelectedIndex(regionsArrayList.indexOf("Library"));

    }

}
