package br.almadaapps.myfirebasesample.listeners;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.almadaapps.myfirebasesample.domain.User;

/**
 * Created by vinicius on 28/08/16.
 */

public class CustomValueEventListener implements ValueEventListener {
    public static final String TAG = "ValueEventListener: ";

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d : dataSnapshot.getChildren()) {
            User u = (User) d.getValue();

            Log.i(TAG, "Nome: " + u.getNome());
            Log.i(TAG, "Email: " + u.getEmail());
            Log.i(TAG, "Cargo: " + u.getCargo());

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
