package com.example.arcadefinder.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.Activities.AdminActivity;
import com.example.arcadefinder.Activities.LoginActivity;
import com.example.arcadefinder.Models.ProfileModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.ProfileViewModel;
import com.parse.ParseFile;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    TextView tvProfileUsername;
    ImageView ivProfileImage;
    Button btnChangePfp;
    Button btnLogout;
    Button btnAdminSettings;
    ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePfp = view.findViewById(R.id.btnChangePfp);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        btnAdminSettings = view.findViewById(R.id.btnAdminSettings);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                profileViewModel.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
                startActivity(i);
                getActivity().finish();
            }
        });

        btnChangePfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        // permission not granted
                        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                        requestPermissionForResult.launch(permission);
                    } else {
                        // permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    // system os less than marshmellow
                    pickImageFromGallery();
                }
            }
        });

        btnAdminSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AdminActivity.class);
                startActivity(i);
            }
        });

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile().observe(getViewLifecycleOwner(), new Observer<ProfileModel>() {
            @Override
            public void onChanged(ProfileModel profileModel) {
                String username = profileModel.getUsername();
                String url = profileModel.getProfileImagePath();
                ParseFile parseFile = profileModel.getProfileImage();
                boolean isAdmin = profileModel.getIsAdmin();

                if (isAdmin) {
                    btnAdminSettings.setVisibility(View.VISIBLE);
                    String adminUsername = username + " [admin]";
                    tvProfileUsername.setText(adminUsername);
                } else {
                    tvProfileUsername.setText(username);
                }

                if (parseFile != null && parseFile.getUrl() != null) {
                    // Loading image from database if exists
                    Log.i(TAG, "onChanged: parsefile is: " + parseFile.getUrl());
                    Glide.with(getContext()).load(parseFile.getUrl()).placeholder(R.drawable.defaultpfp).into(ivProfileImage);
                } else if (url == null) {
                    // Default image
                    Glide.with(getContext()).load(R.drawable.defaultpfp).into(ivProfileImage);
                } else {
                    Glide.with(getContext()).load(url).placeholder(R.drawable.defaultpfp).into(ivProfileImage);
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startGalleryForResult.launch(intent);
    }

    ActivityResultLauncher<Intent> startGalleryForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                // set image
                Intent data = result.getData();
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                File image = new File(mCurrentPhotoPath);
                ParseFile pf = new ParseFile(image);
                profileViewModel.setPfp(pf);
                profileViewModel.setUrl(mCurrentPhotoPath);
            }
        }
    });

    ActivityResultLauncher<String> requestPermissionForResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                // permission was granted
                pickImageFromGallery();
            } else {
                // permission was denied
                Toast.makeText(getContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
            }
        }
    });
}