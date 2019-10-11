package ups.sistemas.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ups.sistemas.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //zoom minimo y maximo permitido
        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(20);

        // Add a marker in Sydney and move the camera
        LatLng cuenca = new LatLng(-2.897482, -79.004537);
        mMap.addMarker(new MarkerOptions().position(cuenca).title("Marcador en Cuenca-EC").draggable(true));

        //posicion de la camara
        CameraPosition position = new CameraPosition.Builder()
                .target(cuenca)                                                                     //target objetivo
                .zoom(20)
                .bearing(145)                                                                        //orientacion de la camara hacia el este 90 grados 0-365
                .tilt(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cuenca));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Corto", Toast.LENGTH_SHORT).show();
            }
        });

        //cuando presionamos por un rato
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Largo", Toast.LENGTH_SHORT).show();
            }
        });

        //cuando necesitos arrastrar el marcador
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(MapsActivity.this, "empezando a arrastrar: "+marker.getPosition().latitude +" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(MapsActivity.this, "arrastrando: "+marker.getPosition().latitude +" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MapsActivity.this, "empezando a soltar: "+marker.getPosition().latitude +" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
