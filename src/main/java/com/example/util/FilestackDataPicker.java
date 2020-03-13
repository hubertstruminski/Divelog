package com.example.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilestackDataPicker {

    private String apiKey;
    private String policy;
    private String signature;

    public FilestackDataPicker(String apiKey, String policy, String signature) {
        this.apiKey = apiKey;
        this.policy = policy;
        this.signature = signature;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static FilestackDataPicker build(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<String> keys = new ArrayList<>();
        try {
            String line = br.readLine();

            while (line != null) {
                String[] splitedLine = line.split(":");
                keys.add(splitedLine[1]);
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return new FilestackDataPicker(keys.get(0), keys.get(1), keys.get(2));
    }
}
