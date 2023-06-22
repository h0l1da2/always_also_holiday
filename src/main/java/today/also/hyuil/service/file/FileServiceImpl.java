package today.also.hyuil.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.Files;
import today.also.hyuil.domain.file.IsWhere;
import today.also.hyuil.domain.file.Type;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.repository.file.FileJpaRepository;
import today.also.hyuil.repository.file.FileRepository;
import today.also.hyuil.service.file.inter.FileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileJpaRepository fileJpaRepository;

    private final AmazonS3Client amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String fanLetterDir = "fanLetter_1/";
    private String marketSellDir = "marketSell_1/";

    @Override
    public FileInfo saveFileInfo(FileInfo fileInfo) {
        return fileRepository.insertFileInfo(fileInfo);
    }

    @Override
    public List<FileInfo> fileInfoListForFanBoard(Long letterNum) {
        return fileRepository.selectInfoListForFanBoard(letterNum);
    }

    @Override
    public List<FileInfo> fileInfoListForMarket(Long marketId) {
        return fileRepository.selectInfoListForMarket(marketId);
    }

    @Override
    public Page<FileInfo> fileInfoListForMarketSellList(Pageable pageable) {
        Pageable pageRequest =
                PageRequest.of(pageable.getPageNumber(), 6, Sort.Direction.DESC, "id");

        return fileJpaRepository.findAll(pageRequest);
    }

    @Override
    public List<String> getFilePaths(List<FileInfo> fileInfoList) {
        List<String> filePaths = new ArrayList<>();

        for (FileInfo fileInfo : fileInfoList) {
            // AWS
            String filePath = awsPath(fileInfo);
            // Local
//            String filePath = localPath(fileInfo);
            filePaths.add(filePath);
        }
        return filePaths;
    }

    private String saveFilesToAmazonS3(String dir, MultipartFile multipartFile, String fileName) throws IOException {
        // 파일 사이즈 알려주기
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        // 아마존에 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 파일 주소 가져오기 path
        return amazonS3.getUrl(dir, fileName).toString();

    }

    public List<FileInfo> getFileInfoList(String where, List<MultipartFile> files) throws IOException, MimeTypeNotMatchException {

        String dir = getDir(where);

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

    private boolean isHaveFiles(List<MultipartFile> files) {
        if (files == null) {
            return false;
        }
        return true;
    }

    private String awsPath(FileInfo fileInfo) {
        String fileName = fileInfo.getFile().getUuid()+fileInfo.getFile().getMimeType();
        return "https://"+bucket+".s3."+amazonS3.getRegion()+".amazonaws.com/"+fileName;
    }

    private String getDir(String where) {
        String dir = "";
        if (where.equals("fanLetter")) {
            dir = fanLetterDir;
        }
        if (where.equals("marketSell")) {
            dir = marketSellDir;
        }
        return dir;
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
//        saveFile(multipartFile, imgMimeType, fileUuid, dir);

        file.imgMimeType(imgMimeType);

        file.filePath(filePath);
        file.fileType(Type.IMAGE);
        return file;
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


    private void saveFile(MultipartFile multipartFile, String mimeType, String fileUuid, String filePath) throws IOException {
        File fileIo = new File(filePath + fileUuid + mimeType);
        multipartFile.transferTo(fileIo);
    }

    private String localPath(FileInfo fileInfo) {
        String filePath = fileInfo.getFile().getPath() + fileInfo.getFile().getUuid() + fileInfo.getFile().getMimeType();
        int startPath = "/Users/holiday/IdeaProjects/also_hyuil/src/main/resources".length();
        filePath = filePath.substring(startPath);
        return filePath;
    }

}
