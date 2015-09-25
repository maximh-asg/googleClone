package com.google.clone.service;

import com.google.clone.dao.IndexDao;
import com.google.clone.model.Index;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhuziv on 24-Sep-15.
 */
@Component
public class IndexServiceImpl implements IndexService {

    final public String ERROR = "Can't parse current url";
    final public String SUCCESS = "Page successfully indexed!";

    @Autowired
    IndexDao indexDao;

    private String getHtmlFromUrl(String url) throws Exception {
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        String resultLine = "";
        while ((inputLine = in.readLine()) != null)
            resultLine += inputLine;
        in.close();
        return resultLine;
    }

    private List<String> findAllLinks(String html) {
        List<String> urlList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        for (Element link : doc.select("a[href]")) {
            urlList.add(link.attr("href"));
        }
        return urlList;
    }

    private String fixLink(String url, String link) throws Exception {
        if(link.startsWith("www.") || link.startsWith("http")) {
            return link;
        }
        URI uri = new URI(url);
        String domain = uri.getHost();
        domain = domain.startsWith("www.") ? domain.substring(4) : domain;
        return "http://" + domain + link;
    }

    public String indexPage(String url, int count) {
        if(count <= 0) {
            return "";
        }
        try {
            String html = getHtmlFromUrl(url);
            String title = Jsoup.parse(html).title();
            List<String> links = findAllLinks(html);
            String text = Jsoup.parse(html).body().toString().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
            if(text.equals("") || title.equals("")) {
                return ERROR;
            }
            indexDao.save(new Index(url, title, text));
            for(String link : links) {
                if(link.startsWith("#")) {
                    continue;
                }
                indexPage(fixLink(url, link),count - 1);
            }
        } catch (Exception e) {
            return ERROR;
        }
        return SUCCESS;
    }
}
