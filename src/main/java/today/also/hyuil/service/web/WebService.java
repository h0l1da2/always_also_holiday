package today.also.hyuil.service.web;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.domain.dto.fanLetter.BoardListDto;
import today.also.hyuil.domain.dto.fanLetter.CommentWriteDto;
import today.also.hyuil.domain.dto.market.MarketWriteDto;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.Files;
import today.also.hyuil.domain.file.IsWhere;
import today.also.hyuil.domain.file.Type;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WebService {

    private final AmazonS3Client amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public WebService(AmazonS3Client amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void sessionSetMember(Member member, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("id", member.getId());
        session.setAttribute("nickname", member.getNickname());
    }

    public Long getIdInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession();
        Long id = null;
        try {
            String stringId = String.valueOf(session.getAttribute("id"));
            id = Long.parseLong(stringId);

        } catch (NumberFormatException e) {
            throw new MemberNotFoundException("세션에 아이디가 없음");
        }
        return id;
    }
    public String getNicknameInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession();
        String nickname = null;
        try {
            nickname = String.valueOf(session.getAttribute("nickname"));
            System.out.println("nickname = " + nickname);
        } catch (NullPointerException e) {
            throw new MemberNotFoundException("세션에 닉네임이 없음");
        }
        return nickname;
    }

    public ResponseEntity<String> okResponseEntity(JsonObject jsonObject) {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    public ResponseEntity<String> badResponseEntity(String cause) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", cause);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.badRequest()
                .body(jsonResponse);
    }

    public Page<BoardListDto> listToPage(Pageable pageable, List dtoList) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()),  (int) pageable.getOffset() + dtoList.size());
        return new PageImpl<>(dtoList.subList(start, end), pageable, dtoList.size());
    }

    public boolean commentWriteDtoNullCheck(CommentWriteDto commentWriteDto) {
        if (commentWriteDto == null) {
            return false;
        }
        if (commentWriteDto.getBoardNum() == null) {
            return false;
        }
        if (!StringUtils.hasText(commentWriteDto.getContent())) {
            return false;
        }
        return true;
    }

    public boolean marketWriteDtoNullCheck(MarketWriteDto marketWriteDto) {
        if (marketWriteDto == null) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getTitle())) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getName())) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getContent())) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getTrade().toString())) {
            return false;
        }
        if (marketWriteDto.getPrice() == null) {
            return false;
        }
        if (marketWriteDto.getQuantity() == null) {
            return false;
        }
        return true;
    }

    public List<FileInfo> getFileInfoList(String dir, List<MultipartFile> files) throws IOException, MimeTypeNotMatchException {
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (isHaveFiles(files)) {
            for (MultipartFile multipartFile : files) {

                Files file = getFiles(dir, multipartFile);

                FileInfo fileInfo = new FileInfo(file);
                fileInfo.whereFileIs(IsWhere.FAN_BOARD);

                fileInfoList.add(fileInfo);

            }
        }
        return fileInfoList;
    }

    private Files getFiles(String dir, MultipartFile multipartFile) throws MimeTypeNotMatchException, IOException {
        String fileUuid = UUID.randomUUID().toString();
        // path type mimeType
        String imgMimeType = setImgMimeType(multipartFile);
        Files file = new Files(multipartFile);
        file.fileUUID(fileUuid);

        // 아마존에 파일 저장
        String filePath = saveFilesToAmazonS3(dir, multipartFile, fileUuid+imgMimeType);
//        // 파일 저장 -> java.io.File (path/UUID.jpg)
//        saveFile(multipartFile, imgMimeType, fileUuid, filePath);

        file.imgMimeType(imgMimeType);

        file.filePath(filePath);
        file.fileType(Type.IMAGE);
        return file;
    }

    private void saveFile(MultipartFile multipartFile, String mimeType, String fileUuid, String filePath) throws IOException {
        File fileIo = new File(filePath + fileUuid + mimeType);
        multipartFile.transferTo(fileIo);
    }
    public List<String> getFilePaths(List<FileInfo> fileInfoList) {
        List<String> filePaths = new ArrayList<>();

        for (FileInfo fileInfo : fileInfoList) {
//            String filePath = pathSubstring(fileInfo);
            String filePath = awsPath(fileInfo);
            System.out.println("filePath = " + filePath);
            filePaths.add(filePath);
        }
        return filePaths;
    }

    // TODO AWS 에서 이미지가 안 보임;;
    private String awsPath(FileInfo fileInfo) {
        String fileName = fileInfo.getFile().getUuid()+fileInfo.getFile().getMimeType();
        return "https://"+bucket+".s3."+amazonS3.getRegion()+".amazonaws.com/"+fileName;
    }

    private String pathSubstring(FileInfo fileInfo) {
        String filePath = fileInfo.getFile().getPath() + fileInfo.getFile().getUuid() + fileInfo.getFile().getMimeType();
        int startPath = "/Users/holiday/IdeaProjects/also_hyuil/src/main/resources".length();
        filePath = filePath.substring(startPath);
        return filePath;
    }
    private String setImgMimeType(MultipartFile multipartFile) throws MimeTypeNotMatchException {
        String imgMimeType = "";
        if (multipartFile.getContentType().contains("image/jpeg")) {
            imgMimeType = ".jpg";
        } else if (multipartFile.getContentType().contains("image/png")) {
            imgMimeType = ".png";
        } else if (multipartFile.getContentType().contains("image/gif")) {
            imgMimeType = ".gif";
        } else {
            // exception 만들기
            System.out.println("올바른 확장자가 아닙니다");
            throw new MimeTypeNotMatchException("올바른 확장자가 아닙니다");
        }
        return imgMimeType;
    }

    private boolean isHaveFiles(List<MultipartFile> files) {
        if (files == null) {
            return false;
        }
        return true;
    }

    public String saveFilesToAmazonS3(String dir, MultipartFile multipartFile, String fileName) throws IOException {
        // 파일 사이즈 알려주기
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        // 아마존에 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 파일 주소 가져오기 path
        return amazonS3.getUrl(dir, fileName).toString();

    }
}
