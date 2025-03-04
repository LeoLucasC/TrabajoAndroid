package com.example.proyectofinalandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectofinalandroid.data.DatabaseHelper;

public class Registro extends AppCompatActivity {

    private EditText editTextNombre, editTextEmail, editTextPassword;
    private Button btnRegister;
    private TextView signUpPrompt;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        // Inicializar vistas
        editTextNombre = findViewById(R.id.etFullName);
        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        signUpPrompt = findViewById(R.id.signUpPrompt);

        // Inicializar DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Configurar el OnClickListener para el botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los campos de texto
                String nombre = editTextNombre.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String contrasena = editTextPassword.getText().toString().trim();

                // Verificar si los campos están vacíos
                if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insertar el usuario en la base de datos
                long resultado = databaseHelper.insertarUsuario(nombre, email, contrasena);

                if (resultado != -1) {
                    // Registro exitoso: mostrar un cuadro de diálogo
                    mostrarDialogoRegistroExitoso();
                } else {
                    // Error en el registro: mostrar un mensaje de error
                    Toast.makeText(Registro.this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signUpPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir al usuario a la actividad de inicio de sesión
                Intent intent = new Intent(Registro.this, login.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void mostrarDialogoRegistroExitoso() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registro exitoso");
        builder.setMessage("¡Tu cuenta ha sido creada con éxito!");
        builder.setIcon(R.drawable.comprobado);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Registro.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}