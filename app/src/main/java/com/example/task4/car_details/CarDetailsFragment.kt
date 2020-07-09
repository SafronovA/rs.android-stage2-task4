package com.example.task4.car_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.task4.R
import com.example.task4.ViewModelFactory
import com.example.task4.database.CarDatabase
import com.example.task4.databinding.FragmentCarDetailsBinding

class CarDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCarDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_car_details, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = CarDatabase.getInstance(application).carDatabaseDao

        val viewModelFactory = ViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(CarDetailsViewModel::class.java)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        viewModel.insertNewCar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                viewModel.insertNewCarCard()
                this.findNavController()
                    .navigate(CarDetailsFragmentDirections.actionCarDetailsFragmentToCarListFragment())
            }
        })
        return binding.root

    }
}