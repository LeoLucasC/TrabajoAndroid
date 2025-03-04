package com.example.proyectofinalandroid.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.proyectofinalandroid.Residuo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    public boolean verificarCredenciales(String email, String contrasena) {
        boolean resultado = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);

            String query = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";
            cursor = db.rawQuery(query, new String[]{email, contrasena});

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

    public long insertarUsuario(String nombre, String email, String contrasena) {
        long resultado = -1;
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READWRITE);

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("email", email);
            values.put("contrasena", contrasena);

            resultado = db.insert("usuarios", null, values);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error insertando usuario: " + e.getMessage());
        } finally {
            if (db != null) db.close();
        }
        return resultado;
    }


    public long insertarResiduo(int usuarioId, String tipo, float peso, String fecha) {
        long resultado = -1;
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READWRITE);

            ContentValues values = new ContentValues();
            values.put("usuario_id", usuarioId);
            values.put("tipo", tipo);
            values.put("peso", peso);
            values.put("fecha", fecha);

            resultado = db.insert("residuos", null, values);

            if (resultado == -1) {
                Log.e("DatabaseHelper", "⚠ Error: No se pudo insertar el residuo.");
            } else {
                Log.d("DatabaseHelper", "✅ Residuo insertado con éxito. ID: " + resultado);
            }

        } catch (Exception e) {
            Log.e("DatabaseHelper", "⚠ Error insertando residuo: " + e.getMessage());
        } finally {
            if (db != null) db.close();
        }
        return resultado;
    }


    public String obtenerNombreUsuario(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String nombreUsuario = "Usuario"; // Valor por defecto en caso de error

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
            String query = "SELECT nombre FROM usuarios WHERE email = ?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                nombreUsuario = cursor.getString(0); // Obtener el nombre del usuario
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo el nombre del usuario: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return nombreUsuario;
    }

    public Map<String, Float> obtenerResiduosDelDia() {
        Map<String, Float> residuosMap = new HashMap<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);

            // Obtener la fecha actual en formato YYYY-MM-DD
            String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Query para obtener la suma del peso de cada tipo de residuo del día actual
            String query = "SELECT tipo, SUM(peso) as total FROM residuos WHERE fecha LIKE ? GROUP BY tipo";
            cursor = db.rawQuery(query, new String[]{fechaActual + "%"});

            while (cursor.moveToNext()) {
                String tipo = cursor.getString(0);
                float totalPeso = cursor.getFloat(1);
                residuosMap.put(tipo, totalPeso);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo residuos del día: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return residuosMap;
    }

    public Map<String, Float> obtenerResiduosPorFecha(String fecha) {
        Map<String, Float> residuosMap = new HashMap<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);

            Log.d("DatabaseHelper", "Consultando residuos para la fecha: " + fecha);

            // Asegurar que la fecha esté en formato correcto
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date date = formatoEntrada.parse(fecha);
                fecha = formatoSalida.format(date);
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error formateando fecha en consulta: " + e.getMessage());
            }

            String query = "SELECT tipo, SUM(peso) as total FROM residuos WHERE fecha = ? GROUP BY tipo";
            cursor = db.rawQuery(query, new String[]{fecha});

            while (cursor.moveToNext()) {
                String tipo = cursor.getString(0);
                float totalPeso = cursor.getFloat(1);
                residuosMap.put(tipo, totalPeso);
                Log.d("DatabaseHelper", "Tipo: " + tipo + ", Peso total: " + totalPeso);
            }

            if (residuosMap.isEmpty()) {
                Log.d("DatabaseHelper", "⚠ No se encontraron registros para la fecha: " + fecha);
            }

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo residuos por fecha: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return residuosMap;
    }

    public List<Residuo> obtenerListaResiduosPorFecha(String fecha) {
        List<Residuo> listaResiduos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);

            Log.d("DatabaseHelper", "📥 Consultando lista de residuos para la fecha: " + fecha);

            // Asegurar que la fecha esté en formato correcto
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date date = formatoEntrada.parse(fecha);
                fecha = formatoSalida.format(date);
            } catch (Exception e) {
                Log.e("DatabaseHelper", "⚠ Error formateando fecha en consulta: " + e.getMessage());
            }

            // Consulta SQL para traer los datos con el nombre del usuario
            String query = "SELECT r.id, u.nombre, r.tipo, r.peso, r.fecha " +
                    "FROM residuos r " +
                    "INNER JOIN usuarios u ON r.usuario_id = u.id " +
                    "WHERE r.fecha = ? " +
                    "ORDER BY r.id ASC";

            cursor = db.rawQuery(query, new String[]{fecha});

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String nombreUsuario = cursor.getString(1);
                String tipo = cursor.getString(2);
                float peso = cursor.getFloat(3);
                String fechaResiduo = cursor.getString(4);

                listaResiduos.add(new Residuo(id, nombreUsuario, tipo, peso, fechaResiduo));
            }

            if (listaResiduos.isEmpty()) {
                Log.d("DatabaseHelper", "⚠ No se encontraron registros para la fecha: " + fecha);
            } else {
                Log.d("DatabaseHelper", "✅ Se encontraron " + listaResiduos.size() + " residuos para la fecha: " + fecha);
            }

        } catch (Exception e) {
            Log.e("DatabaseHelper", "⚠ Error obteniendo lista de residuos por fecha: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return listaResiduos;
    }



}
