package com.decibel.home.presenter;

import android.support.annotation.NonNull;

import com.decibel.home.HomeContract;
import com.decibel.model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegionPresenter implements HomeContract.RegionPresenter {

    private HomeContract.RegionView mRegionView;

    public RegionPresenter(HomeContract.RegionView regionView) {
        mRegionView = regionView;
    }

    @Override
    public void fetchRooms(String region) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations")
                .whereEqualTo("region", region)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Room> rooms = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Room room = doc.toObject(Room.class);
                                room.setId(doc.getId());
                                rooms.add(room);
                            }
                            mRegionView.fetchRoomsSuccess(rooms);
                        } else {
                            mRegionView.fetchRoomsError();
                        }
                    }
                });
    }

    @Override
    public void fetchRegions() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("regions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> regions = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                regions.add(doc.getId());
                            }
                            mRegionView.fetchRegionsSuccess(regions);
                        } else {
                            String message = "";
                            mRegionView.fetchRegionsError(message);
                        }
                    }
                });
    }
}
