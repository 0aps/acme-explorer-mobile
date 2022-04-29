package com.mis.acmeexplorer.maps;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class IpService {

    final private OkHttpClient client;
    final private String url = "https://ifconfig.co/json";

    public IpService(OkHttpClient client) {
        this.client = client;
    }

    public Call getCurrentIp() {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return client.newCall(request);
    }

}
