package com.example.quanlyquanan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.activity.ActivityChart;
import com.example.quanlyquanan.activity.ActivityListCategory;
import com.example.quanlyquanan.activity.ActivityListFood;
import com.example.quanlyquanan.activity.ActivityListTable;
import com.example.quanlyquanan.activity.MainActivity;
import com.example.quanlyquanan.hao.activity_table_menu;
import com.example.quanlyquanan.model.ItemGridViewHome;
import com.example.quanlyquanan.setting.MyApplication;

import java.util.List;

public class GridViewHomeAdapter extends BaseAdapter {
    Context context;
    List<ItemGridViewHome> itemGridViewHomeList;

    public GridViewHomeAdapter(Context context, List<ItemGridViewHome> itemGridViewHomeList) {
        this.context = context;
        this.itemGridViewHomeList = itemGridViewHomeList;
    }

    @Override
    public int getCount() {
        return itemGridViewHomeList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemGridViewHomeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.item_gird_home, null);

        TextView textView = convertView.findViewById(R.id.label_item_grid_home);
        ImageView imageView = convertView.findViewById(R.id.img_item_grid_home);

        textView.setText(itemGridViewHomeList.get(position).getLabel());
        imageView.setImageResource(itemGridViewHomeList.get(position).getImg());

        final ItemGridViewHome item = itemGridViewHomeList.get(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dùng currentItem để lấy thông tin của item được nhấn
                String label = item.getLabel();
                int imgResource = item.getImg();

                // Bây giờ bạn có thể thực hiện bất kỳ thao tác nào với dữ liệu của item được nhấn
//                Toast.makeText(context, "Clicked on: " + label, Toast.LENGTH_LONG).show();
                Log.d("GRIDVIEW", "onClick: item grid " + label);

                if (item.getLabel().equals(MyApplication.LABEL_FOOD)) {
                    Intent intent = new Intent(context, ActivityListFood.class);
                    context.startActivity(intent);
                } else if (item.getLabel().equals(MyApplication.LABEL_BANAN)) {
                    Intent intent = new Intent(context, ActivityListTable.class);
                    context.startActivity(intent);
                } else if (item.getLabel().equals(MyApplication.LABEL_CATEGORY)) {
                    Intent intent = new Intent(context, ActivityListCategory.class);
                    context.startActivity(intent);
                } else if (item.getLabel().equals(MyApplication.LABEL_DATBAN)) {
                    Intent intent = new Intent(context, activity_table_menu.class);
                    context.startActivity(intent);
                } else if (item.getLabel().equals(MyApplication.LABEL_THONGKE)) {
                    Intent intent = new Intent(context, ActivityChart.class);
                    context.startActivity(intent);
                }
            }
        });


        return convertView;
    }
}
