package com.example.proyectofinalandroid;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalandroid.data.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Panelcontrol extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvUsuario;
    private EditText etSearch;
    private PieChart pieChart;
    private DatabaseHelper databaseHelper;
    private String fechaSeleccionada;


    private RecyclerView recyclerView;
    private ResiduoAdapter residuoAdapter;
    private List<Residuo> listaResiduos;

    private Button btnGestionar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panelcontrol);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaResiduos = new ArrayList<>();

        recyclerView.setAdapter(residuoAdapter);

        btnGestionar = findViewById(R.id.btnGestionar);
        btnGestionar.setEnabled(false);


        etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v -> mostrarDatePicker());


        // Referencias a los elementos de la UI
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvUsuario = findViewById(R.id.tvUsuario);
        etSearch = findViewById(R.id.etSearch);
        pieChart = findViewById(R.id.pieChart);


        residuoAdapter = new ResiduoAdapter(this, listaResiduos, isSelected -> {
            if (isSelected) {
                btnGestionar.setEnabled(true);
                btnGestionar.setBackgroundColor(Color.RED);
            } else {
                btnGestionar.setEnabled(false);
                btnGestionar.setBackgroundColor(Color.parseColor("#80FF0000")); // Rojo transparente
            }
        });
        recyclerView.setAdapter(residuoAdapter);



        mostrarFechaHoraActual();

        // Obtener el usuario desde el intent
        String usuario = getIntent().getStringExtra("nombreUsuario");
        tvUsuario.setText("Usuario: " + (usuario != null && !usuario.isEmpty() ? usuario : "Desconocido"));

        // Configurar el DatePickerDialog al hacer clic en el campo de fecha
        etSearch.setOnClickListener(v -> mostrarSelectorDeFecha());

        // Establecer la fecha actual por defecto
        fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        etSearch.setText(fechaSeleccionada);
        cargarDatosEnPieChart(fechaSeleccionada);

        configurarPieChart();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void mostrarSelectorDeFecha() {
        final Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Formatear la fecha seleccionada
            fechaSeleccionada = year + "-" + String.format(Locale.getDefault(), "%02d", (month + 1)) + "-" +
                    String.format(Locale.getDefault(), "%02d", dayOfMonth);

            etSearch.setText(fechaSeleccionada);

            Log.d("Panelcontrol", "Fecha seleccionada: " + fechaSeleccionada); // Verificar si la fecha cambia

            // Cargar los datos del gráfico con la fecha seleccionada
            cargarDatosEnPieChart(fechaSeleccionada);
            cargarResiduosEnTabla(fechaSeleccionada);
        }, año, mes, dia);

        datePickerDialog.show();
    }


    private void configurarPieChart() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setRotationEnabled(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setExtraOffsets(5, 10, 5, 10);
        pieChart.setMaxAngle(360f);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextSize(12f);
    }

    private void cargarDatosEnPieChart(String fecha) {
        Log.d("Panelcontrol", "Cargando datos para la fecha: " + fecha); // Verificar fecha antes de la consulta

        Map<String, Float> residuosMap = databaseHelper.obtenerResiduosPorFecha(fecha);

        if (residuosMap.isEmpty()) {
            Log.d("Panelcontrol", "No hay datos para la fecha seleccionada: " + fecha);
            pieChart.setNoDataText("No hay datos registrados para esta fecha.");
            pieChart.clear();
            pieChart.invalidate();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : residuosMap.entrySet()) {
            Log.d("Panelcontrol", "Tipo: " + entry.getKey() + ", Peso: " + entry.getValue()); // Verificar datos obtenidos
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Tipos de Residuos");
        dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.MAGENTA, Color.CYAN});
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + " kg";
            }
        });

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refrescar gráfico
    }


    private void mostrarFechaHoraActual() {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String horaActual = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());

        tvFecha.setText("Fecha: " + fechaActual);
        tvHora.setText("Hora: " + horaActual);
    }


    private void mostrarDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = year + "-" + (month + 1) + "-" + dayOfMonth;
            etSearch.setText(fechaSeleccionada);
            cargarResiduosEnTabla(fechaSeleccionada); // Llamamos al método para actualizar la tabla
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void cargarResiduosEnTabla(String fecha) {
        List<Residuo> listaResiduos = databaseHelper.obtenerListaResiduosPorFecha(fecha);
        residuoAdapter.actualizarLista(listaResiduos);

        if (listaResiduos.isEmpty()) {
            Toast.makeText(this, "No hay registros para esta fecha", Toast.LENGTH_SHORT).show();
        }
    }




}
