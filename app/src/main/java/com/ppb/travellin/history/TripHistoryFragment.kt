package com.ppb.travellin.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppb.travellin.TravellinApps
import com.ppb.travellin.databinding.FragmentTripHistoryBinding
import com.ppb.travellin.pesan.PesanActivity
import com.ppb.travellin.services.database.AppDatabaseViewModel
import com.ppb.travellin.services.database.AppDatabaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TripHistoryFragment : Fragment() {

    private val binding by lazy { FragmentTripHistoryBinding.inflate(layoutInflater) }

    private lateinit var appViewModel : AppDatabaseViewModel

    private val launcherToPesan = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("TripHistoryFragment", "onActivityResult: RESULT_OK")

            //TODO: Do something with the result

        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d("TripHistoryFragment", "onActivityResult: RESULT_CANCELED")
        } else {
            Log.d("TripHistoryFragment", "onActivityResult: RESULT_UNKNOWN")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AppDatabaseViewModelFactory((requireActivity().application as TravellinApps).appRepository)
        appViewModel = ViewModelProvider(requireActivity(), factory)[AppDatabaseViewModel::class.java]

        setFloatingActionButton()

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        with(binding) {
            lifecycleScope.launch {
                val listOfHistory = withContext(Dispatchers.IO) {
                    appViewModel.listTicketHistory()
                }

                if (listOfHistory != null) {
                    recyclerViewHistoryTrip.apply {
                        layoutManager = LinearLayoutManager(requireActivity())
                        adapter = TripHistoryAdapter(
                            list = listOfHistory,
                            context = requireActivity(),
                        )
                    }
                }
            }


        }
    }


    private fun setFloatingActionButton() {
        Log.d("DashboardFragment", "setFloatingActionButton Generated")

        binding.nestedScrollViewHistoryTrip.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY && binding.extendedFloatingActionButtonPesan.isExtended) {
                binding.extendedFloatingActionButtonPesan.shrink()
            } else if (scrollY < oldScrollY && !binding.extendedFloatingActionButtonPesan.isExtended) {
                binding.extendedFloatingActionButtonPesan.extend()
            }
        })


        binding.extendedFloatingActionButtonPesan.setOnClickListener {
            Log.d("DashboardFragment", "FAB navigate to PesanFragment")
            val intentToPesan = Intent(context, PesanActivity::class.java)
            launcherToPesan.launch(intentToPesan)
        }
    }


}