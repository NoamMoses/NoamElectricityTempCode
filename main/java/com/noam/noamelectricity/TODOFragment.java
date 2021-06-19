package com.noam.noamelectricity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.noam.noamelectricity.functions.SystemVariables;

public class TODOFragment extends Fragment {
    /*
     * TODO page
     * -----------
     * The page is just list-views texts being initialised using arrays
     * Therefore, everything is set in the XML file. All I do here is to initialise the list-views
     *
     * Notes:
     *   - Similar to AboutFragment.java
     *   - Previously OtherFragment.java
     * */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // to-do
        String[] todo_list = {
                "Add more features",
                "Add more languages support, and improve existing",
                "Better design",
                "Organise & improve the code",
                "Better app name&logo",
                "Resistors\\Capacitors in series\\parallel calculator",
                "That's it.. pretty much.. Any ideas?\n"
        };
        /*
        * Padding bottom for the ListView will always put a space
        * \n will put a space just under the last item - looks better
        * But I think I will keep both - right now it is with padding bottom of 20dp in xml, and here one \n
        * */

        // At work
        String[] atWork_list = {
                "Better design",
                "Adding French & Spanish",
                "Adding support for 6 bands resistor"
        };

        // Known bugs
        String[] knownBugs_list = {
                "Some quotes are a bit too big - fixed",
                "Bad GUI support when rotating the screen - fixed",
                "Two bugs were enough! Don't you think??"
        };

        /* Setting the adapters of the list-views */
        Context context = getContext();
        if (context != null) {
            ArrayAdapter<String> lv_todo_adapter = new ArrayAdapter<>(getContext(), R.layout.todo_list_items, todo_list),
                    lv_atWork_adapter = new ArrayAdapter<>(getContext(), R.layout.todo_list_items, atWork_list),
                    lv_knownBugs_adapter = new ArrayAdapter<>(getContext(), R.layout.todo_list_items, knownBugs_list);
            ListView lv_todo = view.findViewById(R.id.lv_todo),
                    lv_atWork = view.findViewById(R.id.lv_atWork),
                    lv_knownBugs = view.findViewById(R.id.lv_knownBugs);
            lv_todo.setAdapter(lv_todo_adapter);
            lv_atWork.setAdapter(lv_atWork_adapter);
            lv_knownBugs.setAdapter(lv_knownBugs_adapter);
        } else {
            Log.d("ERR | TODOFragment", SystemVariables.ERR_NULL_CONTEXT);
        }
    }
}
