package com.example.quanlyquanan.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {



    public DecimalDigitsInputFilter() {
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        String currentText = dest.toString();
        int dotIndex = currentText.indexOf('.');


        if (dotIndex != -1 && source.equals(".") && currentText.contains(".")) {
            // Ngăn chặn thêm dấu chấm mới nếu đã có một dấu chấm trong chuỗi
            return "";
        }

        return null; // Cho phép nhập tiếp tục
    }
}