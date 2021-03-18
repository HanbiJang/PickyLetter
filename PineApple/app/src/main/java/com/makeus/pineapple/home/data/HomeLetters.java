package com.makeus.pineapple.home.data;

public interface HomeLetters {
    //상속 관계 : home.NewLetter, home.OldLetter
    //홈화면
    void setPlatformImageUrl(String platformImageUrl);

    void setThumbnailImageUrl(String thumbnailImageUrl);

    void setCreatedAt(String createdAt);

    void setTitle(String title);

    void setPlatformName(String platformName);

    //홈메일, 북마크
    void setLetterId(Integer letterId);

    void setBookmarkId(Integer bookmarkId);

    void setBookmarkCount(Integer bookmarkCount);

}
