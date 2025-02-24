package com.example.proyectofinalandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinalandroid.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etUsuario, etContrasena;
    private Button btnIngresar, btnCrearUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Instancia del helper de la base de datos
        dbHelper = new DatabaseHelper(this);

        // Enlazar campos de entrada
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);

        // Enlazar botones
        btnIngresar = findViewById(R.id.btnIngresar);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);

        // Evento para ingresar
        btnIngresar.setOnClickListener(view -> {
            String usuario = etUsuario.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.verificarCredenciales(usuario, contrasena)) {
                    Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Usuario autenticado correctamente.");
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error: Usuario o contraseña incorrectos.");
                }
            }
        });

        // Evento para crear usuario (Aún no implementado)
        btnCrearUsuario.setOnClickListener(view -> {
            Toast.makeText(this, "Función de creación de usuario aún no implementada", Toast.LENGTH_SHORT).show();
        });
    }
}
