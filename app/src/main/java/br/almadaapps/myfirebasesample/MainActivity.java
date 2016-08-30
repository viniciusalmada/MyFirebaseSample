package br.almadaapps.myfirebasesample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import br.almadaapps.myfirebasesample.domain.User;

/**
 * Created by vinicius on 28/08/16.
 */
public class MainActivity extends CommonActivity {
    public static final String TAG = "MainActivity: ";

    private TextView text;
    private ImageView img;
    private TextView tvl;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseUser mUser;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mUsersRef = mRootRef.child(User.DB_NODE_USERS);

//    private CustomValueEventListener mValueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
//        String data;

    }

    @Override
    protected void initViews() {
        text = (TextView) findViewById(R.id.text);
        img = (ImageView) findViewById(R.id.imageView);
        tvl = (TextView) findViewById(R.id.tv_listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User u = dataSnapshot.child(mUser.getUid()).getValue(User.class);
                    String s = u.getNome() + "\n" + u.getEmail() + "\n" + u.getCargo();
                    text.setText(s);
                    StorageReference myRef = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(getString(R.string.firebase_storage_bucket))
                            .child(User.DB_NODE_USERS)
                            .child("images")
                            .child(mUser.getUid())
                            .child("/image.jpg");

                    /*final long HALF_MEGABYTE = 512 * 1024;
                    myRef.getBytes(HALF_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            img.setImageBitmap(bitmap);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                        @Override
                        public void onComplete(@NonNull Task<byte[]> task) {

                        }
                    });*/
                    final File localFile = new File(getCacheDir().getPath() + "/" + mUser.getUid() + ".png");
                    final FileDownloadTask downloadTask = myRef.getFile(localFile);
                    downloadTask.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            /*if (progress == 50){
                                downloadTask.pause();
                            }*/
                            tvl.setText("Download is " + progress + "% done");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                            img.setImageBitmap(bitmap);
                        }
                    });
                    /*myRef.getStream().addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeStream(taskSnapshot.getStream());
                            img.setImageBitmap(bitmap);
                        }
                    });*/
//                    bitmap = BitmapFactory.decodeFile(localFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null)
            mAuth.removeAuthStateListener(mAuthStateListener);
//        if (mDatabaseReference != null)
//            mDatabaseReference.removeEventListener(mValueEventListener);

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
