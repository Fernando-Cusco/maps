package ups.sistemas.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ups.sistemas.R;
import ups.sistemas.activities.MapsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private GoogleMap mMap;
    private MapView mapView;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    //donde se crean las vistas
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) rootView.findViewById(R.id.map);
        if(mapView != null) {
            //creamos el mapa
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
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
                Toast.makeText(getContext(), "Corto", Toast.LENGTH_SHORT).show();
            }
        });

        //cuando presionamos por un rato
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(getContext(), "Largo", Toast.LENGTH_SHORT).show();
            }
        });

        //cuando necesitos arrastrar el marcador
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getContext(), "empezando a arrastrar: "+marker.getPosition().latitude +" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(getContext(), "arrastrando: "+marker.getPosition().latitude +" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getContext(), "empezando a soltar: "+marker.getPosition().latitude +" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
