package com.moris.tavda.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moris.tavda.ActivityWebview;
import com.moris.tavda.Data.MySourceFactory;
import com.moris.tavda.R;
import com.moris.tavda.adapter.ClickRecyclerAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dto.RemindDTO;

public class HistoryFragment extends AbstractTabFragment implements  ClickRecyclerAdapter.OnItemClickListener {
    private static final int LAYOUT = R.layout.fragment_history;

    //    public Element element;
//    public ArrayList<RemindDTO> remindDTOArrayList = new ArrayList <RemindDTO>();
//    private ArrayAdapter adapter;
//    private
    private RecyclerView rv;
    private ClickRecyclerAdapter adapter;
    private List<RemindDTO> data = new ArrayList<>();
    private Parse parse;
    private Integer Num = 0;
    private Toolbar toolbar1;
    MySourceFactory sourceFactory;
//    private ProgressDialog mProgressDialog;
//
//    public void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(getActivity());
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setMessage("Loading...");
//        }
//
//        mProgressDialog.show();
//    }
//
//    public void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//    }

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
        menu.findItem(R.id.search).setIcon(R.drawable.ic_account_check);
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
        toolbar1 = view.findViewById(R.id.toolbar2);
        toolbar1.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
//        int i = ((MainActivity) getActivity()).getViewPager();
//        toolbar1.setTitle("Cтраница 1"+ " " + String.valueOf(i));
        toolbar1.setTitle("Cтраница 1");
        toolbar1.inflateMenu(R.menu.menu_news);
        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), Authentication.class);
                Num = Num + 1;
//                if (BuildConfig.DEBUG) {
                toolbar1.setTitle("Страница " + (Num + 1));
//                    Snackbar.make(view, "Страница " + Num, Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
////                showProgressDialog();
                final LinearLayout lyt_progress = (LinearLayout) view.findViewById(R.id.lyt_progress);
                lyt_progress.setVisibility(View.VISIBLE);
                lyt_progress.setAlpha(1.0f);
                parse = new Parse();
                parse.execute(Num);
                return false;
            }
        });

        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num != 0) Num = Num - 1;
//                if (BuildConfig.DEBUG) {
                toolbar1.setTitle("Страница " + (Num + 1));
//                    Snackbar.make(view, "Страница " + Num, Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
////                showProgressDialog();
                final LinearLayout lyt_progress = (LinearLayout) view.findViewById(R.id.lyt_progress);
                lyt_progress.setVisibility(View.VISIBLE);
                lyt_progress.setAlpha(1.0f);
//                parse = new Parse();
//                parse.execute(Num);
            }
        });
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar1);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            FloatingActionButton fab = getActivity().findViewById(R.id.fab);

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else if (dy < 0) {
                    fab.show();
                }
            }
        });

        //Toolbar toolbar =  view.findViewById(R.id.toolbar);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//       ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);

//        List<RemindDTO> remindDTOList = new ArrayList<>();
//        parse = new Parse();
        final LinearLayout lyt_progress = (LinearLayout) view.findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
////        showProgressDialog();
//        parse.execute(0);
//        adapter = new RemindListAdapter(getLayoutInflater(),data);
  //      adapter = new ClickRecyclerAdapter(getLayoutInflater(), data, this);

//        LinearLayout linearLayout =  (LinearLayout) rv.findViewById(R.id.recyclerView);
//
//        linearLayout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(rv, "You clicked the Linear Layout", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
//        rv.setAdapter(adapter);
//        rv.setAdapter(new RemindListAdapter(creatMockData()));
        // DataSource
        final DiffUtil.ItemCallback<RemindDTO> DIFF_CALLBACK =
                new DiffUtil.ItemCallback<RemindDTO>() {
                    @Override
                    public boolean areItemsTheSame(RemindDTO oldItem, RemindDTO newItem) {
                        return ((oldItem.getNode().doubleValue()) == (newItem.getNode().floatValue()));
                    }

                    @Override
                    public boolean areContentsTheSame(RemindDTO oldItem, RemindDTO newItem) {
                        return ((oldItem.getNode().doubleValue()) == (newItem.getNode()).doubleValue());
                    }
                };
        // Adapter
        if (adapter == null) {
            adapter = new ClickRecyclerAdapter(DIFF_CALLBACK);
        }
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setHasFixedSize(false); // неизменый экран
        adapter.setOnItemClickListener(new ClickRecyclerAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                String str;
                str = adapter.getItemData(position).getUrl_DTO().toString();
                str = "http://www.adm-tavda.ru" + str;

//        if(BuildConfig.DEBUG)   Toast.makeText(view.getContext(), str+"-> "+BuildConfig.FLAVOR , Toast.LENGTH_SHORT).show();

                Intent startIntent = new Intent(getActivity(), ActivityWebview.class);
                startIntent.putExtra("INTENT_EXTRA_URL", str);
                startActivity(startIntent);
            }
        });
//        DTODataSource dataSource = new DTODataSource();
        sourceFactory = new MySourceFactory();
        // PagedList
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(15)
                .build();

        LiveData<PagedList<RemindDTO>> pagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor (Executors.newSingleThreadExecutor())
//                .setFetchExecutor(Executors.newFixedThreadPool(1))
              .build();
//
//        PagedList<RemindDTO> pagedList = new PagedList.Builder<>(dataSource, config)
//                .setMainThreadExecutor(new MainThreadExecutor())
//                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
//                .build();
//        // Adapter
//        adapter = new RemindListAdapter(DIFF_CALLBACK);
//        adapter.submitList();

        SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sourceFactory.datasourceLiveData.getValue().invalidate();
            }
        });

         pagedList.observe(getViewLifecycleOwner(), new Observer<PagedList<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable PagedList<RemindDTO> pagedListDTO) {
                //Log.d(TAG, "submit PagedList");
                adapter.submitList(pagedListDTO);
//                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });


        // RecyclerView
        rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        String str;
//        str = adapter.getItemData(position).getTitle().toString();
        str = "http://www.adm-tavda.ru" + adapter.getItemData(position).getUrl_DTO().toString();

//        if(BuildConfig.DEBUG)   Toast.makeText(view.getContext(), str+"-> "+BuildConfig.FLAVOR , Toast.LENGTH_SHORT).show();

        Intent startIntent = new Intent(getActivity(), ActivityWebview.class);
        startIntent.putExtra("INTENT_EXTRA_URL", str);
        startActivity(startIntent);
        //        getActivity().invalidateOptionsMenu();
//   ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ssssssssss");
//        Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).getSupportActionBar().;
//        toolbar.setOverflowIcon(R.drawable.ic_account_check);;
    }


    class Parse extends AsyncTask<Integer, Void, List<RemindDTO>> {
        // TODO: 8/14/2018 Прогрес бар
        @Override
        protected List<RemindDTO> doInBackground(Integer... arg) {
            Document document;

            try {
                document = Jsoup.connect("http://www.adm-tavda.ru/node?page=" + arg[0].toString()).get();
                Elements elements = document.select(".node.story.promote");
//                String dd;
                data.clear();
                for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
                    data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
                }

            } catch (IOException e) {
                e.printStackTrace();
                data = creatMockData();
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<RemindDTO> remindDTOS) {
            super.onPostExecute(remindDTOS);
            rv.setAdapter(adapter);
            final LinearLayout lyt_progress = (LinearLayout) view.findViewById(R.id.lyt_progress);
            lyt_progress.setVisibility(View.INVISIBLE);
            lyt_progress.setAlpha(1.0f);
////            hideProgressDialog();
//            rv.setAdapter(new RemindListAdapter(creatMockData()));
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ddddd");
//            (MainActivity) rv.getActivity().setToolbarTitle("ssss")''
        }
    }

    private List<RemindDTO> creatMockData() {
//        List<RemindDTO> data = new ArrayList<>();
        data.add(new RemindDTO("Нет соединения с источником", "Источник не доступен", "", "Нет соеденения", "http://www.1.ru"));
//        data.add(new RemindDTO("Item 3", "zzzz", "", "lorem", "http://www.1.ru"));
//        data.add(new RemindDTO("Item 4", "zzzz", "", "lorem", "http://www.1.ru"));
//        data.add(new RemindDTO("Item 5", "zzzz", "", "lorem", "http://www.1.ru"));
//        data.add(new RemindDTO("Item 6", "zzzz", "", "lorem", "http://www.1.ru"));
//        data.add(new RemindDTO("Item 2", "zzzz", "", "lorem", "http://www.1.ru"));
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ddddd");
//        (MainActivity) getActivity().setToolbarTitle("ssss");
//        getActivity().invalidateOptionsMenu();
        return data;
    }
}
