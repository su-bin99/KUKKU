package com.example.kukku

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Transformations.map
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_gyoyang.*
import kotlinx.android.synthetic.main.fragment_ku_map.*

class Fragment_KuMap : Fragment() {
    private var root: View? = null
    lateinit var googleMap:GoogleMap
    var checkNum = 0
    var loc = LatLng(37.541554, 127.076360)

    lateinit var maplist :Array<String>
    lateinit var adapter :ArrayAdapter<String>
    lateinit var checkBox : ArrayList<CheckBox>
    lateinit var mymap : Map<CheckBox, LatLng>

    val markMap = mutableMapOf<LatLng, Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_ku_map, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        init()
        initcheckBtn()
    }

    override fun onPause() {
        super.onPause()
        for(check in checkBox){
            check.isChecked = false
        }
    }

    fun init(){
        maplist = resources.getStringArray(R.array.maps)
        adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            maplist
        )
        completeText.setAdapter(adapter)
        checkBox = arrayListOf<CheckBox>(
            check1, check2, check3, check4, check5, check6, check7, check8, check9, check10,
            check11, check12, check13, check14, check15, check16, check17, check18, check19, check20,
            check21, check22, check23, check24, check25, check26, check27, check28, check29
        )
        mymap = mapOf<CheckBox, LatLng>(
            check1 to LatLng(37.543170, 127.075132), //행정관
            check2 to LatLng(37.544276, 127.076076), //경영관
            check3 to LatLng(37.544136, 127.075276), //상허연구관
            check4 to LatLng(37.544022, 127.074135),//교육과학관
            check5 to LatLng(37.542778, 127.073088),//예술문화관
            check6 to LatLng(37.542516, 127.074658),//언어교육원
            check7 to LatLng(37.542348, 127.075549),//박물관
            check8 to LatLng(37.541694, 127.075052),//법학관
            check9 to LatLng(37.542002, 127.073809),//상허기념도서관
            check10 to LatLng(37.541502, 127.072201),//의생명과학연구관
            check11 to LatLng(37.541001, 127.074518),//생명과학관
            check12 to LatLng(37.540299, 127.074336),//동물생명과학관
            check13 to LatLng(37.540261, 127.073524),//입학정보관
            check14 to LatLng(37.540002, 127.073300),//산학협동관
            check15 to LatLng(37.539234, 127.074702),//수의학관
            check16 to LatLng(37.543554, 127.077413),//새천년관
            check17 to LatLng(37.543649, 127.078372),//건축관
            check18 to LatLng(37.543356, 127.078176),//해봉부동산학관
            check19 to LatLng(37.542774, 127.078155),//인문학관
            check20 to LatLng(37.541867, 127.077892),//학생회관
            check21 to LatLng(37.541630, 127.078796),//공학관
            check22 to LatLng(37.540597, 127.079506),//신공학관
            check23 to LatLng(37.541560, 127.080367),//과학관
            check24 to LatLng(37.541174, 127.081655),//창의관
            check25 to LatLng(37.539754, 127.077263),//국제학사
            check26 to LatLng(37.539469, 127.077833),//쿨하우스
            check27 to LatLng(37.541252, 127.082462),//건대부중
            check28 to LatLng(37.540047, 127.080317),//건대부고
            check29 to LatLng(37.540344, 127.071753)//건국대학교병원
        )
        checkBtn.setOnClickListener {
            val str = completeText.text.toString()
            if(maplist.contains(str)){
                completeText.text = null
                val index = maplist.indexOf(str)
                checkBox.get(index).isChecked = true
            }else{
                Toast.makeText(requireActivity(), "없는 건물입니다.", Toast.LENGTH_SHORT).show()
                completeText.text = null
            }
        }
    }

    fun initMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.ku_map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)
        }
    }

    fun initcheckBtn(){
        for( i in checkBox.indices){
            val cbox = checkBox.get(i)
            cbox.setOnCheckedChangeListener{ buttonView, isChecked ->
                if(isChecked){
                    checkNum += 1
                    val newloc = mymap.getValue(cbox)
                    setCam_addMark(newloc, i)
                }else{
                    checkNum -=1
                    val oldloc = mymap.getValue(cbox)
                    delMark(oldloc)
                }
            }
        }
    }

    fun setCam_addMark(newloc :LatLng, index : Int){
        if(checkNum == 1){
            loc = newloc
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f))
            addMark(newloc, index)
        }else{
            //카메라 이동
            val lat = (loc.latitude*(checkNum-1) + newloc.latitude)/checkNum
            val lon = (loc.longitude*(checkNum-1) + newloc.longitude)/checkNum
            loc = LatLng(lat, lon)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))

            //마크 추가
            addMark(newloc, index)
        }
    }

    fun delMark(delloc:LatLng ){
        if(checkNum == 0){
            loc = LatLng(37.541554, 127.076360)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
        }else{
            val newlat = (loc.latitude*(checkNum+1)-delloc.latitude)/checkNum
            val newlon = (loc.longitude*(checkNum+1)-delloc.longitude)/checkNum
            loc = LatLng(newlat, newlon)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
        }
        markMap.get(delloc)?.remove()
    }

    fun addMark(newloc:LatLng, index : Int){
        val options = MarkerOptions()
        options.position(newloc)
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        options.title(maplist[index])

        if( index >27) options.snippet((index +2).toString() + "번 건물")
        else options.snippet((index+1).toString() + "번 건물")

        val mk1 = googleMap.addMarker(options)
        markMap.put(newloc, mk1)
        mk1.showInfoWindow()
    }
}
