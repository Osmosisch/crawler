import com.goikosoft.crawler4j.crawler.CrawlConfig;
import com.goikosoft.crawler4j.crawler.CrawlController;
import com.goikosoft.crawler4j.crawler.GenericCrawlController;
import com.goikosoft.crawler4j.fetcher.PageFetcher;
import com.goikosoft.crawler4j.robotstxt.RobotstxtConfig;
import com.goikosoft.crawler4j.robotstxt.RobotstxtServer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/data/crawl/root";
        int numberOfCrawlers = 3;
        int numberOfSeconds = 10;
        int maxDepth = 5;
        int nBestResultsOutput = 10;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(maxDepth);
        config.setPolitenessDelay(1000);

        GenericCrawlController<MyCrawler, Map<String, Integer>> controller = createController(config);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        controller.addSeed("https://en.wikipedia.org/wiki/Open-source_intelligence");

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<MyCrawler> factory = MyCrawler::new;

        // Start nonblocking so we
        controller.startNonBlocking(factory, numberOfCrawlers);

        // Wait for 30 seconds
        Thread.sleep(numberOfSeconds * 1000);
        controller.shutdown();
        controller.waitUntilFinish();

        List<Pair<String, Integer>> integratedResults = MapUtil.integrateResults(controller.getCrawlersLocalData());

        printOut(integratedResults, nBestResultsOutput);
    }

    private static void printOut(List<Pair<String, Integer>> integratedResults, int nBestResultsOutput) {
        IntStream.range(0, nBestResultsOutput)
                .mapToObj(integratedResults::get)
                .map(result -> result.getLeft() + ": " + result.getRight())
                .forEach(System.out::println);
    }

    private static GenericCrawlController<MyCrawler, Map<String, Integer>> createController(CrawlConfig config) throws Exception {
        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        return new GenericCrawlController<>(config, pageFetcher, robotstxtServer);
    }
}
