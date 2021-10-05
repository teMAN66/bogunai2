package city.zgr45.bogunai2.ui.slideshow

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import city.zgr45.bogunai2.R
import city.zgr45.bogunai2.databinding.FragmentSlideshowBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions





class SlideshowFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var mMap: GoogleMap
    private val TAG = SlideshowFragment::class.java.simpleName

    private val binding get() = _binding!!

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private val window: View = layoutInflater.inflate(R.layout.custom_info_contents, null)
        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)
        override fun getInfoWindow(marker: Marker): View? {
            render(marker, window)
            return null

        }

        override fun getInfoContents(marker: Marker): View? {
            render(marker, contents)
            return contents
        }
        private fun render(marker: Marker, view: View) {
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.title)

            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                titleUi.text = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(Color.RED), 0, length, 0)
                }
            } else {
                titleUi.text = ""
            }

            val snippet: String? = marker.snippet
            val snippetUi = view.findViewById<TextView>(R.id.snippet)
            if (snippet != null && snippet.length > 12) {
                snippetUi.text = SpannableString(snippet).apply {
                    setSpan(ForegroundColorSpan(Color.MAGENTA), 0, 10, 0)
                    setSpan(ForegroundColorSpan(Color.GREEN), 11, 18, 0)
                    setSpan(ForegroundColorSpan(Color.BLUE), 19, snippet.length, 0)
                }
            } else {
                snippetUi.text = ""
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle(mMap)

        val success =
            MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map_style
            )



        val o1 = LatLng(56.107541,94.557696)
        val o2 = LatLng(56.107659,94.558803)

        with(mMap) {
            addMarker(
                MarkerOptions().position(o1).title("Типография-1")
                    .snippet("25: 23 мин \n 33: 1 мин 25: 23 мин \n" +
                            " 33: 1 мин 25: 23 мин \n" +
                            " 33: 1 мин 25: 23 мин \n" +
                            " 33: 1 мин")
            )
            moveCamera(CameraUpdateFactory.newLatLng(o1))

            addMarker(
                MarkerOptions().position(o2).title("Типография-2").snippet("25: 3 мин \n 33: 4 мин")
            )
            moveCamera(CameraUpdateFactory.newLatLng(o2))

            moveCamera(CameraUpdateFactory.newLatLngZoom(o2, 17.0f))
            setInfoWindowAdapter(CustomInfoWindowAdapter())

        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle? ): View? {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}