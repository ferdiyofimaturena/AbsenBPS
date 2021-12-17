package com.ferdi.absenbps.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdi.absenbps.R;
import com.ferdi.absenbps.adapter.DaftarAdapter;
import com.ferdi.absenbps.api.Apiservice;
import com.ferdi.absenbps.api.Apiurl;
import com.ferdi.absenbps.kelas.Pegawai;
import com.ferdi.absenbps.kelas.Pegawais;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaftarCloudActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DaftarAdapter mAdapter;

    private ArrayList<Pegawai> absenArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_cloud);

        setTitle("Daftar Hadir Cloud");

        recyclerView = findViewById(R.id.rv_daftar_cloud);

        absenArrayList = new ArrayList<>();
        mAdapter = new DaftarAdapter(this, absenArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        getAbsen();
    }

    private void getAbsen(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Apiurl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        Apiservice service = retrofit.create(Apiservice.class);

        Call<Pegawais> call = service.tampilDataAll(Apiurl.API_KEY);

        call.enqueue(new Callback<Pegawais>() {
            @Override
            public void onResponse(Call<Pegawais> call, Response<Pegawais> response) {

                assert response.body() != null;

                absenArrayList.clear();
//
//                for (int i = 0; i < response.body().getAbsens().size(); i++) {
//
//                    String id = response.body().getAbsens().get(i).getId_pengguna();
//
//                    Kelas m = new Kelas(idp);
//
//                    absenArrayList.add(m);
//                }

                mAdapter.notifyDataSetChanged();

                mAdapter = new DaftarAdapter(getApplicationContext(), response.body().getPegawaiArrayList());

                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<Pegawais> call, Throwable t) {

                Log.d("onFailure DCA", t.toString());
            }
        });

    }
}