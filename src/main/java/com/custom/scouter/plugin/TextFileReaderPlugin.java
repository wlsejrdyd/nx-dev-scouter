package com.custom.scouter.plugin;

import scouter.lang.counters.CounterConstants;
import scouter.lang.plugin.PluginConstants;
import scouter.lang.plugin.annotation.ServerPlugin;
import scouter.server.Configure;
import scouter.server.CounterManager;
import scouter.server.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TextFileReaderPlugin {

    private static final String CONFIG_FILE_PATH = "custom_text_file_path";
    private static final String CONFIG_CHECK_INTERVAL = "custom_check_interval";
    
    private static long lastModified = 0;
    private static long lastCheckTime = 0;
    private static Map<String, String> fileCache = new HashMap<>();

    /**
     * 주기적으로 호출되는 Counter 플러그인
     */
    @ServerPlugin(PluginConstants.PLUGIN_SERVER_COUNTER)
    public void counter(Object objTypeCode) {
        Configure conf = Configure.getInstance();
        
        // 설정에서 파일 경로 가져오기
        String filePath = conf.getValue(CONFIG_FILE_PATH, "/tmp/sample.txt");
        long checkInterval = conf.getLong(CONFIG_CHECK_INTERVAL, 60000); // 기본 60초
        
        long currentTime = System.currentTimeMillis();
        
        // 체크 간격 확인
        if (currentTime - lastCheckTime < checkInterval) {
            return;
        }
        
        lastCheckTime = currentTime;
        
        File file = new File(filePath);
        
        if (!file.exists()) {
            Logger.println("File not found: " + filePath);
            return;
        }
        
        // 파일 변경 감지
        if (file.lastModified() > lastModified) {
            lastModified = file.lastModified();
            Logger.println("File changed, reading: " + filePath);
            readAndProcessFile(filePath);
        }
    }

    /**
     * 파일 읽기 및 처리
     */
    private void readAndProcessFile(String filePath) {
        try {
            // 전체 파일 내용 읽기
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Logger.println("File content:\n" + content);
            
            // 라인별로 읽기
            processLineByLine(filePath);
            
        } catch (IOException e) {
            Logger.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * 라인별 처리 (메트릭 파싱 예제)
     */
    private void processLineByLine(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // 빈 줄 스킵
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // 주석 스킵
                if (line.trim().startsWith("#")) {
                    continue;
                }
                
                // 파싱 로직 (예: key=value 형식)
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        
                        fileCache.put(key, value);
                        Logger.println("Parsed: " + key + " = " + value);
                        
                        // 숫자면 메트릭으로 전송 가능
                        try {
                            float numValue = Float.parseFloat(value);
                            // sendMetric(key, numValue); // 필요시 구현
                        } catch (NumberFormatException e) {
                            // 문자열 값
                        }
                    }
                }
            }
            
            Logger.println("Processed " + lineNumber + " lines");
            
        } catch (IOException e) {
            Logger.println("Error processing file: " + e.getMessage());
        }
    }

    /**
     * 캐시된 데이터 조회
     */
    public static String getCachedValue(String key) {
        return fileCache.get(key);
    }

    /**
     * 전체 캐시 조회
     */
    public static Map<String, String> getAllCachedData() {
        return new HashMap<>(fileCache);
    }
}
