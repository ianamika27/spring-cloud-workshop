package com.learn.moviecatelogservice.resources;

import com.learn.moviecatelogservice.models.CatelogItems;
import com.learn.moviecatelogservice.models.Movie;
import com.learn.moviecatelogservice.models.Rating;
import com.learn.moviecatelogservice.models.UserRating;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/catelog")
public class MovieCatelogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@RequestMapping("/{userId}")
	public List<CatelogItems> getCatelogs(@PathVariable("userId") String userId){
		//Get all rated movie IDs
		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId,UserRating.class);
		return ratings.getUserRatings().stream().map(rating-> {
			//For each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(),Movie.class);
			
			//Put them all together
			return new CatelogItems(movie.getName(), "Desc", rating.getRating());
				
		})		 
		.collect(Collectors.toList());	
	}
	/*
	Movie movie = webClientBuilder.build()
			.get()
			.uri("http://localhost:8082/movies/"+rating.getMovieId())
			.retrieve()
			.bodyToMono(Movie.class)
			.block();
	*/
}
