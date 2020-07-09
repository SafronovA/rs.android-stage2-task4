package com.example.task4.cars_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task4.R
import com.example.task4.cars_list.dialog_fragments.CarCardDialogFragment
import com.example.task4.database.Car
import com.example.task4.databinding.ItemContainerBinding

class CarListAdapter(
    private val context: Context,
    private val viewModel: CarListViewModel
) : ListAdapter<Car,
        CarListAdapter.ViewHolder>(CarDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(context, viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            viewModel: CarListViewModel,
            car: Car
        ) {
            binding.car = car

            binding.carCard.setOnLongClickListener {
                CarCardDialogFragment(
                    car.carId,
                    viewModel
                ).show(
                    (context as AppCompatActivity).supportFragmentManager,
                    context.getString(R.string.card_dialog_dialog_tag)
                )
                return@setOnLongClickListener true
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContainerBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class CarDiffCallback : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem.carId == newItem.carId
    }

    override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem == newItem
    }
}

class CarClickListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(car: Car) = clickListener(car.carId)
}