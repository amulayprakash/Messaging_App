package com.esf.quagnitia.messaging_app.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esf.quagnitia.messaging_app.Model.Data;
import com.esf.quagnitia.messaging_app.Model.MessageList;
import com.esf.quagnitia.messaging_app.Model.School;
import com.esf.quagnitia.messaging_app.Model.UserResponse;
import com.esf.quagnitia.messaging_app.R;
import com.esf.quagnitia.messaging_app.Storage.Preferences;
import com.esf.quagnitia.messaging_app.adapter.MessageAdaptor;
import com.esf.quagnitia.messaging_app.adapter.OtherMessageAdaptor;
import com.esf.quagnitia.messaging_app.util.NetworkUtils;
import com.esf.quagnitia.messaging_app.util.PaginationScrollListener;
import com.esf.quagnitia.messaging_app.webservice.ApiServices;
import com.esf.quagnitia.messaging_app.webservice.RetrofitClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMap extends Fragment implements OnMapReadyCallback {

    View rootView;
    GoogleMap gmap;
    TextView tv_AQI;
    WebView txtmsgdetail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater lf, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("check", "onCreateView");
        // Inflate the layout for this fragment
        rootView = lf.inflate(R.layout.fragment_map, container, false);

        tv_AQI = rootView.findViewById(R.id.tv_AQI);
        txtmsgdetail = rootView.findViewById(R.id.txtmsgdetail);
        txtmsgdetail.setVisibility(View.GONE);

        tv_AQI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtmsgdetail.getVisibility() == View.GONE) {
                    txtmsgdetail.setVisibility(View.VISIBLE);
                    tv_AQI.setText("AQI Explained (hide)");
                } else {
                    txtmsgdetail.setVisibility(View.GONE);
                    tv_AQI.setText("AQI Explained (show)");
                }
            }
        });

        // Gets the MapView from the XML layout and creates it
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

        return rootView;

    }

    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        loadSchoolMap();
    }

    private void setMap(final ArrayList<School> school, String levels) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.getUiSettings().setCompassEnabled(false);
        gmap.getUiSettings().setIndoorLevelPickerEnabled(false);
        gmap.getUiSettings().setMapToolbarEnabled(false);
        gmap.getUiSettings().setMyLocationButtonEnabled(false);
        gmap.getUiSettings().setTiltGesturesEnabled(false);
        gmap.getUiSettings().setRotateGesturesEnabled(false);


        if (school.size() == 1) {
            School sch = school.get(0);
            LatLng sydney = new LatLng(Double.parseDouble(sch.getLat()), Double.parseDouble(sch.getLng()));

            String abb = sch.getSchoolNameShort();

            gmap.addMarker(createMarker(getActivity(), sydney, abb, sch.getBackground()));

            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11.5f));

        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < school.size(); i++) {
                School sch = school.get(i);
                LatLng sydney = new LatLng(Double.parseDouble(sch.getLat()), Double.parseDouble(sch.getLng()));

                String abb = sch.getSchoolNameShort();

                gmap.addMarker(createMarker(getActivity(), sydney, abb, sch.getBackground()));

                builder.include(sydney);

            }

            LatLngBounds bounds = builder.build();
            LatLng center = bounds.getCenter();
            builder.include(new LatLng(center.latitude-0.001f,center.longitude-0.001f));
            builder.include(new LatLng(center.latitude+0.001f,center.longitude+0.001f));
            bounds = builder.build();

            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 11.5f));

            if (levels.contains("<html><body style='margin:0;padding:0;'>")) {
                txtmsgdetail.loadData(levels, "text/html", "utf-8");
            } else {
                txtmsgdetail.loadData("<html><body style='margin:0;padding:0;'>" + levels + "</body></html>", "text/html", "utf-8");
            }
        }
//        // Setting a custom info window adapter for the google map
//        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getActivity(), school);
//        gmap.setInfoWindowAdapter(markerInfoWindowAdapter);
//
//        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//
//
//                marker.hideInfoWindow();
//            }
//        });

//        gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
//        {
//            @Override
//            public View getInfoWindow(Marker marker)
//            {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(final Marker marker)
//            {
//                WebView webview = new WebView(getActivity());
//                webview.loadData(school.getMessage().getBody(), "text/html", "utf-8");//.loadUrl(school.getMessage().getBody());
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                AlertDialog alert = builder.setView(webview).create();
//                alert.show();
//                alert.getWindow().setLayout(500, 500);
//                mGhost.post(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        marker.hideInfoWindow();
//                    }
//                });
//                return mGhost;
//            }
//        });

//        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
//                return false;
//            }
//        });

//        // Adding and showing marker when the map is touched
//        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng arg0) {
//                gmap.clear();
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(arg0);
//                gmap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
//                Marker marker = gmap.addMarker(markerOptions);
//                marker.showInfoWindow();
//            }
//        });
    }


    public static MarkerOptions createMarker(Context context, LatLng point, String name, String color) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        int px = context.getResources().getDimensionPixelSize(R.dimen.dimension18dp);

        // circle
        int diameter = px + 4;
        Bitmap bm = Bitmap.createBitmap(diameter+2, diameter+2, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        p.setStrokeCap(Paint.Cap.BUTT);
        p.setColor(Color.BLACK);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, p);

        Paint p2 = new Paint();
        p2.setStyle(Paint.Style.FILL);
        p2.setStrokeWidth(1);
        p2.setAntiAlias(true);
        p2.setStrokeCap(Paint.Cap.BUTT);
        p2.setColor(Color.parseColor(color));
        canvas.drawCircle(diameter / 2, diameter / 2, px / 2, p2);

//        // border
//        p2.setStyle(Paint.Style.STROKE);
//        p2.setColor(Color.parseColor(color));
//        p2.setStrokeWidth(1);
//        canvas.drawCircle(diameter / 2, diameter / 2, px / 2, p);

        Paint p3 = new Paint();
        p3.setColor(Color.WHITE);
        p3.setTextSize(17f);
        p3.setAntiAlias(true);
        p3.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        p3.getTextBounds(name, 0, name.length(), bounds);
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText(name, diameter / 2, (diameter / 2) + 5, p3);

        marker.icon(BitmapDescriptorFactory.fromBitmap(bm));
        return marker;
    }

    private void loadSchoolMap() {//nikita
        try {
            if (!NetworkUtils.checkNetworkConnection(getActivity())) {
                callMapWS();
            } else {
                Toast.makeText(getActivity(), "No internet connetion!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void callMapWS() {//nikita
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("loading...");
            pd.setCanceledOnTouchOutside(false);

            ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);

            Call<UserResponse> call;//nikita

            pd.show();
            String Schid = "0";
            if (new Preferences(getActivity()).getString("UT").equalsIgnoreCase("admin")) {
                Schid = "0";
            } else {
                Schid = "0";//new Preferences(getActivity()).getString("schoolId");
            }
            call = apiService.getSchoolMap(new Preferences(getActivity()).getAgentId(getActivity()), Schid, new Preferences(getActivity()).getString("SI"));


            Log.i("@nikita", "Url:" + call.request().url().toString());
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    try {
                        Log.i("@nikita", "Resp" + response);
                        pd.dismiss();
                        if (response.code() == 200) {// success
                            if (response.body() != null && response.body().getIsSessionValid().equalsIgnoreCase("true")) {//&& response.body().getError().equals("0")
//
                                setMap(response.body().getSchool(), response.body().getLevels());
                            } else if (response.body() != null) {//&& response.body().getError().equals("1")
                                if (response.body().getIsSessionValid().equalsIgnoreCase("false")) {
//                                    if (!response.body().getMessage().isEmpty()) {
                                    Toast.makeText(getActivity(), "Invalid session...", Toast.LENGTH_SHORT).show();
//                                    }
                                    new com.esf.quagnitia.messaging_app.Storage.Preferences(getActivity()).clearPreferences();
                                    Intent newIntent = new Intent(getActivity(), MainActivity.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);
                                    getActivity().finish();
                                } else {
                                }
                            }
                        } else if (response.code() == 401) {// invalid
                            if (response.errorBody() != null) {
                                JSONObject jos = new JSONObject(response.errorBody().string().trim());

                                if (jos.optString("isSessionValid").equalsIgnoreCase("true")) {

                                } else {
                                    Toast.makeText(getActivity(), "Invalid session...", Toast.LENGTH_SHORT).show();
                                    new com.esf.quagnitia.messaging_app.Storage.Preferences(getActivity()).clearPreferences();
                                    Intent newIntent = new Intent(getActivity(), MainActivity.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);
                                    getActivity().finish();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.i("@nikita", "Error" + t);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

//    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
//        private Context context;
//        private School school;
//
//        public MarkerInfoWindowAdapter(Context context, School school) {
//            this.context = context.getApplicationContext();
//            this.school = school;
//        }
//
//        @Override
//        public View getInfoWindow(Marker arg0) {
//            return null;
//        }
//
//        @Override
//        public View getInfoContents(final Marker arg0) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflater.inflate(R.layout.map_marker_info_window, null);
//
//            TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
//            TextView tv_AQI = (TextView) v.findViewById(R.id.tv_AQI);
//            TextView tvLng = (TextView) v.findViewById(R.id.txtmsgdetail);
//            tvLat.setText(school.getMessage().getSubject());
//
//
//            String[] str = school.getMessage().getBody().split("<p>");
//
//            String[] str2 = str[1].split("</p>");
//
//            String str3 = str2[0].replace("<br>","\n");
//            tvLng.setText(str3);
//            int red = Color.parseColor("" + school.getMessage().getBackground());
//            tvLng.setBackgroundColor(red);
//
//            tv_AQI.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    arg0.hideInfoWindow();
//                }
//            });
////            tvLng.getSettings().setJavaScriptEnabled(true);
////            tvLng.loadData(school.getMessage().getBody().toString(), "text/html; charset=UTF-8;", null);
////            tvLng.loadData(school.getMessage().getBody(), "text/html", "utf-8");
//
//            return v;
//        }
//    }

}
