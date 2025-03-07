package com.example.proyectofinalandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CreateDatabase extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "ResiduosDB.db";
    private static final int DATABASE_VERSION = 1;

    // Sentencia SQL para crear la tabla de usuarios
    private static final String CREATE_TABLE_USUARIOS =
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "contrasena TEXT NOT NULL);";

    // Sentencia SQL para crear la tabla de residuos
    private static final String CREATE_TABLE_RESIDUOS =
            "CREATE TABLE residuos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "usuario_id INTEGER NOT NULL, " +
                    "tipo TEXT NOT NULL, " +
                    "peso REAL NOT NULL, " +
                    "fecha TEXT NOT NULL, " +
                    "FOREIGN KEY (usuario_id) REFERENCES usuarios(id));";

    // Constructor
    public CreateDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Crear las tablas
            db.execSQL(CREATE_TABLE_USUARIOS);
            db.execSQL(CREATE_TABLE_RESIDUOS);
            Log.d("CreateDatabase", "✅ Base de datos creada correctamente.");
        } catch (Exception e) {
            Log.e("CreateDatabase", "⚠ Error al crear la base de datos: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes manejar las actualizaciones de la base de datos
        Log.d("CreateDatabase", "Actualizando base de datos de la versión " + oldVersion + " a " + newVersion);

        // Eliminar las tablas existentes (si es necesario)
        db.execSQL("DROP TABLE IF EXISTS residuos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");

        // Volver a crear las tablas
        onCreate(db);
    }
}