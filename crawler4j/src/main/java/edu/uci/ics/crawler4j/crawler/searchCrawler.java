package edu.uci.ics.crawler4j.crawler;

/**
 * Created by komori on 2016/09/21.
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class searchCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String hrefUrl = url.getURL();
        String href = hrefUrl.toLowerCase();
        return !FILTERS.matcher(href).matches()
                && hrefUrl.matches("http://d\\.hatena\\.ne\\.jp/Kazuhira/2015\\d\\d.+");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        logger.info("URL: {}", url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            logger.info("Number of outgoing links: {}", links.size());

            Document jsoupDoc = Jsoup.parse(html);
            String title = jsoupDoc.title();
            Element elm = jsoupDoc.select("div.body div.section").get(0);
            String text = elm.text();

            logger.info("add Solr Document.");

            Map<String, Object> document = new LinkedHashMap<>();
            document.put("url", url);
            document.put("title", title);
            document.put("contents", text);
            Solr.getInstance().addDocument(document);
        }
    }
}