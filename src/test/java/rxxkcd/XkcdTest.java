package rxxkcd;

import org.junit.Test;

import io.reactivex.functions.Function;
import retrofit2.Response;

public class XkcdTest {

    private final Xkcd xkcd = new Xkcd();

    @Test
    public void getComic() {
        final XkcdComic comic = new XkcdComic(
                "1", 1000, "http://xkcd.com/1000/large/", "2012", "", "1000 Comics",
                "[[1000 characters, numerous of which have appeared previously in other comics, are arranged to create the number \"1000\". Two more people stand in the foreground commenting on the formation]]\n\nPerson 1: WOOOO!\nPerson 2: Wow - Just 24 to go until a big round-number milestone!\n\n{{Title text: Thank you for making me feel less alone.}}", "Thank you for making me feel less alone.", "http://imgs.xkcd.com/comics/1000_comics.png",
                "1000 Comics", "6");

        xkcd.fetchComic(1000)
                .map(new Function<Response<XkcdComic>, XkcdComic>() {
                    @Override
                    public XkcdComic apply(final Response<XkcdComic> response) throws Exception {
                        return response.body();
                    }
                })
                .test()
                .assertValue(comic)
                .assertComplete();
    }

    @Test
    public void getComicOutOfRangeTooLow() {
        xkcd.fetchComic(-1)
                .test()
                .assertNoValues()
                .assertError(IllegalArgumentException.class);
    }

    @Test
    public void getComicOutOfRangeTooHigh() {
        xkcd.fetchComic(1000000)
                .map(new Function<Response<XkcdComic>, Object>() {
                    @Override
                    public Object apply(final Response<XkcdComic> response) throws Exception {
                        return response.isSuccessful();
                    }
                })
                .test()
                .assertValue(false)
                .assertComplete();
    }
}
