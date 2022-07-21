package com.example.arcadefinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.arcadefinder.Models.QueryModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.QueryViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Headers;

public class QueryActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();

    AutoCompleteTextView etGameQuery;
    Spinner spinnerRadii;
    Button btnSubmitQuery;
    ArrayList<String> suggestions = new ArrayList<>();
    QueryViewModel queryViewModel;

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
                if (etGameQuery.getText().toString().equals("")) {
                    Toast.makeText(QueryActivity.this, "Please type a game", Toast.LENGTH_SHORT).show();
                    return;
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

        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        queryViewModel.getQueryModel().observe(this, new Observer<QueryModel>() {
            @Override
            public void onChanged(QueryModel queryModel) {
                JSONObject jsonObject = queryModel.getResponse();
                if (jsonObject != null) {
                    try {
                        suggestions.clear();
                        JSONObject results = jsonObject.getJSONObject("query");
                        JSONArray search = results.getJSONArray("search");
                        // Get 5 or less elements
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
                        e.printStackTrace();
                    }
                }
            }
        });

        etGameQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Replace spaces with %20 to make safe for links
                String encodeTitle = "";
                try {
                    encodeTitle = URLEncoder.encode(s.toString(), "UTF-8").replace("+", "%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String wikiQueryURL = String.format("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s+incategory:Arcade_video_games&format=json", encodeTitle);
                queryViewModel.getLocations(wikiQueryURL);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}