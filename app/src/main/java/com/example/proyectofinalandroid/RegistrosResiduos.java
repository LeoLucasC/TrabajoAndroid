package com.example.proyectofinalandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.proyectofinalandroid.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class RegistrosResiduos extends AppCompatActivity {

    private EditText etCantidadPapel, etCantidadPlastico, etCantidadVidrio, etCantidadOrganico,etCantidadTotal;
    private DatabaseHelper dbHelper;
    private String nombreUsuario; // Definir como variable de clase
    private String emailUsuario;
    private int usuario_id; // Esto debería obtenerse del usuario logueado




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registros_residuos);


        TextView tvWelcomeR = findViewById(R.id.tvWelcomeR);
        dbHelper = new DatabaseHelper(this);

        // Obtener datos del Intent
        emailUsuario = getIntent().getStringExtra("emailUsuario");

        Log.d("RegistrosResiduos", "📌 Email recibido en Intent: " + emailUsuario);

        if (emailUsuario != null && !emailUsuario.isEmpty()) {
            Map<String, String> usuarioData = dbHelper.obtenerUsuarioPorEmail(emailUsuario);
            usuario_id = Integer.parseInt(usuarioData.get("usuario_id"));
            nombreUsuario = usuarioData.get("nombreUsuario"); // Asignar a la variable de clase

            if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                tvWelcomeR.setText("Bienvenido, " + nombreUsuario);
            } else {
                tvWelcomeR.setText("Bienvenido, Usuario Desconocido");
            }
        } else {
            Log.e("RegistrosResiduos", "⚠ Error: emailUsuario es NULL. No se puede obtener usuario_id.");
        }


        Log.d("RegistrosResiduos", "📌 Usuario ID obtenido: " + usuario_id);





        etCantidadPapel = findViewById(R.id.etCantidadPapelR);
        etCantidadPlastico = findViewById(R.id.etCantidadPlastico);
        etCantidadVidrio = findViewById(R.id.etCantidadVidrio);
        etCantidadOrganico = findViewById(R.id.etCantidadOrganico);
        etCantidadTotal = findViewById(R.id.etCantidadR);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnPanelControl = findViewById(R.id.btnPanelControl);

        agregarTextWatcher(etCantidadPapel);
        agregarTextWatcher(etCantidadPlastico);
        agregarTextWatcher(etCantidadVidrio);
        agregarTextWatcher(etCantidadOrganico);





        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarResiduos();
            }
        });

        btnPanelControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrosResiduos.this, Panelcontrol.class);
                intent.putExtra("nombreUsuario", nombreUsuario); // Enviar nombre del usuario
                intent.putExtra("emailUsuario", emailUsuario); // Enviar email del usuario
                intent.putExtra("usuario_id", usuario_id); // Enviar el ID del usuario
                startActivity(intent);
            }
        });
    }

    private void registrarResiduos() {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        boolean seRegistro = false;

        seRegistro = insertarResiduoSiEsValido("Papel", etCantidadPapel, fechaActual) || seRegistro;
        seRegistro = insertarResiduoSiEsValido("Plástico", etCantidadPlastico, fechaActual) || seRegistro;
        seRegistro = insertarResiduoSiEsValido("Vidrio", etCantidadVidrio, fechaActual) || seRegistro;
        seRegistro = insertarResiduoSiEsValido("Orgánico", etCantidadOrganico, fechaActual) || seRegistro;

        if (seRegistro) {
            mostrarDialogoRegistroExitoso();
        } else {
            Toast.makeText(this, "No se ingresaron residuos válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean insertarResiduoSiEsValido(String tipo, EditText editText, String fecha) {
        String cantidadStr = editText.getText().toString().trim();

        if (cantidadStr.isEmpty()) {
            Log.e("Panelcontrol", "⚠ Error: El campo de cantidad está vacío.");
            return false;
        }

        try {
            float peso = Float.parseFloat(cantidadStr);
            Log.d("Panelcontrol", "✔ Peso convertido correctamente: " + peso);

            if (peso <= 0) {
                Log.e("Panelcontrol", "⚠ Error: El peso no puede ser 0 o negativo.");
                return false;
            }

            // Ya no necesitamos convertir la fecha aquí, porque ya es `yyyy-MM-dd`
            Log.d("Panelcontrol", "✅ Insertando residuo: Tipo=" + tipo + ", Peso=" + peso + "kg, Fecha=" + fecha);

            long resultado = dbHelper.insertarResiduo(usuario_id, tipo, peso, fecha);

            if (resultado == -1) {
                Log.e("Panelcontrol", "⚠ Error: No se pudo insertar el residuo en la BD.");
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            Log.e("Panelcontrol", "⚠ Error al convertir peso: " + e.getMessage());
        }

        return false;
    }


    private void mostrarDialogoRegistroExitoso() {
        // Inflar el diseño personalizado
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_registro_exitoso, null);

        // Referencia al ImageView dentro del diálogo
        ImageView gifImageView = dialogView.findViewById(R.id.gifImageView);

        // Cargar el GIF con Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.check) // Asegúrate de que el GIF está en res/drawable
                .into(gifImageView);

        // Construir el AlertDialog con el diseño personalizado
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView); // Usar la vista personalizada
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Solo cerrar el diálogo, sin cambiar de pantalla
            }
        });

        builder.setCancelable(false); // Evita que el usuario lo cierre tocando fuera
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Deseas cerrar sesión y volver a la pantalla de inicio de sesión?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Volver a la pantalla de Login
                Intent intent = new Intent(RegistrosResiduos.this, login.class);
                startActivity(intent);
                finish(); // Cierra esta actividad para que no pueda regresar
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // No hace nada, solo cierra el diálogo
            }
        });

        builder.setCancelable(false); // Evita que se cierre el diálogo al tocar fuera
        builder.show();
    }


    // Método para agregar un listener a cada EditText
    private void agregarTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarCantidadTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void actualizarCantidadTotal() {
        double papel = obtenerValorNumerico(etCantidadPapel);
        double plastico = obtenerValorNumerico(etCantidadPlastico);
        double vidrio = obtenerValorNumerico(etCantidadVidrio);
        double organico = obtenerValorNumerico(etCantidadOrganico);

        double sumaTotal = papel + plastico + vidrio + organico;

        // Mostrar el resultado en el campo de cantidad total
        etCantidadTotal.setText(String.valueOf(sumaTotal));
    }

    // Método para obtener un valor numérico seguro desde un EditText
    private double obtenerValorNumerico(EditText editText) {
        String texto = editText.getText().toString().trim();
        if (!texto.isEmpty()) {
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                return 0; // Si hay un error, retorna 0 en vez de crashear la app
            }
        }
        return 0; // Si el campo está vacío, retorna 0
    }


}