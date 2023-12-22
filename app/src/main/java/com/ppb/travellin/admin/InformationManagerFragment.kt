package com.ppb.travellin.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ppb.travellin.R
import com.ppb.travellin.TravellinApps
import com.ppb.travellin.databinding.FragmentInformationManagerBinding
import com.ppb.travellin.dialog.GlobalSheetFragment
import com.ppb.travellin.dialog.adapter.GlobalTypeAdapter
import com.ppb.travellin.services.database.AppDatabaseViewModel
import com.ppb.travellin.services.database.AppDatabaseViewModelFactory
import com.ppb.travellin.services.database.DatabaseInformationManager
import com.ppb.travellin.services.database.stations.StationsTable
import com.ppb.travellin.services.database.train_classes.TrainClassesTable
import com.ppb.travellin.services.database.trains.TrainsTable
import kotlinx.coroutines.launch


class InformationManagerFragment : Fragment() {

    private val binding by lazy {
        FragmentInformationManagerBinding.inflate(layoutInflater)
    }
    private val informationViewModel : InformationViewModel by viewModels()
    private lateinit var appViewModel : AppDatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AppDatabaseViewModelFactory((requireActivity().application as TravellinApps).appRepository)
        appViewModel = ViewModelProvider(requireActivity(), factory)[AppDatabaseViewModel::class.java]

        floatingButton()
        pilihTipeDatabase()

        setupSwipeRefresh()

        binding.buttonHapusData.setOnClickListener {
            deleteData()
        }

        binding.buttonAmbilData.setOnClickListener {
            clearFields()

            if (GlobalSheetFragment.isDialogOpen) {
                return@setOnClickListener
            }

            retrieveData()
        }

        binding.buttonSimpanData.setOnClickListener {
            uploadData()
        }

        binding.buttonReset.setOnClickListener {
            clearFields()
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayoutInformationManager.setOnRefreshListener {
            lifecycleScope.launch {
                updateData()
                val currentQueue = appViewModel.countQueue()
                if (currentQueue > 0 ) {
                    val activity = requireActivity() as AppCompatActivity
                    val dBManager = DatabaseInformationManager(requireActivity(), requireActivity().application, activity)
                    dBManager.sendQueue()
                }
                binding.countLogTextView.text = currentQueue.toString()
                binding.swipeRefreshLayoutInformationManager.isRefreshing = false
            }
        }
    }

    private suspend fun updateData() {
        val activity = requireActivity() as AppCompatActivity
        val dBManager = DatabaseInformationManager(requireActivity(), requireActivity().application, activity)
        dBManager.checkAndUpdate()
    }

    private fun deleteData() {
        if (informationViewModel.isUpdate && informationViewModel.targetId != null) {
            uploadData(targetAction = "delete")
        }
    }

    private fun retrieveData() {

        lifecycleScope.launch {
            val optionDb = informationViewModel.getOption()
            if (optionDb == 0) {
                val list = appViewModel.listStations()

                val bottomSheet = GlobalSheetFragment<StationsTable>(
                    title = "Pilih Stasiun Keberangkatan",
                    globalAdapter = GlobalTypeAdapter<StationsTable>(
                        list = list.toMutableList(),
                        onClickItemListener = {},
                        textLabelLogic = { itemBinding, station ->
                            Log.d("InformationManagerFragment", "Stasiun Kedatangan: $station")
                            val option = "${station.city}, ${station.name}"
                            itemBinding.textViewOptionItem.text = option
                        }
                    ),
                    searchFeatures = true,
                )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("InformationManagerFragment", "Stasiun Keberangkatan: $it")

                    informationViewModel.optionNameValue = it.name
                    informationViewModel.optionWeightValue = it.weight.toString()
                    informationViewModel.optionCityValue = it.city

                    binding.nameInputEditText.setText(informationViewModel.optionNameValue)
                    binding.cityInputEditText.setText(informationViewModel.optionCityValue)
                    binding.weightInputEditText.setText(informationViewModel.optionWeightValue)
                    informationViewModel.isUpdate = true
                    informationViewModel.targetId = it.id

                    bottomSheet.dismiss()
                    changeIconToEditMode()
                }
                bottomSheet.searchLogic = { query ->
                    lifecycleScope.launch {
                        val results = appViewModel.searchStations(query)
                        bottomSheet.globalAdapter.updateList(results)
                    }
                }

                bottomSheet.show(requireActivity().supportFragmentManager, "StationsSheetFragment")


            } else if (optionDb == 1) {
                val list = appViewModel.listTrains()

                val bottomSheet = GlobalSheetFragment<TrainsTable>(
                    title = "Pilih Kereta",
                    globalAdapter = GlobalTypeAdapter<TrainsTable>(
                        list = list.toMutableList(),
                        onClickItemListener = {},
                        textLabelLogic = { itemBinding, train ->
                            Log.d("InformationManagerFragment", "Kereta: $train")
                            itemBinding.textViewOptionItem.text = train.name
                        }
                    ),
                    searchFeatures = true,
                )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("InformationManagerFragment", "Kereta: $it")

                    informationViewModel.optionNameValue = it.name
                    informationViewModel.optionWeightValue = it.weight.toString()

                    binding.nameInputEditText.setText(informationViewModel.optionNameValue)
                    binding.weightInputEditText.setText(informationViewModel.optionWeightValue)
                    informationViewModel.isUpdate = true
                    informationViewModel.targetId = it.id

                    bottomSheet.dismiss()
                    changeIconToEditMode()
                }
                bottomSheet.searchLogic = { query ->
                    lifecycleScope.launch {
                        val results = appViewModel.searchTrains(query)
                        bottomSheet.globalAdapter.updateList(results)
                    }
                }

                bottomSheet.show(requireActivity().supportFragmentManager, "TrainsSheetFragment")

            } else if (optionDb == 2) {
                val list = appViewModel.listTrainClasses()

                val bottomSheet = GlobalSheetFragment<TrainClassesTable>(
                    title = "Pilih Kelas Kereta",
                    globalAdapter = GlobalTypeAdapter<TrainClassesTable>(
                        list = list.toMutableList(),
                        onClickItemListener = {},
                        textLabelLogic = { itemBinding, trainClass ->
                            Log.d("InformationManagerFragment", "Kelas Kereta: $trainClass")
                            itemBinding.textViewOptionItem.text = trainClass.name
                        }
                    ),
                    searchFeatures = true,
                )
                bottomSheet.globalAdapter.onClickItemListener = {
                    Log.d("InformationManagerFragment", "Kelas Kereta: $it")

                    informationViewModel.optionNameValue = it.name
                    informationViewModel.optionWeightValue = it.weight.toString()

                    binding.nameInputEditText.setText(informationViewModel.optionNameValue)
                    binding.weightInputEditText.setText(informationViewModel.optionWeightValue)
                    informationViewModel.isUpdate = true
                    informationViewModel.targetId = it.id

                    bottomSheet.dismiss()
                    changeIconToEditMode()
                }
                bottomSheet.searchLogic = { query ->
                    lifecycleScope.launch {
                        val results = appViewModel.searchTrainClasses(query)
                        bottomSheet.globalAdapter.updateList(results)
                    }
                }

                bottomSheet.show(requireActivity().supportFragmentManager, "TrainClassesSheetFragment")
            }
        }

    }

    private fun changeIconToEditMode() {
        binding.buttonSimpanData.setImageResource(R.drawable.edit_fill1_wght400_grad0_opsz24)
    }

    private fun changeIconToInsertMode() {
        binding.buttonSimpanData.setImageResource(R.drawable.baseline_cloud_upload_24)
    }

    private fun uploadData(targetAction : String = "update") {
        lifecycleScope.launch {
            val option = informationViewModel.getOption()

            if (option == 0) {
                if (binding.cityInputEditText.text.toString().isEmpty()) {
                    binding.cityInputLayout.error = "Kota tidak boleh kosong"
                    return@launch
                }
            }

            if (binding.nameInputEditText.text.toString().isEmpty()) {
                binding.nameInputLayout.error = "Nama tidak boleh kosong"
                return@launch
            }
            if (binding.weightInputEditText.text.toString().isEmpty()) {
                binding.weightInputLayout.error = "Berat tidak boleh kosong"
                return@launch
            }

            if (informationViewModel.isUpdate) {
                appViewModel.insertQueue(
                    targetTable = binding.buttonPilihTipeDatabase.text.toString(),
                    targetAction = targetAction,
                    idTarget = informationViewModel.targetId!!,
                    name = binding.nameInputEditText.text.toString(),
                    weight = binding.weightInputEditText.text.toString().toInt(),
                    additionalData = binding.cityInputEditText.text.toString()
                )
            } else {
                appViewModel.insertQueue(
                    targetTable = binding.buttonPilihTipeDatabase.text.toString(),
                    targetAction = "insert",
                    idTarget = "",
                    name = binding.nameInputEditText.text.toString(),
                    weight = binding.weightInputEditText.text.toString().toInt(),
                    additionalData = binding.cityInputEditText.text.toString()
                )
            }

            var currentQueue = appViewModel.countQueue()
            binding.countLogTextView.text = currentQueue.toString()

            val activity = requireActivity() as AppCompatActivity
            val dBManager = DatabaseInformationManager(requireActivity(), requireActivity().application, activity)
            dBManager.sendQueue()
            currentQueue = appViewModel.countQueue()
            binding.countLogTextView.text = currentQueue.toString()

            clearFields()
        }
    }

    private fun pilihTipeDatabase() {
        binding.buttonPilihTipeDatabase.setOnClickListener {
            lifecycleScope.launch {
                val newLabel = informationViewModel.nextOptionString()
                binding.buttonPilihTipeDatabase.text = newLabel
            }
        }

        informationViewModel.option.observe(viewLifecycleOwner) {
            clearFields()
            when (it) {
                0 -> {
                    binding.cityInputLayout.visibility = View.VISIBLE
                }
                1 -> {
                    binding.cityInputLayout.visibility = View.GONE
                }
                2 -> {
                    binding.cityInputLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun clearFields() {
        binding.nameInputEditText.setText("")
        binding.weightInputEditText.setText("")
        binding.cityInputEditText.setText("")

        binding.nameInputEditText.clearFocus()
        binding.weightInputEditText.clearFocus()
        binding.cityInputEditText.clearFocus()

        binding.nameInputEditText.error = null
        binding.weightInputEditText.error = null
        binding.cityInputEditText.error = null

        informationViewModel.isUpdate = false
        informationViewModel.targetId = null

        informationViewModel.optionNameValue = null
        informationViewModel.optionWeightValue = null
        informationViewModel.optionCityValue = null

        changeIconToInsertMode()
    }

    private fun floatingButton() {
        binding.floatingActionButtonAccounts.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}