package com.project.seorigami.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.project.seorigami.R
import com.project.seorigami.adapter.KategoriAdapter
import com.project.seorigami.adapter.MitraAdapter
import com.project.seorigami.databinding.FragmentHomeBinding
import com.project.seorigami.model.response.MitraDataModel
import com.project.seorigami.util.GridItemDecoration
import com.project.seorigami.util.ItemClickListener
import com.project.seorigami.util.KeyIntent
import com.project.seorigami.util.PixelHelper
import com.project.seorigami.util.State
import com.project.seorigami.view.activity.DetailLayananActivity
import com.project.seorigami.view.activity.MapsActivity
import com.project.seorigami.viewmodel.HomeViewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private val listenerMitra = object : ItemClickListener<MitraDataModel> {
        override fun onClickItem(item: MitraDataModel) {
            val intent = Intent(requireActivity(), DetailLayananActivity::class.java)
            intent.putExtra(KeyIntent.MITRA_ID.name, item.id)
            startActivity(intent)
        }
    }

    private val listenerMapsMitra = object : ItemClickListener<String> {
        override fun onClickItem(item: String) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/?q=${item}")
            )
            startActivity(intent)
        }
    }

    private val listenerKategori = object : ItemClickListener<Int> {
        override fun onClickItem(item: Int) {
            viewModel.mitra(requireActivity(), "Malang", item)
        }
    }

    private var binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var dialog: ProgressDialog
    private var kategoriAdapter = KategoriAdapter(listenerKategori)
    private var mitraAdapter = MitraAdapter(listenerMitra, listenerMapsMitra)

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequestBuilder: LocationSettingsRequest.Builder
    private lateinit var locationCallback: LocationCallback

    private var isFirstOpen = true
    private var kabKota = ""
    private val LAT_LONG_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(requireActivity())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
        locationSettingsRequestBuilder = LocationSettingsRequest.Builder()
        locationSettingsRequestBuilder.addLocationRequest(locationRequest)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val loc = p0.lastLocation
                if (loc != null) {
                    getGeocoder(p0.lastLocation!!)
                } else {
                    Toast.makeText(requireActivity(), "Tidak dapat mendapatkan lokasi, silahkan coba lagi", Toast.LENGTH_SHORT)
                }
            }
        }

        binding?.recyclerViewKategori?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.kategoriAdapter
        }

        binding?.recyclerViewPenjahitTerdekat?.apply {
            layoutManager = GridLayoutManager(requireActivity(), resources.getInteger(R.integer.grid_count))
            itemAnimator = null
//            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
//            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
//            addItemDecoration(
//                GridItemDecoration(
//                    resources.getInteger(R.integer.grid_count),
//                    marginDp,
//                    true
//                )
//            )
            adapter = mitraAdapter
        }

        binding?.textInputEditTextKabKota?.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            startActivityForResult(intent, LAT_LONG_REQUEST_CODE)
        }

        viewModel.dataKategori.observe(requireActivity()) {
            kategoriAdapter.data = it ?: emptyList()
            kategoriAdapter.notifyDataSetChanged()

            if (it.isNotEmpty()) {
                viewModel.mitra(requireActivity(), kabKota, it.first().id)
                kategoriAdapter.selected = it.first()
            }
        }

        viewModel.dataMitra.observe(requireActivity()) {
            mitraAdapter.data = it ?: emptyList()
            mitraAdapter.notifyDataSetChanged()

            if (it.isNullOrEmpty()) {
                binding?.recyclerViewPenjahitTerdekat?.visibility = View.GONE
                binding?.linearLayoutKosong?.visibility = View.VISIBLE
            } else {
                binding?.recyclerViewPenjahitTerdekat?.visibility = View.VISIBLE
                binding?.linearLayoutKosong?.visibility = View.GONE
            }
        }

        viewModel.stateKategori.observe(requireActivity()) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                }

                State.LOADING -> {
                    showProgressDialog()
                }

                else -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.stateMitra.observe(requireActivity()) {
            when (it) {
                State.COMPLETE -> {
                    dialog.dismiss()
                }

                State.LOADING -> {
                    showProgressDialog()
                }

                else -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.errorMessage.observe(requireActivity()) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it.toString())
            }
        }

        return binding?.root
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun startLocationUpdates() {
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), locationPermissionCode)
        } else {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper())
            } else {
                Toast.makeText(requireActivity(), "Mohon aktifkan GPS anda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGeocoder(location: Location) {
        if (isFirstOpen) {
            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            var addresses: List<Address>? = emptyList()

            try {
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (!addresses.isNullOrEmpty()) {
                Log.e("subAdminArea", addresses[0].subAdminArea.toString())
                kabKota = addresses[0].subAdminArea.toString().removePrefix("Kabupaten ").removePrefix("Kota ").removeSuffix(" Regency")
                binding?.textInputEditTextKabKota?.setText(kabKota)

                viewModel.kategori(requireActivity())
                isFirstOpen = false
            } else {
                startLocationUpdates()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
                Log.e("Location", "Permission granted")
            } else {
                Toast.makeText(requireActivity(), "Tolong berikan semua izin untuk aplikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == LAT_LONG_REQUEST_CODE) {
                    Log.e("kota A", "${data?.getStringExtra(KeyIntent.KOTA_DATA.name)}")

                    binding?.textInputEditTextKabKota?.setText("${data?.getStringExtra(KeyIntent.KOTA_DATA.name)}")
                    kabKota = "${data?.getStringExtra(KeyIntent.KOTA_DATA.name)}"
                    viewModel.kategori(requireActivity())
                }
            }
        }
    }
}