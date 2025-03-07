package com.example.proyectofinalandroid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.proyectofinalandroid.data.DatabaseHelper;

public class datos extends AppCompatActivity {

    private EditText etTotalID, etusuario, ettipo, etpeso, etfecha;
    private Button btnEliminar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos);

        dbHelper = new DatabaseHelper(this);

        // Inicializar los campos de texto
        etTotalID = findViewById(R.id.etID);
        etusuario = findViewById(R.id.etusuario);
        ettipo = findViewById(R.id.etTipo);
        etpeso = findViewById(R.id.etpeso);
        etfecha = findViewById(R.id.etfecha);


        // Verificar si hay datos en el Intent
        if (getIntent() != null) {
            // Obtener los datos del Intent
            int id = getIntent().getIntExtra("id", -1);
            String usuario = getIntent().getStringExtra("usuario");
            String tipo = getIntent().getStringExtra("tipo");
            float peso = getIntent().getFloatExtra("peso", 0);
            String fecha = getIntent().getStringExtra("fecha");

            // Asignar los valores a los campos de texto
            etTotalID.setText(String.valueOf(id)); // Mostrar el ID
            etusuario.setText(usuario); // Mostrar el nombre del usuario
            ettipo.setText(tipo); // Mostrar el tipo de residuo
            etpeso.setText(String.valueOf(peso)); // Mostrar el peso
            etfecha.setText(fecha); // Mostrar la fecha
        }



        // Configurar el padding para la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}