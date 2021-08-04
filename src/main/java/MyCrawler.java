import com.goikosoft.crawler4j.crawler.GenericWebCrawler;
import com.goikosoft.crawler4j.crawler.Page;
import com.goikosoft.crawler4j.parser.HtmlParseData;
import com.goikosoft.crawler4j.parser.ParseData;
import com.goikosoft.crawler4j.url.WebURL;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MyCrawler extends GenericWebCrawler<Map<String, Integer>> {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");
    private final static Pattern VALID_INTERNAL_URLS = Pattern.compile("/wiki/(?!.*:[^_]).*");

    private final Map<String, Integer> seenCounts = new HashMap<>();

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        return fitsPatterns(url);
    }

    private boolean fitsPatterns(WebURL url) {
        String urlString = url.getURL().toLowerCase();
        String path = url.getPath();

        return !FILTERS.matcher(urlString).matches() &&
                urlString.startsWith("https://en.wikipedia.org/wiki/") &&
                VALID_INTERNAL_URLS.matcher(path).matches();
    }

    @Override
    public void visit(Page page) {
        ParseData parseData = page.getParseData();

        if (parseData instanceof HtmlParseData) {
            HtmlParseData d = (HtmlParseData) parseData;
            d.getOutgoingUrls()
                    .stream().filter(this::fitsPatterns)
                    .forEach(webURL -> seenCounts.merge(webURL.getURL(), 1, Integer::sum));
        }
    }

    @Override
    public Map<String, Integer> getMyLocalData() {
        return seenCounts;
    }

}
