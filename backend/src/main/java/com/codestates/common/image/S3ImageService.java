package com.codestates.common.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;


@Slf4j
@Service
public class S3ImageService {
    public String getImageKeyFromUrl(String imageUrl) {
        try {

            URL url = new URL(imageUrl);
            String path = url.getPath();

            if(path.startsWith("/profile/")) {
                String profileUrl = path.substring(path.lastIndexOf("/")+1); // 슬래시 이후 파일명 추출
                return profileUrl;
            }
            else if(path.startsWith("/thumbnail/")) {
                String thumbnailUrl = path.substring(path.lastIndexOf("/")+1);
                return thumbnailUrl;
            }

            throw new IllegalArgumentException("잘못된 이미지 URL 입니다.");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("잘못된 이미지 URL 입니다.");
        }
    }
}
