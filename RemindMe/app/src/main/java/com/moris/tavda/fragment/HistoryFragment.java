package com.moris.tavda.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.moris.tavda.R;
import com.moris.tavda.adapter.ClickRecyclerAdapter;
import com.moris.tavda.adapter.RemindListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dto.RemindDTO;

public class HistoryFragment extends AbstractTabFragment implements
        ClickRecyclerAdapter.OnItemClickListener{
    private static final int LAYOUT = R.layout.fragment_history;

    //    public Element element;
//    public ArrayList<RemindDTO> remindDTOArrayList = new ArrayList <RemindDTO>();
//    private ArrayAdapter adapter;
//    private
    private RecyclerView rv;
    private RemindListAdapter adapter;
    private List<RemindDTO> data = new ArrayList<>();
    private Parse parse;
    private Integer Num = 0;

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.search).setIcon(getResources().getDrawable(R.drawable.ic_account_check, getActivity().getTheme()));
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
        view = inflater.inflate(LAYOUT, container, false);
        rv = view.findViewById(R.id.recyclerView);
        //Toolbar toolbar =  view.findViewById(R.id.toolbar);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//       ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);

        List<RemindDTO> remindDTOList = new ArrayList<>();
        parse = new Parse();
        parse.execute(0);
//        adapter = new RemindListAdapter(getLayoutInflater(),data);
        adapter = new ClickRecyclerAdapter(getLayoutInflater(),data,this);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setHasFixedSize(true); // неизменый экран
//        LinearLayout linearLayout =  (LinearLayout) rv.findViewById(R.id.recyclerView);
//
//        linearLayout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(rv, "You clicked the Linear Layout", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        Button nexButton = view.findViewById(R.id.nex);
        nexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Num = Num + 1;
                Snackbar.make(view, "Страница "+ Num, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                parse = new Parse();
                parse.execute(Num);
            }
        });

        Button topButton = view.findViewById(R.id.pre);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Num != 0) Num = Num - 1;
                Snackbar.make(view, "Страница "+ Num, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                parse = new Parse();
                parse.execute(Num);
            }
        });

//        rv.setAdapter(adapter);
//        rv.setAdapter(new RemindListAdapter(creatMockData()));
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        String str;
//        str = adapter.getItemData(position).getTitle().toString();
        str = adapter.getItemData(position).getUrl_DTO().toString();
        Toast.makeText(view.getContext(), str , Toast.LENGTH_SHORT).show();
//        getActivity().invalidateOptionsMenu();
//   ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ssssssssss");
//        Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).getSupportActionBar().;
//        toolbar.setOverflowIcon(R.drawable.ic_account_check);;
    }


    class Parse extends AsyncTask<Integer, Void, List<RemindDTO>> {

        @Override
        protected List<RemindDTO> doInBackground(Integer... arg) {
            Document document;

            try {
                document = Jsoup.connect("http://www.adm-tavda.ru/node?page=" + arg[0].toString()).get();
                Elements elements = document.select(".node.story.promote");
                data.clear();
                for (Element element : elements) {
                    data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader.a").attr("href")));
                }

            } catch (IOException e) {
                e.printStackTrace();
                data=creatMockData();
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<RemindDTO> remindDTOS) {
            super.onPostExecute(remindDTOS);
            rv.setAdapter(adapter);
//            rv.setAdapter(new RemindListAdapter(creatMockData()));
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ddddd");
//            (MainActivity) rv.getActivity().setToolbarTitle("ssss")''
        }
    }

    private List<RemindDTO> creatMockData() {
//        List<RemindDTO> data = new ArrayList<>();
        data.add(new RemindDTO("Item 1", "zzzz", "", "lorem", "http://www.mail.ru"));
        data.add(new RemindDTO("Item 3", "zzzz", "", "lorem", "http://www.mail.ru"));
        data.add(new RemindDTO("Item 4", "zzzz", "", "lorem", "http://www.mail.ru"));
        data.add(new RemindDTO("Item 5", "zzzz", "", "lorem", "http://www.mail.ru"));
        data.add(new RemindDTO("Item 6", "zzzz", "", "lorem", "http://www.ya.ru"));
        data.add(new RemindDTO("Item 2", "zzzz", "", "lorem", "http://www.google.ru"));
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ddddd");
//        (MainActivity) getActivity().setToolbarTitle("ssss");
//        getActivity().invalidateOptionsMenu();
        return data;
    }
}
