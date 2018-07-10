package com.icesoft.tumblrget.tumblr.models;

import java.util.List;

public interface IVideo {
    String getVideoUrl();
    String getPosterUrl();
    String getSlug();
    List<String> getTags();
    long getNoteCount();
}
