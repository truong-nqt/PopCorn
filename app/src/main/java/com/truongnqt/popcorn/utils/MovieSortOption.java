package com.truongnqt.popcorn.utils;

import static com.truongnqt.popcorn.utils.MovieSortOption.ORIGINAL_TITLE_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.ORIGINAL_TITLE_DESC;
import static com.truongnqt.popcorn.utils.MovieSortOption.POPULARITY_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.POPULARITY_DESC;
import static com.truongnqt.popcorn.utils.MovieSortOption.PRIMARY_RELEASE_DATE_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.PRIMARY_RELEASE_DATE_DESC;
import static com.truongnqt.popcorn.utils.MovieSortOption.RELEASE_DATE_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.RELEASE_DATE_DESC;
import static com.truongnqt.popcorn.utils.MovieSortOption.REVENUE_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.REVENUE_DESC;
import static com.truongnqt.popcorn.utils.MovieSortOption.VOTE_AVERAGE_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.VOTE_AVERAGE_DESC;
import static com.truongnqt.popcorn.utils.MovieSortOption.VOTE_COUNT_ASC;
import static com.truongnqt.popcorn.utils.MovieSortOption.VOTE_COUNT_DESC;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

@Retention(SOURCE)
@StringDef({
        POPULARITY_ASC,
        POPULARITY_DESC,
        RELEASE_DATE_ASC,
        RELEASE_DATE_DESC,
        REVENUE_ASC,
        REVENUE_DESC,
        PRIMARY_RELEASE_DATE_ASC,
        PRIMARY_RELEASE_DATE_DESC,
        ORIGINAL_TITLE_ASC,
        ORIGINAL_TITLE_DESC,
        VOTE_AVERAGE_ASC,
        VOTE_AVERAGE_DESC,
        VOTE_COUNT_ASC,
        VOTE_COUNT_DESC
})
public @interface MovieSortOption {
    String POPULARITY_ASC = "popularity.asc";
    String POPULARITY_DESC = "popularity.desc";//default
    String RELEASE_DATE_ASC = "release_date.asc";
    String RELEASE_DATE_DESC = "release_date.desc";
    String REVENUE_ASC = "revenue.asc";
    String REVENUE_DESC = "revenue.desc";
    String PRIMARY_RELEASE_DATE_ASC = "primary_release_date.asc";
    String PRIMARY_RELEASE_DATE_DESC = "primary_release_date.desc";
    String ORIGINAL_TITLE_ASC = "original_title.asc";
    String ORIGINAL_TITLE_DESC = "original_title.desc";
    String VOTE_AVERAGE_ASC = "vote_average.asc";
    String VOTE_AVERAGE_DESC = "vote_average.desc";
    String VOTE_COUNT_ASC = "vote_count.asc";
    String VOTE_COUNT_DESC = "vote_count.desc";
}

