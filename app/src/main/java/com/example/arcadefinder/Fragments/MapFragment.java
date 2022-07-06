package com.example.arcadefinder.Fragments;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public final String TAG = getClass().getSimpleName();
    public static final double DEFAULT_RADIUS = 50;

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
                        String locationName = location.getLocationName();
                        String address = location.getAddress();
                        placeMarker(lat, lng, locationName, address);
                    }
                    Toast.makeText(getContext(), "Done with search!", Toast.LENGTH_SHORT).show();
                    locationsToDisplay.clear();
                }
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

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QueryActivity.class);
                startActivity(i);
            }
        });
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

    public void placeMarker(double lat, double lng, String locationName, String address) {
        LatLng markerLocation = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(markerLocation).title(locationName).snippet(address));
    }

    public void checkPermissions() {
        // TODO: Should I check for both permissions? Or only one?
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No location permissions, need to request them
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestLocation.launch(permissions);
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

    ActivityResultLauncher<String[]> requestLocation = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
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