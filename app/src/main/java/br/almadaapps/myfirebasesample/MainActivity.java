package br.almadaapps.myfirebasesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.almadaapps.myfirebasesample.domain.User;

/**
 * Created by vinicius on 28/08/16.
 */
public class MainActivity extends CommonActivity {
    public static final String TAG = "MainActivity: ";

    private TextView text;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseUser mUser;

    private DatabaseReference mDatabaseReference;

    private ValueEventListener mValueEventListener;

//    private CustomValueEventListener mValueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
            }
        };

//        mValueEventListener = new CustomValueEventListener();
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                Toast.makeText(MainActivity.this, u.getNome(), Toast.LENGTH_SHORT).show();

                Log.i(TAG, "Nome: " + u.getNome());
                Log.i(TAG, "Email: " + u.getEmail());
                Log.i(TAG, "Cargo: " + u.getCargo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String data;
        if (mUser != null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            mDatabaseReference.addValueEventListener(mValueEventListener);
        } else {
            Toast.makeText(this, "User não logado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initViews() {
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null)
            mAuth.removeAuthStateListener(mAuthStateListener);
        if (mDatabaseReference != null)
            mDatabaseReference.removeEventListener(mValueEventListener);

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
