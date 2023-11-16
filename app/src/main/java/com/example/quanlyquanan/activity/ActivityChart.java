package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterSpinnerDate;
import com.example.quanlyquanan.api.BillApi;
import com.example.quanlyquanan.model.MyDate;
import com.example.quanlyquanan.response.ResponseBill;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChart extends AppCompatActivity implements OnChartValueSelectedListener {

    private CombinedChart mChart;
    private List<MyDate> listMonth = new ArrayList<>(); // su dung cho spinner
    private AdapterSpinnerDate adapterCurrTimeSpinner, adapterTargetTimeSpinner;
    private Spinner currTimeSpinner, targetTimeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        setControl();


        mChart = (CombinedChart) findViewById(R.id.combined_chart_activity_thongke);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        final List<String> xLabel = new ArrayList<>();
        xLabel.add("Jan");
        xLabel.add("Feb");
        xLabel.add("Mar");
        xLabel.add("Apr");
        xLabel.add("May");
        xLabel.add("Jun");
        xLabel.add("Jul");
        xLabel.add("Aug");
        xLabel.add("Sep");
        xLabel.add("Oct");
        xLabel.add("Nov");
        xLabel.add("Dec");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });

        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart());
        lineDatas.addDataSet((ILineDataSet) newDataChart());

        data.setData(lineDatas);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();
    }

    private void setControl() {
        initSpinnerData();

        currTimeSpinner = findViewById(R.id.sp_curr_month_activity_thongke);
        targetTimeSpinner = findViewById(R.id.sp_target_month_activity_thongke);

        adapterCurrTimeSpinner = new AdapterSpinnerDate(this, R.layout.item_spinner_selected, listMonth);
        adapterTargetTimeSpinner = new AdapterSpinnerDate(this, R.layout.item_spinner_selected, listMonth);
        currTimeSpinner.setAdapter(adapterCurrTimeSpinner);
        targetTimeSpinner.setAdapter(adapterTargetTimeSpinner);


        findViewById(R.id.ic_back_activity_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityChart.this.finish();
            }
        });


//        getMyData();
    }

    private void initSpinnerData() {
        for(int i =2023;i<=2023;i++) {
            for(int j = 1;j<=12;j++) {
                // khong can quan tam toi ngay
                listMonth.add(new MyDate("dontcare", String.valueOf(j), String.valueOf(i)));
            }
        }
    }

    private void getMyData() {

        BillApi.foodApi.getBillByDate("15", "15", "15")
                .enqueue(new Callback<ResponseBill>() {
                    @Override
                    public void onResponse(Call<ResponseBill> call, Response<ResponseBill> response) {
                        Toast.makeText(ActivityChart.this, "Thanh cong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBill> call, Throwable t) {
                        Toast.makeText(ActivityChart.this, "That bai", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(this, "Value: "
                + e.getY()
                + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private static DataSet dataChart() {

        LineData d = new LineData();
        int[] data = new int[]{5, 6, 7, 4, 5, 8, 1, 2, 4, 4, 6, 5};

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 12; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Lợi nhuận tháng này");
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(5f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }

    private static DataSet newDataChart() {

        LineData d = new LineData();
        int[] data = new int[]{7, 7, 7, 8, 9, 9, 7, 8, 7, 8, 7, 9};

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 12; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Lợi nhuận tháng trước");
        set.setColor(Color.RED);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(5f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.RED);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }
}