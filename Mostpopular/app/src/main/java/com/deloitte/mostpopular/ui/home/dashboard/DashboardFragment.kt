package com.deloitte.mostpopular.ui.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.deloitte.mostpopular.R
import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.databinding.FragmentDashboardBinding
import com.deloitte.mostpopular.ui.util.extension.showToast
import com.deloitte.mostpopular.ui.home.dashboard.DashboardViewModel.DashboardViewAction.*
import com.deloitte.mostpopular.ui.home.dashboard.DashboardViewModel.DashboardViewEvent.*
import com.deloitte.mostpopular.ui.home.dashboard.DashboardViewModel.DashboardViewState.*
import com.deloitte.mostpopular.ui.home.dashboard.adapter.NewsAdapter
import com.deloitte.mostpopular.ui.home.dashboard.filter.FilterBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    @Inject
    lateinit var requestManager: RequestManager

    private val viewModel: DashboardViewModel by viewModels()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null


    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        setupRecyclerView()
        initObserveViewModel()
        initMenu()
    }

    private fun initObserveViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        showProgress(it is Loading)
                        when (it) {
                            is Success -> setNewsInList(it.newsList)
                            Idle -> resetUi()
                            else -> {}
                        }
                    }
                }
                launch {
                    viewModel.uiEvent.collect {
                        when (it) {
                            OpenFilter -> openFilter()
                            is OpenNewsDetails -> openNewsDetails(it.news)
                            is ShowToast -> showToast(it.message)
                        }
                    }
                }
            }
        }
    }

    private fun openNewsDetails(news: News) {
        val action = DashboardFragmentDirections.actionNavigationDashboardToNewsDetailsFragment(
            news.date,
            news.description,
            news.imageUrl
        )
        findNavController().navigate(action)
    }

    private fun initListener() {
        binding.swiperefresh.setOnRefreshListener {
            viewModel.processUiAction(OnRefresh)
        }

    }

    private fun setupRecyclerView() {

        if (!::newsAdapter.isInitialized) {
            newsAdapter = NewsAdapter(requestManager, ::showNewsDetail)
        }

        binding.recyclerView.run {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isNestedScrollingEnabled = false

            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }
    }

    private fun initMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here

                menuInflater.inflate(R.menu.menu_toolbar, menu)

                searchItem = menu.findItem(R.id.action_search)
                searchItem?.collapseActionView()
                searchView = searchItem?.actionView as SearchView

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query ?: return true
                        viewModel.processUiAction(Search(query))
                        searchView?.clearFocus()

                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText ?: return true

                        viewModel.processUiAction(Search(newText))

                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_filter -> {
                        viewModel.processUiAction(FilterClicked)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun openFilter() {
        setFragmentResultListener(FilterBottomSheetFragment.REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(FilterBottomSheetFragment.RESULT_KEY)
            result?.let {
                viewModel.processUiAction(FilterApply(result))
            }
        }
        val action = DashboardFragmentDirections.actionNavigationDashboardToNavigationFilter()
        findNavController().navigate(action)
    }

    private fun setNewsInList(newsList: List<News>) {
        newsAdapter.submitList(newsList) {
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun showProgress(show: Boolean) {
        binding.swiperefresh.isRefreshing = show
    }

    private fun showNewsDetail(news: News) {
        viewModel.processUiAction(NewsDetailClicked(news))
    }

    private fun resetUi() {
        searchItem?.collapseActionView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}