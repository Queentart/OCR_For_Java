package org.example;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // 이미지 파일 경로
        String imagePath = "C:\\JAVA_OCR\\images\\japanese_sentence2.png";

        // Tesseract OCR 인스턴스 생성
        ITesseract tesseract = new Tesseract();

        // OCR 언어 설정
        // 다수의 언어 설정 (예: 영어, 프랑스어, 독일어)
        String[] languages = {"eng", "fra", "deu", "chi_sim", "jpn"};
        for (String language : languages) {
            tesseract.setLanguage(language);
        }

        try {
            // 이미지 파일로부터 텍스트 추출
            File imageFile = new File(imagePath);
            String extractedText = tesseract.doOCR(imageFile);

            // Google Translation API JSON 키 파일 경로
            // 업로드 시 빼고 업로드 요망 - String jsonKeyFilePath 변수명은 쓰되, "" 안에 있는 글자는 명시하지 말 것.
            // 이 주석 바로 밑에 작성(변수명: String jsonKeyFilePath)
            String jsonKeyFilePath = "C:\\translation_api_key\\spherical-entry-391823-2f5e9256772c.json";

            // JSON 키 파일을 사용하여 인증 정보 로드
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonKeyFilePath));

            // Translate 서비스 클라이언트 생성
            Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();

            // 텍스트 언어 감지
            String detectedLanguage = detectLanguage(extractedText, translate);

            // 번역 서비스 호출하여 번역
            Translation translation = translate.translate(extractedText, Translate.TranslateOption.targetLanguage("ko"));

            // 디코딩된 텍스트
            String decodedText = StringEscapeUtils.unescapeHtml4(translation.getTranslatedText());

            // 추출된 텍스트 출력
            System.out.println("Extracted Text: " + extractedText);
            System.out.println("Detected Language: " + detectedLanguage);

            // 디코딩된 번역 결과 출력
            System.out.println("Translated Text: " + decodedText);
        } catch (TesseractException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String detectLanguage(String textToDetect, Translate translate) {
        // 텍스트 언어 감지
        return translate.detect(textToDetect).getLanguage();
    }
}