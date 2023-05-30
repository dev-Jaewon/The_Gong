package com.codestates.common.image;import com.amazonaws.services.s3.AmazonS3Client;import com.amazonaws.services.s3.model.ObjectMetadata;import com.codestates.auth.utils.ErrorResponse;import com.codestates.member.entity.Member;import com.codestates.member.service.MemberService;import com.codestates.room.entity.Room;import com.codestates.room.service.RoomService;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Value;import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import javax.validation.constraints.Positive;import java.io.IOException;import java.util.Map;@Slf4j@RestController@RequiredArgsConstructor@RequestMappingpublic class S3ImageController {    @Value("${cloud.aws.s3.bucket}")    private String bucket;    private final AmazonS3Client amazonS3Client;    private final S3ImageService s3ImageService;    private final RoomService roomService;    private final MemberService memberService;    @Value("${default.profile.image}")    private String profile;    @Value("${default.thumbnail.image}")    private String thumbnail;    // Todo : 프로필 업로드    @PostMapping("/profile/{member-id}")    public ResponseEntity uploadProfile(@PathVariable("member-id") @Positive long memberId,                                        @RequestParam("image") MultipartFile file,                                        Authentication authentication) {        Map<String, Object> principal = (Map) authentication.getPrincipal();        long jwtMemberId = ((Number) principal.get("memberId")).longValue();        boolean isAdmin = (boolean) principal.get("isAdmin");        if (isAdmin == false) {            if(jwtMemberId != (memberId)) {                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);            }        }        try {            String fileName = file.getOriginalFilename();            ObjectMetadata metadata = new ObjectMetadata();            metadata.setContentType(file.getContentType());            metadata.setContentLength(file.getSize());            amazonS3Client.putObject(bucket,"profile/"+fileName,file.getInputStream(),metadata);            return ResponseEntity.ok(amazonS3Client.getUrl(bucket,"profile/"+fileName).toString());        } catch (IOException e) {            e.printStackTrace();            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();        }    }    // Todo : 썸네일 업로드    @PostMapping("/thumbnail")    public ResponseEntity uploadThumbnail(@RequestParam("image") MultipartFile file,                                          Authentication authentication) {        if (authentication == null) {                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);        }        try {            String fileName = file.getOriginalFilename();            ObjectMetadata metadata = new ObjectMetadata();            metadata.setContentType(file.getContentType());            metadata.setContentLength(file.getSize());            amazonS3Client.putObject(bucket,"thumbnail/"+fileName,file.getInputStream(),metadata);            return ResponseEntity.ok(amazonS3Client.getUrl(bucket,"thumbnail/"+fileName).toString());        } catch (IOException e){            e.printStackTrace();            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();        }    }    // Todo : 프로필 삭제    @DeleteMapping("/profile/{member-id}")    public ResponseEntity deleteProfile(@PathVariable("member-id") long memberId,                                        @RequestParam("image") String imageUrl,                                        Authentication authentication) {        Map<String, Object> principal = (Map) authentication.getPrincipal();        long jwtMemberId = ((Number) principal.get("memberId")).longValue();        boolean isAdmin = (boolean) principal.get("isAdmin");        Member member = memberService.findMember(memberId);        if (isAdmin == false) {            if(jwtMemberId != (memberId)) {                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);            }        }        try{            String profileImage = s3ImageService.getImageKeyFromUrl(imageUrl);            amazonS3Client.deleteObject(bucket,profileImage);            s3ImageService.deleteProfile(member);            return ResponseEntity.ok(profile);        } catch (Exception e) {            e.printStackTrace();            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();        }    }    // Todo : 썸네일 삭제    @DeleteMapping("/thumbnail/{room-id}")    public ResponseEntity deleteThumbnail(@PathVariable("room-id") long roomId,                                          @RequestParam("image") String imageUrl,                                          Authentication authentication) {        Map<String, Object> principal = (Map) authentication.getPrincipal();        long jwtMemberId = ((Number) principal.get("memberId")).longValue();        boolean isAdmin = (boolean) principal.get("isAdmin");        Room room = roomService.findRoom(roomId);        long adminId = room.getAdminMemberId();        if (isAdmin == false) {            if(jwtMemberId != adminId) {                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);            }        }        try {            String thumbnailImage = s3ImageService.getImageKeyFromUrl(imageUrl);            amazonS3Client.deleteObject(bucket, thumbnailImage);            s3ImageService.deleteThumbnail(room);            return ResponseEntity.ok(thumbnail);        } catch (Exception e) {            e.printStackTrace();            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();        }    }}