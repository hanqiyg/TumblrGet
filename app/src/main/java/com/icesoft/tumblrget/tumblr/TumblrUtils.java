package com.icesoft.tumblrget.tumblr;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/26.
 */

public class TumblrUtils
{
    private static final String T = "TumblrUtils.class";

    public static boolean isTumblrLink(String link)
    {
        final String PATTERN = "^((https|http)://)([\\w-]+\\.)(tumblr.com/post/)[\\w-]+";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(link);
        return m.find();
    }
    public static String getBlogIdentifier(String link)
    {
        final String PATTERN = "^(https|http)://(.*?)(.tumblr.com/post/)(\\d+)[\\w-]+";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(link);
        if(m.find()){return m.group(2);}
        return null;
    }
    public static long getPostId(String link){
        final String PATTERN = "(tumblr.com/post/)(\\d+)";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(link);
        long id = -1;
        if(m.find()){
            try{
                //System.out.println(m.group(2));
                //id = Long.valueOf(m.group(2));
                id = Long.parseLong(m.group(2));
            }catch(NumberFormatException e){
                System.out.println("NumberFormatException");
            }
        }
        return id;
    }
    public static String getFileExtFromEmbed(String embed)
    {
        final String PATTERN = "(<source)(.*?)(src=\")(.*?)(\")(.*?)(type=\")(.*?)(\")";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find()){return m.group(9);}
        return null;
    }
/*    public static String getFileExtFromUrl(String url){
        //URL: "http://photosaaaaa.net/photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg"
        // String filename = "";
        //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg

        String extension="";
            //Checks for both forward and/or backslash
        //NOTE:**While backslashes are not supported in URL's
        //most browsers will autoreplace them with forward slashes
        //So technically if you're parsing an html page you could run into
        //a backslash , so i'm accounting for them here;
        String[] pathContents = url.split("[\\\\/]");
        if(pathContents != null){
            int pathContentsLength = pathContents.length;
            //System.out.println("Path Contents Length: " + pathContentsLength);
//            for (int i = 0; i < pathContents.length; i++) {
//                System.out.println("Path " + i + ": " + pathContents[i]);
//            }
            //lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength-1];
            String[] lastPartContents = lastPart.split("\\.");
            if(lastPartContents != null && lastPartContents.length > 1){
                int lastPartContentLength = lastPartContents.length;
                // System.out.println("Last Part Length: " + lastPartContentLength);
                //filenames can contain . , so we assume everything before
                //the last . is the name, everything after the last . is the
                //extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    // System.out.println("Last Part " + i + ": "+ lastPartContents[i]);
                    if(i < (lastPartContents.length -1)){
                        name += lastPartContents[i] ;
                        if(i < (lastPartContentLength -2)){
                            name += ".";
                        }
                    }
                }
                extension = lastPartContents[lastPartContentLength -1];
                // filename = name + "." +extension;
                // System.out.println("Name: " + name);
                // System.out.println("Extension: " + extension);
                // System.out.println("Filename: " + filename);
            }
        }
        return extension;
    }*/
    /*
        <video  id='embed-5a6ad654a383f189432712' class='crt-video crt-skin-default' width='500' height='500'
        poster='https://78.media.tumblr.com/tumblr_oa4u1mrGfq1vzjmmn_smart1.jpg' preload='none'
            muted data-crt-video data-crt-options='
            {"autoheight":null,"duration":24,"hdUrl":false,
            "filmstrip":{"url":"https:\/\/67.media.tumblr.com\/previews\/tumblr_oa4u1mrGfq1vzjmmn_filmstrip.jpg","width":"200","height":"200"}}' >
            <source src="https://ccdjb.tumblr.com/video_file/t:7zvTpAR0TE99FXwWQQBpTg/170138975353/tumblr_oa4u1mrGfq1vzjmmn" type="video/mp4">
        </video>
    * */
    public static int getWidthFromEmbed(String embed)
    {
        int width = -1;
        final String PATTERN = "(<video)((.*?))(width=\')((.*?))(\')";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find())
        {
            String w = m.group(5);
            try{
                width = Integer.parseInt(w);
            }catch(NumberFormatException nfe){
                Log.d(T,nfe.getMessage());
            }
        }
        return width;
    }
    public static int getHeightFromEmbed(String embed)
    {
        int width = -1;
        final String PATTERN = "(<video)((.*?))(height=\')((.*?))(\')";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find())
        {
            String w = m.group(5);
            try{
                width = Integer.parseInt(w);
            }catch(NumberFormatException nfe){
                Log.d(T,nfe.getMessage());
            }
        }
        return width;
    }
    public static String getUrlFromEmbed(String embed)
    {
        //System.out.println(embed);
        final String PATTERN = "(<source)((.*?))(src=\")((.*?))(\")";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find()){return m.group(5);}
        return null;
    }
    public static String getPosterFromEmbed(String embed)
    {
        final String PATTERN = "(poster=')((.*?))(')";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find()){
            return m.group(2);
        }
        return null;
    }

    public static final String REGEX =
                    //id 10 class 18 width 26 height 34
                    "(?:<)(?:.*)(?:video)(?:.*)(?:id)(?:.*)(?:=)(?:.*)(?:\"|')(.*)(?:\"|')(?:.*)(?:class)(?:.*)(?:=)(?:.*)(?:\"|')(.*)(?:\"|')(?:.*)(?:width)(?:.*)(?:=)(?:.*)(?:\"|')(\\d*)(?:\"|')(?:.*)(?:height)(?:.*)(?:=)(?:.*)(?:\"|')(\\d*)(?:\"|')(?:.*)"+
                    //poster 42
                    "(?:poster)(?:.*)(?:=)(?:.*)(?:\"|')(.*)(?:\"|')(?:.*)(?:preload)(?:.*)(?:=)(?:.*)(?:\"|')(.*)(?:\"|')(?:.*)" +
                    //data-crt-options json 56
                    "(?:data-crt-options)(?:.*)(?:=)(.*)(?:>)(?:.*)(?:\\s)(?:.*)" +
                    //src 68 minetype 76
                    "(?:<)(?:.*)(?:source)(?:.*)(?:src)(?:.*)(?:=)(?:.*)(?:\"|')(.*)(?:\"|')(?:.*)(?:type)(?:.*)(?:=)(?:.*)(?:\"|')(.*)(?:\"|')(?:.*)(?:>)(?:(.*)(?:\\s)(?:.*))";
    public static String getMineTypeFromEmbed(String embed)
    {
        final String PATTERN = "(<source)(.*)(src=\")(.*)(\")(.*)(type=\")((.*?))(\")";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find()){
            return m.group(8);
        }
        return null;
    }
    public static String getIdFromEmbed(String embed)
    {
        final String PATTERN = "(<video)(.*?)(id=')(.*?)(')";
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(embed);
        if(m.find()){
            return m.group(4);
        }
        return null;
    }
/*    public static String getVideoExtFilename(String embed)
    {
        if(embed != null) {
            String mimeType = TumblrUtils.getMineTypeFromEmbed(embed);
            if (mimeType != null) {
                return MimeTypeUtils.getInstance().mimeTypeForExtension(mimeType);
            }
        }
        return null;
    }*/
}
