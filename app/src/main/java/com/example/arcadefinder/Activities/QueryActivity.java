package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.arcadefinder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;

public class QueryActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();

    AutoCompleteTextView etGameQuery;
    Spinner spinnerRadii;
    Button btnSubmitQuery;
    ArrayList<String> suggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        etGameQuery = findViewById(R.id.etGameQuery);
        spinnerRadii = findViewById(R.id.spinnerRadii);
        btnSubmitQuery = findViewById(R.id.btnSubmitQuery);

        // https://code.tutsplus.com/tutorials/how-to-add-a-dropdown-menu-in-android-studio--cms-37860
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.radii, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerRadii.setAdapter(adapter);

        btnSubmitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etGameQuery.equals("")) {
                    Toast.makeText(QueryActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(QueryActivity.this, MainActivity.class);
                i.putExtra("gameTitle", etGameQuery.getText().toString());

                // Convert string from drop down into an integer
                String radiusString = spinnerRadii.getSelectedItem().toString();
                int radius = Integer.parseInt(radiusString.split(" ")[0]);

                ArrayList<String> savedSuggestions = suggestions;

                i.putExtra("radius", radius);
                i.putExtra("suggestions", savedSuggestions);
                i.putExtra("querying", true);
                startActivity(i);
            }
        });

        etGameQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Replace spaces with %20 to make safe for links
                String searchQuery = s.toString().replace(" ", "%20");
                String wikiQueryURL = String.format("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s+incategory:Arcade_video_games&format=json", searchQuery);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(wikiQueryURL, new JsonHttpResponseHandler() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        suggestions.clear();
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONObject results = jsonObject.getJSONObject("query");
                            JSONArray search = results.getJSONArray("search");
                            int iterateLen = 5;
                            if (search.length() < 5) {
                                iterateLen = search.length();
                            }

                            for (int i = 0; i < iterateLen; i++) {
                                JSONObject item = search.getJSONObject(i);
                                String title = item.getString("title");
                                suggestions.add(title);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, suggestions);
                            etGameQuery.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception ", e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}