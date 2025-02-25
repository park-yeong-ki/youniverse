package com.ssafy.youniverse.controller;

import com.ssafy.youniverse.dto.req.MovieReqDto;
import com.ssafy.youniverse.dto.res.MovieResDto;
import com.ssafy.youniverse.entity.Movie;
import com.ssafy.youniverse.mapper.MovieMapper;
import com.ssafy.youniverse.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    //영화 생성
    @PostMapping("/register")
    public ResponseEntity<?> registerMovie(@RequestBody MovieReqDto movieReqDto) {
        Movie movie = movieMapper.movieReqDtoToMovie(movieReqDto);
        Movie createdMovie = movieService.createMovie(movie);
        MovieResDto movieResDto = movieMapper.movieToMovieResDto(createdMovie);
        return new ResponseEntity<>(movieResDto, HttpStatus.OK);
    }

    //영화 조회
    @GetMapping("/{movie-id}")
    public ResponseEntity<?> findMovie(@PathVariable("movie-id") long movieId) {
        Movie movie = movieService.readMovie(movieId);
        MovieResDto movieResDto = movieMapper.movieToMovieResDto(movie);
        return new ResponseEntity<>(movieResDto, HttpStatus.OK);
    }

    //영화 전체 조회 - 페이지네이션 구현
    //선호도 조사 영화 조회시 로그인된 회원 식별자 전송
    //감독, 배우, 타이틀별 조회
    @GetMapping
    public ResponseEntity<?> findMovies(@PageableDefault(sort = "movieId", direction = Sort.Direction.ASC) Pageable pageable,
                                        @RequestParam(name = "member-id", required = false) Long memberId,
                                        @RequestParam(name = "director", required = false) String director,
                                        @RequestParam(name = "actor", required = false) String actor,
                                        @RequestParam(name = "title", required = false) String title,
                                        @RequestParam(name = "type", required = false) Integer type,
                                        @RequestParam(name = "ott-id", required = false) Long ottId) {
        Page<Movie> moviePage = movieService.readMovies(pageable, memberId, director, actor, title, type, ottId);
        Page<MovieResDto> movieResDtoPage = moviePage.map(movie -> movieMapper.movieToMovieResDto(movie));
        return new ResponseEntity<>(movieResDtoPage, HttpStatus.OK);
    }

    //영화 수정
    @PutMapping("/{movie-id}")
    public ResponseEntity<?> modifyMovie(@PathVariable("movie-id") long movieId, @RequestBody MovieReqDto movieReqDto) {
        movieReqDto.setMovieId(movieId);
        Movie movie = movieMapper.movieReqDtoToMovie(movieReqDto);
        Movie updatedMovie = movieService.updateMovie(movie);
        MovieResDto movieResDto = movieMapper.movieToMovieResDto(updatedMovie);
        return new ResponseEntity<>(movieResDto, HttpStatus.OK);
    }

    //영화 삭제
    @DeleteMapping("/{movie-id}")
    public ResponseEntity<?> removeMovie(@PathVariable("movie-id") long movieId) {
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
