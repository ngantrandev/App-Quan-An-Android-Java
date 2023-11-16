package com.example.quanlyquanan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.activity.ActivityListTable;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.TableApi;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.response.ResponseTableById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListTable extends RecyclerView.Adapter<AdapterListTable.ViewHolder> {
    private Context context;
    private List<Table> tableList;
    private View mView;


    public AdapterListTable(Context context, List<Table> tableList) {
        this.context = context;
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.tvTableName.setText(table.getTableName());
        holder.tvNote.setText(table.getNote());

        holder.tvStatus.setText(table.getStatus() == 0
                ? context.getString(R.string.tv_tablestatus_available)
                : context.getString(R.string.tv_tablestatus_unavailable));

        holder.iconStatus.setImageResource(table.getStatus() == 0
                ? R.drawable.circle_status_available
                : R.drawable.circle_status_unavailable);

        setEvent(holder, position);


    }

    private void setEvent(ViewHolder holder, int position) {

        holder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedModify(tableList.get(position));
            }
        });
    }

    private void onClickedModify(Table table) {

        View viewBottomSheet = LayoutInflater.from(context).inflate(R.layout.layout_bottomsheet_table, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(viewBottomSheet);
        Button btnXacNhan = bottomSheetDialog.findViewById(R.id.btn_submit_bottomsheet_table);
        EditText edtName = bottomSheetDialog.findViewById(R.id.edt_tablename_bottomsheet_table);
        EditText edtNote = bottomSheetDialog.findViewById(R.id.edt_note_bottomsheet_table);
        edtName.setText(table.getTableName());
        edtNote.setText(table.getNote());


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("Xác nhận đổi tên\nHành động này không thể hoàn tác");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        bottomSheetDialog.dismiss();

//                        Toast.makeText(context,edtNote.getText().toString().trim(), Toast.LENGTH_LONG).show();


                        saveDataToServer(table, edtName.getText().toString().trim(), edtNote.getText().toString().trim());
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });


        bottomSheetDialog.show();
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) viewBottomSheet.getParent());
        bottomSheetBehavior.setPeekHeight(800);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void saveDataToServer(Table table, String name, String note) {

        TableApi.tableApi.changeTableInfo(table.get_id()
                        , new Table(table.get_id(), note, name, table.getStatus()))
                .enqueue(new Callback<ResponseTableById>() {
                    @Override
                    public void onResponse(Call<ResponseTableById> call, Response<ResponseTableById> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Table newTable=response.body().getTable();
                                table.setTableName(newTable.getTableName());
                                table.setNote(newTable.getNote());
                                table.setStatus(newTable.getStatus());

                                notifyDataSetChanged();
                            }
                        } else {
                            showErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseTableById> call, Throwable t) {
                        Toast.makeText(context, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return tableList == null ? 0 : tableList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableName, tvStatus, tvNote;
        ImageView iconStatus;
        Button btnModify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTableName = itemView.findViewById(R.id.tv_tablename_item_table);
            tvNote = itemView.findViewById(R.id.tv_note_item_table);
            tvStatus = itemView.findViewById(R.id.tv_status_item_table);
            iconStatus = itemView.findViewById(R.id.ic_status_item_table);
            btnModify = itemView.findViewById(R.id.btn_modify_item_table);

        }
    }

    private void showErrorResponse(Response<?> response) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = null;
        try {
            mJson = parser.parse(response.errorBody().string());
            Gson gson = new Gson();
            ApiError errorResponse = gson.fromJson(mJson, ApiError.class);

            Toast.makeText(context, errorResponse.getError(), Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
