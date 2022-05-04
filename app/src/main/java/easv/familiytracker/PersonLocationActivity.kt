package easv.familiytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import easv.familiytracker.databinding.ActivityMapsBinding
import java.lang.Double.parseDouble

class PersonLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val FMName = intent.getStringExtra("Extra_Name").toString()
        val FMLatLong = intent.getStringExtra("Extra_latLong").toString()
        var latLong = FMLatLong.split(',')
        var latitude = parseDouble(latLong[0])
        val longitude = parseDouble(latLong[1])
        println("${latitude + longitude}")

        // Add a marker in Sydney and move the camera
        val secret = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(secret).title("${FMName}'s location."))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(secret))
    }
}