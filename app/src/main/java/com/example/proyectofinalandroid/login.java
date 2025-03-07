    package com.example.proyectofinalandroid;

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

    public class login extends AppCompatActivity {

        private EditText editTextEmail, editTextPassword;
        private Button loginButton;
        private TextView signUpPrompt;
        private DatabaseHelper databaseHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);




            editTextEmail = findViewById(R.id.editTextEmail);
            editTextPassword = findViewById(R.id.editTextPassword);
            loginButton = findViewById(R.id.loginButton);
            signUpPrompt = findViewById(R.id.signUpPrompt);


            databaseHelper = new DatabaseHelper(this);

            // Configurar el OnClickListener para el botón de inicio de sesión
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(login.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Verificar credenciales
                    boolean credencialesValidas = databaseHelper.verificarCredenciales(email, password);

                    if (credencialesValidas) {
                        // Obtener el nombre del usuario desde la base de datos
                        String nombreUsuario = databaseHelper.obtenerNombreUsuario(email);

                        // Enviar el nombre del usuario a la siguiente actividad
                        Intent intent = new Intent(login.this, RegistrosResiduos.class);
                        intent.putExtra("nombreUsuario", nombreUsuario); // Enviar nombre al intent
                        intent.putExtra("emailUsuario", email); // Enviar email al intent
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Email o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Configurar el OnClickListener para el TextView de registro
            signUpPrompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirigir al usuario a la actividad de registro
                    Intent intent = new Intent(login.this, Registro.class);
                    startActivity(intent);
                }
            });
        }
    }