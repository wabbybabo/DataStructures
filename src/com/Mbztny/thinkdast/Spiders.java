package com.Mbztny.thinkdast;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class WikiPhilosophy {

    final static List<String> visited = new ArrayList<String>();
    final static WikiFetcher wf = new WikiFetcher();
    private static long lastRequestTime = -1;
	private static long minInterval = 1000;
	private static int limit = 10;
	private static String[] url = new String[limit]; 
	private static int time = 1;

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     *
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     *
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String destination = "https://baike.baidu.com/item/%E5%8F%A3%E5%B2%B8%E5%87%BA%E5%A2%83%E5%85%8D%E7%A8%8E%E5%BA%97%E7%AE%A1%E7%90%86%E6%9A%82%E8%A1%8C%E5%8A%9E%E6%B3%95?sefr=enterbtn";
        String source = "https://baike.baidu.com/";
        if(source.equals(destination))
        	System.out.println("失败");
        testConjecture(destination, source, limit);
    }

    /**
     * Starts from given URL and follows first link until it finds the destination or exceeds the limit.
     *
     * @param destination
     * @param source
     * @throws IOException
     */
    public static void testConjecture(String destination, String source, int limit) throws IOException {
        // TODO: FILL THIS IN!
    	sleepIfNeeded();
    	Connection conn = Jsoup.connect(source);
    	Document doc = conn.get();
    	Elements cotent = doc.getAllElements();
    	Elements paras = cotent.select("a");
    	Deque<Node> tack = new ArrayDeque<Node>();
    	List<Node> nodes = new ArrayList<Node>(paras);
    	Collections.reverse(nodes);
    	for (Node node : nodes) {
    		tack.push(node);
    	}
    	searchUrl(tack, limit,destination);
    	System.out.println("遍历过了地址：");
    	for (String url : visited) {
			System.out.println(url);
		}
    	
    }
	private static void sleepIfNeeded() {
		if (lastRequestTime != -1) {
			long currentTime = System.currentTimeMillis();
			long nextRequestTime = lastRequestTime + minInterval;
			if (currentTime < nextRequestTime) {
				try {
					//System.out.println("Sleeping until " + nextRequestTime);
					Thread.sleep(nextRequestTime - currentTime);
				} catch (InterruptedException e) {
					System.err.println("Warning: sleep interrupted in fetchWikipedia.");
				}
			}
		}
		lastRequestTime = System.currentTimeMillis();
	}
	private static void searchUrl(Deque<Node> tack,int limit,String destination) {

    	for (int i = 0; i < limit && !tack.isEmpty(); i++) {
    		url[i] = "http"+StringUtils.substringBefore(StringUtils.substringAfter(tack.pop().toString(), "http"), "\"");
    		visited.add(url[i]); 
    		visited.remove("http");
    	}
    		
    	for (String url : url) {
			if(url.equals(destination)) {
				System.out.println("成功");
				System.out.println("查询次数:"+time);
				System.out.println(url);
				System.out.println();
				return;
			}
		}
    	time++;
    	searchUrl(tack, limit, destination);
	}

}
