package com.ppb.travellin.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ppb.travellin.R
import com.ppb.travellin.databinding.CardTicketTripBinding
import com.ppb.travellin.services.calendar.CalendarModule
import com.ppb.travellin.services.database.ticket_history.TicketHistoryTable
import java.util.Calendar
import java.util.Date

class TripHistoryAdapter(
    private val list: List<TicketHistoryTable>,
    private val context: Context
) : RecyclerView.Adapter<TripHistoryAdapter.TripHistoryAdapter>() {

    inner class TripHistoryAdapter(
        private val binding : CardTicketTripBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ticketHistoryTable: TicketHistoryTable) {
            marqueeSupport()
            with(binding) {


                theCard.visibility = View.VISIBLE
                buttonActive.visibility = View.VISIBLE

                val calendar = Calendar.getInstance()
                calendar.time = ticketHistoryTable.tanggalKeberangkatan!!

                var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                var month = calendar.get(Calendar.MONTH)
                var year = calendar.get(Calendar.YEAR)

                val intervalCheck = CalendarModule.getIntervalDay(dayOfMonth, month, year)
                if (intervalCheck > 0) {
                    buttonProcess.visibility = View.GONE
                    buttonActive.visibility = View.VISIBLE
                } else {
                    buttonProcess.visibility = View.VISIBLE
                    buttonActive.visibility = View.GONE
                }


                setDate(ticketHistoryTable.tanggalKeberangkatan, textKeretaTanggal)
                textKeretaNama.text = ticketHistoryTable.kereta
                textKeretaKelas.text = ticketHistoryTable.kelas
                textTotalHarga.text = "Rp${ticketHistoryTable.harga.toString()}"

                kotaAsal.text = ticketHistoryTable.kotaAsal
                kotaTujuan.text = ticketHistoryTable.kotaTujuan
                stasiunAsal.text = ticketHistoryTable.stasiunAsal
                stasiunTujuan.text = ticketHistoryTable.stasiunTujuan


                ticketHistoryTable.paketBinary.forEachIndexed { index, digit ->
                    if (digit == '0') {
                        when(index) {
                            0 -> paket1.visibility = View.GONE
                            1 -> paket2.visibility = View.GONE
                            2 -> paket3.visibility = View.GONE
                            3 -> paket4.visibility = View.GONE
                            4 -> paket5.visibility = View.GONE
                            5 -> paket6.visibility = View.GONE
                            6 -> paket7.visibility = View.GONE
                        }
                    } else {
                        when(index) {
                            0 -> paket1.visibility = View.VISIBLE
                            1 -> paket2.visibility = View.VISIBLE
                            2 -> paket3.visibility = View.VISIBLE
                            3 -> paket4.visibility = View.VISIBLE
                            4 -> paket5.visibility = View.VISIBLE
                            5 -> paket6.visibility = View.VISIBLE
                            6 -> paket7.visibility = View.VISIBLE
                        }
                    }
                }

                buttonActive.setOnClickListener {

                    calendar.time = ticketHistoryTable.tanggalKeberangkatan!!

                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                    month = calendar.get(Calendar.MONTH)
                    year = calendar.get(Calendar.YEAR)

                    val interval = CalendarModule.getIntervalDay(dayOfMonth, month, year)
                    Toast.makeText(context, "Perjalanan dimulai $interval hari lagi", Toast.LENGTH_SHORT).show()
                }

            }

        }

        private fun marqueeSupport() {
            binding.textKeretaTanggal.isSelected = true
            binding.paket1.isSelected = true
            binding.paket2.isSelected = true
            binding.paket3.isSelected = true
            binding.paket4.isSelected = true
            binding.paket5.isSelected = true
            binding.paket6.isSelected = true
            binding.paket7.isSelected = true
        }

        private fun setDate(date: Date?, view: TextView) {
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date

                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                val months = context.resources.getStringArray(R.array.months)

                val theDate = "$dayOfMonth ${months[month]} $year"
                view.text =  theDate
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHistoryAdapter {
        val binding = CardTicketTripBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TripHistoryAdapter(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TripHistoryAdapter, position: Int) {
        holder.bind(list[position])
    }


}
