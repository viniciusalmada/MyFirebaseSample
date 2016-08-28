package br.almadaapps.myfirebasesample.domain;

/**
 * Created by vinicius on 28/08/16.
 */

public class User {
    private String nome;
    private String igreja;
    private String cargo;
    private String email;
    private String password;
    private String id;

    public User() {
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setIgreja(String igreja) {
        this.igreja = igreja;
    }

    public String getIgreja() {
        return igreja;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
