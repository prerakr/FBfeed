package com.n00bc0der.fbfeed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class second extends Fragment {


    public second() {
        // Required empty public constructor
    }

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ProgressBar spinner;

    ArrayList<HashMap<String, String>> postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new second.GetFeed().execute();
    }
    private class GetFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postList = new ArrayList<>();
            lv = (ListView) getView().findViewById(R.id.list);

            spinner = (ProgressBar)getView().findViewById(R.id.progressBar1);
            spinner.setVisibility(View.VISIBLE);


        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://graph.facebook.com/ism.srijan?fields=feed&access_token=1129225313855352|xSt8pYnGjEvLPNbib68HTl022Ms";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject root = new JSONObject(jsonStr);
                    JSONObject jsonObj = root.getJSONObject("feed");

                    // Getting JSON Array node
                    JSONArray feeds = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject f = feeds.getJSONObject(i);
                        String story=null;
                        String message=null;
                        String time = f.getString("created_time");
                        if (f.has("message"))
                            message = f.getString("message");
                        else
                        {
                            if(f.has("story"))
                                message = f.getString("story");
                        }



                        // Phone node is JSON Object


                        // tmp hash map for single contact
                        HashMap<String, String> post = new HashMap<>();

                        // adding each child node to HashMap key => value
                        post.put("time", time);
                        post.put("msg", message);


                        // adding contact to contact list
                        postList.add(post);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());


                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            spinner.setVisibility(View.GONE);
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getActivity(), postList,
                    R.layout.list_item, new String[]{ "msg","time"},
                    new int[]{R.id.msg, R.id.time});
            lv.setAdapter(adapter);
        }

    }

}
