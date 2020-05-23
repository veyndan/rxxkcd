package rxxkcd;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

// TODO Include proguard rules in project or tell them in README e.g. in Paper for Reddit
// only fetchComic() and possibly fetchLatest() is needed, all the other methods aren't used. In
// Paper for XKCD, all the methods will probably be used.

// TODO Find a way where a subclass of XkcdComic can be passed so custom logic can be done inside
// the comic, e.g. displayMonth() which returns the month name from the month field. This doesn't
// seem to possible through a type variable (e.g. T) or a wildcard in the service as I think they
// need to be of concrete type. Maybe a custom Moshi JsonAdapter will do the trick?

public class Xkcd {

    private static final String XKCD_BASE_URL = "https://xkcd.com";

    private final XkcdComicService xkcdComicService;

    public Xkcd() {
        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(XKCD_BASE_URL)
                .build();

        xkcdComicService = retrofit.create(XkcdComicService.class);
    }

    /**
     * Fetches the comic associated with the comic num.
     *
     * @param comicNum the number of the comic to fetch.
     * @return an Observable which emits a single comic.
     * @throws IllegalArgumentException if parameter 'comicNum' is < 1, as 1 is the earliest comic
     *                                  available.
     */
    public Single<Response<XkcdComic>> fetchComic(final int comicNum) {
        if (comicNum < 1) {
            return Single.error(new IllegalArgumentException("Parameter 'comicNum' must be " +
                    ">= 1. The earliest comic possible is 1 so a value of " + comicNum +
                    " is not allowed."));
        }
        return xkcdComicService.num(comicNum);
    }

    /**
     * Fetches the latest comic published by xkcd.
     *
     * @return an Observable which emits a single comic.
     */
    public Single<Response<XkcdComic>> fetchLatest() {
        return xkcdComicService.latest();
    }

    /**
     * Fetches every xkcd comic ever made.
     *
     * @return an Observable which emits each comic individually.
     */
    public Observable<Response<XkcdComic>> fetchAllComics() {
        return fetchAllComicsFrom(1);
    }

    /**
     * Fetches every xkcd comic from the desired comic up to and including the most recent comic.
     *
     * @param fromComicNum the earliest comic to fetch from (inclusive).
     * @return an Observable which emits each comic individually.
     * @throws IllegalArgumentException if parameter 'fromComicNum' is < 1, as 1 is the earliest
     *                                  comic
     *                                  available.
     */
    public Observable<Response<XkcdComic>> fetchAllComicsFrom(final int fromComicNum) {
        if (fromComicNum < 1) {
            return Observable.error(new IllegalArgumentException("Parameter 'fromComicNum' must be >= 1. The earliest " +
                    "comic possible is 1 so a value of " + fromComicNum + " is not allowed."));
        }

        return xkcdComicService.latest()
                .flatMapObservable(new Function<Response<XkcdComic>, ObservableSource<? extends Response<XkcdComic>>>() {
                    @Override
                    public ObservableSource<? extends Response<XkcdComic>> apply(final Response<XkcdComic> response) throws Exception {
                        // TODO It feels like polling the network will be hard if it isn't successful, but I don't know why.
                        if (response.isSuccessful()) {
                            return fetchComicsRange(fromComicNum, response.body().getNum());
                        }
                        return Observable.just(response);
                    }
                });
    }

    /**
     * Fetches every xkcd comic from the desired comic up to the desired comic.
     *
     * @param fromComicNum The earliest comic to fetch from (inclusive).
     * @param toComicNum   The latest comic to fetch to (inclusive).
     * @return An Observable which emits each comic individually.
     * @throws IllegalArgumentException if parameter 'fromComicNum' or 'toComicNum' is < 1, as 1 is
     *                                  the earliest comic available. // TODO
     */
    public Observable<Response<XkcdComic>> fetchComicsRange(final int fromComicNum, final int toComicNum) {
        if (fromComicNum < 1) {
            return Observable.error(new IllegalArgumentException("Parameter 'fromComicNum' must be >= 1. The " +
                    "earliest comic possible is 1 so a value of " + fromComicNum + " is not " +
                    "allowed."));
        }

        if (toComicNum < 1) {
            return Observable.error(new IllegalArgumentException("Parameter 'toComicNum' must be >= 1. The " +
                    "earliest comic possible is 1 so a value of " + toComicNum + " is not " +
                    "allowed."));
        }

        final List<Integer> numRange = new ArrayList<>(toComicNum - fromComicNum + 1);
        for (int num = toComicNum; num >= fromComicNum; num--) numRange.add(num);

        // Unsure how to use Observable.range() in reverse.
        return Observable.fromIterable(numRange)
                .flatMapSingle(new Function<Integer, SingleSource<? extends Response<XkcdComic>>>() {
                    @Override
                    public SingleSource<? extends Response<XkcdComic>> apply(final Integer num) throws Exception {
                        return xkcdComicService.num(num);
                    }
                });
    }
}
