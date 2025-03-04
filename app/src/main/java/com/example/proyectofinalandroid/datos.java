package com.example.proyectofinalandroid;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class datos extends AppCompatActivity {

    private EditText etTotalResiduos, etCantidadPapel, etCantidadPlastico, etCantidadVidrio, etCantidadOrganico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos);



            etTotalResiduos = findViewById(R.id.etTotalResiduos);
            etCantidadPapel = findViewById(R.id.etCantidadPapel);
            etCantidadPlastico = findViewById(R.id.etCantidadPlastico);
            etCantidadVidrio = findViewById(R.id.etCantidadVidrio);
            etCantidadOrganico = findViewById(R.id.etCantidadOrganico);


            if (getIntent() != null) {
                String tipo = getIntent().getStringExtra("tipo");
                float peso = getIntent().getFloatExtra("peso", 0);

                // Asignar los valores a los campos correctos según el tipo
                switch (tipo) {
                    case "Papel":
                        etCantidadPapel.setText(String.valueOf(peso));
                        break;
                    case "Plástico":
                        etCantidadPlastico.setText(String.valueOf(peso));
                        break;
                    case "Vidrio":
                        etCantidadVidrio.setText(String.valueOf(peso));
                        break;
                    case "Orgánico":
                        etCantidadOrganico.setText(String.valueOf(peso));
                        break;
                }


                float totalResiduos = peso;
                etTotalResiduos.setText(String.valueOf(totalResiduos));

            }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}