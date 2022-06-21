package com.example.arcadefinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;
    public static final String TAG = "ProfileFragment";

    TextView tvProfileUsername;
    ImageView ivProfileImage;
    Button btnChangePfp;
    Button btnLogout;
    TextView tvYouAreGuest;

    private ParseUser currentUser = ParseUser.getCurrentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO: Is this good practice?
        if (ParseUser.getCurrentUser() == null) {
            return inflater.inflate(R.layout.fragment_guest, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_profile, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Is this good practice?
        if (ParseUser.getCurrentUser() == null) {
            // make the text 'log in' clickable
            tvYouAreGuest = view.findViewById(R.id.tvYouAreGuest);
            String s = getString(R.string.you_are_a_guest_log_in_to_see_your_profile);
            SpannableString ss = new SpannableString(s);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(@NonNull View widget) {
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            };

            ss.setSpan(clickableSpan, 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.link_blue)), 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new UnderlineSpan(), 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvYouAreGuest.setText(ss);
            tvYouAreGuest.setMovementMethod(LinkMovementMethod.getInstance());
            tvYouAreGuest.setHighlightColor(Color.TRANSPARENT);
            return;
        }

        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePfp = view.findViewById(R.id.btnChangePfp);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);

        tvProfileUsername.setText(currentUser.getUsername());

        getProfileImage();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                ParseUser.logOut();
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
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
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
    }

    private void getProfileImage() {
        ParseFile profileImg = currentUser.getParseFile("profileImage");
        if (profileImg != null) {
            Glide.with(getContext()).load(profileImg.getUrl()).placeholder(R.drawable.defaultpfp).into(ivProfileImage);
        } else {
            Glide.with(getContext()).load(R.drawable.defaultpfp).into(ivProfileImage);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    pickImageFromGallery();
                } else {
                    // permission was denied
                    Toast.makeText(getContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // set image
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String mCurrentPhotoPath = cursor.getString(columnIndex);
            cursor.close();
            File image = new File(mCurrentPhotoPath);
            ParseFile pf = new ParseFile(image);
            currentUser.put("profileImage", pf);
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error while saving", e);
                        Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getContext(), "Image saved successfully!", Toast.LENGTH_SHORT).show();
                    getProfileImage();
                }
            });
        }
    }
}