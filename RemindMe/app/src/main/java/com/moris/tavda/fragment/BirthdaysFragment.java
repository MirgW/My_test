package com.moris.tavda.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moris.tavda.R;
import com.moris.tavda.SimpleWebViewClientImpl;

public class BirthdaysFragment extends AbstractTabFragment {
    private static final int LAYOUT =R.layout.fragment_covid;
    private WebView wv;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        wv = (WebView) view.findViewById(R.id.webviewmag_covid);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.setWebViewClient(new SimpleWebViewClientImpl(getActivity()));
        wv.setWebChromeClient(new WebChromeClient());
/*
        String customHtml =
                "<html><head>\n" +
                        "\t\n" +
                        "\t<title>Quick Start - Leaflet</title>\n" +
                        "\n" +
                        "\t<meta charset=\"utf-8\">\n" +
                        "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "\t\n" +
                        "\t<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"docs/images/favicon.ico\">\n" +
                        "\n" +
                        "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.6.0/dist/leaflet.css\" integrity=\"sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ==\" crossorigin=\"\">\n" +
                        "    <script src=\"https://unpkg.com/leaflet@1.6.0/dist/leaflet.js\" integrity=\"sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew==\" crossorigin=\"\"></script>\n" +
                        "<link rel=\"stylesheet\" href=\"leaflet/MarkerCluster.css\">\n" +
                        "<link rel=\"stylesheet\" href=\"leaflet/MarkerCluster.Default.css\">\n" +
                        "<script src=\"https://unpkg.com/esri-leaflet@2.4.1/dist/esri-leaflet.js\"\n" +
                        "  integrity=\"sha512-xY2smLIHKirD03vHKDJ2u4pqeHA7OQZZ27EjtqmuhDguxiUvdsOuXMwkg16PQrm9cgTmXtoxA6kwr8KBy3cdcw==\"\n" +
                        "  crossorigin=\"\"></script>\n" +
                        " <!-- Load Esri Leaflet Geocoder from CDN -->\n" +
                        "  <link rel=\"stylesheet\" href=\"https://unpkg.com/esri-leaflet-geocoder@2.3.3/dist/esri-leaflet-geocoder.css\"\n" +
                        "    integrity=\"sha512-IM3Hs+feyi40yZhDH6kV8vQMg4Fh20s9OzInIIAc4nx7aMYMfo+IenRUekoYsHZqGkREUgx0VvlEsgm7nCDW9g==\"\n" +
                        "    crossorigin=\"\">\n" +
                        "  <script src=\"https://unpkg.com/esri-leaflet-geocoder@2.3.3/dist/esri-leaflet-geocoder.js\"\n" +
                        "    integrity=\"sha512-HrFUyCEtIpxZloTgEKKMq4RFYhxjJkCiF5sDxuAokklOeZ68U2NPfh4MFtyIVWlsKtVbK5GD2/JzFyAfvT5ejA==\"\n" +
                        "    crossorigin=\"\"></script>\n" +
                        "<script src=\"leaflet/leaflet.markercluster.js\"></script>\n" +
                        "\n" +
                        "\n" +
                        "\t\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "\n" +
                        "<div id=\"mapid\" style=\"width: 800px; height: 600px; position: relative; outline: none;\" class=\"leaflet-container leaflet-fade-anim leaflet-grab leaflet-touch-drag\" tabindex=\"0\"></div>\n" +
                        "\n" +
                        "<script>\n" +
                        "\n" +
                        "\n" +
                        "\tvar mymap = L.map('mapid').setView([58.0419, 65.273235], 13);\n" +
                        "\n" +
                        "\tL.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                        "\t\tmaxZoom: 18,\n" +
                        "\t\tattribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors, ' +\n" +
                        "\t\t\t'<a href=\"https://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, ' +\n" +
                        "\t\t\t'Imagery © <a href=\"https://www.mapbox.com/\">Mapbox</a>',\n" +
                        "\t}).addTo(mymap);\n" +
                        "\n" +
                        "\n" +
                        "const openstreetmap = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                        "    attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'\n" +
                        "});\n" +
                        "const legend = L.control({position: 'bottomleft'});\n" +
                        "\n" +
                        "\n" +
                        "legend.onAdd=function(map) {\n" +
                        "    let div = L.DomUtil.create('div');\n" +
                        "    div.style=\"background: white; border: 1px black solid; padding: 0.4rem\";\n" +
                        "    div.innerHTML=\"<a href=\\\"http://www.66.rospotrebnadzor.ru/514/-/asset_publisher/BV9e/content/%D0%BA%D0%B0%D1%80%D1%82%D0%B0-%D0%B7%D0%B0%D0%B1%D0%BE%D0%BB%D0%B5%D0%B2%D0%B0%D0%B5%D0%BC%D0%BE%D1%81%D1%82%D0%B8-covid-19-%D0%BD%D0%B0-%D1%82%D0%B5%D1%80%D1%80%D0%B8%D1%82%D0%BE%D1%80%D0%B8%D0%B8-%D1%81%D0%B2%D0%B5%D1%80%D0%B4%D0%BB%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B8-%D0%BE%D0%B1%D0%BB%D0%B0%D1%81%D1%82%D0%B8-%D0%B2-%D1%81%D0%B2%D1%8F%D0%B7%D0%B8-%D1%81-%D0%BE%D1%81%D0%BE%D0%B1%D0%B5%D0%BD%D0%BD%D0%BE%D1%81%D1%82%D1%8F%D0%BC%D0%B8-%D1%84%D0%BE%D1%80%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F-%D0%B1%D0%B0%D0%B7-%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85-%D0%B8%D0%BD%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%86%D0%B8%D1%8F-%D0%BD%D0%B0-%D0%BA%D0%B0%D1%80%D1%82%D0%B5-%D0%BF%D1%80%D0%B8%D0%B2%D0%BE%D0%B4%D0%B8%D1%82%D1%81%D1%8F-%D0%BF%D0%BE-%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D1%8E-%D0%BD%D0%B0-%D0%BF%D1%80%D0%B5%D0%B4%D1%8B%D0%B4%D1%83%D1%89%D0%B8%D0%B8-%D0%B4%D0%B5%D0%BD%D1%8C-*%D0%B4%D0%BB%D1%8F-%D0%BC%D0%B0%D0%BB%D1%8B%D1%85-%D0%BD%D0%B0%D1%81%D0%B5%D0%BB%D0%B5%D0%BD%D0%BD%D1%8B%D1%85-%D0%BF%D1%83%D0%BD%D0%BA%D1%82%D0%BE%D0%B2-%D0%B3%D0%B5%D0%BE%D0%BB%D0%BE%D0%BA%D0%B0%D1%86%D0%B8%D1%8F-%D1%8F%D0%B2%D0%BB%D1%8F%D0%B5%D1%82%D1%81%D1%8F-%D0%BF%D1%80%D0%B8%D0%B1%D0%BB%D0%B8%D0%B7%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE%D0%B8\\\">Использованы данные с сайта Роспотребнадзора</a>\";\n" +
                        "    return div;\n" +
                        "};\n" +
                        "T=[\n" +
                        "[58.049931,65.253083],[58.049774,65.261303],[58.047035,65.286761],[58.063102,65.21265],[58.058917,65.230778],[58.10017,65.268211],[58.048983,65.230167],[58.047211,65.275083],[58.069319,65.244558],[58.035892,65.265794],[58.029626,65.324625],[58.030618,65.278425],[58.020919,65.299724],[58.045782,65.273071],[58.036502,65.25885],[58.036502,65.25885],[58.045087,65.284964],[58.046921,65.277059],[58.037303,65.246705],[58.040666,65.242393],[58.039065,65.274086],[58.039065,65.274086],[58.056326,65.236078],[58.032724,65.296822],[58.055184,65.213036],[58.036688,65.32502],[58.032629,65.295825],[58.052993,65.224849],[58.048778,65.253487],[58.032023,65.324634],[58.041838,65.256182]\n" +
                        "];\n" +
                        "var searchControl = L.esri.Geocoding.geosearch().addTo(mymap);\n" +
                        "var results = L.layerGroup().addTo(mymap);\n" +
                        "legend.addTo(mymap);\n" +
                        "let markers = L.markerClusterGroup();\n" +
                        "        T.forEach(function(item){\n" +
                        "        L.marker([item[0],item[1]]).addTo(mymap).bindPopup('<p>'+item[3]+' -' +item[2] +'</p><a href=\"'+ item[5] +'\"><img src='+ item[4] +' width=\"189\" height=\"255\" alt=\"lorem\">')\n" +
                        "        });\n" +
                        "\n" +
                        "</script>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "</body></html>";

        wv.loadData(customHtml, "text/html; charset=UTF-8", null);
*/
        wv.loadUrl("http://oddball-stomachs.000webhostapp.com/libs/cOVID1.html");
        return view;
    }
}
