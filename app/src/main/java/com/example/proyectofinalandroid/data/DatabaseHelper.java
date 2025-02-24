package com.example.proyectofinalandroid.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;

public class DatabaseHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "ResiduosDB.db";
    private final Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    // Método para verificar si la base de datos existe
    public boolean baseDeDatosExiste() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    // Método para verificar la conexión a la base de datos
    public boolean verificarConexion() {
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
            db.close();
            Log.d("DatabaseHelper", "Conexión exitosa a la base de datos.");
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    // Método para verificar credenciales del usuario en la base de datos
    public boolean verificarCredenciales(String usuario, String contrasena) {
        boolean resultado = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);

            String query = "SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?";
            cursor = db.rawQuery(query, new String[]{usuario, contrasena});

            if (cursor.getCount() > 0) {
                resultado = true; // Usuario encontrado
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error verificando credenciales: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return resultado;
    }
}
