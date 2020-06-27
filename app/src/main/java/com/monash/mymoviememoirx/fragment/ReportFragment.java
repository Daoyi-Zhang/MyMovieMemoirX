package com.monash.mymoviememoirx.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.network.NetworkConnection;
import com.monash.mymoviememoirx.pojo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ParcelCreator")
public class ReportFragment extends Fragment implements CalendarConstraints.DateValidator {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";


    // TODO: Rename and change types of parameters
    private User user;
    private PieChart pieChart;
    private BarChart barChart;

    private class CountByTimeRange4EachPostcode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String userId = strings[0];
            String startDate = strings[1];
            String endDate = strings[2];
            return new NetworkConnection().countByTimeRange4EachPostcode(userId, startDate, endDate);
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<PieEntry> list = new ArrayList<>();
            try {
                if (s != null) {
                    JSONArray results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        list.add(new PieEntry(Float.parseFloat(result.getString("totalMovieWatched")), result.getString("locationPostcode")));
                    }
                }
                else {
                    Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
            }
            initPieChart(list);
        }

        public void initPieChart(List<PieEntry> values) {
            pieChart = getView().findViewById(R.id.pie_chart);
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            //pieChart.setExtraOffsets(5, 10, 5, 5);
            pieChart.setDrawHoleEnabled(false);
            pieChart.animateXY(1000, 1000);

            //pieChart.setTransparentCircleRadius(61f);

            PieDataSet dataSet = new PieDataSet(values, "Postcode");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            //dataSet.setValueLineColor(Color.WHITE);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(18f);
            data.setValueTextColor(Color.BLACK);

            Description description = new Description();
//        description.setText("Movie Watching Distribution By Location");
//        description.setTextColor(Color.WHITE);

            pieChart.getLegend().setTextColor(Color.WHITE);
            pieChart.getLegend().setTextSize(18f);
            pieChart.setDescription(description);
            pieChart.setData(data);
            pieChart.setEntryLabelTextSize(18f);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
        }
    }

    private class CountByYear4EachMonth extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String userId = strings[0];
            String year = strings[1];
            return new NetworkConnection().countByYear4EachMonth(userId, year);
        }

        @Override
        protected void onPostExecute(String s) {
            JSONArray results = null;
            ArrayList<BarEntry> values = new ArrayList<>();
            HashMap<String, Integer> map = new HashMap<>();
            List<String> months = Arrays.asList("January", "February", "March",
                    "April", "May", "June",
                    "July", "August", "September",
                    "October", "November", "December");
            for (int i = 0; i < 12; i++) {
                map.put(months.get(i), 0);
            }
            if (s != null) {
                try {
                    results = new JSONArray(s);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        map.put(result.getString("monthOf2020"), Integer.valueOf(result.getString("totalMovieWatched")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_LONG).show();
            }
            for (int i = 0; i < 12; i++) {
                values.add(new BarEntry(i, map.get(months.get(i))));
                //values.add(new BarEntry(i, 10));
            }
            initBarChar(values);
        }

        public void initBarChar(List<BarEntry> values) {

            barChart = getView().findViewById(R.id.bar_char);
            barChart.getDescription().setEnabled(false);
            BarDataSet dataSet = new BarDataSet(values, "Month");
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            dataSet.setDrawValues(true);
            dataSet.setValueFormatter(new IndexAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(18f);

            List<String> months = Arrays.asList("January", "February", "March",
                    "April", "May", "June",
                    "July", "August", "September",
                    "October", "November", "December");

            BarData data = new BarData(dataSet);
            data.setValueTextColor(Color.WHITE);
            barChart.animateXY(1000, 1000);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextColor(Color.WHITE);
            xAxis.setTextSize(16f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(months.size());
            xAxis.setLabelRotationAngle(270);
            barChart.getLegend().setTextColor(Color.WHITE);
            barChart.getLegend().setTextSize(18f);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
    }

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(User user) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        initDateRangePicker(view);

        initSpinner(view);
        return view;
    }


    public void initSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner);
        final ArrayList<String> list = new ArrayList<>();
        list.add("2020");
        list.add("2019");
        list.add("2018");
        list.add("2017");
        list.add("2016");
        list.add("2015");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountByYear4EachMonth task2 = new CountByYear4EachMonth();
                task2.execute(user.getUserId().toString(), list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void initDateRangePicker(View view) {

        CalendarConstraints.Builder cbuilder = new CalendarConstraints.Builder();
        cbuilder.setValidator(this);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a time range");
        builder.setCalendarConstraints(cbuilder.build());
        final MaterialDatePicker dp = builder.build();
        final Button button = view.findViewById(R.id.button);
        String initStart = "2020-01-01";
        String initEnd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String initShowed = initStart + " TO " + initEnd;
        button.setText(initShowed);
        CountByTimeRange4EachPostcode task = new CountByTimeRange4EachPostcode();
        task.execute(user.getUserId().toString(), initStart, initEnd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp.show(getActivity().getSupportFragmentManager(), "DATE_RANGE_PICKER");
            }
        });
        dp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(((Pair<Long, Long>) dp.getSelection()).first));
                String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(((Pair<Long, Long>) dp.getSelection()).second));
                String show = start + " TO " + end;
                button.setText(show);
                CountByTimeRange4EachPostcode task1 = new CountByTimeRange4EachPostcode();
                task1.execute(user.getUserId().toString(), start, end);
            }
        });
    }

    @Override
    public boolean isValid(long date) {
        if (MaterialDatePicker.todayInUtcMilliseconds() < date) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }
}
