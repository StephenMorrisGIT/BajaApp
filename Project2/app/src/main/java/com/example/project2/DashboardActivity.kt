package com.example.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlin.collections.ArrayList

class DashboardActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var rpm: TextView
    // private lateinit var rpmDisplay: TextView
    private lateinit var fuel: TextView
    // private lateinit var fuelDisplay: TextView

    private lateinit var ambient: TextView
    // private lateinit var ambientDisplay: TextView
    private lateinit var Object: TextView
    // private lateinit var objectDisplay: TextView

    private lateinit var start: Button
    private lateinit var map: Button
    private lateinit var create: Button
    private var coords = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val intent = getIntent()
        val language = intent.getBooleanExtra("language", false)

        firebaseDatabase = FirebaseDatabase.getInstance()
        start = findViewById(R.id.startButton)
        map = findViewById(R.id.mapsButton)
        // create = findViewById(R.id.dataButton)
        rpm = findViewById(R.id.rpmDisplay)
        fuel = findViewById(R.id.fuelDisplay)
        ambient = findViewById(R.id.ambientDisplay)
        Object = findViewById(R.id.objectDisplay)

        if(language){
            rpm.setText("velocidad actual")
            fuel.setText("altitud actual")
            create.setText("crear datos")
            start.setText("iniciar sesión")
            map.setText("Ir al mapa")
        }
        /*
        create.setOnClickListener{
            val putRPM: Double = 17.24
            val putFuel: Double = 99.23
            // val putUser: String = "peterwalsh"
            val putLatitude = 38.907
            val putLongitude = -77.036
            var x: Double = 1.00

            for(i in 1..100) {
                x += 0.01

                val reference = firebaseDatabase.getReference("Session/December1/$i")
                val metrics = Metrics(
                    rpm = putRPM + x,
                    fuel = putFuel + x,
                    // user = putUser,
                    latitude = putLatitude + x,
                    longitude = putLongitude - x
                )
                reference.setValue(metrics)
            }

            // val time = Timestamp(System.currentTimeMillis())
            // Log.d("time", time.toString())
        }
        */
        start.setOnClickListener {
            pullData()
        }

        map.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putParcelableArrayListExtra("breadcrumbs", ArrayList(coords))
            intent.putExtra("language", language)
            startActivity(intent)
        }

    }

    private fun pullData(){
        // val coords = mutableListOf<LatLng>()
        // Need a differentiating reference that the rasberry pi can send out with the data

        val reference = firebaseDatabase.getReference("Baja_Data/2-push")

        reference.addValueEventListener(object  : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@DashboardActivity, "Failed to retrieve Data! Error: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                dataSnapshot.children.forEach() { data ->
                    val metric = data.getValue(Metrics::class.java)
                    if (metric != null) {
                        // metrics.add(metric)
                        rpmDisplay.setText(metric.RPM.toString())
                        fuelDisplay.setText(metric.RemainingFuel.toString() + "%")
                        ambientDisplay.setText(metric.ambient.toString() + "F")
                        objectDisplay.setText(metric.Object.toString() + "F")
                        // val coord: LatLng = LatLng(metric.latitude, metric.longitude)
                        // coords.add(coord)
                        Log.d("Pull data", "Data pulled successfully")
                    }
                }
            }
        })

        // var count = 0
        //firebaseDatabase.getReference("session/${count++}")
    }
}