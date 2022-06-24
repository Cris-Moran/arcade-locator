package com.example.arcadefinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    EditText etGame;
    EditText etAddress;
    ImageView ivGamePic;
    Button btnUploadPic;
    Button btnSubmitUpload;

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
        etAddress = view.findViewById(R.id.etAddress);
        ivGamePic = view.findViewById(R.id.ivGamePic);
        btnUploadPic = view.findViewById(R.id.btnUploadPic);
        btnSubmitUpload = view.findViewById(R.id.btnSubmitUpload);

        btnSubmitUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Request submitted!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}