package com.example.individualassignment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.individualassignment.database.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner monthSpinner;
    private EditText unitInput, rebateInput;
    private Button calculateButton, aboutButton;
    private ListView billListView;

    private DatabaseHelper db;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> billList;
    private ArrayList<Integer> billIds;

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure this matches your layout file name

        // Initialize Views
        monthSpinner = findViewById(R.id.spinnerMonth);
        unitInput = findViewById(R.id.editUnit);
        rebateInput = findViewById(R.id.editRebate);
        calculateButton = findViewById(R.id.btnCalculate);
        aboutButton = findViewById(R.id.btnAbout);
        billListView = findViewById(R.id.listBills);

        // Setup Spinner
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.months_array,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Initialize DB & List
        db = new DatabaseHelper(this);
        billList = new ArrayList<>();
        billIds = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, billList);
        billListView.setAdapter(listAdapter);

        // Calculate button click
        calculateButton.setOnClickListener(v -> calculateAndSave());

        // About button click
        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Click on bill to view detail
        billListView.setOnItemClickListener((parent, view, position, id) -> {
            int billId = billIds.get(position);
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
            detailIntent.putExtra("billId", billId);
            startActivity(detailIntent);
        });

        // Load previous bills from database
        loadBills();
    }

    private void calculateAndSave() {
        String month = monthSpinner.getSelectedItem().toString();
        String unitStr = unitInput.getText().toString().trim();
        String rebateStr = rebateInput.getText().toString().trim();

        if (unitStr.isEmpty() || rebateStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        int unit = Integer.parseInt(unitStr);
        double rebatePercent = Double.parseDouble(rebateStr);

        if (rebatePercent < 0 || rebatePercent > 5) {
            Toast.makeText(this, "Rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
            return;
        }

        double rebate = rebatePercent / 100;
        double total = calculateTotal(unit);
        double finalCost = total - (total * rebate);

        db.insertBill(month, unit, total, rebatePercent, finalCost);
        Toast.makeText(this, "Bill saved successfully.", Toast.LENGTH_SHORT).show();

        unitInput.setText("");
        rebateInput.setText("");
        loadBills();
    }


    private double calculateTotal(int unit) {
        double total = 0;
        if (unit <= 200) {
            total = unit * 0.218;
        } else if (unit <= 300) {
            total = (200 * 0.218) + ((unit - 200) * 0.334);
        } else if (unit <= 600) {
            total = (200 * 0.218) + (100 * 0.334) + ((unit - 300) * 0.516);
        } else {
            total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unit - 600) * 0.546);
        }
        return total;
    }

    private void loadBills() {
        billList.clear();
        billIds.clear();
        Cursor cursor = db.getAllBills();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String month = cursor.getString(1);
                double finalCost = cursor.getDouble(5);

                billList.add(month + " - RM " + df.format(finalCost));
                billIds.add(id);
            } while (cursor.moveToNext());
        }
        listAdapter.notifyDataSetChanged();
    }
}
