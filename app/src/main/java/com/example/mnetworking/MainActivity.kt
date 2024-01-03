package com.example.mnetworking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mnetworking.data.network.RestaurantService
import com.example.mnetworking.data.response.CustomerReviewsItem
import com.example.mnetworking.data.response.Restaurant
import com.example.mnetworking.data.response.RestaurantResponse
import com.example.mnetworking.databinding.ActivityMainBinding
import com.example.mnetworking.ui.adapter.ReviewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

//    private fun initializeService() {
//        val client = RestaurantService.getRestaurant().getRestaurant("uewq1zg2zlskfw1e867")
//        client.enqueue(object : Callback<RestaurantResponse> {
//            override fun onResponse(
//                call: Call<RestaurantResponse>, response: Response<RestaurantResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    Log.d("MainActivity.kt", "onResponse: $responseBody")
//                }
//
//            }
//
//            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)
        findRestaurant()

//        initializeService()
    }

    private fun findRestaurant() {
        showLoading(true)
        val client = RestaurantService.getRestaurant().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setRestaurantData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant?.customerReviews)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setRestaurantData(restaurant: Restaurant?) {
        binding.tvTitle.text = restaurant?.name
        binding.tvDescription.text = restaurant?.description
        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant?.pictureId}")
            .into(binding.ivPicture)
    }

    private fun setReviewData(consumerReviews: List<CustomerReviewsItem?>?) {
        val adapter = ReviewAdapter()
        adapter.submitList(consumerReviews)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}