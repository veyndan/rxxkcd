package rxxkcd;

import com.squareup.moshi.Json;

public class XkcdComic {

    private String month;
    private int num;
    private String link;
    private String year;
    private String news;
    @Json(name = "safe_title") private String safeTitle;
    private String transcript;
    private String alt;
    private String img;
    private String title;
    private String day;

    // TODO I don't particularly want to make this instantiable, but need for testing.
    public XkcdComic(final String month, final int num, final String link, final String year,
                     final String news, final String safeTitle, final String transcript,
                     final String alt, final String img, final String title, final String day) {
        this.month = month;
        this.num = num;
        this.link = link;
        this.year = year;
        this.news = news;
        this.safeTitle = safeTitle;
        this.transcript = transcript;
        this.alt = alt;
        this.img = img;
        this.title = title;
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public int getNum() {
        return num;
    }

    public String getLink() {
        return link;
    }

    public String getYear() {
        return year;
    }

    public String getNews() {
        return news;
    }

    public String getSafeTitle() {
        return safeTitle;
    }

    public String getTranscript() {
        return transcript;
    }

    public String getAlt() {
        return alt;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getDay() {
        return day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final XkcdComic xkcdComic = (XkcdComic) o;

        if (num != xkcdComic.num) return false;
        if (!month.equals(xkcdComic.month)) return false;
        if (!link.equals(xkcdComic.link)) return false;
        if (!year.equals(xkcdComic.year)) return false;
        if (!news.equals(xkcdComic.news)) return false;
        if (!safeTitle.equals(xkcdComic.safeTitle)) return false;
        if (!transcript.equals(xkcdComic.transcript)) return false;
        if (!alt.equals(xkcdComic.alt)) return false;
        if (!img.equals(xkcdComic.img)) return false;
        if (!title.equals(xkcdComic.title)) return false;
        return day.equals(xkcdComic.day);

    }

    @Override
    public int hashCode() {
        int result = month.hashCode();
        result = 31 * result + num;
        result = 31 * result + link.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + news.hashCode();
        result = 31 * result + safeTitle.hashCode();
        result = 31 * result + transcript.hashCode();
        result = 31 * result + alt.hashCode();
        result = 31 * result + img.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + day.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "XkcdComic{" +
                "month='" + month + '\'' +
                ", num=" + num +
                ", link='" + link + '\'' +
                ", year='" + year + '\'' +
                ", news='" + news + '\'' +
                ", safeTitle='" + safeTitle + '\'' +
                ", transcript='" + transcript + '\'' +
                ", alt='" + alt + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
