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
import com.saverone.exercise.databinding.FragmentMoviesBinding
import com.saverone.exercise.databinding.ItemMovieBinding
import com.saverone.exercise.model.Movie
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoviesFragment : Fragment() {


    private lateinit var viewModel: MoviesViewModel
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = arrayOf("Popular", "Currently Played", "Favorites")
        val spinner = binding.spinnerFilterTypes
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item, list
        )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val filterType = when (position) {
                    0 -> RemoteDataSource.MovieFilterType.popular
                    1 -> RemoteDataSource.MovieFilterType.currently_played
                    else -> RemoteDataSource.MovieFilterType.favorites
                }
                viewModel.applyFilterType(filterType)
            }
        }

        binding.listMovies.adapter = MoviesAdapter(fun(movie) {
            val bundle = Bundle()
            bundle.putInt("currentID", movie.ID!!)
            bundle.putString("currentTitle", movie.originalTitle)
            bundle.putString("currentPoster", movie.posterPath)
            NavHostFragment.findNavController(this).navigate(R.id.action_moviesFragment_to_movieInfoFragment, bundle);
        })

        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.results != null) {
                    if (viewModel.page == 1) {
                        (binding.listMovies.adapter as MoviesAdapter).clear();
                    }
                    (binding.listMovies.adapter as MoviesAdapter).appendMovies(it.results)
                }
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
        }

        binding.listMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.fetchMovies(false)
                }
            }
        })

        viewModel.fetchMovies(true)

    }

    class MoviesAdapter(var clickItemCallbak: (Movie) -> Unit) : RecyclerView
    .Adapter<MoviesAdapter.ViewHolder>() {

        private var moviesList: ArrayList<Movie> = ArrayList()

        inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding);
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val movie = moviesList[position];
            viewHolder.binding.title.text = movie.originalTitle
            Glide
                .with(viewHolder.itemView)
                .load(RemoteDataSource.BASE_IMAGES_URL + movie.posterPath)
                .centerCrop()
                .into(viewHolder.binding.image);

            viewHolder.itemView.setOnClickListener {
                clickItemCallbak(movie);
            }
        }

        override fun getItemCount() = moviesList.size

        fun appendMovies(list: List<Movie>) {
            moviesList.addAll(list)
            notifyDataSetChanged();
        }

        fun clear() {
            moviesList.clear()
        }
    }


    override fun onDestroyView() {
        binding.listMovies.adapter = null
        super.onDestroyView()
        _binding = null
    }

}