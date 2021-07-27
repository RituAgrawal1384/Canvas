package com.automation.platform.filehandling;

import com.automation.platform.config.Configvariable;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.azure.core.util.polling.SyncPoller;
import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.ShareFileClientBuilder;
import com.azure.storage.file.share.models.ShareFileCopyInfo;
import com.azure.storage.file.share.models.ShareFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AzureStorageUtils {
    private static final Logger logger = LoggerFactory.getLogger(AzureStorageUtils.class);

    @Autowired
    private Configvariable configvariable;

    public void downloadFileFromAzureStorageToLocal(String azureDirName, String azureFileName, String downloadFilePath) {
        String endpoint = configvariable.getStringVar("azure.storage.file.endpoint");
        String token = configvariable.getStringVar("azure.storage.sas.token");
        String shareName = configvariable.getStringVar("azure.storage.file.share.name");

        OutputStream outputStream = null;
        ShareDirectoryClient sourceDirectoryClient = null;
        ShareFileClient sourceFileClient = null;

        try {
            sourceDirectoryClient = new ShareFileClientBuilder().endpoint(endpoint)
                    .sasToken(token).shareName(shareName).resourcePath(azureDirName).buildDirectoryClient();

            sourceFileClient = sourceDirectoryClient.getFileClient(azureFileName);
            outputStream = new FileOutputStream(downloadFilePath);
            sourceFileClient.download(outputStream);
        } catch (FileNotFoundException e) {
            logger.error("Failed to download azure storage file " + downloadFilePath);
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to download azure storage file: [{}]", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to create a Azure File share client" + azureFileName);
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to create a Azure File share client: [{}]", e.getMessage());
        }

    }


    public void uploadFileToAzureStorageFromLocal(String azureDirName, String azureFileName, String UploadFilePath) {
        String endpoint = configvariable.getStringVar("azure.storage.file.endpoint");
        String token = configvariable.getStringVar("azure.storage.sas.token");
        String shareName = configvariable.getStringVar("azure.storage.file.share.name");

        ShareDirectoryClient desDirectoryClient = null;
        ShareFileClient destFileClient = null;

        try {
            desDirectoryClient = new ShareFileClientBuilder().endpoint(endpoint)
                    .sasToken(token).shareName(shareName).resourcePath(azureDirName).buildDirectoryClient();

            desDirectoryClient.createFile(azureFileName, 1024 * 1024);

            destFileClient = desDirectoryClient.getFileClient(azureFileName);

            destFileClient.uploadFromFile(UploadFilePath);
            desDirectoryClient.listFilesAndDirectories().forEach(
                    resource -> {
                        if (resource.getName().equals(azureFileName)) {
                            logger.info(azureFileName + " has been successfully copied to azure storage location " + azureDirName);
                        }
                    }
            );

        } catch (Exception e) {
            logger.error("Failed to create a Azure File share client" + azureFileName);
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to create a Azure File share client: [{}]", e.getMessage());
        }
    }


    public void copyFileToStorage(String endpoint, String token, String shareName) {
        ShareFileClient sourceFileClient = new ShareFileClientBuilder().sasToken(token)
                .endpoint(endpoint).shareName(shareName).resourcePath("1_Submitted Quotations/2Q2IKK_TestSub_5200710221026/2Q2IKK_TestSub_5200710221026.xlsx").buildFileClient();

        ShareDirectoryClient desDirectoryClient = new ShareFileClientBuilder().endpoint(endpoint)
                .sasToken(token).shareName(shareName).resourcePath("2_Quotations Ready for In-Force").buildDirectoryClient();

        desDirectoryClient.createFile("2Q2IKK_TestSub_5200710221026.xlsx", 1024 * 1024);

        ShareFileClient destFileClient = desDirectoryClient.getFileClient("2Q2IKK_TestSub_5200710221026.xlsx");

//        String sourceURL = "https://stasgrassuataz1robxsa009.file.core.windows.net/azurefiles-robxsa-sgrass/1_Submitted%20Quotations/2Q2IKK_TestSub_5200710221026/2Q2IKK_TestSub_5200710221026.xlsx";


        Duration pollInterval = Duration.ofSeconds(2);
        SyncPoller<ShareFileCopyInfo, Void> poller = destFileClient.beginCopy(sourceFileClient.getFileUrl(), null, pollInterval);
    }


    public void waitForFileToProcess(String azureDirName, String azureFileName, int waitTime) throws InterruptedException {
        String endpoint = configvariable.getStringVar("azure.storage.file.endpoint");
        String token = configvariable.getStringVar("azure.storage.sas.token");
        String shareName = configvariable.getStringVar("azure.storage.file.share.name");

        ShareDirectoryClient desDirectoryClient = null;
        ShareFileClient destFileClient = null;

        desDirectoryClient = new ShareFileClientBuilder().endpoint(endpoint)
                .sasToken(token).shareName(shareName).resourcePath(azureDirName).buildDirectoryClient();

        destFileClient = desDirectoryClient.getFileClient(azureFileName);
        int count = waitTime;
        while (destFileClient.exists() && count > 0) {
            Thread.sleep(1500);
            --count;
            boolean fileExists = destFileClient.exists();
            if (!fileExists) {
                break;
            } else if (fileExists && count <= 0) {
                logger.error("maximum file processing time out " + azureFileName);
                throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to process a Azure File share client in a given time");
            }
        }
    }


    public void getFileFromAzureStorageToLocal(String azureDirName, String actionType, String timestamp) {
        String endpoint = configvariable.getStringVar("azure.storage.file.endpoint");
        String token = configvariable.getStringVar("azure.storage.sas.token");
        String shareName = configvariable.getStringVar("azure.storage.file.share.name");

        ShareDirectoryClient sourceDirectoryClient = null;
        String fileName = null;
        String latestFileName = null;
        LocalTime lastModifiedTime1 = LocalTime.parse("00:00:00");

        try {
            sourceDirectoryClient = new ShareFileClientBuilder().endpoint(endpoint)
                    .sasToken(token).shareName(shareName).resourcePath(azureDirName).buildDirectoryClient();
            for (ShareFileItem files : sourceDirectoryClient.listFilesAndDirectories()) {
                if (files.getName().contains(actionType) && files.getName().contains(timestamp) && !files.isDirectory()) {
                    fileName = files.getName();
                    LocalTime lastModifiedTime2 = sourceDirectoryClient.getFileClient(fileName).getProperties().getLastModified().toLocalTime();
                    if (lastModifiedTime2.isAfter(lastModifiedTime1)) {
                        lastModifiedTime1 = lastModifiedTime2;
                        latestFileName = fileName;
                    }

                }
            }

            configvariable.setStringVariable(latestFileName, "AZURE_STORAGE_FILENAME");
            logger.info(latestFileName + " has been successfully retrieved to azure storage location " + azureDirName);

        } catch (Exception e) {
            logger.error("Failed to retrieved the file from azure storage location " + azureDirName);
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to get a Azure File share client: [{}]", e.getMessage());
        }

    }


    public void getFileUsingLocalUsingSchedulerTimeStamp(String azureDirName, String actionType, String timestamp1, String timestamp2, int waitTime) {
        String endpoint = configvariable.getStringVar("azure.storage.file.endpoint");
        String token = configvariable.getStringVar("azure.storage.sas.token");
        String shareName = configvariable.getStringVar("azure.storage.file.share.name");

        ShareDirectoryClient sourceDirectoryClient = null;
        String fileName = null;
        String empReqFileName = null;
//        ArrayList<String> fileList = new ArrayList<>();
//        Map<String,String> fileMap = new HashMap<>();
        int count = waitTime;
        LocalDateTime fileTimeStamp;
        LocalDateTime formattedTimeStamp1 = LocalDateTime.parse(timestamp1.replace(" ", "T"));
        LocalDateTime formattedTimeStamp2 = LocalDateTime.parse(timestamp2.replace(" ", "T"));

        try {
            sourceDirectoryClient = new ShareFileClientBuilder().endpoint(endpoint)
                    .sasToken(token).shareName(shareName).resourcePath(azureDirName).buildDirectoryClient();
            if (formattedTimeStamp2.isEqual(formattedTimeStamp1)) {
                while (empReqFileName == null && count > 0) {
                    Thread.sleep(1500);
                    --count;
                    for (ShareFileItem files : sourceDirectoryClient.listFilesAndDirectories()) {
                        fileName = files.getName();
                        fileTimeStamp = sourceDirectoryClient.getFileClient(fileName).getProperties().getLastModified().toLocalDateTime();
                        if (fileName.contains(actionType) && !files.isDirectory() && (fileTimeStamp.isBefore(formattedTimeStamp2.plusMinutes(1)) || fileTimeStamp.isEqual(formattedTimeStamp2)) && fileTimeStamp.isAfter(formattedTimeStamp1.minusMinutes(5))) {
                            empReqFileName = fileName;
                            break;
                        }

                    }
                }
            } else if (formattedTimeStamp2.isAfter(formattedTimeStamp1)) {
                while (empReqFileName == null && count > 0) {
                    Thread.sleep(1500);
                    --count;
                    for (ShareFileItem files : sourceDirectoryClient.listFilesAndDirectories()) {
                        fileName = files.getName();
                        fileTimeStamp = sourceDirectoryClient.getFileClient(fileName).getProperties().getLastModified().toLocalDateTime();
                        if (fileName.contains(actionType) && !files.isDirectory() && (fileTimeStamp.isBefore(formattedTimeStamp2.plusMinutes(1)) || fileTimeStamp.isEqual(formattedTimeStamp2)) && fileTimeStamp.isAfter(formattedTimeStamp1)) {
                            empReqFileName = fileName;
                            break;
                        }

                    }
                }
            }
            if (empReqFileName == null && count <= 0) {
                logger.error("maximum file request time out");
                throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to submit a Azure File share client in a given time");
            }
            configvariable.setStringVariable(empReqFileName, "AZURE_STORAGE_FILENAME");
            logger.info(empReqFileName + " has been successfully retrieved to azure storage location " + azureDirName);
        } catch (Exception e) {
            logger.error("Failed to retrieved the file from azure storage location " + azureDirName);
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to get a Azure File share client: [{}]", e.getMessage());
        }

    }
}

