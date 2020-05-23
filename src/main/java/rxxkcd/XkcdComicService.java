package rxxkcd;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface XkcdComicService {

    @GET("info.0.json")
    Single<Response<XkcdComic>> latest();

    @GET("{num}/info.0.json")
    Single<Response<XkcdComic>> num(@Path("num") int num);
}
