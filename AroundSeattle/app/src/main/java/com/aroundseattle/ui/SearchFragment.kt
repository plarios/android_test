package com.aroundseattle.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.aroundseattle.AppExecutors
import com.aroundseattle.databinding.FragmentSearchBinding
import com.aroundseattle.R
import com.aroundseattle.adapters.AutocompleteAdapter
import com.aroundseattle.adapters.VenuesListAdapter
import com.aroundseattle.api.FoursquareDataProvider
import com.aroundseattle.binding.FragmentDataBindingComponent
import com.aroundseattle.db.FavoritesDao
import com.aroundseattle.viewmodel.SearchViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var dataProvider: FoursquareDataProvider

    @Inject
    lateinit var favoritesDao: FavoritesDao

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding: FragmentSearchBinding? = null
    var adapter: VenuesListAdapter? = null

    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search,
                container,
                false,
                dataBindingComponent
        )
        binding!!.mapButton?.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.showMap())
        }
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = VenuesListAdapter(dataBindingComponent, appExecutors, favoritesDao) {
            venue ->  findNavController().navigate(SearchFragmentDirections.showVenue(venue.id))
        }
        binding?.venues?.adapter = adapter

        searchViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(SearchViewModel::class.java)
        searchViewModel.results.observe(this, Observer { result ->
            binding?.loading = false
            binding?.hasVenues = result != null && result.size > 0
            adapter?.submitList(result)
        })

        setupSearchInput()
    }

    private fun setupSearchInput() {
        val adapter = AutocompleteAdapter(requireContext(), dataProvider)
        binding?.input?.setAdapter(adapter)
        binding?.input?.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val venue = adapter.getItem(position)
                if (venue != null) {
                    binding?.input?.setText(venue.name)
                    search(binding?.input)
                }
            }
        }
        binding?.input?.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(view)
                true
            } else {
                false
            }
        }
        binding?.input?.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search(view)
                true
            } else {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun search(view: View?) {
        binding?.loading = true
        binding?.hasVenues = false
        hideKeyboard(view?.windowToken ?: activity?.currentFocus?.windowToken)
        searchViewModel.setQuery(binding?.input?.text.toString())
    }

    private fun hideKeyboard(windowToken : IBinder?) {
        if (binding?.input!!.isPopupShowing()) {
            binding?.input!!.dismissDropDown()
        }
        if (windowToken != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}
