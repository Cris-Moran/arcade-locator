package com.example.arcadefinder.Fragments;

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
import android.widget.TextView;

import com.example.arcadefinder.Activities.LoginActivity;
import com.example.arcadefinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestFragment extends Fragment {

    TextView tvYouAreGuest;

    public GuestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}