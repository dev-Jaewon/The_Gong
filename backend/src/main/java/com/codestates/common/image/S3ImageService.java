package com.codestates.common.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3ImageService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${default.profile.image}")
    public String profile;

    @Value("${default.thumbnail.image}")
    private String thumbnail;


    public ResponseEntity<String> uploadP(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, "prifile/" + fileName, file.getInputStream(), metadata);
        return ResponseEntity.ok(amazonS3Client.getUrl(bucket,"profile/"+fileName).toString());
    }


    public ResponseEntity<String> uploadT(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, "thumbnail/" + fileName, file.getInputStream(), metadata);
        return ResponseEntity.ok(amazonS3Client.getUrl(bucket,"thumbnail/"+fileName).toString());
    }



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


    public ResponseEntity<String> deleteProfile(Member member, String imageUrl) {
        String profileImage = getImageKeyFromUrl(imageUrl);
        amazonS3Client.deleteObject(bucket + "/profile", profileImage);
        member.setImageUrl(profile);
        memberRepository.save(member);
        return ResponseEntity.ok(profile);
    }


    public ResponseEntity<String> deleteThumbnail(Room room, String imageUrl) {
        String thumbnailImage = getImageKeyFromUrl(imageUrl);
        amazonS3Client.deleteObject(bucket + "/thumbnail", thumbnailImage);
        room.setImageUrl(thumbnail);
        roomRepository.save(room);
        return ResponseEntity.ok(thumbnail);
    }

    public String getDefaultProfileImage(){
        return profile;
    }
}
