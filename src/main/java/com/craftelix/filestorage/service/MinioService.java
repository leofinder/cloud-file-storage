package com.craftelix.filestorage.service;

import com.craftelix.filestorage.exception.MinioServiceException;
import com.craftelix.filestorage.exception.MinioObjectAlreadyExistsException;
import com.craftelix.filestorage.exception.MinioObjectNotFoundException;
import com.craftelix.filestorage.service.props.MinioProperties;
import io.minio.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    public void createFolder(String path) {
        validateObjectNotExists(path);

        try {
            putObject(path, new ByteArrayInputStream(new byte[0]), "application/x-directory");
        } catch (Exception e) {
            throw new MinioServiceException("Failed to create folder in MinIO. Target path: " + path, e);
        }
    }

    public void renameFile(String oldObjectName, String newObjectName) {
        validateObjectExists(oldObjectName);
        validateObjectNotExists(newObjectName);

        try {
            copyObject(oldObjectName, newObjectName);
            removeObject(oldObjectName);
        } catch (Exception e) {
            throw new MinioServiceException("Failed to rename object in MinIO. Source object: "
                    + oldObjectName + ", Target object: " + newObjectName, e);
        }
    }

    public void renameFolder(String oldPrefix, String newPrefix) {
        validateObjectExists(oldPrefix);

        try {
            Iterable<Result<Item>> objects = getListObjects(oldPrefix);

            for (Result<Item> result : objects) {
                Item item = result.get();
                String oldObjectName = item.objectName();
                String newObjectName = oldObjectName.replaceFirst(oldPrefix, newPrefix);

                validateObjectNotExists(newObjectName);

                copyObject(oldObjectName, newObjectName);
                removeObject(oldObjectName);
            }
        } catch (Exception e) {
            throw new MinioServiceException("Failed to rename folder in MinIO. Source prefix: "
                    + oldPrefix + ", Target prefix: " + newPrefix, e);
        }
    }

    public void deleteFile(String path) {
        validateObjectExists(path);

        try {
            removeObject(path);
        } catch (Exception e) {
            throw new MinioServiceException("Failed to delete object in MinIO. Object path: " + path, e);
        }
    }

    public void deleteFolder(String path) {
        validateObjectExists(path);

        try {
            Iterable<Result<Item>> objects = getListObjects(path);

            List<DeleteObject> deleteObjects = new ArrayList<>();
            for (Result<Item> result : objects) {
                deleteObjects.add(new DeleteObject(result.get().objectName()));
            }

            removeObjects(deleteObjects);
        } catch (Exception e) {
            throw new MinioServiceException("Failed to delete objects in MinIO. Folder path: " + path, e);
        }
    }

    public void uploadFile(String path, MultipartFile file) {
        validateObjectNotExists(path);

        try {
            putObject(path, file.getInputStream(), file.getContentType());
        } catch (Exception e) {
            throw new MinioServiceException("Failed to upload file to MinIO. Target path: " + path, e);
        }
    }

    public InputStreamResource getFileAsStream(String path) {
        validateObjectExists(path);

        try {
            return new InputStreamResource(getObject(path));
        } catch (Exception e) {
            throw new MinioServiceException("Failed to download file from MinIO. Target path: " + path, e);
        }
    }

    public InputStreamResource getFolderAsZipStream(String path) {
        validateObjectExists(path);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
            Iterable<Result<Item>> objects = getListObjects(path);
            for (Result<Item> result : objects) {
                Item item = result.get();
                String objectName = item.objectName();

                ByteArrayOutputStream fileContentStream = new ByteArrayOutputStream();
                getObject(objectName).transferTo(fileContentStream);

                zos.putNextEntry(new ZipEntry(objectName.replace(path, "")));
                zos.write(fileContentStream.toByteArray());
                zos.closeEntry();
            }
        } catch (Exception e) {
            throw new MinioServiceException("Failed to download folder from MinIO. Target path: " + path, e);
        }

        return new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    private void putObject(String objectName, InputStream inputStream, String contentType) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build()
        );
    }

    private InputStream getObject(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .build()
        );
    }

    private Iterable<Result<Item>> getListObjects(String prefix) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .prefix(prefix)
                        .recursive(true)
                        .build()
        );
    }

    public void copyObject(String oldObjectName, String newObjectName) throws Exception {
        minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(newObjectName)
                        .source(CopySource.builder()
                                .bucket(minioProperties.getBucket())
                                .object(oldObjectName)
                                .build())
                        .build()
        );
    }

    private void removeObject(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .build()
        );
    }

    private void removeObjects(List<DeleteObject> deleteObjects) {
        minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .objects(deleteObjects)
                        .build()
        );
    }

    private void validateObjectExists(String path) {
        if (!isObjectExist(path) && !isPrefixExist(path)) {
            throw new MinioObjectNotFoundException("Object not found in MinIO. Target path: " + path);
        }
    }

    private void validateObjectNotExists(String path) {
        if (isObjectExist(path) || isPrefixExist(path)) {
            throw new MinioObjectAlreadyExistsException("Object already exists in MinIO. Target path: " + path);
        }
    }

    public boolean isObjectExist(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPrefixExist(String prefix) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .prefix(prefix)
                            .maxKeys(1)
                            .build()
            );
            for (Result<Item> result : results) {
                return true;
            }
        } catch (Exception e) {
            throw new MinioServiceException("Failed to check prefix existence in MinIO. Prefix: " + prefix, e);
        }
        return false;
    }
}
