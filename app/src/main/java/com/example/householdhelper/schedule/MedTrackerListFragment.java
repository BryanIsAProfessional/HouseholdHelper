package com.example.householdhelper.schedule;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.lists.DeleteItemDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The fragment for displaying tracked medications.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class MedTrackerListFragment extends Fragment implements DeleteItemDialog.DeleteItemDialogListener{

    private static final long MILLIS_PER_HOUR = 3600000;

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private MedicineListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tutorialTextView;
    private ImageView tutorialImageView;
    private ArrayList<Medicine> list = new ArrayList<>();

    public MedTrackerListFragment() {
        // Required empty public constructor
    }


    public static MedTrackerListFragment newInstance() {
        MedTrackerListFragment fragment = new MedTrackerListFragment();
        return fragment;
    }

    /**
     * passes the layoutManager, adapter, and ArrayList to the recyclerview
     */
    private void startRecyclerView(){
        adapter = new MedicineListAdapter(list);
        layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    /**
     * retrieves list items from database and stores them in an ArrayList<Medicine>
     */
    private void setVariablesFromDatabase(){
        db = new DatabaseHelper(getContext());
        list = new ArrayList<>();

        Cursor ret = db.getAllMeds();
        if(ret.moveToFirst()){
            do{
                Boolean automatic = ret.getInt(5) > 0;
                long millisSinceUpdate = 0;

                try{
                    Date lastChanged = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(ret.getString(6));
                    Date now = new Date();

                    millisSinceUpdate = now.getTime() - lastChanged.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int hoursSinceUpdate = (int) (millisSinceUpdate / MILLIS_PER_HOUR);
                int hoursBetween = ret.getInt(4);
                int remaining = ret.getInt(2);
                if(automatic){
                    remaining -= hoursSinceUpdate / hoursBetween;
                }
                list.add(new Medicine(ret.getString(0), ret.getString(1), remaining, ret.getInt(3), hoursBetween, automatic, ret.getString(6), ret.getString(7), list.size(), ret.getInt(8)));
            }while(ret.moveToNext());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_med_tracker_list, container, false);
    }

    /**
     * initialize views in layout
     * @param view the layout
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.medTrackerRecyclerView);
        tutorialImageView = view.findViewById(R.id.tutorialImageView);
        tutorialTextView = view.findViewById(R.id.tutorialTextView);
        setVariablesFromDatabase();
        startRecyclerView();

        if(list.size() > 0){
            tutorialImageView.setVisibility(View.GONE);
            tutorialTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteItem(int position) {
        db.deleteMedById(list.get(position).getId());
        list.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVariablesFromDatabase();
        adapter.notifyDataSetChanged();
    }
}