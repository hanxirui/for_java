package com.hxr.javatone.algorithms.toposearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hxr.javatone.util.JsonUtil;

/**
 * {网络拓扑路径遍历} <br>
 * <p>
 * Create on : 2015年8月21日<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author hanxirui<br>
 * @version javastone v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class Bsf {
    public static void main(final String[] args) throws IOException {

        String filepath =
            Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + "com/hxr/javatone/algorithms/toposearch/data.json";
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer json = new StringBuffer();
        String s = "";
        while ((s = br.readLine()) != null)
            json.append(s);
        br.close();
        System.out.println(json.toString());
        List<Link> links = JsonUtil.buildNonNullMapper().fromJson(json.toString(), List.class, Link.class);

        List<String> paths = new ArrayList<String>();
        List<String> starts = new ArrayList<String>();

        String start = "d37e487e-82f8-37b4-b7b7-e872de0e230f";
        String end = "b61b7ea0-1ddf-3df7-a2c4-cca6ccfa2038";
        starts.add(start);

        search(links, paths, starts, end);

        for (String t_path : paths) {
            System.out.println(t_path);
        }
    }

    public static void search(final List<Link> links, final List<String> path, final List<String> starts,
        final String end) {
        int size = links.size();
        List<String> newstarts = new ArrayList<String>();
        for (int n = 0; n < starts.size(); n++) {
            String start = starts.get(n);
            for (int i = links.size() - 1; i >= 0; i--) {
                String srcId = links.get(i).getSrcId();
                String destId = links.get(i).getDestId();
                if (srcId.equals(start)) {
                    if (path.size() == 0) {
                        path.add(srcId);
                    }
                    for (int m = path.size() - 1; m >= 0; m--) {
                        String t_string = path.get(m);
                        if (t_string.lastIndexOf(start) == (t_string.length() - start.length())) {
                            path.add(t_string + "," + destId);
                        }
                    }
                    newstarts.add(destId);
                    links.remove(i);
                }
                if (destId.equals(start)) {
                    if (path.size() == 0) {
                        path.add(destId);
                    }
                    for (int m = path.size() - 1; m >= 0; m--) {
                        String t_string = path.get(m);
                        if (t_string.lastIndexOf(destId) == (t_string.length() - destId.length())) {
                            path.add(t_string + "," + srcId);
                        }
                    }
                    newstarts.add(srcId);
                    links.remove(i);
                }

            }
            for (String t_string : path) {
                if (t_string.lastIndexOf(start) == (t_string.length() - start.length())) {
                    path.remove(start);
                    break;
                }
            }
        }
        if (links.size() == 0 || size == links.size()) {
            return;
        } else {
            search(links, path, newstarts, end);
        }
    }
}
