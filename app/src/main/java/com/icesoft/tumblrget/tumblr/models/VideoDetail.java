package com.icesoft.tumblrget.tumblr.models;
import com.icesoft.tumblrget.Constant;

import java.util.List;

public class VideoDetail implements ITumblrDetail,IBlog,IVideo{
    public static final int TYPE = 1;
    private String blogName;
    private String posterUrl;
    private String videoUrl;
    private String slug;
    private List<String> tags;
    private long noteCount;

    public VideoDetail(String blogName, String videoUrl, String posterUrl,String slug, List<String> tags, long noteCount, long downloadId) {
        this.blogName = blogName;
        this.videoUrl = videoUrl;
        this.posterUrl = posterUrl;
        this.slug = slug;
        this.tags = tags;
        this.noteCount = noteCount;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String getBlogName() {
        return blogName;
    }

    @Override
    public String getBlogAvatarUrl() {
        return String.format(Constant.AVATAR_FORMATER, blogName);
    }

    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public long getNoteCount() {
        return noteCount;
    }

}
