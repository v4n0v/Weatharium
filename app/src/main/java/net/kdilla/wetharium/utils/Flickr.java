//package net.kdilla.wetharium.utils;
//
//import android.content.Context;
//import android.graphics.ColorSpace;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created by avetc on 15.12.2017.
// */
//
//public class Flickr extends ColorSpace.Model {
//    public interface FlickrModelResponseHandler {
//        public void onSuccess(ArrayList<HashMap<String, String>> responseArray);
//
//        public void onFailure(String error);
//    }
//
//    public Flickr(Context context) {
//        super(context);
//        setUrl("/services/rest/");
//    }
//
//
//    public void fetch(Map<String, String> inData, Map<String, Object> options, final FlickrModelResponseHandler handler) {
//        HashMap<String, String> fetchedParams = new HashMap(inData);
//        fetchedParams.put("api_key", "9c6b4a5f6ad93dafa5a5ca0ef3b2f864");
//        fetchedParams.put("extras", "url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o");
//        fetchedParams.put("format", "json");
//        fetchedParams.put("per_page", "50");
//        fetchedParams.put("safe_search", "safe");
//        fetchedParams.put("content_type", "1");
//        fetchedParams.put("media", "photos");
//        fetchedParams.put("sort", "relevance");
//        fetchedParams.put("license", "1,2,3,4,5,6");
//        super.fetch(fetchedParams, new HashMap<String, Object>() {{
//            put("host", "https://api.flickr.com");
//        }}, new ModelResponseHandler() {
//            @Override
//            public void onSuccess(Map<String, Object> responseDict) {
//                ArrayList<HashMap<String, String>> photos = ((HashMap<String, ArrayList<HashMap<String, String>>>) (responseDict.get("photos"))).get("photo");
//                if (handler != null)
//                    handler.onSuccess(photos);
//            }
//
//            @Override
//            public void onFailure(String error) {
//                if (handler != null)
//                    handler.onFailure(error);
//            }
//        });
//    }
//    @Override
//    protected JSONObject deserialize(String responseString) {
//        Pattern p = Pattern.compile(".*?\\((.*)\\)$");
//        Matcher m = p.matcher(responseString);
//        String json = null;
//        if (m.matches()) {
//            json = m.group(1);
//        }
//
//        JSONObject response = null;
//        try {
//            response = new JSONObject(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
//}