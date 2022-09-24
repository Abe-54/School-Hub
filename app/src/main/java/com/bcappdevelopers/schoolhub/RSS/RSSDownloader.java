package com.bcappdevelopers.schoolhub.RSS;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class RSSDownloader extends AsyncTask<Void,Void,Object> {

    Context c;
    String urlAddress;
    RecyclerView rv;

    public RSSDownloader(Context c, String urlAddress, RecyclerView rv) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        return downloadData();
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);

        if(data.toString().startsWith("Error"))
        {
            Toast.makeText(c, data.toString(), Toast.LENGTH_SHORT).show();
        }else {
            //PARSE
            new RSSParser(c, (InputStream) data,rv).execute();
        }
    }
    private Object downloadData()
    {
        Object connection= RSSConnector.connect(urlAddress);
        if(connection.toString().startsWith("Error"))
        {
            return connection.toString();
        }

        try
        {
            HttpURLConnection con= (HttpURLConnection) connection;
            int responseCode=con.getResponseCode();

            if(responseCode==con.HTTP_OK)
            {
                InputStream is=new BufferedInputStream(con.getInputStream());
                return is;
            }

            return RSSErrorTracker.RESPONSE_EROR+con.getResponseMessage();

        } catch (IOException e) {
            e.printStackTrace();
            return RSSErrorTracker.IO_EROR;
        }
    }
}