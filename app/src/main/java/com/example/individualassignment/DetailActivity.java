package com.example.individualassignment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.individualassignment.database.DatabaseHelper;
import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    private TextView txtDetail;
    private Button btnBack;
    private DatabaseHelper db;
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtDetail = findViewById(R.id.txtDetail);
        btnBack = findViewById(R.id.btnBack);
        db = new DatabaseHelper(this);

        int billId = getIntent().getIntExtra("billId", -1);
        if (billId != -1) loadDetails(billId);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Simply go back to previous screen
            }
        });
    }

    private void loadDetails(int id) {
        Cursor cursor = db.getAllBills();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == id) {
                String text = "Month: " + cursor.getString(1) +
                        "\nUnit Used: " + cursor.getInt(2) +
                        "\nTotal Charges: RM " + df.format(cursor.getDouble(3)) +
                        "\nRebate: " + df.format(cursor.getDouble(4)) + "%" +
                        "\nFinal Cost: RM " + df.format(cursor.getDouble(5));
                txtDetail.setText(text);
                break;
            }
        }
    }
}
