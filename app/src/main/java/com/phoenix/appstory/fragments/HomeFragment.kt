package com.phoenix.appstory.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.phoenix.appstory.Constants
import com.phoenix.appstory.MyApp
import com.phoenix.appstory.R
import com.phoenix.appstory.VideoPreLoadingService
import com.phoenix.appstory.adapter.StoryAdapter
import com.phoenix.appstory.databinding.FragmentHomeBinding
import com.phoenix.appstory.model.Story
import com.phoenix.appstory.model.StoryResponse
import kohii.v1.core.MemoryMode.HIGH
import kohii.v1.exoplayer.ExoPlayerConfig
import kohii.v1.exoplayer.Kohii
import kohii.v1.exoplayer.createKohii

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val homeBinding: FragmentHomeBinding get() = _binding!!

    private val simpleCache: SimpleCache = MyApp.simpleCache

    private val homeStories = listOf(
        Story("https://cdn.chetal.co/videos/480p/90312.mp4"),
        Story("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4"),
        Story("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4"),
        Story("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4"),
        Story("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4")
    )

    private val kohii: Kohii by lazy {
        createKohii(requireContext(), ExoPlayerConfig.FAST_START.copy(cache = simpleCache))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding.btn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_dashFragment)
        }

        kohii.cleanUp()

        Log.i("Trading", kohii.toString())

        val manager = kohii.register(this, HIGH, RESUMED)
            .addBucket(homeBinding.videos)

        val adapter = StoryAdapter(
            getData(),
            requireContext(),
            kohii, manager
        )

        homeBinding.videos.adapter = adapter
        homeBinding.videos.offscreenPageLimit = 1
    }

    private fun startPreLoadingService() {
        val preloadingServiceIntent = Intent(context, VideoPreLoadingService::class.java)
        preloadingServiceIntent.putStringArrayListExtra(
            Constants.VIDEO_LIST,
            arrayListOf(
                "https://cdn.chetal.co/videos/480p/90312.mp4",
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4",
            )
        )
        context?.startService(preloadingServiceIntent)
    }

    private fun getData(): List<StoryResponse> {
        val storyResponse = mutableListOf<StoryResponse>()
        storyResponse.add(StoryResponse("Test hello", homeStories.shuffled()))
        storyResponse.add(StoryResponse("Test Mean", homeStories.shuffled()))
        storyResponse.add(StoryResponse("Test Mage", homeStories.shuffled()))

        return storyResponse
    }


    override fun onDestroyView() {
        super.onDestroyView()


        _binding = null
    }

    override fun onDestroy() {
        kohii.cleanUp()
        kohii.lockActivity(requireActivity())

        requireActivity().onBackPressed()

        super.onDestroy()
    }
}