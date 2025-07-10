package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.model.Chapter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ChapterSplittingAITestMain {
    public static void main(String[] args) throws Exception {
        // Đọc nội dung truyện từ file (hoặc dùng chuỗi mẫu nếu không có file)
        String storyContent;
        try {
            storyContent = Files.readString(Paths.get("uploads/tinhyeuvanhungbimat.txt"));
        } catch (Exception e) {
            // Nếu không có file, dùng chuỗi mẫu
            storyContent = "Ngày xửa ngày xưa, có một cậu bé tên là Nam. Cậu sống ở một làng nhỏ bên sông. Một ngày nọ, Nam quyết định lên đường phiêu lưu...\nSau nhiều ngày đi qua rừng núi, Nam gặp một con rồng lớn. Cuộc chiến giữa Nam và rồng diễn ra ác liệt...\nCuối cùng, Nam chiến thắng và trở về làng, mang theo kho báu và những bài học quý giá.";
        }

        ChapterSplittingAIService service = new ChapterSplittingAIService();
        List<Chapter> chapters = service.splitChapters(storyContent);

        System.out.println("Kết quả chia chương:");
        for (Chapter chapter : chapters) {
            System.out.println("--- " + chapter.getTitle() + " ---");
            System.out.print(chapter.getContentUrl());
            System.out.println();
        }
    }
} 