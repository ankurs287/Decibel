package com.decibel.home.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.decibel.home.HomeContract;
import com.decibel.model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class NoisePresenter implements HomeContract.NoisePresenter {

    private HomeContract.NoiseView mNoiseView;

    public NoisePresenter(HomeContract.NoiseView noiseView) {
        mNoiseView = noiseView;
    }

    @Override
    public void fetchRoomsWithLeastNoise(int cap) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations")
                .orderBy("noiseStrength")
                .orderBy("vacancy", Query.Direction.DESCENDING)
                .limit(cap)
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
                            mNoiseView.fetchRoomsSuccess(rooms);
                        } else {
                            String message = Objects.requireNonNull(task.getException()).getMessage();
                            Log.d("FB", message);
                            mNoiseView.fetchRoomsError();
                        }
                    }
                });
    }
}
