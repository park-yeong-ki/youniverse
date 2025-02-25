package com.ssafy.youniverse.mapper;

import com.ssafy.youniverse.dto.req.MovieReqDto;
import com.ssafy.youniverse.dto.res.*;
import com.ssafy.youniverse.entity.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper extends CustomMapper {
    default Movie movieReqDtoToMovie(MovieReqDto movieReqDto){
        if ( movieReqDto == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setMovieId(movieReqDto.getMovieId() );
        movie.setTitle(movieReqDto.getTitle() );
        movie.setLanguage( movieReqDto.getLanguage() );
        movie.setOverView( movieReqDto.getOverView() );
        movie.setRate(movieReqDto.getRate());
        movie.setRuntime(movieReqDto.getRuntime());
        movie.setMovieImage(movieReqDto.getMovieImage());

        if (movieReqDto.getOttList() != null) { //ott 목록이 존재하는 경우
            List<OttMovie> ottMovies = new ArrayList<>();
            for (long ottId : movieReqDto.getOttList()) {
                Ott ott = new Ott();
                ott.setOttId(ottId);
                OttMovie ottMovie = new OttMovie();
                ottMovie.setOtt(ott);
                ottMovie.setMovie(movie);
                ottMovies.add(ottMovie);
            }
            movie.setOttMovies(ottMovies);
        }

        if (movieReqDto.getKeywordList() != null) { //키워드 목록이 존재하는 경우
            List<KeywordMovie> keywordMovies = new ArrayList<>();
            for (long keywordId : movieReqDto.getKeywordList()) {
                Keyword keyword = new Keyword();
                keyword.setKeywordId(keywordId);
                KeywordMovie keywordMovie = new KeywordMovie();
                keywordMovie.setKeyword(keyword);
                keywordMovie.setMovie(movie);
                keywordMovies.add(keywordMovie);
            }
            movie.setKeywordMovies(keywordMovies);
        }

        if (movieReqDto.getActorList() != null) { //영화 배우 목록이 존재하는 경우
            List<ActorMovie> actorMovies = new ArrayList<>();
            for (long actorId : movieReqDto.getActorList()) {
                Actor actor = new Actor();
                actor.setActorId(actorId);
                ActorMovie actorMovie = new ActorMovie();
                actorMovie.setActor(actor);
                actorMovie.setMovie(movie);
                actorMovies.add(actorMovie);
            }
            movie.setActorMovies(actorMovies);
        }

        if (movieReqDto.getGenreList() != null) { // 영화 장르 목록이 존재하는 경우
            List<GenreMovie> genreMovies = new ArrayList<>();
            for (long genreId : movieReqDto.getGenreList()) {
                Genre genre = new Genre();
                genre.setGenreId(genreId);
                GenreMovie genreMovie = new GenreMovie();
                genreMovie.setGenre(genre);
                genreMovie.setMovie(movie);
                genreMovies.add(genreMovie);
            }
            movie.setGenreMovies(genreMovies);
        }

        if (movieReqDto.getDirectorList() != null) { // 영화 감독 목록이 존재하는 경우
            List<DirectorMovie> directorMovies = new ArrayList<>();
            for (long directorId : movieReqDto.getDirectorList()) {
                Director director = new Director();
                director.setDirectorId(directorId);
                DirectorMovie directorMovie = new DirectorMovie();
                directorMovie.setDirector(director);
                directorMovie.setMovie(movie);
                directorMovies.add(directorMovie);
            }
            movie.setDirectorMovies(directorMovies);
        }

        return movie;
    }

    default MovieResDto movieToMovieResDto(Movie movie){
        if ( movie == null ) {
            return null;
        }

        MovieResDto movieResDto = new MovieResDto();

        movieResDto.setMovieId( movie.getMovieId() );
        movieResDto.setTitle( movie.getTitle() );
        movieResDto.setLanguage(movie.getLanguage() );
        movieResDto.setOverView( movie.getOverView());
        movieResDto.setRate(movie.getRate() );
        movieResDto.setRuntime(movie.getRuntime());
        movieResDto.setMovieImage(movie.getMovieImage());

        movieResDto.setOttResDtos(movie.getOttMovies().stream()
                .map(ottMovie -> {
                    Ott ott = ottMovie.getOtt();
                    OttResDto ottResDto = new OttResDto();
                    ottResDto.setOttId(ott.getOttId());
                    ottResDto.setOttName(ott.getOttName());
                    ottResDto.setOttImage(ott.getOttImage());

                    return ottResDto;
                })
                .collect(Collectors.toList())
        );

        movieResDto.setKeywordResDtos(movie.getKeywordMovies().stream()
                .map(keywordMovie -> {
                    Keyword keyword = keywordMovie.getKeyword();
                    KeywordResDto keywordResDto = new KeywordResDto();
                    keywordResDto.setKeywordId(keyword.getKeywordId());
                    keywordResDto.setKeywordName(keyword.getKeywordName());

                    return keywordResDto;
                })
                .collect(Collectors.toList())
        );

        //좋아요한 회원 목록
        movieResDto.setHeartMovieResDtos(movie.getHeartMovies().stream()
                .map(heartMovie -> {
                    HeartMovieResDto heartMovieResDto = new HeartMovieResDto();
                    heartMovieResDto.setHeartMovieId(heartMovie.getHeartMovieId());
                    heartMovieResDto.setMemberSimpleResDto(memberToMemberSimpleResDto(heartMovie.getMember()));
//                    heartMovieResDto.setMovieSimpleResDto(movieToMovieSimpleResDto(heartMovie.getMovie()));
                    return heartMovieResDto;
                })
                .collect(Collectors.toList())
        );

        //싫어요한 회원 목록
        movieResDto.setHateMovieResDtos(movie.getHateMovies().stream()
                .map(hateMovie -> {
                    HateMovieResDto hateMovieResDto = new HateMovieResDto();
                    hateMovieResDto.setHateMovieId(hateMovie.getHateMovieId());
                    hateMovieResDto.setMemberSimpleResDto(memberToMemberSimpleResDto(hateMovie.getMember()));
//                    hateMovieResDto.setMovieSimpleResDto(movieToMovieSimpleResDto(hateMovie.getMovie()));
                    return hateMovieResDto;
                })
                .collect(Collectors.toList())
        );

        //인생영화로 등록한 회원 목록
        movieResDto.setBestMovieResDtos(movie.getBestMovies().stream()
                .map(bestMovie -> {
                    BestMovieResDto bestMovieResDto = new BestMovieResDto();
                    bestMovieResDto.setBestMovieId(bestMovie.getBestMovieId());
                    bestMovieResDto.setMemberSimpleResDto(memberToMemberSimpleResDto(bestMovie.getMember()));
//                    bestMovieResDto.setMovieSimpleResDto(movieToMovieSimpleResDto(bestMovie.getMovie()));
                    return bestMovieResDto;
                })
                .collect(Collectors.toList())
        );

        //리뷰 목록
        movieResDto.setReviewResDtos(movie.getReviews().stream()
                .map(review -> {
                    ReviewResDto reviewResDto = new ReviewResDto();
                    reviewResDto.setReviewId(review.getReviewId());
                    reviewResDto.setReviewRate(review.getReviewRate());
                    reviewResDto.setReviewContent(review.getReviewContent());
                    reviewResDto.setMemberSimpleResDto(memberToMemberSimpleResDto(review.getMember()));
//                    reviewResDto.setMovieSimpleResDto(movieToMovieSimpleResDto(review.getMovie()));
                    return reviewResDto;
                })
                .collect(Collectors.toList())
        );

        //영화 배우 목록
        movieResDto.setActorResDtos(movie.getActorMovies().stream()
                .map(actorMovie -> {
                    ActorResDto actorResDto = new ActorResDto();
                    actorResDto.setActorId(actorMovie.getActor().getActorId());
                    actorResDto.setActorName(actorMovie.getActor().getActorName());
                    actorResDto.setActorImage(actorMovie.getActor().getActorImage());
                    return actorResDto;
                })
                .collect(Collectors.toList())
        );

        //영화 장르 목록
        movieResDto.setGenreResDtos(movie.getGenreMovies().stream()
                .map(genreMovie -> {
                    GenreResDto genreResDto = new GenreResDto();
                    genreResDto.setGenreId(genreMovie.getGenre().getGenreId());
                    genreResDto.setGenreName(genreMovie.getGenre().getGenreName());
                    return genreResDto;
                })
                .collect(Collectors.toList())
        );

        //영화 감독 목록
        movieResDto.setDirectorResDtos(movie.getDirectorMovies().stream()
                .map(directorMovie -> {
                    DirectorResDto directorResDto = new DirectorResDto();
                    directorResDto.setDirectorId(directorMovie.getDirector().getDirectorId());
                    directorResDto.setDirectorName(directorMovie.getDirector().getDirectorName());
                    directorResDto.setDirectorImage(directorMovie.getDirector().getDirectorImage());
                    return directorResDto;
                })
                .collect(Collectors.toList())
        );

        return movieResDto;
    }
}
