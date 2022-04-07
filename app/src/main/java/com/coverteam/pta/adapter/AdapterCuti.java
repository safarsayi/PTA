package com.coverteam.pta.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.coverteam.pta.R;
import com.coverteam.pta.model.DataCuti;

import java.util.List;

public class AdapterCuti extends ArrayAdapter<DataCuti> {
    private Activity context;
    private List<DataCuti> dataCutiList;

    public AdapterCuti(Activity context, List<DataCuti> dataCutiList){

        super(context, R.layout.desain_list_cuti, dataCutiList);
        this.context = context;
        this.dataCutiList = dataCutiList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.desain_list_cuti,null,true);
        TextView textNama = listViewItem.findViewById(R.id.namacuti);
        TextView textTglMulai = listViewItem.findViewById(R.id.tglcuti);

        DataCuti dataCutilist = dataCutiList.get(position);
        textNama.setText(dataCutilist.getCutiNama());
        textTglMulai.setText(dataCutilist.getCutiTglMulai());
        return listViewItem;
    }
}

