package com.example.myapplication

import android.content.Intent
import android.media.audiofx.AudioEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapter.SettingsListAdapter
import com.example.myapplication.adapter.SettingsListClickListener
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.util.RecyclerViewDecorator
import com.example.myapplication.viewmodel.SettingsViewModel
import com.example.myapplication.viewmodel.SettingsViewModelFactory
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private lateinit var viewModel: SettingsViewModel
    private lateinit var viewModelFactory: SettingsViewModelFactory

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application

        viewModelFactory = SettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]

        val settingsListAdapter = context?.let {
            SettingsListAdapter(
                it,
                SettingsListClickListener { setting ->
                    viewModel.onSettingsListItemClicked(setting)
                }
            )
        }

        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.list_end_padding)
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height)
        binding.settingsList.addItemDecoration(RecyclerViewDecorator(bottomSpaceHeight, dividerHeight))

        binding.settingsList.adapter = settingsListAdapter
        viewModel.settingsList.observe(viewLifecycleOwner) { settingsList ->
            settingsList?.let {
                settingsListAdapter?.submitList(settingsList)
                binding.settingsList.invalidateItemDecorations()
            }
        }

        viewModel.settingsEvent.observe(viewLifecycleOwner) {
            when (it) {
                "Equalizer" -> openEqualizer()
                /* TODO should restrict number of channels to fetch when loading the map,
                    placeholder for now */
                "Station count limit" -> true //setStationCountLimit()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Opens the build in equalizer if that's available
     */
    private fun openEqualizer() {
        val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context?.packageName)
        intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}