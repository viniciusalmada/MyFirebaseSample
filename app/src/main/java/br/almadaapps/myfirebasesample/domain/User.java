package br.almadaapps.myfirebasesample.domain;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vinicius on 28/08/16.
 */

public class User {
    public static final String DB_NODE_USERS = "users";
    private String nome;
    private String cargo;
    private String email;
//    private String urlDownloadImage;

    public User() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getUrlDownloadImage() {
//        return urlDownloadImage;
//    }
//
//    public void setUrlDownloadImage(String urlDownloadImage) {
//        this.urlDownloadImage = urlDownloadImage;
//    }

    public void save(String uid) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child(DB_NODE_USERS).child(uid).setValue(this);
    }
}
