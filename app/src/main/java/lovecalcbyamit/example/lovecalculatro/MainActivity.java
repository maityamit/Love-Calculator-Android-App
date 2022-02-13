package lovecalcbyamit.example.lovecalculatro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.text.TextRunShaper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {



    EditText editText1,editText2;
    Button button;
    PieChart pieChart;
    ProgressBar progressBar;
    TextView textView;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText1 = findViewById(R.id.edit_text_trip_name1);
        editText2 = findViewById(R.id.edit_text_trip_name2);
        button = findViewById(R.id.calculate);
        pieChart = findViewById(R.id.piechart1);
        cardView = findViewById(R.id.cardview);
        progressBar = findViewById(R.id.progress_circular);
        textView = findViewById(R.id.desc_goal_const);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string1 = editText1.getText().toString();
                String string2 = editText2.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(string1) && !TextUtils.isEmpty(string2)){
                    fetchData(string1,string2);
                }

            }
        });


    }

    private void fetchData(String string1, String string2) {



        String url = "https://love-calculator.p.rapidapi.com/getPercentage?sname="+string1+"&fname="+string2;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "8215e9e382msh411551c64217c12p1fe5adjsn276704f44ec2")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, "Some eror !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()){
                    String resp = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                String val1 =  jsonObject.getString("percentage");
                                String val2 = jsonObject.getString("result");

                                cardView.setVisibility(View.VISIBLE);
                                pieChart.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textView.setText(val2);
                                int i = Integer.parseInt(val1);
                                pieChart.addPieSlice(new PieModel("Done", i, Color.parseColor("#FF2E7D32")));
                                pieChart.addPieSlice(new PieModel("Not Done", (100-i), Color.parseColor("#FFC62828")));

                                pieChart.startAnimation();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });


    }
}