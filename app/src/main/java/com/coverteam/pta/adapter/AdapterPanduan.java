package com.coverteam.pta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.pta.R;
import com.coverteam.pta.model.DataPanduan;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class AdapterPanduan  extends PagerAdapter {
    private Context context;
    List<DataPanduan> dataPanduanList;
    LayoutInflater inflater;

    public AdapterPanduan(Context context, List<DataPanduan> dataPanduanList) {
        this.context = context;
        this.dataPanduanList = dataPanduanList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataPanduanList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.desain_panduan_detail,container,false);

        ImageView gambartutor = view.findViewById(R.id.gambar);
        TextView judultutor = view.findViewById(R.id.judul);
        TextView desktutor = view.findViewById(R.id.keterangan);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar);

        //Toast.makeText(context,dataPanduanList.get(position).getGambar(),Toast.LENGTH_SHORT).show();

        //Picasso.with(context).load(dataTutorialSlides.get(position).getFototutor()).into(gambartutor);
        Picasso.with(context)
                .load(dataPanduanList.get(position).getGambar())
                .into(gambartutor, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context,"Gagal Memuat Foto",Toast.LENGTH_SHORT).show();
                    }
                });

        judultutor.setText(dataPanduanList.get(position).getJudul());
        desktutor.setText(dataPanduanList.get(position).getKeterangan());
        container.addView(view);
        return view;
    }

}
