package com.example.arcadefinder.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.arcadefinder.R;

import java.util.ArrayList;

public class NotFoundDialogFragment extends DialogFragment {

    public interface NotFoundDialogListener {
        public void onItemSelected(String selectedItem);
    }

    NotFoundDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NotFoundDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        ArrayList<String> suggestions = bundle.getStringArrayList("suggestions");
        String[] finalSuggestions = suggestions.toArray(new String[suggestions.size()]);
        if (finalSuggestions.length > 0) {
            builder.setTitle("Did you mean?")
                    .setItems(finalSuggestions, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            String selectedItem = finalSuggestions[which];
                            listener.onItemSelected(selectedItem);
                        }
                    });
        } else {
            builder.setTitle("Game Not Found").setMessage("No suggestions found. Try another search term.");
        }
        return builder.create();
    }
}
