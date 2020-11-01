package com.example.mymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.maps.android.SphericalUtil;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    Spinner spinner;

    double distance;

    Normal normal;
    Satelite satelite;
    Hibrido hibrido;
    Terreno terreno;

    LatLng myloc= new LatLng(-18.0237082,-70.2673986);
    LatLng Roma = new LatLng(41.902609, 12.494847);
    LatLng Tokyo = new LatLng(35.680513, 139.769051);
    LatLng Berlin = new LatLng(52.516934, 13.403190);
    LatLng Paris= new LatLng(48.843489, 2.355331);


    private GoogleMap mMap;
    private  static final  String TAG ="Estilo del mapa";
    private static  final int REQUEST_LOCATION_PERMISSION =1;
   


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map_options, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     switch (item.getItemId()){
         case R.id.normal_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
             break;
         case R.id.hybrid_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
             break;
         case R.id.satellite_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
             break;
         case R.id.terrain_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
             break;
         default:
             return super.onOptionsItemSelected(item);

     }
return  true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        spinner =findViewById(R.id.spinner);
        normal = new Normal();
        satelite = new Satelite();
        hibrido = new Hibrido();
        terreno = new Terreno();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MapsActivity.this,
        R.layout.custom_spinner,
        getResources().getStringArray(R.array.fragments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        setFragment(normal);
                        distance = SphericalUtil.computeDistanceBetween(myloc, Tokyo);
                        Toast.makeText(MapsActivity.this,"LA DISTANCIA DESDE SU UBICACION ES DE"+":"+" "+ distance/1000 +" "+ "KILOMETROS", Toast.LENGTH_LONG).show();

                        Log.e("distancia", "distancia en km ="+distance/1000);
                        break;
                    case 1:
                        setFragment(satelite);
                        distance = SphericalUtil.computeDistanceBetween(myloc, Berlin);
                        Toast.makeText(MapsActivity.this,"LA DISTANCIA DESDE SU UBICACION ES DE"+":"+" "+ distance/1000 +" "+ "KILOMETROS", Toast.LENGTH_LONG).show();
                        Log.e("distancia", "distancia en km "+distance/1000);
                        break;
                    case 2:
                        setFragment(hibrido);
                        distance = SphericalUtil.computeDistanceBetween(myloc, Roma);
                        Toast.makeText(MapsActivity.this,"LA DISTANCIA DESDE SU UBICACION ES DE"+":"+" "+ distance/1000 +" "+ "KILOMETROS", Toast.LENGTH_LONG).show();
                        Log.e("distancia", "distancia en km ="+distance/1000);
                        break;
                    case 3:
                        setFragment(terreno);
                        distance = SphericalUtil.computeDistanceBetween(myloc, Paris);
                        Toast.makeText(MapsActivity.this,"LA DISTANCIA DESDE SU UBICACION ES DE"+":"+" "+ distance/1000 +" "+ "KILOMETROS", Toast.LENGTH_LONG).show();
                        Log.e("distancia", "distancia en km ="+distance/1000);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 16;

        try{
            boolean success = mMap.setMapStyle(MapStyleOptions
                    .loadRawResourceStyle(getApplicationContext(),R.raw.map_style));
            if(!success){
                Log.e(TAG, "Fallo al cargar estilo del mapa");
            }
        } catch (Resources.NotFoundException exception){
            Log.e(TAG, "No es posible hallar el estilo. Error: ",exception);
        }

        // Add a marker in Sydney and move the camera
        LatLng tacna = new LatLng(-18.0237082,-70.2673986);
        mMap.addMarker(new MarkerOptions().position(tacna).title("Marker in Tacna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tacna,zoom));
        GroundOverlayOptions iconOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.descarga))
                .position(tacna, 100);
        mMap.addGroundOverlay(iconOverlay);




        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();

        setInfoWindowClickToPanorama(mMap);
    }


    private  void  setMapLongClick(final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng){
                String snippet = String.format(Locale.getDefault()
                        ,"Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(latLng)
                .title(getString(R.string.app_name))
                .snippet(snippet));
            }
        });
    }

    private void  setPoiClick(final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Marker poiMarker = map.addMarker(new MarkerOptions()
                .position(pointOfInterest.latLng)
                .title(pointOfInterest.name));
                poiMarker.setTag("poi");
                poiMarker.showInfoWindow();
            }
        });
    }
    private void enableMyLocation(){
        if(ContextCompat.checkSelfPermission(getBaseContext()
        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this
            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
            ,REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    enableMyLocation();
                }
                break;
        }
    }

    private  void  setInfoWindowClickToPanorama(GoogleMap map){
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(marker.getTag() == "poi") {
                    Toast.makeText(getApplicationContext(), "Hola si me funciona"
                            ,Toast.LENGTH_SHORT).show();
                    StreetViewPanoramaOptions options = new StreetViewPanoramaOptions()
                            .position(marker.getPosition());
                    SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                            SupportStreetViewPanoramaFragment.newInstance(options);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, streetViewPanoramaFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    public void  setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}