package br.almadaapps.myfirebasesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.almadaapps.myfirebasesample.domain.User;

/**
 * Created by vinicius on 28/08/16.
 */

public class CreateAccountActivity extends CommonActivity {
    public static final String TAG = "CreateAccountActivity: ";

    private EditText etNome;
    private EditText etCargo;
    private EditText etEmail;
    private EditText etPassword;

    private FirebaseAuth mFirebaseAuth;

    private User user;

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
                            if (fu != null) user.save(fu.getUid());
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
