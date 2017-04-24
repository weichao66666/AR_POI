package io.weichao.ar_poi.datasource;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.weichao.ar_poi.R;
import io.weichao.ar_poi.domain.IconMarker;
import io.weichao.ar_poi.domain.Marker;


public class LocalDataSource extends DataSource {
    private List<Marker> cachedMarkers = new ArrayList<>();
    private static Bitmap icon = null;
    private AssetManager mManager;

    public LocalDataSource(Resources res) {
        if (res == null) {
            throw new NullPointerException();
        }

        createIcon(res);
    }

    public LocalDataSource(Resources res, AssetManager manager) {
        if (res == null || manager == null) {
            throw new NullPointerException();
        }

        mManager = manager;
    }

    protected void createIcon(Resources res) {
        if (res == null) {
            throw new NullPointerException();
        }

        icon = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
    }

    public List<Marker> getMarkers() {
////        Marker atl = new IconMarker("ATL", 39.931269, -75.051261, 0, Color.DKGRAY, icon);
//        Marker atl = new IconMarker("ATL", 39.970475, 116.330105, 0, Color.DKGRAY, icon);
//        cachedMarkers.add(atl);
//
////        Marker home = new Marker("Mt Laurel", 39.95, -74.9, 0, Color.YELLOW);
//        Marker home = new Marker("Mt Laurel", 39.970475, 116.340105, 0, Color.YELLOW);
//        cachedMarkers.add(home);

        String jsonStr = getStringFromAssets("local_data.json");

        List<Marker> list = loadDataFromJson(jsonStr);
        cachedMarkers.addAll(list);

        return cachedMarkers;
    }

    public String getStringFromAssets(String fileName) {
        String Result = "";
        BufferedReader bufReader = null;
        try {
            InputStreamReader inputReader = new InputStreamReader(mManager.open(fileName), "GBK");
            bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result;
    }

    private Bitmap getBitmapFromAssets(String fileName) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = mManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;

    }

    private List<Marker> loadDataFromJson(String str) {
        LinkedList<Marker> list = new LinkedList<>();

        try {
            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                IconMarker iconMarker = null;
                try {
                    JSONObject jObj = (JSONObject) jsonArray.get(i);
                    String name = jObj.getString("name");
                    double latitude = jObj.getDouble("latitude") - 0.000836;
                    double longitude = jObj.getDouble("longitude") - 0.006556;
                    double altitude;
                    try {
                        altitude = jObj.getDouble("altitude");
                    } catch (Exception e) {
                        altitude = 0;
                    }
                    String color;
                    try {
                        color = jObj.getString("color");
                    } catch (Exception e) {
                        color = getRandomColorStr();
                    }
                    String bitmap;
                    try {
                        bitmap = jObj.getString("bitmap");
                    } catch (Exception e) {
                        bitmap = "building";
                    }
                    iconMarker = new IconMarker(name, latitude, longitude, altitude, Color.parseColor(color), getBitmapFromAssets(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (iconMarker != null) {
                    list.add(iconMarker);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private String getRandomColorStr() {
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum *= 255;
            sum += new Random().nextInt(255);
        }
        return "#" + Integer.toHexString(sum);
    }
}