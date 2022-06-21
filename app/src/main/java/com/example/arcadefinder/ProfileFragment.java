package com.example.arcadefinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    ImageView ivProfileImage;
    Button btnChangePfp;
    Button btnLogout;

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
        if (ParseUser.getCurrentUser() != null) {

            ivProfileImage = view.findViewById(R.id.ivProfileImage);

            ParseFile profileImg = currentUser.getParseFile("profileImage");
            Glide.with(getContext()).load(profileImg.getUrl()).placeholder(R.drawable.defaultpfp).into(ivProfileImage);

            btnLogout = view.findViewById(R.id.btnLogout);

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
        }
    }

}