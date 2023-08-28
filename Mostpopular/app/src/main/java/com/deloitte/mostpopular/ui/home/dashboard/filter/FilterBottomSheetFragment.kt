package com.deloitte.mostpopular.ui.home.dashboard.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.deloitte.mostpopular.R
import com.deloitte.mostpopular.databinding.FragmentFilterBottomSheetBinding
import com.deloitte.mostpopular.ui.home.dashboard.filter.FilterBottomSheetViewModel.FilterViewAction.ApplyClicked
import com.deloitte.mostpopular.ui.home.dashboard.filter.FilterBottomSheetViewModel.FilterViewAction.CloseClicked
import com.deloitte.mostpopular.ui.home.dashboard.filter.FilterBottomSheetViewModel.FilterViewAction.FilterSelected
import com.deloitte.mostpopular.ui.util.extension.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class FilterBottomSheetFragment : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding: FragmentFilterBottomSheetBinding get() = _binding!!

    private val viewModel: FilterBottomSheetViewModel by viewModels()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        setupSpinner()
        initObserveViewModel()
    }

    private fun initObserveViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiEvent.collect {
                    when (it) {
                        FilterBottomSheetViewModel.FilterViewEvent.DismissFilter -> dismiss()
                        is FilterBottomSheetViewModel.FilterViewEvent.SetResult -> setResult(it.filterType)
                    }
                }

            }

        }
    }

    private fun setResult(selectedFilterType: FilterBottomSheetViewModel.FilterType?) {
        selectedFilterType?.let {

            setFragmentResult(REQUEST_KEY,  bundleOf(RESULT_KEY to selectedFilterType.period))
        }.also { dismiss() }

    }


    private fun initListener() {
        binding.buttonApplyFilter.setOnSingleClickListener {
          viewModel.processUiAction(ApplyClicked)
        }

        binding.closeFilter.setOnSingleClickListener {
          viewModel.processUiAction(CloseClicked)
        }


    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.period_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
            binding.spinner.onItemSelectedListener = this

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedFilterType = FilterBottomSheetViewModel.FilterType.getType(position)
        viewModel.processUiAction(FilterSelected(selectedFilterType))


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {

        const val REQUEST_KEY = "filter_request"
        const val RESULT_KEY = "filter_type"
    }

}