package com.movieapi.service.imp;

import com.movieapi.dtos.MovieDto;
import com.movieapi.entitys.Movie;
import com.movieapi.repository.MovieRepo;
import com.movieapi.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImp implements MovieService {

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    final private MovieRepo repo;

    final private FileServiceImp fileService;

    public MovieServiceImp(MovieRepo repo, FileServiceImp fileService) {
        this.repo = repo;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        // 1. upload the file
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw  new RuntimeException("File Already exist pls enter the new file Name!");
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        // 2. set the value of filed 'poster' as fileName
        movieDto.setPoster(uploadedFileName);

        // 3. map dto to Movie obj
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getMovieCast(),
                movieDto.getPoster()
        );

        // 4. save the movie object -> saved movie object
        Movie savedMovie = repo.save(movie);

        // 5. generate postUrl
        String postUrl = baseUrl + "/api/" + uploadedFileName;

        // 6. map Movie obj to DTO obj and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getMovieCast(),
                savedMovie.getPoster(),
                postUrl
        );

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {

        // 1. check the data in DB if exist , fetch the data
        Movie movie = repo.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Fount"));

        // 2. generate Url
        String url = baseUrl + "/api/" + movie.getPoster();

        // 3. map to MovieDTO object and return it
        MovieDto movieDto = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getMovieCast(),
                movie.getPoster(),
                url
        );
        return movieDto;
    }

    @Override
    public List<MovieDto> getAllMovies() {

        // 1. fetch all data from DB
        List<Movie> movies = repo.findAll();

        List<MovieDto> movieDtoList = new ArrayList<>();
        // 2. Movie to MovieDTO List
        for(Movie movie:movies){
            String url = baseUrl + "/api" + movie.getPoster();

            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getMovieCast(),
                    movie.getPoster(),
                    url
            );
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

    @Override
    public MovieDto updateMovie(Integer MovieId, MovieDto movieDto, MultipartFile file) throws IOException {

        Movie mv = repo.findById(MovieId).orElseThrow(() -> new RuntimeException("File Not Fount"));

        String fileName = mv.getPoster();

        if(file!=null){
            Files.deleteIfExists(Paths.get(path +File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }

        movieDto.setPoster(fileName);

        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getMovieCast(),
                movieDto.getPoster()
        );
        Movie updatedMovie = repo.save(movie);

        String postUrl = baseUrl + "/api/" + fileName;

        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getMovieCast(),
                movie.getPoster(),
                postUrl
        );

        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {

        Movie mv = repo.findById(movieId).orElseThrow(() -> new RuntimeException("File Not Fount"));
        Integer id = mv.getMovieId();

        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        repo.deleteById(movieId);

        return "Movie deleted With id = " + id;
    }
}
