package com.ppb.travellin.pesan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ppb.travellin.R
import com.ppb.travellin.databinding.CardPaketKeretaBinding
import com.ppb.travellin.services.model.PaketModel

class PaketAdapter(
    private val listPaket: List<PaketModel>,
    private val onIsChecked: (PaketModel) -> Unit,
    private val onNotChecked: (PaketModel) -> Unit
) : RecyclerView.Adapter<PaketAdapter.PaketViewHolder>() {

    inner class PaketViewHolder(private val binding: CardPaketKeretaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(paketModel: PaketModel) {
            with(binding) {
                textViewPaket.text = paketModel.name
                textViewStatus.text = if (paketModel.status) "Paket Aktif" else "Aktifkan Paket"
                textViewDescription.text = paketModel.description

                setIcon(paketModel.id)

                checkboxPaket.isChecked = paketModel.status
                checkboxPaket.setOnClickListener {
                    if (checkboxPaket.isChecked) {
                        onIsChecked(paketModel)
                    } else {
                        onNotChecked(paketModel)
                    }
                    paketModel.status = !paketModel.status
                    textViewStatus.text = if (paketModel.status) "Paket Aktif" else "Paket tidak aktif"
                }
            }
        }

        private fun setIcon(id: Int) {
            val drawableId  = when(id) {
                1 -> R.drawable.outline_vrpano_24
                2 -> R.drawable.outline_local_cafe_24
                3 -> R.drawable.outline_brunch_dining_24
                4 -> R.drawable.outline_movie_24
                5 -> R.drawable.outline_fastfood_24
                6 -> R.drawable.flowsheet
                7 -> R.drawable.outline_accessible_24
                else -> R.drawable.outline_vrpano_24
            }


            binding.iconPaket.load(drawableId) {
                crossfade(true)
                crossfade(1000)
                size(24, 24)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaketViewHolder {
        val binding = CardPaketKeretaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaketViewHolder(binding)
    }

    override fun getItemCount(): Int = listPaket.size

    override fun onBindViewHolder(holder: PaketViewHolder, position: Int) {
        holder.bind(listPaket[position])
    }
}