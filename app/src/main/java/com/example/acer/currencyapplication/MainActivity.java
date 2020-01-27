package com.example.acer.currencyapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etEnterCurrency;
    private String baseCurrency;
    private ArrayList<CurrencyItem> currencyList;
    private CurrencyAdapter adapter;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvCurrencies = findViewById(R.id.lvCurrencies);
        etEnterCurrency = findViewById(R.id.etEnterCurrency);
        Button btnSearch = findViewById(R.id.btn_search);
        currencyList = new ArrayList<>();

        adapter = new CurrencyAdapter(this, R.layout.listitem, currencyList);
        lvCurrencies.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseCurrency = etEnterCurrency.getText().toString();

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        getCurrencies();
                    }
                };

                Thread thread = new Thread(null, runnable, "background");
                thread.start();

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void getCurrencies(){
        final String url = "http://data.fixer.io/api/latest?access_key=d6702240f0bb4bdf0849deb4838813ea&base=";
        String urlWithbase = url.concat(TextUtils.isEmpty(baseCurrency) ? "USD" : baseCurrency);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, urlWithbase, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        try {
                            response = response.getJSONObject("rates");
                            currencyList.clear();

                            for (int i = 0; i < response.names().length(); i++) {
                                String key = response.names().getString(i);
                                double value = Double.parseDouble(response.get(response.names().getString(i)).toString());

                                currencyList.add(new CurrencyItem(key, value));
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                    }
                });
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
