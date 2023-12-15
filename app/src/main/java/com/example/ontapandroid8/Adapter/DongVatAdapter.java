package com.example.ontapandroid8.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ontapandroid8.DAO.DongVatDAO;
import com.example.ontapandroid8.DTO.DongVatDTO;
import com.example.ontapandroid8.R;

import java.util.ArrayList;

public class DongVatAdapter extends RecyclerView.Adapter<DongVatAdapter.viewHolder>{

    Context context;
    ArrayList<DongVatDTO> listDV;

    public DongVatAdapter(Context context, ArrayList<DongVatDTO> listDV) {
        this.context = context;
        this.listDV = listDV;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v1 = inflater.inflate(R.layout.item_dongvat, parent, false);
        return  new viewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DongVatDTO dongVatDTO = listDV.get(position);
        DongVatDAO dongVatDAO = new DongVatDAO(context);
        holder.txt_ma.setText("Mã: " + dongVatDTO.getMa());
        holder.txt_ten.setText("Tên: " + dongVatDTO.getTen());
        holder.txt_loai.setText("Loại: " + dongVatDTO.getLoai());
        holder.txt_soluong.setText("Số lượng: " + dongVatDTO.getSoLuong());

        holder.btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kq = dongVatDAO.deleteRow(dongVatDTO);
                if (kq > 0) {
                    listDV.remove(dongVatDTO);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_sua, null);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                EditText edt_ten = view.findViewById(R.id.edt_ten);
                EditText edt_loai = view.findViewById(R.id.edt_loai);
                EditText edt_soluong = view.findViewById(R.id.edt_soluong);
                Button btn_suaDialog = view.findViewById(R.id.btn_suaDialog);
                Button btn_huySua = view.findViewById(R.id.btn_huySua);

                edt_ten.setText(dongVatDTO.getTen());
                edt_loai.setText(dongVatDTO.getLoai());
                edt_soluong.setText(dongVatDTO.getSoLuong()+"");

                btn_suaDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String ten = edt_ten.getText().toString().trim();
                        String loai = edt_loai.getText().toString().trim();
                        String soluong = edt_soluong.getText().toString().trim();

                        dongVatDTO.setTen(ten);
                        dongVatDTO.setTen(loai);

                        if (ten.isEmpty()||loai.isEmpty()||soluong.isEmpty()) {
                            Toast.makeText(context, "Không bỏ trống dữ liệu", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            int sl = Integer.parseInt(soluong);
                            if (sl < 0) {
                                Toast.makeText(context, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dongVatDTO.setSoLuong(sl);

                            int kq = dongVatDAO.updateRow(dongVatDTO);
                            if (kq > 0) {
                                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Số lượng phải là số", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return listDV.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txt_ma, txt_ten, txt_loai, txt_soluong;
        Button btn_sua, btn_xoa;

        DongVatAdapter dongVatAdapter;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            dongVatAdapter = new DongVatAdapter(context, listDV);

            txt_ma = itemView.findViewById(R.id.txt_ma);
            txt_ten = itemView.findViewById(R.id.txt_ten);
            txt_loai = itemView.findViewById(R.id.txt_loai);
            txt_soluong = itemView.findViewById(R.id.txt_soluong);

            btn_sua = itemView.findViewById(R.id.btn_sua);
            btn_xoa = itemView.findViewById(R.id.btn_xoa);
        }
    }
}
