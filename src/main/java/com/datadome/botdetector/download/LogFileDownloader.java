package com.datadome.botdetector.download;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogFileDownloader {

    private static final String DOWNLOAD_URL = "http://www.almhuette-raith.at/apache-log/access.log";
    private static final String FILENAME = "access.log";

    /**
     * Executes task sequentially every 10ms after an initial delay of 10s
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
    @Transactional
    public void downloadFileFromUrl() throws IOException, URISyntaxException {
        log.info("Starting download of log file file from URL");
        File outputFile = new File(FILENAME);
        URLConnection downloadFileConnection = resumableDownloadConnection(outputFile);
        download(downloadFileConnection, outputFile);
    }

    private URLConnection resumableDownloadConnection(File outputFile) throws IOException, URISyntaxException {
        long existingFileSize;
        URLConnection downloadFileConnection = new URI(DOWNLOAD_URL).toURL().openConnection();

        if (outputFile.exists() && downloadFileConnection instanceof HttpURLConnection) {
            HttpURLConnection httpFileConnection = (HttpURLConnection) downloadFileConnection;
            HttpURLConnection tmpFileConn = (HttpURLConnection) new URI(DOWNLOAD_URL).toURL()
                    .openConnection();
            tmpFileConn.setRequestMethod("HEAD");
            long fileLength = tmpFileConn.getContentLengthLong();

            existingFileSize = outputFile.length();
            if (existingFileSize < fileLength) {
                httpFileConnection.setRequestProperty("Range", "bytes=" + existingFileSize + "-" + fileLength);
            } else {
                throw new IOException("File Download already completed.");
            }
        }
        return downloadFileConnection;
    }

    private void download(URLConnection downloadFileConnection, File outputFile) throws IOException {
        long bytesDownloaded = 0L;
        try (InputStream is = downloadFileConnection.getInputStream(); OutputStream os = new FileOutputStream(outputFile, true)) {
            byte[] buffer = new byte[1024];
            int bytesCount;
            while ((bytesCount = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesCount);
                bytesDownloaded += bytesCount;
            }
        }
        log.info("Finished downloading to {} with bytes number: {}", outputFile.getName(), bytesDownloaded);
    }

}
