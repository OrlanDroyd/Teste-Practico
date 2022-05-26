package com.gmail.orlandroyd.testepratico.presentation.detail

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gmail.orlandroyd.testepratico.R
import com.gmail.orlandroyd.testepratico.databinding.FragmentDetailBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment


class DetailFragment : Fragment(R.layout.fragment_detail) {

    // BINDING
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // ARGS
    private val args by navArgs<DetailFragmentArgs>()

    // MAP
    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init binding
        _binding = FragmentDetailBinding.bind(view)

        setupView()

        binding.fabBuy.setOnClickListener {
            val action = DetailFragmentDirections.actionNavDetailToConfirmDialogFragment()
            findNavController().navigate(action)
        }

        // MapView
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync() { map ->

            googleMap = map

            if (ActivityCompat.checkSelfPermission(
                    binding.root.context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    binding.root.context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 101
                    )
                }
            } else {
                googleMap.isMyLocationEnabled = true
            }
        }
    }

    private fun setupView() {
        binding.apply {
            val product = args.product

            Glide.with(this@DetailFragment)
                .load(product.thumbnail)
                .into(imageView)

            tvTitleDetails.text = product.title

            tvDetails.text = product.subtitle

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}