package com.example.proyectofinalandroid;

public class Residuo {
    private int id;
    private String nombreUsuario;
    private String tipo;
    private float peso;
    private String fecha;

    public Residuo(int id, String nombreUsuario, String tipo, float peso, String fecha) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.tipo = tipo;
        this.peso = peso;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getTipo() { return tipo; }
    public float getPeso() { return peso; }
    public String getFecha() { return fecha; }
}

