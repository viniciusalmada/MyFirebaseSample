package br.almadaapps.myfirebasesample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.almadaapps.myfirebasesample.domain.User;

/**
 * Created by vinicius on 28/08/16.
 */

public class CreateAccountActivity extends CommonActivity {
    public static final String TAG = "CreateAccountActivity: ";
    public static final String IMAGE_TYPE = "image/*";
    public static final int GET_IMAGE_CODE = 10;

    private EditText etNome;
    private EditText etCargo;
    private EditText etEmail;
    private EditText etPassword;
    private ImageView ivUserImage;

    private FirebaseAuth mFirebaseAuth;

    private User user;

    private InputStream mFileStream;

    private Uri mUriImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initViews();

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAuth != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }*/

    @Override
    protected void initViews() {
        etNome = (EditText) findViewById(R.id.et_name);
        etCargo = (EditText) findViewById(R.id.et_funcao);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE_CODE && resultCode == RESULT_OK) {
            Uri imagem_selected = data.getData();
            CropImage.activity(imagem_selected)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mUriImage = result.getUri();
                ivUserImage.setImageURI(mUriImage);
                try {
                    mFileStream = new FileInputStream(new File(mUriImage.getPath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                saveOriginalImage(resultUri, name_image_for_result);
//                Bitmap thumb = createThumbAndDeleteOriginalFile(name_image_for_result, header_image);

//                saveThumbImage(thumb, name_thumb_for_result);
                /*if (header_image) {
                    iv_bg_header_chooser.setImageBitmap(thumb);
                    profile.setPath_header_img(name_thumb_for_result);
                } else {
                    civ_profile.setImageBitmap(thumb);
                    profile.setPath_img(name_thumb_for_result);
                }*/
            }

        }
    }

    private void uploadFileToFirebase(String uid) {
        StorageReference fileReference = FirebaseStorage.getInstance().getReferenceFromUrl(getString(R.string.firebase_storage_bucket));
        StorageReference imagesRef = fileReference.child(User.DB_NODE_USERS).child("images");
        StorageReference individualImageRef = imagesRef.child(uid + "/image.jpg");
        /*Bitmap bitmap = BitmapFactory.decodeFile(saveOriginalImage(mUriImage, this.getFilesDir().getPath() + "/img.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();
        final String[] dowloadLink = new String[1];*/

        UploadTask uploadTask = individualImageRef.putStream(mFileStream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccountActivity.this, "Falha no upload da imagem", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    dowloadLink[0] = taskSnapshot.getDownloadUrl().toString();
            }
        });

//        Log.d(TAG, "uploadFileToFirebase: "+dowloadLink[0]);

//        return dowloadLink[0];
    }

    /*private String saveOriginalImage(Uri imagem, String path) {
        try {
            OutputStream outputStream = new FileOutputStream(path);
            InputStream inputStream = getBaseContext().getContentResolver().openInputStream(imagem);
            byte[] buffer = new byte[1024];
            int lenght;
            if (inputStream != null) {
                while ((lenght = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, lenght);
            }
            if (inputStream != null)
                inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }*/

    public void setImageProfile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(IMAGE_TYPE);
        startActivityForResult(intent, GET_IMAGE_CODE);
    }

    public void createAccount(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Log.d(TAG, "createAccount:" + email);

        showProgressDialog();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "CreateUserWithEmail:onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this, "Falha na operação", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();
                            saveUserData();
                            FirebaseUser fu = mFirebaseAuth.getCurrentUser();
                            if (fu != null) {
                                uploadFileToFirebase(fu.getUid());
//                                user.setUrlDownloadImage(uploadFileToFirebase(fu.getUid()));
                                user.save(fu.getUid());
                            }
                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void saveUserData() {
        user = new User();
        user.setNome(etNome.getText().toString());
        user.setCargo(etCargo.getText().toString());
        user.setEmail(etEmail.getText().toString());
    }
}
