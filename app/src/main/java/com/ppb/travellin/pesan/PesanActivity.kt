package com.ppb.travellin.pesan

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppb.travellin.TravellinApps
import com.ppb.travellin.authenticate.WelcomeActivity
import com.ppb.travellin.databinding.ActivityPesanBinding
import com.ppb.travellin.dialog.GlobalSheetFragment
import com.ppb.travellin.dialog.adapter.GlobalTypeAdapter
import com.ppb.travellin.dialog.viewmodel.StationsKeberangkatanViewModel
import com.ppb.travellin.dialog.viewmodel.StationsKedatanganViewModel
import com.ppb.travellin.dialog.viewmodel.TrainClassesViewModel
import com.ppb.travellin.dialog.viewmodel.TrainsViewModel
import com.ppb.travellin.services.calendar.CalendarModule
import com.ppb.travellin.services.database.AppDatabaseViewModel
import com.ppb.travellin.services.database.AppDatabaseViewModelFactory
import com.ppb.travellin.services.database.DatabaseInformationManager
import com.ppb.travellin.services.database.PaketDatabase
import com.ppb.travellin.services.database.stations.StationsTable
import com.ppb.travellin.services.database.train_classes.TrainClassesTable
import com.ppb.travellin.services.database.trains.TrainsTable
import com.ppb.travellin.services.model.PaketModel
import com.ppb.travellin.services.notification.NotifReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

class PesanActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val binding by lazy {
        ActivityPesanBinding.inflate(layoutInflater)
    }



    // Form Data
    private val basePrice = 10000
    private var previousTrainClassPrice = 0
    private var previousTrainPrice = 0
    private var previousDistancePrice = 0
    private var previousPaketPrice = 0

    private var totalPrice = MutableLiveData<Int>(0)
    private suspend fun getPrice() : Int {
        return totalPrice.asFlow().first()
    }

    private var selectedDate : Date? = null

    private val stasiunKeberangkatanViewModel by lazy {
        ViewModelProvider(this)[StationsKeberangkatanViewModel::class.java]
    }


    private val stasiunKedatanganViewModel by lazy {
        ViewModelProvider(this)[StationsKedatanganViewModel::class.java]
    }


    private val kelasKeretaViewModel by lazy {
        ViewModelProvider(this)[TrainClassesViewModel::class.java]
    }

    private val keretaViewModel by lazy {
        ViewModelProvider(this)[TrainsViewModel::class.java]
    }

    private lateinit var appViewModel : AppDatabaseViewModel

    private lateinit var listStasiun : List<StationsTable>
    private lateinit var listKelasKereta : List<TrainClassesTable>
    private lateinit var listKereta : List<TrainsTable>

    private val selectedPaket = mutableListOf<PaketModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchDataAndSetupSpinner()

        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        val backgroundColor = typedValue.data
        window.statusBarColor = backgroundColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }


        pilihTanggalKeberangkatan()
        setUpRecyclerPaket()

        observeDataForPrice()
        marqueeSupport()


        // Check Out
        binding.buttonCheckout.setOnClickListener {
            if (selectedDate == null) {
                Toast.makeText(this, "Pilih Tanggal Keberangkatan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (stasiunKeberangkatanViewModel.data.value == null) {
                Toast.makeText(this, "Pilih Stasiun Keberangkatan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (stasiunKedatanganViewModel.data.value == null) {
                Toast.makeText(this, "Pilih Stasiun Kedatangan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (kelasKeretaViewModel.data.value == null) {
                Toast.makeText(this, "Pilih Kelas Kereta Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (keretaViewModel.data.value == null) {
                Toast.makeText(this, "Pilih Kereta Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var paketBinary = "0000000"
            selectedPaket.forEach {
                paketBinary = paketBinary.replaceRange(it.id - 1, it.id, "1")
            }

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val price = getPrice()

                    appViewModel.insertTicketHistory(
                        tanggalKeberangkatan = selectedDate!!,
                        kereta = keretaViewModel.data.value!!.name!!,
                        kelas = kelasKeretaViewModel.data.value!!.name!!,
                        harga = price,
                        kotaAsal = stasiunKeberangkatanViewModel.data.value!!.city!!,
                        stasiunAsal = stasiunKeberangkatanViewModel.data.value!!.name!!,
                        kotaTujuan = stasiunKedatanganViewModel.data.value!!.city!!,
                        stasiunTujuan = stasiunKedatanganViewModel.data.value!!.name!!,
                        paketBinary = paketBinary
                    )
                }

                Toast.makeText(this@PesanActivity, "Berhasil Checkout", Toast.LENGTH_SHORT).show()

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this@PesanActivity, NotifReceiver::class.java).apply {
                    putExtra("MESSAGE", "Keretamu akan berangkat tanggal ${binding.textViewTanggalKeberangkatan} ini. ${keretaViewModel.data.value!!.name} dari ${stasiunKeberangkatanViewModel.data.value!!.name} menuju ${stasiunKedatanganViewModel.data.value!!.name}")
                    putExtra("TITLE", "Siapkan dirimu untuk Besok!")
                }
                val pendingIntent = PendingIntent.getBroadcast(this@PesanActivity, 0, intent, PendingIntent.FLAG_MUTABLE)

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDate!!.time
                    add(Calendar.DAY_OF_YEAR, -1)
                }

                if (Calendar.getInstance().after(calendar)) {
                    val message = "Keretamu akan berangkat hari ini. ${keretaViewModel.data.value!!.name} dari ${stasiunKeberangkatanViewModel.data.value!!.name} menuju ${stasiunKedatanganViewModel.data.value!!.name}"
                    (application as TravellinApps).showNotification("Your trip is TODAY!", message)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                    }
                }




                setResult(RESULT_OK)
                val intentLogin = Intent(this@PesanActivity, WelcomeActivity::class.java)

                startActivity(intentLogin)
                finish()

            }

        }

    }

    private fun marqueeSupport() {
        binding.textViewTanggalKeberangkatan.isSelected = true
        binding.buttonPilihStasiunKedatangan.isSelected = true
        binding.buttonPilihStasiunKeberangkatan.isSelected = true
        binding.buttonPilihKelasKereta.isSelected = true
        binding.textViewPilihanKereta.isSelected = true
    }

    private fun observeDataForPrice() {
        totalPrice.observe(this) {
            val newPrice = "Rp. $it"
            binding.textTotalHarga.text =  newPrice
        }
    }

    private fun setUpRecyclerPaket() {
        with(binding) {
            recyclerView.apply {
                adapter = PaketAdapter(
                    PaketDatabase.paketList,
                    onIsChecked = {
                        Log.d("PesanActivity", "setUpRecyclerPaket Checked: $it")
                        selectedPaket.add(it)
                        calculatePaketPrice()
                    },
                    onNotChecked = {
                        Log.d("PesanActivity", "setUpRecyclerPaket UnChecked: $it")
                        selectedPaket.remove(it)
                        calculatePaketPrice()
                    }
                )

                layoutManager = LinearLayoutManager(this@PesanActivity, LinearLayoutManager.HORIZONTAL, false )
            }
        }
    }

    private fun calculatePaketPrice() {
        var newPaketPrice = 0
        selectedPaket.forEach {
            newPaketPrice += it.price
        }

        lifecycleScope.launch {
            val price = getPrice()
            val tempPrice = price - previousPaketPrice
            val newPrice = tempPrice + newPaketPrice
            totalPrice.value = newPrice
            previousPaketPrice = newPaketPrice
        }

    }

    private fun fetchDataAndSetupSpinner() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val dBManager = DatabaseInformationManager(this@PesanActivity, application, this@PesanActivity)
                dBManager.checkAndUpdate()

                val factory = AppDatabaseViewModelFactory((application as TravellinApps).appRepository)
                appViewModel = ViewModelProvider(this@PesanActivity, factory)[AppDatabaseViewModel::class.java]

                listStasiun = appViewModel.listStations()
                Log.d("PesanActivity", "Stations fetched: $listStasiun")
                listKelasKereta = appViewModel.listTrainClasses()
                listKereta = appViewModel.listTrains()

                // Spinner
                withContext(Dispatchers.Main) {
                    setupSpinner()
                }

            }

        }

    }

    private fun setupSpinner() {
        stasiunKeberangkatan(listStasiun)
        stasiunKedatangan(listStasiun)
        kelasKereta(listKelasKereta)
        kereta(listKereta)
    }

    private fun kereta(list: List<TrainsTable>) {
        binding.textViewPilihanKereta.setOnClickListener {
            if (GlobalSheetFragment.isDialogOpen) {
                return@setOnClickListener
            }

            val bottomSheet = GlobalSheetFragment<TrainsTable>(
                title = "Pilih Kereta",
                globalAdapter = GlobalTypeAdapter<TrainsTable>(
                    list = list.toMutableList(),
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, train ->
                        Log.d("PesanActivity", "Kereta: $train")
                        itemBinding.textViewOptionItem.text = train.name
                    }
                ),
                searchFeatures = true
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Kereta: $it")
                keretaViewModel.data.value = TrainsTable(
                    id = it.id,
                    name = it.name,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.searchLogic = {
                lifecycleScope.launch {
                    val searchResult = appViewModel.searchTrains(it)
                    bottomSheet.globalAdapter.updateList(searchResult)
                }
            }

            bottomSheet.show(supportFragmentManager, "TrainsSheetFragment")
        }

        keretaViewModel.data.observe(this) {
            Log.d("PesanActivity", "Kereta: $it")
            binding.textViewPilihanKereta.text = it.name

            if ( it.weight != null ) {
                val newWeightPrice = basePrice * it.weight!! / 10
                lifecycleScope.launch {
                    val price = getPrice()
                    val tempPrice = price - previousTrainPrice
                    val newPrice = tempPrice + newWeightPrice
                    totalPrice.value = newPrice
                    previousTrainPrice = newWeightPrice
                }
            }
        }
    }

    private fun kelasKereta(list: List<TrainClassesTable>) {
        binding.buttonPilihKelasKereta.setOnClickListener {
            if (previousTrainPrice == 0) {
                Toast.makeText(this, "Pilih Kereta Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (GlobalSheetFragment.isDialogOpen) {
                return@setOnClickListener
            }

            val bottomSheet = GlobalSheetFragment<TrainClassesTable>(
                title = "Pilih Kelas Kereta",
                globalAdapter = GlobalTypeAdapter<TrainClassesTable>(
                    list = list.toMutableList(),
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, trainClass ->
                        Log.d("PesanActivity", "Kelas Kereta: $trainClass")
                        itemBinding.textViewOptionItem.text = trainClass.name
                    }
                ),
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Kelas Kereta: $it")
                kelasKeretaViewModel.data.value = TrainClassesTable(
                    id = it.id,
                    name = it.name,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.show(supportFragmentManager, "TrainClassesSheetFragment")
        }

        kelasKeretaViewModel.data.observe(this) {
            Log.d("PesanActivity", "Kelas Kereta: $it")
            binding.buttonPilihKelasKereta.text = it.name

            if ( it.weight != null ) {
                val newWeightPrice = basePrice * it.weight!! / 10
                lifecycleScope.launch {
                    val price = getPrice()
                    val tempPrice = price - previousTrainClassPrice
                    val newPrice = tempPrice + newWeightPrice
                    totalPrice.value = newPrice
                    previousTrainClassPrice = newWeightPrice
                }
            }
        }
    }

    private fun stasiunKedatangan(list: List<StationsTable>) {
        binding.buttonPilihStasiunKedatangan.setOnClickListener {
            if (GlobalSheetFragment.isDialogOpen) {
                return@setOnClickListener
            }

            val bottomSheet = GlobalSheetFragment<StationsTable>(
                title = "Pilih Stasiun Kedatangan",
                globalAdapter = GlobalTypeAdapter<StationsTable>(
                    list = list.toMutableList(),
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, station ->
                        Log.d("PesanActivity", "Stasiun Kedatangan: $station")
                        val option = "${station.city}, ${station.name}"
                        itemBinding.textViewOptionItem.text = option
                    }
                ),
                searchFeatures = true
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Stasiun Kedatangan: $it")
                stasiunKedatanganViewModel.data.value = StationsTable(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.searchLogic = {
                lifecycleScope.launch {
                    val searchResult = appViewModel.searchStations(it)
                    bottomSheet.globalAdapter.updateList(searchResult)
                }
            }

            bottomSheet.show(supportFragmentManager, "StationsSheetFragment")
        }

        stasiunKedatanganViewModel.data.observe(this) {
            Log.d("PesanActivity", "Stasiun Kedatangan: $it")
            binding.buttonPilihStasiunKedatangan.text = it.name

            if (stasiunKeberangkatanViewModel.data.value?.weight != null && it.weight != null) {
                val arrival = it.weight!!
                val departure = stasiunKeberangkatanViewModel.data.value!!.weight!!

                // set weight
                val newDistanceWeight: Int = abs(arrival - departure)/10 * basePrice
                lifecycleScope.launch {
                    val price = getPrice()
                    val tempPrice = price - previousDistancePrice
                    val newPrice = tempPrice + newDistanceWeight
                    totalPrice.value = newPrice
                    previousDistancePrice = newDistanceWeight
                }
            }
        }
    }

    private fun stasiunKeberangkatan(list : List<StationsTable>) {
        Log.d("PesanActivity", "Stations for departure spinner: $list")
        binding.buttonPilihStasiunKeberangkatan.setOnClickListener {
            if (GlobalSheetFragment.isDialogOpen) {
                return@setOnClickListener
            }

            val bottomSheet = GlobalSheetFragment<StationsTable>(
                title = "Pilih Stasiun Keberangkatan",
                globalAdapter = GlobalTypeAdapter<StationsTable>(
                    list = list.toMutableList(),
                    onClickItemListener = {},
                    textLabelLogic = { itemBinding, station ->
                        Log.d("PesanActivity", "Stasiun Kedatangan: $station")
                        val option = "${station.city}, ${station.name}"
                        itemBinding.textViewOptionItem.text = option
                    }
                ),
                searchFeatures = true
            )
            bottomSheet.globalAdapter.onClickItemListener = {
                Log.d("PesanActivity", "Stasiun Keberangkatan: $it")
                stasiunKeberangkatanViewModel.data.value = StationsTable(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    weight = it.weight
                )
                bottomSheet.dismiss()
            }
            bottomSheet.searchLogic = {
                lifecycleScope.launch {
                    val searchResult = appViewModel.searchStations(it)
                    bottomSheet.globalAdapter.updateList(searchResult)
                }
            }

            bottomSheet.show(supportFragmentManager, "StationsSheetFragment")
        }

        stasiunKeberangkatanViewModel.data.observe(this) {
            Log.d("PesanActivity", "Stasiun Keberangkatan: $it")
            binding.buttonPilihStasiunKeberangkatan.text = it.name

            if (stasiunKedatanganViewModel.data.value?.weight != null && it.weight != null) {
                val arrival = stasiunKedatanganViewModel.data.value!!.weight!!
                val departure = it.weight!!

                // set weight
                val newDistanceWeight: Int = abs(arrival - departure)/10 * basePrice
                lifecycleScope.launch {
                    val price = getPrice()
                    val tempPrice = price - previousDistancePrice
                    val newPrice = tempPrice + newDistanceWeight
                    totalPrice.value = newPrice
                    previousDistancePrice = newDistanceWeight
                }


            }
        }
    }

    private fun pilihTanggalKeberangkatan() {
        binding.buttonPilihTanggal.setOnClickListener {
            val datePickerDialog = com.ppb.travellin.dialog.DatePicker()
            datePickerDialog.show(supportFragmentManager, "DatePicker")

        }
    }


    override fun onDateSet(
        view: DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        Log.d("PesanActivity", "onDateSet: $dayOfMonth ${month+1} $year")
        val interval = CalendarModule.getIntervalDay(dayOfMonth, month, year)
        if (interval < 0) {
            Log.w("PesanActivity", "onDateSet: Invalid date input $dayOfMonth ${month+1} $year")
            Toast.makeText(this, "Tanggal tidak valid", Toast.LENGTH_SHORT).show()
            return
        } else if (interval > 400) {
            Log.w("PesanActivity", "onDateSet: Invalid date input $dayOfMonth ${month+1} $year")
            Toast.makeText(this, "Tanggal tidak valid : Terlalu Lama", Toast.LENGTH_SHORT).show()
            return
        }

        val months = resources.getStringArray(com.ppb.travellin.R.array.months)
        val selectedDate="$dayOfMonth ${months[month]} $year"

        val getTailText = binding.countTanggalKeberangkatan.text.toString().split(" ")
        var intervalText = interval.toString()
        getTailText.forEach {
            if (getTailText.indexOf(it) != 0) {
                intervalText += " $it"
            }
        }

        // Simpan Data
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        this.selectedDate = calendar.time

        binding.textViewTanggalKeberangkatan.text = selectedDate
        binding.countTanggalKeberangkatan.text = intervalText

    }
}