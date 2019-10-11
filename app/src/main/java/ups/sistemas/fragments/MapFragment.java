package ups.sistemas.fragments;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ups.sistemas.R;
import ups.sistemas.activities.MapsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private View rootView;
    private GoogleMap mMap;
    private MapView mapView;

    private Geocoder geocoder;
    private List<Address> address;

    private MarkerOptions marker;

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
    public void onResume() {
        super.onResume();
        checkIfGpsActivate();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //zoom minimo y maximo permitido
        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(20);

        // Add a marker in Sydney and move the camera
        LatLng cuenca = new LatLng(-2.897482, -79.004537);
        //zoom al marcador
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        //instanciamos el nuevo marcador
        marker = new MarkerOptions();
        marker.position(cuenca);
        marker.title("Mi marcador");
        marker.draggable(true);
        marker.snippet("Soy un marcador personalizado");
        marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on));
        mMap.addMarker(marker);
        //mMap.addMarker(new MarkerOptions().position(cuenca).title("Marcador en Cuenca-EC").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cuenca));
        mMap.animateCamera(zoom);
        mMap.setOnMarkerDragListener(this);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitud = marker.getPosition().latitude;
        double longitud = marker.getPosition().longitude;

        try {
            //recuperamos la lista de direcciones
            address = geocoder.getFromLocation(latitud, longitud, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String direcciones = address.get(0).getAddressLine(0);
        String ciudad = address.get(0).getLocality();
        String estado = address.get(0).getAdminArea();
        String pais = address.get(0).getCountryName();
        String codigoPostal = address.get(0).getPostalCode();
        marker.setTitle(pais+"-"+estado+"-"+ciudad);
        marker.setSnippet("Direcciones: "+direcciones+"\n"+
                "Codigo Postal: "+codigoPostal+"\n");
        marker.showInfoWindow();
    }

    private void checkIfGpsActivate(){
        //preguntar por el gps
        try {
            int gps = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            if(gps == 0){
                Toast.makeText(getContext(), "Gps no esta activado", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
