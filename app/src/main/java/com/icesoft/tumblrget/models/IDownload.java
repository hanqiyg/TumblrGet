package com.icesoft.tumblrget.models;

public interface IDownload
{
    String getUrl();
    String getMessage();
    long getDownloadId();
    String getAbsolutePath();
    String getMimeType();
}
