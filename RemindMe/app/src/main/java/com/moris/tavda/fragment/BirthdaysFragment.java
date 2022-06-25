package com.moris.tavda.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moris.tavda.R;
import com.moris.tavda.adapter.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BirthdaysFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_covid;
//    private WebView wv;


    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static BirthdaysFragment getInstance(Context context) {
        Bundle arg = new Bundle();
        BirthdaysFragment fragment = new BirthdaysFragment();
        fragment.setArguments(arg);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_birthdays));
        return fragment;
    }

    ArrayList<String> Topics = new ArrayList<>();
    ArrayList<String> Nodes = new ArrayList<>();
    ArrayList<String> time1s = new ArrayList<>();
    ArrayList<String> time2s = new ArrayList<>();
    private static final String JSON_URL = "https://tavda.info/api/edds/events";// UTF-8
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadJSONFromURL(JSON_URL);
        SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSONFromURL(JSON_URL);
//                Toast.makeText(context, "------", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });

        return view;
    }

    private void loadJSONFromURL(String url) {
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setVisibility(ListView.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            Topics.clear();
                            Nodes.clear();
                            time1s.clear();
                            time2s.clear();
                            JSONObject object = new JSONObject("{\"events\":" + response + "}");
                            JSONArray userArray = object.getJSONArray("events");
//                            JSONArray jsonArray = object.getJSONArray("users");
//                            ArrayList< JSONObject> listItems = getArrayListFromJSONArray(jsonArray);
//                            ListAdapter adapter = new ListViewAdapter(getApplicationContext(),R.layout.row,R.id.textViewName,listItems);
//                            listView.setAdapter(adapter);
                            for (int i = 0; i < userArray.length(); i++) {
                                JSONArray jsonRow = userArray.getJSONArray(i);
                                String value = jsonRow.get(0).toString();
                                Topics.add(jsonRow.get(0).toString());
                                Nodes.add(jsonRow.get(1).toString());
                                time1s.add(jsonRow.get(2).toString());
                                time2s.add(jsonRow.get(3).toString());
                                Log.d("JsonParse", "->" + value);
/*                                for (int j = 0; j < jsonRow.length(); j++) {
                                    String value = jsonRow.get(j).toString();
                                    Log.d("JsonParse", j +"->" + value);
                                }*/
                                CustomAdapter customAdapter = new CustomAdapter(context, Topics, Nodes, time1s, time2s);
                                recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Topics.clear();
                        Nodes.clear();
                        time1s.clear();
                        time2s.clear();
//                        Toast.makeText(context, "Нет соединения с интернетом", Toast.LENGTH_SHORT).show();
                        Topics.add("Нет соединения с интернетом");
                        Nodes.add("");
                        time1s.add("");
                        time2s.add("");
                        CustomAdapter customAdapter = new CustomAdapter(context, Topics, Nodes, time1s, time2s);
                        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
