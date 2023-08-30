package com.saverone.exercise.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saverone.exercise.R
import com.saverone.exercise.data.RemoteDataSource
import com.saverone.exercise.databinding.FragmentMovieInfoBinding
import com.saverone.exercise.databinding.FragmentMoviesBinding
import com.saverone.exercise.databinding.ItemMovieBinding
import com.saverone.exercise.model.Movie
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieInfoFragment : Fragment() {


    private lateinit var viewModel: MovieInfoViewModel
    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MovieInfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.favoriteStateLiveData.observe(viewLifecycleOwner) {
            if(it!=null && it) {
                Toast.makeText(context,"Added To Favorites", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Error", Toast.LENGTH_LONG).show()
            }
        }

        binding.title.text = requireArguments().getString("currentTitle")
        Glide
            .with(view)
            .load(RemoteDataSource.BASE_IMAGES_URL+requireArguments().getString("currentPoster"))
            .centerCrop()
            .into(binding.image)

        binding.button.setOnClickListener { viewModel.setFavorite(requireArguments().getInt("currentID")) }

    }








    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}