package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentVoteBinding
import com.example.myapplication.model.Station
import com.example.myapplication.model.Vote
import com.example.myapplication.viewmodel.VoteViewModel
import com.example.myapplication.viewmodel.VoteViewModelFactory

/**
 * Should let the user to post a vote on a channel
 */
class VoteFragment : Fragment() {

    private var _binding: FragmentVoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VoteViewModel
    private lateinit var viewModelFactory: VoteViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoteBinding.inflate(inflater, container, false)

        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        viewModelFactory = VoteViewModelFactory(stationRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[VoteViewModel::class.java]

        val station: Station = StationDetailsFragmentArgs.fromBundle(requireArguments()).station

        binding.postButton.setOnClickListener {
            if (
                (binding.reviewInput.text.isNotEmpty()) &&
                (binding.reviewNameInput.text.isNotEmpty()) &&
                (binding.reviewScoreInput.text.isNotEmpty())
            ) {
                viewModel.postVote(
                    Vote(
                        station.stationUUID,
                        binding.reviewScoreInput.text[0].code,
                        binding.reviewNameInput.text.toString(),
                        binding.reviewCommentInput.text.toString()
                    )
                )
            }
        }

        binding.cancelButton.setOnClickListener {
            val action =
                VoteFragmentDirections.actionNavigationVoteToNavigationStationDetails(station)
            findNavController().navigate(action)
        }

        binding.station = station

        return binding.root
    }
}
