package com.example.gabim.godiva;

import android.*;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private final LatLng KIEL = new LatLng(53.551, 9.993);

    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoogleMap mMap;
    private Marker marker;

    public MapFragment() {
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.d("MyMap", "onResume");
//        setUpMapIfNeeded();
//    }
//
//    private void setUpMapIfNeeded() {
//
//        if (mMap == null) {
//
//            Log.d("MyMap", "setUpMapIfNeeded");
//
//            getMapAsync(this);
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //linha abaixo mostra as construções do mapa//
        mMap.setBuildingsEnabled(true);
        //habilita uma barra de ferramentas no canto direito da tela quando seleciona um ponto no mapa//
        mMap.getUiSettings().setMapToolbarEnabled(true);
        //habilita o botão de mais e menos de zoom//
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng satc = new LatLng(-28.702184, -49.405024);
        LatLng cedup = new LatLng(-28.700020, -49.409788);
        LatLngBounds criciuma = new LatLngBounds(
                new LatLng(-28.796626, -49.483249), new LatLng(-28.62868, -49.245116)
        );
        /*mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cedup, 15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(criciuma.getCenter(), 15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(criciuma, 0));
    }

    private void setUpMap() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setMapToolbarEnabled(true);


        Marker hamburg = mMap.addMarker(new MarkerOptions().position(HAMBURG)
                .title("Hamburg"));
        Marker kiel = mMap.addMarker(new MarkerOptions()
                .position(KIEL)
                .title("Kiel")
                .snippet("Kiel is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_launcher)));

        hamburg.isVisible();
        kiel.setVisible(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
}
