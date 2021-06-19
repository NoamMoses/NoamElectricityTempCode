package com.noam.noamelectricity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.noam.noamelectricity.functions.helperFunctions;
import com.noam.noamelectricity.Shapse.band;
import com.noam.noamelectricity.functions.SystemVariables;

public class ResistanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    /*
    * Resistor colour codes calculator
    * */

    // TODO If I want to use ohm sign-symbol,then it is here Ω


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resistance, container, false);
    }

    /* Setting all of the lists */
    List<String> colours_list = new ArrayList<>(), // Colours
            multipliers_list = new ArrayList<>(), // multipliers
            tolerance_list = new ArrayList<>(); // Tolerances
    Spinner spinner_firstDigit, spinner_secondDigit, spinner_thirdDigit, spinner_multiplier, spinner_tolerance; // The spinners' lists
    RadioButton radio_resistorType_4band, radio_resistorType_5band; // Resistors types
    RadioGroup chooseType;
    TextView result; // The final result
    band band_firstDigit, band_secondDigit, band_thirdDigit, band_multiplier, band_tolerance; // Shapes

    int chosenResistorBands = 5; // Resistor type - number of bands

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /* Basic things */
        result = view.findViewById(R.id.res_result);


        /* Setup for the spinners */
        // Views
        spinner_firstDigit = view.findViewById(R.id.spinner_band_firstDigit);
        spinner_secondDigit = view.findViewById(R.id.spinner_band_secondDigit);
        spinner_thirdDigit = view.findViewById(R.id.spinner_band_thirdDigit);
        spinner_multiplier = view.findViewById(R.id.spinner_band_multiplier);
        spinner_tolerance = view.findViewById(R.id.spinner_band_tolerance);
        // Setting the spinners' listeners to be this class
        spinner_firstDigit.setOnItemSelectedListener(this);
        spinner_secondDigit.setOnItemSelectedListener(this);
        spinner_thirdDigit.setOnItemSelectedListener(this);
        spinner_multiplier.setOnItemSelectedListener(this);
        spinner_tolerance.setOnItemSelectedListener(this);


        // Radio button - resistor 4\5 bands types
        radio_resistorType_4band = view.findViewById(R.id.resistors_chooseType_4band);
        radio_resistorType_5band = view.findViewById(R.id.resistors_chooseType_5band);
        radio_resistorType_4band.setText(String.format(getResources().getString(R.string.resistors_bands), 4));
        radio_resistorType_5band.setText(String.format(getResources().getString(R.string.resistors_bands), 5));
        /* To choose the 5-bands as default */
        // TODO right now the inputs are being changed to 5-bands, but the demo-resistor is 4-bands style
        radio_resistorType_5band.performClick();
        chooseType = view.findViewById(R.id.chooseType); // RadioGroup
        chooseType.check(radio_resistorType_5band.getId());


        // Demo-resistor shapes
        band_firstDigit = view.findViewById(R.id.band_firstDigit);
        band_secondDigit = view.findViewById(R.id.band_secondDigit);
        band_thirdDigit = view.findViewById(R.id.band_thirdDigit);
        band_multiplier = view.findViewById(R.id.band_multiplier);
        band_tolerance = view.findViewById(R.id.band_tolerance);


        /* Init the lists */
        // Colours - for first 3 digits
        colours_list.add("Black 0");
        colours_list.add("Brown 1");
        colours_list.add("Red 2");
        colours_list.add("Orange 3");
        colours_list.add("Yellow 4");
        colours_list.add("Green 5");
        colours_list.add("Blue 6");
        colours_list.add("Violet 7");
        colours_list.add("Grey 8");
        colours_list.add("White 9");
        // Multiplier
        multipliers_list.add("Black 1");
        multipliers_list.add("Brown 10");
        multipliers_list.add("Red 100");
        multipliers_list.add("Orange 1K");
        multipliers_list.add("Yellow 10K");
        multipliers_list.add("Green 100K");
        multipliers_list.add("Blue 1M");
        multipliers_list.add("Violet 10M");
        multipliers_list.add("Grey 100M");
        multipliers_list.add("White 1G");
        multipliers_list.add("Gold 0.1");
        multipliers_list.add("Silver 0.01");
        // Tolerance
        tolerance_list.add("Brown 1%");
        tolerance_list.add("Red 2%");
        tolerance_list.add("Green 0.5%");
        tolerance_list.add("Blue 0.25%");
        tolerance_list.add("Violet 0.1%");
        tolerance_list.add("Grey 0.05%");
        tolerance_list.add("Gold 5%");
        tolerance_list.add("Silver 10%");


        Context context = getContext();
        if (context != null) {
            // Creating the adapters - each adapter is one type of options
            ArrayAdapter<String> adapter_colours = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, colours_list),
                    adapter_multipliers = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, multipliers_list),
                    adapter_tolerance = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, tolerance_list);
            /* Setting the adapters - each spinner and its values */
            spinner_firstDigit.setAdapter(adapter_colours);
            spinner_secondDigit.setAdapter(adapter_colours);
            spinner_thirdDigit.setAdapter(adapter_colours);
            spinner_multiplier.setAdapter(adapter_multipliers);
            spinner_tolerance.setAdapter(adapter_tolerance);
        } else {
            Log.d("ERR | Resistor-Fragment", SystemVariables.ERR_NULL_CONTEXT);
        }

        /* When the radio button of 4/5 bands is being changed */
        chooseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            final LinearLayout layout_multiplier = view.findViewById(R.id.res_layout_band_multiplier), // Layout to be changed
                    layout_thirdDigit = view.findViewById(R.id.res_layout_band_thirdDigit); // Layout to be shown/hidden
            final RelativeLayout.LayoutParams multiplierLayout_params = (RelativeLayout.LayoutParams) layout_multiplier.getLayoutParams(); // Contains the new params
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radio_resistorType_4band.getId()) { // 4-bands resistor
                    chosenResistorBands = 4;
                    layout_thirdDigit.setVisibility(View.INVISIBLE);
                    multiplierLayout_params.addRule(RelativeLayout.BELOW, R.id.res_layout_band_secondDigit);
                    layout_multiplier.setLayoutParams(multiplierLayout_params);
                    band_thirdDigit.setVisibility(View.GONE);
                } else if (checkedId == radio_resistorType_5band.getId()) { // 5-bands resistor
                    chosenResistorBands = 5;
                    layout_thirdDigit.setVisibility(View.VISIBLE);
                    multiplierLayout_params.addRule(RelativeLayout.BELOW, R.id.res_layout_band_thirdDigit);
                    layout_multiplier.setLayoutParams(multiplierLayout_params);
                    band_thirdDigit.setVisibility(View.VISIBLE);
                }
                updateResult();
            }
        });
    }

    int basicNumber = 0; // The "built-up" number. For example if the chosen digits are 1, 2, 3 this variable will be 123
    double multiplierNum = 0.0, // The multiplier number. Needs to be double because there are also 0.1 and 0.01
        basicNumber_multiplied = 0; // The basic number multiplied by the multiplier

    // Inputs' values
    String firstDigit = "0", secondDigit = "0", thirdDigit = "0", multiplier = "0", tolerance = "0";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*
        * Explanation:
        *   The function is being called whenever I choose something, from any list
        *   - There isn't any else statement, since it can't happen
        * */
        if (parent.getId() == R.id.spinner_band_firstDigit) { // First digit
            Log.d("CLICKED", "First digit, colour chose is " + colours_list.get(position));
            firstDigit = colours_list.get(position).split(" ")[1];
            band_firstDigit.setColour(Color.parseColor(indexToColorID(colours_list.get(position))));
        } else if (parent.getId() == R.id.spinner_band_secondDigit) { // Second digit
            Log.d("CLICKED", "Second digit, colour chose is " + colours_list.get(position));
            secondDigit = colours_list.get(position).split(" ")[1];
            band_secondDigit.setColour(Color.parseColor(indexToColorID(colours_list.get(position))));
        } else if (parent.getId() == R.id.spinner_band_thirdDigit) { // Third digit
            Log.d("CLICKED", "Second digit, colour chose is " + colours_list.get(position));
            thirdDigit = colours_list.get(position).split(" ")[1];
            band_thirdDigit.setColour(Color.parseColor(indexToColorID(colours_list.get(position))));
        } else if (parent.getId() == R.id.spinner_band_multiplier) { // Multiplier
            Log.d("CLICKED", "Multiplier, colour chose is " + multipliers_list.get(position));
            multiplier = multipliers_list.get(position).split(" ")[1];
            band_multiplier.setColour(Color.parseColor(indexToColorID(multipliers_list.get(position))));
        } else if (parent.getId() == R.id.spinner_band_tolerance) { // Tolerance
            Log.d("CLICKED", "Tolerance, colour chose is " + tolerance_list.get(position));
            tolerance = tolerance_list.get(position).split(" ")[1];
            band_tolerance.setColour(Color.parseColor(indexToColorID(tolerance_list.get(position))));
        }

        // Getting the multiplier - convert "1k" (string value from list) to "1000" (integer I can work with)
        switch (multiplier) {
            case "1":
                multiplierNum = 1;
                break;
            case "10":
                multiplierNum = 10;
                break;
            case "100":
                multiplierNum = 100;
                break;
            case "1K":
                multiplierNum = 1000;
                break;
            case "10K":
                multiplierNum = 10000;
                break;
            case "100K":
                multiplierNum = 100000;
                break;
            case "1M":
                multiplierNum = 1000000;
                break;
            case "10M":
                multiplierNum = 10000000;
                break;
            case "100M":
                multiplierNum = 100000000;
                break;
            case "1G":
                multiplierNum = 1000000000;
                break;
            case "0.1":
                multiplierNum = 0.1;
                break;
            case "0.01":
                multiplierNum = 0.01;
                break;
        }
        updateResult();
    }

    void updateResult() {
        /*
        * Explanation:
        *   Call the function whenever I want to update the result
        *   For example:
        *     - Choosing a new item from any spinner
        *     - Choosing a different resistor type (need to change 2/3 digits bands)
        * */

        String result_txt; // To build the string of the result
        switch (chosenResistorBands) {
            case 4:
                basicNumber = Integer.parseInt(firstDigit + secondDigit);
                break;
            case 5:
                basicNumber = Integer.parseInt(firstDigit + secondDigit + thirdDigit);
                break;
        }

        // For example 125*0.5
        basicNumber_multiplied = basicNumber*multiplierNum;

        // The formatted number - for example 1000->1k
        result_txt = helperFunctions.analyseDot(basicNumber_multiplied, true, true);
        result.setText(String.format(getResources().getString(R.string.ohm_result), result_txt, tolerance));
    }

    String indexToColorID(String index) {
        /*
        * Explanation:
        *   Getting an index from a spinner and returning its colour code
        * Parameters:
        *   String index: the index - the spinner's current index text
        * */
        String colourCode = "#000000";
        switch (index) {
            case "Black 0":
            case "Black 1":
                colourCode = "#000000";
                break;
            case "Brown 1":
            case "Brown 10":
            case "Brown 1%":
                colourCode = "#732C2C";
                break;
            case "Red 2":
            case "Red 100":
            case "Red 2%":
                colourCode = "#CF0000";
                System.out.println("CHOSEN RED");
                break;
            case "Orange 3":
            case "Orange 1K":
                colourCode = "#FF9B00";
                break;
            case "Yellow 4":
            case "Yellow 10K":
                colourCode = "#FFF000";
                break;
            case "Green 5":
            case "Green 100K":
            case "Green 0.5%":
                colourCode = "#008602";
                break;
            case "Blue 6":
            case "Blue 1M":
            case "Blue 0.25%":
                colourCode = "#0023CF";
                break;
            case "Violet 7":
            case "Violet 10M":
            case "Violet 0.1%":
                colourCode = "#6100ED";
                break;
            case "Grey 8":
            case "Grey 100M":
            case "Grey 0.05%":
                colourCode = "#434343";
                break;
            case "White 9":
            case "White 1G":
                colourCode = "#FFFFFF";
                break;
            case "Gold 0.1":
            case "Gold 5%":
                colourCode = "#B17D43";
                break;
            case "Silver 0.01":
            case "Silver 10%":
                colourCode = "#928B84";
                break;
        }

        return colourCode;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("CLICKED", "Nothing selected? the parent is " + parent.getId());
    }
}