package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterSpinnerDate;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.BillApi;
import com.example.quanlyquanan.model.Bill;
import com.example.quanlyquanan.model.MyDate;
import com.example.quanlyquanan.response.ResponseBill;
import com.example.quanlyquanan.setting.MyApplication;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChart extends AppCompatActivity implements OnChartValueSelectedListener {

    private ImageView btnBack;
    private CombinedChart mChart;
    private List<MyDate> listMonth = new ArrayList<>(); // su dung cho spinner
    private AdapterSpinnerDate adapterCurrTimeSpinner, adapterTargetTimeSpinner;
    private Spinner currTimeSpinner, targetTimeSpinner;

    TextView tvTongLoiNhuan, tvTongDoanhThu, tvTongChiPhi, tvTienMonAn, tvTienDichVu, tvTienNguyenLieu, tvTienMatBang, tvLuongNhanVien, tvSoBillThanhToan, tvSoBillChuaThanhToan, tvTiLeThanhToan, tvtongbill;
    private List<Bill> mListBillCurrMonth;
    private List<Bill> mListBillTargetrMonth;
    private int mBillUnpaid;
    private List<String> xLabels;
    private YAxis rightAxis, leftAxis;
    private CombinedData chartData;
    private LineData lineDatas;
    private XAxis xAxis;
    private int mDaysOfMonth;
    private boolean isListBillLoaded = false, isListBillInfoLoaded = false;
    public static int tienMonAn = 0;
    private static int hoadonchuathanhtoan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        setControl();
        setEvent();

// Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Định dạng thời gian
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        // Lấy tháng và năm từ thời gian hiện tại
        int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
        int currentYear = Integer.parseInt(yearFormat.format(currentDate));

        loadBillDataMonth(mListBillCurrMonth, currentMonth, currentYear); // khởi tạo  data cho thang hien tai
    }

    private void loadBillDataMonth(List<Bill> listBill, int month, int year) {
        BillApi.billApi.getBillByDate(String.valueOf(month), String.valueOf(year))
                .enqueue(new Callback<ResponseBill>() {
                    @Override
                    public void onResponse(Call<ResponseBill> call, Response<ResponseBill> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {
                                Toast.makeText(ActivityChart.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                                listBill.clear();
                                listBill.addAll(response.body().getBillList());

                                notifyMonthDataLoaded();
                            }
                        } else {
                            showErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBill> call, Throwable t) {
                        showFailedResponse();
                    }
                });
    }

    private void notifyMonthDataLoaded() {
//        initFirstChartView();
        lineDatas.clearValues();
        lineDatas.addDataSet((ILineDataSet) dataChart(mListBillCurrMonth, "Lợi nhuận tháng này", Color.GREEN));
//        lineDatas.addDataSet((ILineDataSet) dataChart(mListBillTargetrMonth, "Lợi nhuận tháng so ", Color.RED));
        chartData.setData(lineDatas);
        xAxis.setAxisMaximum(chartData.getXMax() + 0.25f);
        mChart.setData(chartData);
        mChart.invalidate();

        tinhLoiNhuan();
    }

    private void tinhLoiNhuan() {
        int tiendichvu = (int) (tienMonAn * 0.1);
        int tongDoanhThu = tiendichvu + tienMonAn;
        int tiennguyenlieu = (int) (tienMonAn * 0.3);
        int luongnv = 1000000;
        int tienmatbang = 1000000;
        int tongchiphi = luongnv + tiennguyenlieu + tienmatbang;
        int hoadondathanhtoan = mListBillCurrMonth.size() - hoadonchuathanhtoan;
        int loinhuan = tongDoanhThu - tongchiphi;

        tvTongDoanhThu.setText(String.valueOf(tongDoanhThu));
        tvTienMonAn.setText(String.valueOf(tienMonAn));
        tvTienDichVu.setText(String.valueOf(tiendichvu));

        tvLuongNhanVien.setText(String.valueOf(luongnv));
        tvTienNguyenLieu.setText(String.valueOf(tiennguyenlieu));
        tvTienMatBang.setText(String.valueOf(tienmatbang));
        tvTongChiPhi.setText(String.valueOf(tongchiphi));
////
        tvSoBillChuaThanhToan.setText(String.valueOf(hoadonchuathanhtoan));
        tvSoBillThanhToan.setText(String.valueOf(hoadondathanhtoan));
        tvTiLeThanhToan.setText(String.valueOf(Math.floor((float)hoadondathanhtoan / mListBillCurrMonth.size())));
        tvtongbill.setText(String.valueOf(mListBillCurrMonth.size()));
        tvTongLoiNhuan.setText(String.valueOf(loinhuan));





    }

    private void notifyTargetMonthDataLoaded() {

    }

//    private void initFirstChartView() {
//        if (isListBillInfoLoaded && isListBillLoaded) {
//
//        }
//    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityChart.this.finish();
            }
        });

        currTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyDate myDate = listMonth.get(position);

                loadBillDataMonth(mListBillCurrMonth, Integer.parseInt(myDate.getMonth()), Integer.parseInt(myDate.getYear()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        targetTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyDate myDate = listMonth.get(position);

                loadBillDataMonth(mListBillTargetrMonth, Integer.parseInt(myDate.getMonth()), Integer.parseInt(myDate.getYear()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setControl() {
        initSpinnerData();
        Calendar calendar = Calendar.getInstance();
        mDaysOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        currTimeSpinner = findViewById(R.id.sp_curr_month_activity_thongke);
        targetTimeSpinner = findViewById(R.id.sp_target_month_activity_thongke);
        btnBack = findViewById(R.id.ic_back_activity_chart);
        tvTongLoiNhuan = findViewById(R.id.tv_tongloinhuan_activity_thongke);
        tvTongDoanhThu = findViewById(R.id.tv_doanhthu_item_doanhthu);
        tvTienMonAn = findViewById(R.id.tv_tienmonan_item_doanhthu);
        tvTienDichVu = findViewById(R.id.tv_dichvu_doanhthu);
        tvTongChiPhi = findViewById(R.id.tv_chiphi_item_chiphi);
        tvTienNguyenLieu = findViewById(R.id.tv_tiennguyenlieu_item_chiphi);
        tvTienMatBang = findViewById(R.id.tv_tienmatbang_item_chiphi);
        tvLuongNhanVien = findViewById(R.id.tv_luongnv);
        tvSoBillThanhToan = findViewById(R.id.tv_dathanhtoan_item_hoadon);
        tvtongbill = findViewById(R.id.tv_tonghoadon_item_hoadon);
        tvSoBillChuaThanhToan = findViewById(R.id.tv_chuathanhtoan_item_hoadon);
        tvTiLeThanhToan = findViewById(R.id.tv_phantram_itemhoadon);


        adapterCurrTimeSpinner = new AdapterSpinnerDate(this, R.layout.item_spinner_selected, listMonth);
        adapterTargetTimeSpinner = new AdapterSpinnerDate(this, R.layout.item_spinner_selected, listMonth);
        currTimeSpinner.setAdapter(adapterCurrTimeSpinner);
        targetTimeSpinner.setAdapter(adapterTargetTimeSpinner);

        mListBillCurrMonth = new ArrayList<>();
        mListBillTargetrMonth = new ArrayList<>();
        xLabels = new ArrayList<>();

        mChart = findViewById(R.id.combined_chart_activity_thongke);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        setYAxisData();

        setXAxisLabel(mDaysOfMonth);

        chartData = new CombinedData();
        lineDatas = new LineData();
    }

    private void setXAxisLabel(int daysOfMonth) {
        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                Log.d("MYMY", value + "    |   " + (int) value % xLabel.size() + "   |   " + xLabel.get((int) value % xLabel.size()));
//                return xLabel.get((int) value % xLabel.size());
                return "" + (int) value % daysOfMonth;
            }
        });
    }

    private void setYAxisData() {

        rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setAxisMinimum(0f);

        leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

    }

    private void initSpinnerData() {
        for (int i = 2023; i <= 2023; i++) {
            for (int j = 1; j <= 12; j++) {
                // khong can quan tam toi ngay
                listMonth.add(new MyDate("dontcare", String.valueOf(j), String.valueOf(i)));
            }
        }
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

    private static DataSet dataChart(List<Bill> billList, String label, int color) {

        LineData d = new LineData();
        int[] data = new int[31];
        tienMonAn = 0;
        hoadonchuathanhtoan = 0;


        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);

            // Chuyển đổi thời gian từ millisecond sang Calendar
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(bill.getTimeCheckIn()));

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int tips = bill.getTips();
            Log.d("MYMY", "tip: "+tips);
            tienMonAn += tips;
            Log.d("MYMY", "tien an " + tienMonAn);

            if (bill.getStatus() == 0) {
                hoadonchuathanhtoan += 1;
            }

            // Thêm tips vào tổng của ngày đó
            data[day - 1] += tips;
        }

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 31; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setLineWidth(2.5f);
//        set.setCircleColor(color);
//        set.setCircleRadius(0);
//        set.setFillColor(color);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(color);

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

    private void showErrorResponse(Response<?> response) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = null;
        try {
            mJson = parser.parse(response.errorBody().string());
            Gson gson = new Gson();
            ApiError errorResponse = gson.fromJson(mJson, ApiError.class);

            Toast.makeText(this, errorResponse.getError(), Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showFailedResponse() {
        Toast.makeText(this, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDefaultSpinner();
        notifyMonthDataLoaded();
    }

    private void setDefaultSpinner() {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Định dạng thời gian
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        // Lấy tháng và năm từ thời gian hiện tại
        int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
        int currentYear = Integer.parseInt(yearFormat.format(currentDate));


        currTimeSpinner.setSelection(currentMonth - 1); // thang bat dau tu 0
        notifyMonthDataLoaded();

    }
}