package com.example.arcadefinder.Fragments;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.arcadefinder.Activities.GameInfoActivity;
import com.example.arcadefinder.Activities.QueryActivity;
import com.example.arcadefinder.Adapters.CustomWindowAdapter;
import com.example.arcadefinder.GameLocation;
import com.example.arcadefinder.Models.MapModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public final String TAG = getClass().getSimpleName();

    private GoogleMap map;
    private Location currentLocation;
    private LatLngBounds bounds;
    FusedLocationProviderClient fusedLocationProviderClient;
    SearchView searchView;
    MapViewModel mapViewModel;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchView);

        fusedLocationProviderClient = getFusedLocationProviderClient(getContext());

        View.OnClickListener searchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QueryActivity.class);
                startActivity(i);
            }
        };

        searchView.setOnClickListener(searchListener);

        // hack: make entire search bar clickable
        EditText searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setOnClickListener(searchListener);
        searchText.setFocusable(false);
        searchText.setFocusableInTouchMode(false);
        searchText.setCursorVisible(false);

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getMapModel().observe(getViewLifecycleOwner(), new Observer<MapModel>() {
            @Override
            public void onChanged(MapModel mapModel) {
                List<GameLocation> locationsToDisplay = mapModel.getLocationList();
                if (!locationsToDisplay.isEmpty() && mapModel.getLocationPermission()) {
                    for (GameLocation location : locationsToDisplay) {
                        ParseGeoPoint coordinates = location.getCoordinates();
                        double lat = coordinates.getLatitude();
                        double lng = coordinates.getLongitude();
                        String locationName = location.getTitle();
                        String address = location.getAddress();
                        placeMarker(lat, lng, locationName, address);
                    }
                    // Convert meters to miles
                    double miRadius = mapModel.getRadius();
                    double mtrRadius = miRadius * 1609.34;

                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    map.addCircle(new CircleOptions().center(latLng).radius(mtrRadius).strokeWidth(5).strokeColor(Color.BLUE).fillColor(0x220000FF));
                    Toast.makeText(getContext(), "Done with search!", Toast.LENGTH_SHORT).show();
                }
                searchText.setText(mapModel.getQuery());
            }
        });

        ImageView closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                searchText.setText("");
            }
        });

        String searchQuery = "DDR%20Extreme";
        String wikiQueryURL = String.format("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s&format=json", searchQuery);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(wikiQueryURL, new JsonHttpResponseHandler() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject results = jsonObject.getJSONObject("query");
                    JSONArray search = results.getJSONArray("search");
                    for (int i = 0; i < search.length(); i++) {
                        JSONObject item = search.getJSONObject(i);
                        String title = item.getString("title");
                        Log.i(TAG, "onSuccess: title: " + title);
                    }
                    Log.i(TAG, "onSuccess: search object: " + search);
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(MapFragment.this);

        // https://stackoverflow.com/a/36787406
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        checkPermissions();

        map.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
        
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Intent i = new Intent(getContext(), GameInfoActivity.class);
                i.putExtra("title", marker.getTitle());
                i.putExtra("address", marker.getSnippet());
                startActivity(i);
            }
        });
    }

    /**
     * Only call if permission was granted
     */
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        // TODO: Move to a Repo
        // TODO: Do I have to write this twice?

        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getContext());
        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
                        map.moveCamera(cameraUpdate);
                        onLocationChanged(location);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("MapFragment", "Error trying to get last GPS location");
                    e.printStackTrace();
                });
    }

    public void onLocationChanged(Location location) {
        // GPS may be turned off
        if (location == null) {
            return;
        }
        else if (currentLocation != location) {
            currentLocation = location;
            displayLocation();
        }
        if (bounds == null) { //initialize
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            bounds = new LatLngBounds(latLng, latLng);
        }

        // Now that we have location, can search for markers
        Bundle bundle = getArguments();
        if (bundle != null) {
            ParseGeoPoint currCoordinates = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            String gameTitle = bundle.getString("gameTitle");
            int radius = bundle.getInt("radius");
            mapViewModel.queryLocations(gameTitle, radius, currCoordinates);
            Toast.makeText(getContext(), "Searching..", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayLocation() {
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            map.moveCamera(cameraUpdate);
        }
    }

    public Marker placeMarker(double lat, double lng, String gameTitle, String address) {
        LatLng markerLocation = new LatLng(lat, lng);
        return map.addMarker(new MarkerOptions().position(markerLocation).title(gameTitle).snippet(address));
    }

    public void checkPermissions() {
        // TODO: Should I check for both permissions? Or only one?
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No location permissions, need to request them
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestLocationPermission.launch(permissions);
        } else {
            // Already have permission
            mapViewModel.setLocationPermission(true);
            getCurrentLocation();
        }
    }

    /**
     * Call if permission was not granted
     */
    private void getDefaultLocation() {
        LatLng sydney = new LatLng(-33.852, 151.211);
        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    ActivityResultLauncher<String[]> requestLocationPermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            Log.i(TAG, "onActivityResult: " + result);
            if (result.get(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.i(TAG, "onActivityResult: got location permission");
                mapViewModel.setLocationPermission(true);
                getCurrentLocation();
            } else {
                // Permission was denied
                Toast.makeText(getContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
                mapViewModel.setLocationPermission(false);
                getDefaultLocation();
            }
        }
    });
}