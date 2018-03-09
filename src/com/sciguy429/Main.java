package com.sciguy429;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Main {

    static SyndFeed feed;

    public static void main(String[] args) {
        InputStream is = null;
        try {
            URLConnection openConnection = new URL("https://www.thebluealliance.com/event/2018mosl/feed").openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            is = openConnection.getInputStream();
            if("gzip".equals(openConnection.getContentEncoding())){
                is = new GZIPInputStream(is);
            }
            InputSource source = new InputSource(is);
            SyndFeedInput input = new SyndFeedInput();
            feed = input.build(source);
        } catch (IOException | FeedException e) {
            e.printStackTrace();
        }
        for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {
            int matchNumber = Integer.valueOf(entry.getTitle().substring(6));
            ArrayList<Integer> teams = new ArrayList<>();
            Document doc = Jsoup.parse(entry.getDescription().getValue());
            Elements lists = doc.select("li");
            System.out.println("Match " + matchNumber + ":");
            for (int i = 0; i < 6; i++) {
                teams.add(Integer.valueOf(lists.get(i).text()));
            }
            System.out.println("    Team R1: " + teams.get(0));
            System.out.println("    Team R2: " + teams.get(1));
            System.out.println("    Team R3: " + teams.get(2));

            System.out.println("    Team B1: " + teams.get(3));
            System.out.println("    Team B2: " + teams.get(4));
            System.out.println("    Team B3: " + teams.get(5));
        }
    }
}
