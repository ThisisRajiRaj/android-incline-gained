package com.example.verticalrise;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.verticalrise.databinding.FragmentInclinegainedBinding;

import java.text.DecimalFormat;


public class InclinedGainedFragment extends Fragment {

    private FragmentInclinegainedBinding binding;
    private  boolean usingMetric;
    String[] distanceUnits = { "miles", "meters" };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentInclinegainedBinding.inflate(inflater, container, false);
        binding.buttonCompute.setInputType(InputType.TYPE_NULL);
        Spinner spin = binding.ftOrMeters;
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    spin.setSelection(1);
                    usingMetric = true;
                }
                else {
                    spin.setSelection(0);
                    usingMetric = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                usingMetric = false;
            }
        });

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                distanceUnits);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.setAdapter(ad);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonCompute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ((MainActivity)getActivity()).closeKeyboard(view);
                }
            }
        });
        binding.buttonCompute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double grade = Double.parseDouble(binding.editTextGradePercent.getText().toString());
                    Double distance = Double.parseDouble(binding.editTextDistance.getText().toString());

                    double rise;
                    String resultToDisplay;
                    if (usingMetric) {
                        rise = (grade / 100.0) * (distance);
                        resultToDisplay = getString(R.string.rise_metre_result) + " : ";
                    } else {
                        rise = (grade / 100.0) * (distance * 5280.0);
                        resultToDisplay = getString(R.string.rise_feet_result) + " : ";
                    }

                    double adjustedRise = (Math.floor(rise * 100)) / 100;

                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    resultToDisplay += formatter.format(adjustedRise);
                    binding.resultsView.setText(resultToDisplay);
                    ((MainActivity) getActivity()).closeKeyboard(view);
                }
                catch (Exception e)
                {
                    binding.resultsView.setText("");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}