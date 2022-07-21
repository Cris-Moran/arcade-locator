package com.example.arcadefinder.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.UploadViewModel;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    public final String TAG = getClass().getSimpleName();

    AutoCompleteTextView etGame;
    EditText etDescription;
    AutocompleteSupportFragment fragmentAddress;
    ImageView ivGamePic;
    Button btnUploadPic;
    Button btnSubmitUpload;
    UploadViewModel uploadViewModel;
    File file;
    ParseGeoPoint coordinates;
    String locationName;
    String address;
    boolean isConnectedToNetwork;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etGame = view.findViewById(R.id.etGame);
        etDescription = view.findViewById(R.id.etDescription);
        fragmentAddress = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.fragmentAddress);
        ivGamePic = view.findViewById(R.id.ivGamePic);
        btnUploadPic = view.findViewById(R.id.btnUploadPic);
        btnSubmitUpload = view.findViewById(R.id.btnSubmitUpload);

        ivGamePic.setImageResource(R.drawable.placeholderimg);

        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        uploadViewModel.getUpload().observe(getViewLifecycleOwner(), new Observer<UploadModel>() {
            @Override
            public void onChanged(UploadModel uploadModel) {
                // https://stackoverflow.com/a/60285340
                coordinates = uploadModel.getCoordinates();
                locationName = uploadModel.getLocationName();
                address = uploadModel.getAddress();
                boolean submitted = uploadModel.isUploadStatus();
                boolean errorUploading = uploadModel.isErrorUploading();
                if (submitted) {
                    Toast.makeText(getContext(), "Request submitted!", Toast.LENGTH_SHORT).show();
                    // Reload current fragment
                    fragmentAddress.setText("");
                    etGame.setText("");
                    etDescription.setText("");
                    ivGamePic.setImageResource(R.drawable.placeholderimg);
                    file = null;
                } else if (errorUploading) {
                    Toast.makeText(getContext(), "Error while uploading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSubmitUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Cannot upload while offline", Toast.LENGTH_SHORT).show();
                    return;
                }
                String gameTitle = etGame.getText().toString();
                String description = etDescription.getText().toString();
                uploadViewModel.setUploadStatus(false);
                uploadViewModel.setErrorUploading(false);
                if (file == null || coordinates == null || gameTitle.equals("") || description.equals("")) {
                    Toast.makeText(getContext(), "Please fill out all fields before submitting", Toast.LENGTH_SHORT).show();
                } else {
                    ParseFile image = new ParseFile(file);
                    uploadViewModel.createUpload(coordinates, locationName, address, gameTitle, description, image);
                }
            }
        });


        etGame.addTextChangedListener(new TextWatcher() {
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
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONObject results = jsonObject.getJSONObject("query");
                            JSONArray search = results.getJSONArray("search");
                            int iterateLen = 5;
                            if (search.length() < 5) {
                                iterateLen = search.length();
                            }
                            ArrayList<String> suggestions = new ArrayList<>();
                            for (int i = 0; i < iterateLen; i++) {
                                JSONObject item = search.getJSONObject(i);
                                String title = item.getString("title");
                                suggestions.add(title);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, suggestions);
                            etGame.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
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

        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchCameraForResult.launch(takePicIntent);
            }
        });

        uploadViewModel.initPlaces();

        fragmentAddress.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        uploadViewModel.getPlace(fragmentAddress);
    }

    ActivityResultLauncher<Intent> launchCameraForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();
            Intent data = result.getData();
            if (data != null && resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extra.get("data");
                ivGamePic.setImageBitmap(imageBitmap);
                File f = new File(getContext().getCacheDir(), "game.png");
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitMapData = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fos.write(bitMapData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file = f;
            }
        }
    });

    @SuppressWarnings("MissingPermission")
    // https://stackoverflow.com/a/57284789
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

}
