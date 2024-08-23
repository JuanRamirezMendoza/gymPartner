package com.peakDevCol.gympartner.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.databinding.CarouselItemBinding
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import javax.inject.Inject


class CarouselHomeAdapter @Inject constructor(private var list: List<ProviderTypeBodyPart>) :
    RecyclerView.Adapter<CarouselHomeAdapter.CarouselHomeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarouselHomeViewHolder {
        val binding =
            CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselHomeViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            carouselImageView.setImageResource(selectImage(item))
            carouselTextView.text = selectText(item)
            carouselItemContainer.setOnMaskChangedListener { maskRect ->
                carouselTextView.translationX = maskRect.left
                carouselTextView.alpha = lerp(1f, 0f, maskRect.left / 80f)
            }
        }
    }

    private fun lerp(start: Float, stop: Float, fraction: Float): Float {
        return start + (stop - start) * fraction
    }

    private fun selectImage(item: ProviderTypeBodyPart): Int {
        return when (item) {
            ProviderTypeBodyPart.BACK -> R.drawable.back
            ProviderTypeBodyPart.CARDIO -> R.drawable.cardio
            ProviderTypeBodyPart.CHEST -> R.drawable.chest
            ProviderTypeBodyPart.LOWER_ARMS -> R.drawable.lower_arms
            ProviderTypeBodyPart.LOWER_LEGS -> R.drawable.lower_legs
            ProviderTypeBodyPart.NECK -> R.drawable.neck
            ProviderTypeBodyPart.SHOULDERS -> R.drawable.shoulders
            ProviderTypeBodyPart.UPPER_ARMS -> R.drawable.upper_arms
            ProviderTypeBodyPart.UPPER_LEGS -> R.drawable.upper_legs
            ProviderTypeBodyPart.WAIST -> R.drawable.waist
        }
    }

    private fun selectText(item: ProviderTypeBodyPart): String {
        return when (item) {
            ProviderTypeBodyPart.BACK -> "Espalda"
            ProviderTypeBodyPart.CARDIO -> "Cardio"
            ProviderTypeBodyPart.CHEST -> "Pecho"
            ProviderTypeBodyPart.LOWER_ARMS -> "Antebrazo"
            ProviderTypeBodyPart.LOWER_LEGS -> "Pantorrilla"
            ProviderTypeBodyPart.NECK -> "Cuello"
            ProviderTypeBodyPart.SHOULDERS -> "Hombro"
            ProviderTypeBodyPart.UPPER_ARMS -> "Brazo"
            ProviderTypeBodyPart.UPPER_LEGS -> "Pierna"
            ProviderTypeBodyPart.WAIST -> "Abdomen"
        }
    }

    override fun getItemCount(): Int = list.size

    inner class CarouselHomeViewHolder(val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root)


//Ejemplo de como controlar la vista
    /*    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            with(holder.binding) {
                root.setBackgroundColor(
                    if (position % 2 == 0) Color.TRANSPARENT
                    else Color.argb(12, 128, 128, 128)
                )

                if (item.second == null) {
                    episodeAirDate.visibility = View.GONE
                    episodeName.visibility = View.GONE
                } else {
                    episodeAirDate.visibility = View.VISIBLE
                    episodeName.visibility = View.VISIBLE
                }

                val idText = item.second?.let { episode ->
                    episodeName.text = episode.name
                    episodeAirDate.text = episode.airDate
                    "EPISODE ${item.first} (${episode.episode})"
                } ?: "EPISODE " + item.first
                episodeId.text = idText
            }
        }*/

}