package com.moris.tavda.Data;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dto.RemindDTO;

public class DTODataSource extends PageKeyedDataSource<Long, RemindDTO> {
    private List<RemindDTO> data;
    Long Page;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, RemindDTO> callback) {
        data = new ArrayList<>();
        final Document document;
        Page = 0L;
        try {
            document = Jsoup.connect("http://www.adm-tavda.ru/node?page=0").get();
            Elements elements = document.select(".node.story.promote");
//                String dd;
            data.clear();
            for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
                data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
            }

        } catch (IOException e) {
            e.printStackTrace();
            data.add(new RemindDTO("Нет соединения с источником", "Источник не доступен", "", "Нет соеденения", "http://www.1.ru"));
        }
//        List<RemindDTO> result = data;
callback.onResult(data,null,1L);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, RemindDTO> callback) {
        data = new ArrayList<>();
        final Document document;

        try {
            document = Jsoup.connect("http://www.adm-tavda.ru/node?page="+params.key.toString()).get();
            Elements elements = document.select(".node.story.promote");
//                String dd;
            data.clear();
            for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
                data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
            }

        } catch (IOException e) {
            e.printStackTrace();
            data.add(new RemindDTO("Нет соединения с источником", "Источник не доступен", "", "Нет соеденения", "http://www.1.ru"));
        }
//        List<RemindDTO> result = data;
        Page=Page-1;
        callback.onResult(data,params.key.longValue()-1L);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, RemindDTO> callback) {
        data = new ArrayList<>();
        final Document document;

        try {
            document = Jsoup.connect("http://www.adm-tavda.ru/node?page="+params.key.toString()).get();
            Elements elements = document.select(".node.story.promote");
//                String dd;
            data.clear();
            for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
                data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
            }

        } catch (IOException e) {
            e.printStackTrace();
            data.add(new RemindDTO("Нет соединения с источником", "Источник не доступен", "", "Нет соеденения", "http://www.1.ru"));
        }
//        List<RemindDTO> result = data;
        Page=Page+1;
        callback.onResult(data,params.key.longValue()+1L);
    }
}