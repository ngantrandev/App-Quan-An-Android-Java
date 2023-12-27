package com.example.quanlyquanan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
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
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.TableApi;
import com.example.quanlyquanan.hao.OnTransferTable;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.model.TransferTable;
import com.example.quanlyquanan.response.ResponseTableById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListTableChuyenBan extends RecyclerView.Adapter<AdapterListTableChuyenBan.ViewHolder> {
    private Context context;
    private List<Table> tableList;
    private View mView;
    private  Table currTable;
    OnTransferTable onTransferTable;

    public AdapterListTableChuyenBan(Table currTable, Context context, List<Table> tableList,OnTransferTable onTransferTable) {
        this.context = context;
        this.tableList = tableList;
        this.currTable = currTable;
        this.onTransferTable = onTransferTable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(context).inflate(R.layout.item_chuyenban, parent, false);

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

        Table targettable = tableList.get(position);

        holder.btnChuyenBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChuyenBan(targettable);
            }
        });
    }

    private void onClickChuyenBan(Table targetTable) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage("Nhấn \"Xác nhận\" để xác nhận chuyển sang bàn này");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();

                chuyenBan(currTable, targetTable);
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

    private void chuyenBan(Table currTable, Table targetTable) {
        Log.d("TABLE", "chuyenBan: " +currTable.toString());
        Log.d("TABLE", "chuyenBan: " +targetTable.toString());

        TableApi.tableApi.chuyenban(new TransferTable(currTable, targetTable)).enqueue(new Callback<ResponseTableById>() {
            @Override
            public void onResponse(Call<ResponseTableById> call, Response<ResponseTableById> response) {
                Toast.makeText(context,"tra ve", Toast.LENGTH_SHORT);

                if(response.isSuccessful()&&response.body().getStatus().equals("Success")){
                    Toast.makeText(context,response.body().getMessage(), Toast.LENGTH_SHORT);
                    onTransferTable.onTransferTable();
                }
            }

            @Override
            public void onFailure(Call<ResponseTableById> call, Throwable t) {
                Toast.makeText(context,"loi", Toast.LENGTH_SHORT);
            }
        });
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
        Button btnChuyenBan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTableName = itemView.findViewById(R.id.tv_tablename_item_chuyenban);
            tvNote = itemView.findViewById(R.id.tv_note_item_chuyenban);
            tvStatus = itemView.findViewById(R.id.tv_status_item_chuyenban);
            iconStatus = itemView.findViewById(R.id.ic_status_item_chuyenban);
            btnChuyenBan = itemView.findViewById(R.id.btn_modify_item_chuyenban);

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
