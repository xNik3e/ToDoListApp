package com.example.todoplaceholder.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.kizitonwose.colorpreference.ColorPreference;
import com.kizitonwose.colorpreferencecompat.ColorPreferenceCompat;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

public class SettingsFragment extends PreferenceFragmentCompat {
    private MainViewModel mainViewModel;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);


        findPreference(getResources().getString(R.string.pref_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                showColorDialog(preference);
                return true;
            }
        });
    }

    private void showColorDialog(final Preference preference){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View colorView = inflater.inflate(R.layout.dialog_color, null);

        int color = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.pref_key), getResources().getColor(R.color.defaultColor));
        final LobsterPicker lobsterPicker = (LobsterPicker) colorView.findViewById(R.id.lobsterPicker);
        LobsterShadeSlider shadeSlider = (LobsterShadeSlider) colorView.findViewById(R.id.shadeSlider);
        final TextView resetButton = (TextView) colorView.findViewById(R.id.reset);

        lobsterPicker.addDecorator(shadeSlider);
        lobsterPicker.setColorHistoryEnabled(true);
        lobsterPicker.setHistory(color);
        lobsterPicker.setColor(color);

        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                .setView(colorView)
                .setTitle("Choose Color")
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ColorPreferenceCompat) preference).setValue(lobsterPicker.getColor());
                        mainViewModel.setBaseColor(lobsterPicker.getColor());
                    }
                })
                .setNegativeButton("CLOSE", null)
                .show();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lobsterPicker.setColor(getResources().getColor(R.color.defaultColorBackup));
            }
        });
    }
}
