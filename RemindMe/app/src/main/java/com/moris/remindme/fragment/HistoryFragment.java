package com.moris.remindme.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.moris.remindme.R;
import com.moris.remindme.adapter.RemindListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dto.RemindDTO;

public class HistoryFragment extends AbstractTabFragment {
    private static final int LAYOUT =R.layout.fragment_history;

//    public Element element;
//    public ArrayList<RemindDTO> remindDTOArrayList = new ArrayList <RemindDTO>();
//    private ArrayAdapter adapter;
//    private
    private RecyclerView rv;
    private RemindListAdapter adapter;
    private List<RemindDTO> data = new ArrayList<>();

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static HistoryFragment getInstance(Context context) {
        Bundle arg = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(arg);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_history));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        List<RemindDTO> remindDTOList = new ArrayList<>();
        Parse parse = new Parse();
        parse.execute();
        adapter = new RemindListAdapter(data);
        rv.setLayoutManager(new LinearLayoutManager(context));
//        rv.setAdapter(adapter);
//        rv.setAdapter(new RemindListAdapter(creatMockData()));
        return view;
    }


    class Parse extends AsyncTask<Void,Void,List<RemindDTO>>{

        @Override
        protected List<RemindDTO> doInBackground(Void... voids) {
            Document document;

            try {
                document = Jsoup.connect("http://www.adm-tavda.ru/").get();
                Elements elements = document.select(".node.story.promote");
                for (Element element:elements){
                    data.add(new RemindDTO(element.select("h2").text(),element.select("p").text(),element.select("img").attr("src")));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<RemindDTO> remindDTOS) {
//            super.onPostExecute(remindDTOS);
           rv.setAdapter(adapter);
        }
    }

//    private List<RemindDTO> creatMockData() {
//        List<RemindDTO> data = new ArrayList<>();
//        data.add(new RemindDTO("Item 1"));
//        data.add(new RemindDTO("Item 2"));
//        data.add(new RemindDTO("Item 3"));
//        data.add(new RemindDTO("Item 4"));
//        data.add(new RemindDTO("Item 5"));
//        data.add(new RemindDTO("Item 6"));
//        return data;
//    }
}
