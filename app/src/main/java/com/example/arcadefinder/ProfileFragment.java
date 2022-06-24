package com.example.arcadefinder;

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
//    ParseUser currentUser;
    ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePfp = view.findViewById(R.id.btnChangePfp);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);

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
//        profileViewModel.mutableLiveData.observe(getViewLifecycleOwner(), new Observer<ParseUser>() {
//            @Override
//            public void onChanged(ParseUser parseUser) {
//                // Any change in the live data, executed by getUser()
//                Log.i(TAG, "onChanged parseUser");
//                tvProfileUsername.setText(parseUser.getUsername());
//                ParseFile profileImg = parseUser.getParseFile("profileImage");
//                if (profileImg != null) {
//                    Glide.with(getContext()).load(profileImg.getUrl()).placeholder(R.drawable.defaultpfp).into(ivProfileImage);
//                } else {
//                    Glide.with(getContext()).load(R.drawable.defaultpfp).into(ivProfileImage);
//                }
//                currentUser = parseUser;
//            }
//        });
//        profileViewModel.getUser();
        profileViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String username) {
                Log.i(TAG, "onChanged username");
                tvProfileUsername.setText(username);
            }
        });
        profileViewModel.getPfp().observe(getViewLifecycleOwner(), new Observer<ParseFile>() {
            @Override
            public void onChanged(ParseFile parseFile) {
                Log.i(TAG, "onChanged parseFile");
                if (parseFile != null) {
                    if (parseFile.getUrl() != null) {
                        Log.i(TAG, "onChanged: parsefile url is : " + parseFile.getUrl());
                        Glide.with(getContext()).load(parseFile.getUrl()).placeholder(R.drawable.defaultpfp).into(ivProfileImage);
                    }
                } else {
                    Glide.with(getContext()).load(R.drawable.defaultpfp).into(ivProfileImage);
                }
            }
        });
    }

//    private void getProfileImage() {
//        ParseFile profileImg = currentUser.getParseFile("profileImage");
//        if (profileImg != null) {
//            Glide.with(getContext()).load(profileImg.getUrl()).placeholder(R.drawable.defaultpfp).into(ivProfileImage);
//        } else {
//            Glide.with(getContext()).load(R.drawable.defaultpfp).into(ivProfileImage);
//        }
//    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startCameraForResult.launch(intent);
    }

    // TODO: Is this good practice?
    ActivityResultLauncher<Intent> startCameraForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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