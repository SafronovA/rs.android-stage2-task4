package com.example.task4.cars_list

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task4.R
import com.example.task4.ViewModelFactory
import com.example.task4.cars_list.dialog_fragments.ClearAllDialogFragment
import com.example.task4.database.CarDatabase
import com.example.task4.databinding.FragmentCarsListBinding
import com.example.task4.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class CarListFragment : Fragment() {

    private lateinit var adapter: CarListAdapter
    private lateinit var viewModel: CarListViewModel
    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val binding: FragmentCarsListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cars_list, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = CarDatabase.getInstance(application).carDatabaseDao

        val viewModelFactory =
            ViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(CarListViewModel::class.java)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        val manager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = manager

        adapter = CarListAdapter(this.requireContext(), viewModel)

        binding.recyclerView.adapter = adapter

        viewModel.navigateToCreateCarCard.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(CarListFragmentDirections.actionCarListFragmentToCarDetailsFragment())
                viewModel.onCreateCarCardNavigated()
            }
        })

        viewModel.navigateToSettings.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                startActivity(Intent(this.activity, SettingsActivity::class.java))
                viewModel.onSettingsNavigated()
            }
        })

        viewModel.clearAll.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                ClearAllDialogFragment(viewModel).show(
                    (context as AppCompatActivity).supportFragmentManager,
                    getString(R.string.clear_all_dialog_tag)
                )
                viewModel.onClearAllProcessed()
            }
        })

        viewModel.navigateToUpdateCar.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.update_feature_not_implemented),
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.onUpdateCarNavigated()
            }
        })
        return binding.root
    }

    override fun onResume() {
        val selectedOrderPreferences = preferences.getString(
            getString(R.string.order_settings_key),
            getString(R.string.order_settings_default)
        )
        viewModel.getCarsList(selectedOrderPreferences).observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.settings -> {
            viewModel.onSettingsClicked()
            true
        }
        R.id.clear_all -> {
            viewModel.onClearAllClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}