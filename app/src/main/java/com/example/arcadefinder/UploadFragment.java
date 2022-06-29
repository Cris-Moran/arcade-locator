package com.example.arcadefinder;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.Models.UploadModel;
import com.example.arcadefinder.ViewModels.UploadViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    public final String TAG = getClass().getSimpleName();

    EditText etGame;
    EditText etDescription;
    AutocompleteSupportFragment fragmentAddress;
    ImageView ivGamePic;
    Button btnUploadPic;
    Button btnSubmitUpload;
    UploadViewModel uploadViewModel;
    File file;
    static LatLng coordinates;
    static String locationName;
    static String address;

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

        Glide.with(getContext()).load(R.drawable.placeholderimg).into(ivGamePic);

        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        uploadViewModel.getUpload().observe(getViewLifecycleOwner(), new Observer<UploadModel>() {
            @Override
            public void onChanged(UploadModel uploadModel) {
                if (uploadModel.getCoordinates() != null && uploadModel.getLocationName() != null
                        && uploadModel.getAddress() != null && uploadModel.getTitle() != null
                        && uploadModel.getDescription() != null && uploadModel.getImage() != null
                        && uploadModel.getAuthor() != null) {
                    Toast.makeText(getContext(), "Request submitted!", Toast.LENGTH_SHORT).show();
                    resetAddressVariables();
                }
            }
        });

        btnSubmitUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameTitle = etGame.getText().toString();
                String description = etDescription.getText().toString();
                ParseFile image = new ParseFile(file);
                ParseGeoPoint location = new ParseGeoPoint(coordinates.latitude, coordinates.longitude);

                uploadViewModel.createUpload(location, locationName, address, gameTitle, description, image);
            }
        });

        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchCameraForResult.launch(takePicIntent);
            }
        });

        uploadViewModel.initPlaces(getContext());
        uploadViewModel.initSearchBar(fragmentAddress);
    }

    private void resetAddressVariables() {
        coordinates = null;
        locationName = null;
        address = null;
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
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fos.write(bitmapdata);
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

    public static void setCoordinates(LatLng latLng) {
        coordinates = latLng;
    }

    public static void setLocationName(String string) {
        locationName = string;
    }

    public static void setAddress(String string) {
        address = string;
    }
}
