package com.esf.quagnitia.messaging_app.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
    private View mGhost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater lf, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("check", "onCreateView");
        // Inflate the layout for this fragment
        rootView = lf.inflate(R.layout.fragment_map, container, false);
        // Gets the MapView from the XML layout and creates it
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

        mGhost = new View(getActivity());
        mGhost.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        mGhost.setVisibility(View.GONE);

        return rootView;

    }

    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        loadSchoolMap();
    }

    private void setMap(final School school) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(Double.parseDouble(school.getLat()), Double.parseDouble(school.getLng()));

        String abb = getFirstLetterFromEachWordInSentence(school.getSchoolName());

        gmap.addMarker(createMarker(getActivity(), sydney, abb, school.getMessage().getBackground()));
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 9));
        gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        gmap.getUiSettings().setZoomControlsEnabled(true);

        // Setting a custom info window adapter for the google map
        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getActivity(), school);
        gmap.setInfoWindowAdapter(markerInfoWindowAdapter);

        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {


                marker.hideInfoWindow();
            }
        });

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

    /**
     * Gets the first character of every word in the sentence.
     *
     * @param string
     * @return
     */
    public static String getFirstLetterFromEachWordInSentence(final String string) {
        if (string == null) {
            return null;
        }
        String res = "";
        String[] myName = string.split(" ");
        for (int i = 0; i < myName.length; i++) {
            if (!myName[i].isEmpty()) {
                res = res + myName[i].charAt(0);
            }
        }
        return res;
    }

    public static MarkerOptions createMarker(Context context, LatLng point, String name, String color) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        int px = context.getResources().getDimensionPixelSize(R.dimen.dimension50dp);
        View markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_circle_text, null);
        markerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.layout(0, 0, px, px);
        markerView.buildDrawingCache();
        TextView bedNumberTextView = (TextView) markerView.findViewById(R.id.bed_num_text_view);
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        bedNumberTextView.setText("\n" + name);
        if (color.isEmpty()) {
            bedNumberTextView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.color1)));
        } else {
            bedNumberTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        }
        markerView.draw(canvas);
        marker.icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
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
            call = apiService.getSchoolMap(new Preferences(getActivity()).getAgentId(getActivity()), new Preferences(getActivity()).getString("schoolId"), new Preferences(getActivity()).getString("SI"));


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
                                setMap(response.body().getSchool());
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

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private Context context;
        private School school;

        public MarkerInfoWindowAdapter(Context context, School school) {
            this.context = context.getApplicationContext();
            this.school = school;
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoContents(final Marker arg0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.map_marker_info_window, null);

            TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
            TextView tv_AQI = (TextView) v.findViewById(R.id.tv_AQI);
            TextView tvLng = (TextView) v.findViewById(R.id.txtmsgdetail);
            tvLat.setText(school.getMessage().getSubject());


            String[] str = school.getMessage().getBody().split("<p>");

            String[] str2 = str[1].split("</p>");

            String str3 = str2[0].replace("<br>","\n");
            tvLng.setText(str3);
            int red = Color.parseColor("" + school.getMessage().getBackground());
            tvLng.setBackgroundColor(red);

            tv_AQI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arg0.hideInfoWindow();
                }
            });
//            tvLng.getSettings().setJavaScriptEnabled(true);
//            tvLng.loadData(school.getMessage().getBody().toString(), "text/html; charset=UTF-8;", null);
//            tvLng.loadData(school.getMessage().getBody(), "text/html", "utf-8");

            return v;
        }
    }

}
