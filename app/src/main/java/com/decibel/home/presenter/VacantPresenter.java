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

public class VacantPresenter implements HomeContract.VacantPresenter {

    private HomeContract.VacantView mVacantView;

    public VacantPresenter(HomeContract.VacantView vacantView) {
        mVacantView = vacantView;
    }

    @Override
    public void fetchRoomsWithMostVacantSeats(int cap) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations")
                .orderBy("vacancy", Query.Direction.DESCENDING)
                .orderBy("noiseStrength").limit(cap)
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
                            mVacantView.fetchRoomsSuccess(rooms);
                        } else {
                            String message = Objects.requireNonNull(task.getException()).getMessage();
                            Log.d("FB", message);
                            mVacantView.fetchRoomsError();
                        }
                    }
                });
    }
}
