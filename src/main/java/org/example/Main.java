package org.example;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // 이미지 파일 경로
        String imagePath = "C:\\OCR_For_Java\\images\\alphabet.jpg";

        // Tesseract OCR 인스턴스 생성
        ITesseract tesseract = new Tesseract();

        try {
            // 이미지 파일로부터 텍스트 추출
            File imageFile = new File(imagePath);
            String extractedText = tesseract.doOCR(imageFile);

            // Google Translation API JSON 키 파일 경로
            // 업로드 시 빼고 업로드 요망 - String jsonKeyFilePath 변수명은 쓰되, "" 안에 있는 글자는 명시하지 말 것.
            // 이 주석 바로 밑에 작성(변수명: String jsonKeyFilePath)


            // JSON 키 파일을 사용하여 인증 정보 로드
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonKeyFilePath));

            // Translate 서비스 클라이언트 생성
            Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();

            // 추출된 텍스트를 번역
            String sourceLanguage = "en"; // 추출된 텍스트의 언어 (예: 영어)
            String targetLanguage = "ko"; // 번역할 언어 (예: 한국어)
            Translation translation = translate.translate(extractedText, Translate.TranslateOption.sourceLanguage(sourceLanguage), Translate.TranslateOption.targetLanguage(targetLanguage));

            // 추출된 텍스트 출력
            System.out.println("Extracted Text: " + extractedText);

            // 번역 결과 출력
            System.out.println("Translated Text: " + translation.getTranslatedText());
        } catch (TesseractException | IOException e) {
            e.printStackTrace();
        }
    }
}