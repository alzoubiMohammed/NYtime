package com.deloitte.mostpopular.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.deloitte.mostpopular.databinding.FragmentNewsDetailsBinding
import com.deloitte.mostpopular.ui.util.extension.getTimeAgo
import com.deloitte.mostpopular.ui.util.extension.parseDateString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {

    @Inject
    lateinit var requestManager: RequestManager

    private var _binding: FragmentNewsDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsDetailsViewModel by viewModels()

    private val args: NewsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passDataToViewModel()
        initObserveViewModel()
    }

    private fun passDataToViewModel() {
        viewModel.init(args.date, args.description, args.imageUrl)
    }

    private fun initObserveViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer { viewState ->
            binding.textViewDate.text =
                viewState.date.parseDateString()?.getTimeAgo(requireContext()) ?: ""
            binding.textViewDescription.text = viewState.description
            requestManager.load(viewState.imageUrl).centerCrop().into(binding.imageViewNews)

        })
    }
}