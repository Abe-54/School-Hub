package com.bcappdevelopers.schoolhub.RSS;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.models.CampusFeedModel;
import com.bcappdevelopers.schoolhub.student.adapters.CampusFeedAdapter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RSSParser extends AsyncTask<Void,Void,Boolean> {

    Context c;
    InputStream is;
    RecyclerView rv;
    ArrayList<CampusFeedModel> campusFeedModels =new ArrayList<>();

    public RSSParser(Context c, InputStream is, RecyclerView rv) {
        this.c = c;
        this.is = is;
        this.rv = rv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseRSS();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        if(isParsed)
        {
            //BIND
            rv.setAdapter(new CampusFeedAdapter(c, campusFeedModels));

        }else {
            Toast.makeText(c,"Unable To Parse",Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean parseRSS()
    {
        try
        {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();

            parser.setInput(is,null);
            int event=parser.getEventType();

            String tagValue=null;
            Boolean isSiteMeta=true;

            campusFeedModels.clear();
            CampusFeedModel article = new CampusFeedModel();

            do {

                String tagName=parser.getName();

                switch (event)
                {
                    case XmlPullParser.START_TAG:
                        if(tagName.equalsIgnoreCase("item"))
                        {
                            article=new CampusFeedModel();
                            isSiteMeta=false;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        tagValue=parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if(!isSiteMeta)
                        {
                            if(tagName.equalsIgnoreCase("title"))
                            {
                                article.setTitle(tagValue);
                            }else if(tagName.equalsIgnoreCase("link"))
                            {
                                article.setLinkToArticle(tagValue);

                            }else if(tagName.equalsIgnoreCase("pubDate"))
                            {
                                article.setDate(tagValue);
                            }

                        }

                        if(tagName.equalsIgnoreCase("item"))
                        {
                            campusFeedModels.add(article);
                            isSiteMeta=true;
                        }

                        break;

                }

                event=parser.next();

            }while (event != XmlPullParser.END_DOCUMENT);

            return true;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
