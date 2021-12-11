package com.moris.tavda.Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
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
            String dd;
            data.clear();
            for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
                if ((element.select("div.art-article>div").text() != "") & (element.select("div.art-article>div").size() > 1)) {
                    dd = element.select("div.art-article>div").get(0).text() + element.select("p").text();
                } else dd = element.select("p").text();
//                dd = element.select("div.art-article>div").size() + " - " + dd;
                data.add(new RemindDTO(element.select("h2").text(), dd, element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            data.add(new RemindDTO("Нет соединения с источником", "", "", "Проверьте подключение к Wi-Fi или сотовой сети", ""));
        }
//        List<RemindDTO> result = data;
        callback.onResult(data, null, 1L);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, RemindDTO> callback) {
        data = new ArrayList<>();
        final Document document;

        try {
            document = Jsoup.connect("http://www.adm-tavda.ru/node?page=" + params.key.toString()).get();
            Elements elements = document.select(".node.story.promote");
            String dd;
            data.clear();
            for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
//                data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));

                if ((element.select("div.art-article>div").text() != "") & (element.select("div.art-article>div").size() > 1)) {
                    dd = element.select("div.art-article>div").get(0).text() + element.select("p").text();
                } else dd = element.select("p").text();
//                dd = element.select("div.art-article>div").size() + " - " + dd;
                data.add(new RemindDTO(element.select("h2").text(), dd, element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            data.add(new RemindDTO("Нет соединения с источником", "", "", "Проверьте подключение к Wi-Fi или сотовой сети", ""));
        }
//        List<RemindDTO> result = data;
        Page = Page - 1;
        callback.onResult(data, params.key.longValue() - 1L);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, RemindDTO> callback) {
        data = new ArrayList<>();
        final Document document;

        try {
            document = Jsoup.connect("http://www.adm-tavda.ru/node?page=" + params.key.toString()).get();
            Elements elements = document.select(".node.story.promote");
            String dd;
            data.clear();
            for (Element element : elements) {
//                    dd=element.select("h2.art-postheader>a").attr("href");
//                data.add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));

                if ((element.select("div.art-article>div").text() != "") & (element.select("div.art-article>div").size() > 1)) {
                    dd = element.select("div.art-article>div").get(0).text() + element.select("p").text();
                } else dd = element.select("p").text();
//                dd = element.select("div.art-article>div").size() + " - " + dd;
                data.add(new RemindDTO(element.select("h2").text(), dd, element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            data.add(new RemindDTO("Нет соединения с источником", "", "", "Проверьте подключение к Wi-Fi или сотовой сети", ""));
        }
//        List<RemindDTO> result = data;
        Page = Page + 1;
        callback.onResult(data, params.key.longValue() + 1L);
    }
}
