package org.example.springbootproject.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.springbootproject.dto.CurrencyDto;
import org.example.springbootproject.utils.Constants;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService extends BaseService{
    private Map<String, Float> dataCache = new HashMap<>();

    public Map<String, Object> getExchangeRate(String language) throws IOException {

        String url_str = Constants.EXCHANGE_RATE_URL + Constants.EXCHANGE_RATE_API_KEY + "/latest/" + Constants.EXCHANGE_RATE_REGION;
        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Parsing JSON response
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        JsonElement req_result = jsonobj.get("conversion_rates");
        CurrencyDto currencyDto = new CurrencyDto();
        if(language != null && language.equalsIgnoreCase(Constants.JA_LANGUAGE)) {
            currencyDto.setCode(Constants.JA_LANGUAGE);
            currencyDto.setRate(req_result.getAsJsonObject().get("JPY").getAsFloat());
        } else {
            currencyDto.setCode(Constants.EN_LANGUAGE);
            currencyDto.setRate(req_result.getAsJsonObject().get("USD").getAsFloat());
        }
        dataCache.put("rate", currencyDto.getRate());
        Map<String, Object> result = new HashMap<>();
        result.put("result", currencyDto);

        return result;
    }

    public Float getCurrentExchangeRate() {
        if(dataCache.get("rate") != null) {
            return dataCache.get("rate");
        }
        return (float) Constants.DEFAULT_EXCHANGE_RATE;
    }
}

