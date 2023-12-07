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
import com.example.quanlyquanan.model.DayAmount;
import com.example.quanlyquanan.model.MyDate;
import com.example.quanlyquanan.response.ResponseAmount;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    private List<DayAmount> mListBillCurrMonth;
    private List<DayAmount> mListBillTargetrMonth;
    private YAxis rightAxis, leftAxis;
    private CombinedData chartData;
    private LineData lineDatas;
    private XAxis xAxis;
    private int mDaysOfMonth = 30;
    public static int tienMonAn = 0, tienDichVu = 0, hoadonchuathanhtoan = 0, hoadondathanhtoan = 0;

    public int mcurrMonth = 12, mcurrYear = 2022;
    private boolean isUserInteracted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        loadCurrTime();

        setControl();
        setEvent();

        setTimeSpinnerCurrTime();

        loadBillDataMonth(mListBillCurrMonth, mcurrMonth, mcurrYear); // khởi tạo  data cho thang hien tai
    }

    private void loadBillDataMonth(List<DayAmount> listBill, int month, int year) {
        BillApi.billApi.getBillByDate(String.valueOf(month), String.valueOf(year))
                .enqueue(new Callback<ResponseAmount>() {
                    @Override
                    public void onResponse(Call<ResponseAmount> call, Response<ResponseAmount> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {
                                Toast.makeText(ActivityChart.this, MyApplication.MESSAGE_LOAD_DATA_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                                listBill.clear();
                                listBill.addAll(response.body().getListBillAmount());
                                Log.d("MYMY", "load bill list ");
                                notifyMonthDataLoaded();
                            }
                        } else {
                            showErrorResponse(response);
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseAmount> call, Throwable t) {
                        showFailedResponse();
                    }
                });
    }

    private void notifyMonthDataLoaded() {
        lineDatas.clearValues();
        mChart.notifyDataSetChanged();
        mChart.clear();
        mChart.invalidate();

        lineDatas.addDataSet((ILineDataSet) dataChart(mListBillCurrMonth, "Doanh thu tháng này", Color.parseColor("Blue"), true));
        lineDatas.addDataSet((ILineDataSet) dataChart(mListBillTargetrMonth, "Doanh thu tháng so sánh", Color.RED, false));
        chartData.setData(lineDatas);
        xAxis.setAxisMaximum(chartData.getXMax() + 0.25f);
        mChart.setData(chartData);
        mChart.invalidate();

        showLoiNhuan();
    }

    private void showLoiNhuan() {
        int tongDoanhThu = tienDichVu + tienMonAn;
        int tiennguyenlieu = (int) (tienMonAn * 0.8);
        int luongnv = 1000000;
        int tienmatbang = 1000000;
        int tongchiphi = luongnv + tiennguyenlieu + tienmatbang;
        int loinhuan = tongDoanhThu - tongchiphi;
        int tongBill = hoadondathanhtoan + hoadonchuathanhtoan;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        // Tạo đối tượng DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("###,###", symbols);

        tvTongDoanhThu.setText(decimalFormat.format(tongDoanhThu));
        tvTienMonAn.setText(decimalFormat.format(tienMonAn));
        tvTienDichVu.setText(decimalFormat.format(tienDichVu));

        tvLuongNhanVien.setText(decimalFormat.format(luongnv));
        tvTienNguyenLieu.setText(decimalFormat.format(tiennguyenlieu));
        tvTienMatBang.setText(decimalFormat.format(tienmatbang));
        tvTongChiPhi.setText(decimalFormat.format(tongchiphi));

        tvSoBillChuaThanhToan.setText(decimalFormat.format(hoadonchuathanhtoan));
        tvSoBillThanhToan.setText(decimalFormat.format(hoadondathanhtoan));
        tvTiLeThanhToan.setText("" + Math.round(((float) hoadondathanhtoan / tongBill) * 100));
        tvtongbill.setText(decimalFormat.format(tongBill));
        tvTongLoiNhuan.setText(decimalFormat.format(loinhuan));
    }

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
                // tranh goi onItemSelected luc khoi tao
                if(isUserInteracted == false) {
                    return;
                }

                MyDate myDate = listMonth.get(position);
                Log.d("MYMY", "onItemSelected: ");
                loadBillDataMonth(mListBillCurrMonth, Integer.parseInt(myDate.getMonth()), Integer.parseInt(myDate.getYear()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        targetTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // tranh goi onItemSelected luc khoi tao
                if(isUserInteracted == false) {
                    return;
                }

                if(position == currTimeSpinner.getSelectedItemPosition()) {
                    Toast.makeText(getApplicationContext(), "Trùng với tháng hiện tại", Toast.LENGTH_SHORT).show();
                }
                else {
                    MyDate myDate = listMonth.get(position);

                    Log.d("MYMY", "onItemSelected target: ");
                    loadBillDataMonth(mListBillTargetrMonth, Integer.parseInt(myDate.getMonth()), Integer.parseInt(myDate.getYear()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currTimeSpinner.setSelection(0,false);
        targetTimeSpinner.setSelection(0,false);
    }

    private void setControl() {
        initSpinnerData();

        Calendar calendar = Calendar.getInstance();
        mDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        currTimeSpinner = findViewById(R.id.sp_curr_month_activity_thongke);
        targetTimeSpinner = findViewById(R.id.sp_target_month_activity_thongke);
        currTimeSpinner.setSelection(0, false);
        targetTimeSpinner.setSelection(0, false);
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

        mChart = findViewById(R.id.combined_chart_activity_thongke);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(true);
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
        for (int i = mcurrYear; i <= mcurrYear; i++) {
            for (int j = 1; j <= 12; j++) {
                // khong can quan tam toi ngay
                listMonth.add(new MyDate(String.valueOf(j), String.valueOf(i)));
            }
        }
    }

    private static DataSet dataChart(List<DayAmount> listBillAmount, String label, int color, boolean isCurrMonth) {

        LineData d = new LineData();
        int[] data = new int[32]; // 31 la so ngay toi da cua mot thang
        if (isCurrMonth) {
            tienMonAn = 0;
            tienDichVu = 0;
            hoadonchuathanhtoan = 0;
            hoadondathanhtoan = 0;
        }

        for (int i = 0; i < listBillAmount.size(); i++) {
            DayAmount dayAmount = listBillAmount.get(i);

            int day = dayAmount.getDate();
            int totalFoodAmount = dayAmount.getTotalFoodAmount();
            int totalTip = dayAmount.getTotalTips();


            if (isCurrMonth) {
                tienMonAn += totalFoodAmount;
                tienDichVu += totalTip;
                hoadonchuathanhtoan += dayAmount.getChuathanhtoan();
                hoadondathanhtoan += dayAmount.getDathanhtoan();
            }

            // Thêm tips vào tổng của ngày đó
            data[day] = totalFoodAmount + totalTip;
        }

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 1; index <= 31; index++) {
            int value = data[index];
            if (value > 0) // loai bo du lieu bang 0
                entries.add(new Entry(index, value));
        }

        // neu entries rong
        // add 1 entry default
        // vi linedataset yeu cau entries khong duoc rong
        if (entries.size() == 0) {
            entries.add(new Entry(0, 0));
        }

        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setLineWidth(1.5f);
        set.setCircleColor(Color.parseColor("PURPLE"));
//        set.setCircleRadius(0);
//        set.setFillColor(color);
        if(isCurrMonth) {
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            set.setMode(LineDataSet.Mode.LINEAR);
        }

        if (entries.size() == 1 && entries.get(0).getY() == 0) { // truong hop entries chi chua data default
            set.setDrawCircles(false);
            set.setDrawValues(false);
        } else {
            set.setDrawValues(true);
        }

        set.setValueTextSize(10f);
        set.setValueTextColor(color);

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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setDefaultSpinner();
//        notifyMonthDataLoaded();
//    }

    private void setTimeSpinnerCurrTime() {
        currTimeSpinner.setSelection(mcurrMonth - 1); // thang bat dau tu 0
        notifyMonthDataLoaded();
    }

    private void loadCurrTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Định dạng thời gian
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        try {
            // Lấy tháng và năm từ thời gian hiện tại
            mcurrMonth = Integer.parseInt(monthFormat.format(currentDate));
            mcurrYear = Integer.parseInt(yearFormat.format(currentDate));
        } catch (Exception e) {
            Log.d("MYMY", "exception: " + e.toString());
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        Toast.makeText(this, "Value: "
//                + e.getY()
//                + ", index: "
//                + h.getX()
//                + ", DataSet index: "
//                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Ngày " + (int) h.getX(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracted = true;
    }
}