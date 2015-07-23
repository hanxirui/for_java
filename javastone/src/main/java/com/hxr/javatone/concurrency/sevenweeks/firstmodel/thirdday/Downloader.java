package com.hxr.javatone.concurrency.sevenweeks.firstmodel.thirdday;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {写入时复制}
 * <br>
 *  
 * <p>
 * Create on : 2015年7月22日<br>
 * <p>
 * </p>
 * <br>
 * @author hanxirui<br>
 * @version javastone v1.0
 * <p>
 *<br>
 * <strong>Modify History:</strong><br>
 * user     modify_date    modify_content<br>
 * -------------------------------------------<br>
 * <br>
 */
public class Downloader extends Thread {
    private final InputStream in;
    private final OutputStream out;
    private final CopyOnWriteArrayList<ProgressListener> listeners;

    public Downloader(final URL url, final String outputFilename) throws IOException {
        in = url.openConnection().getInputStream();
        out = new FileOutputStream(outputFilename);
        listeners = new CopyOnWriteArrayList<ProgressListener>();
    }

    public void addListener(final ProgressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final ProgressListener listener) {
        listeners.remove(listener);
    }

    private void updateProgress(final int n) {
        for (ProgressListener listener : listeners)
            listener.onProgress(n);
    }

    @Override
    public void run() {
        int n = 0, total = 0;
        byte[] buffer = new byte[1024];

        try {
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
                total += n;
                updateProgress(total);
            }
            out.flush();
        } catch (IOException e) {
        }
    }

    public static void main(final String[] args) throws Exception {
        URL from = new URL("http://download.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2");
        Downloader downloader = new Downloader(from, "download.out");
        downloader.start();
        downloader.addListener(new ProgressListener() {
            public void onProgress(final int n) {
                System.out.print("\r" + n);
                System.out.flush();
            }

            public void onComplete(final boolean success) {
            }
        });
        downloader.join();
    }
}
