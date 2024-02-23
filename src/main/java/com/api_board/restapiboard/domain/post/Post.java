package com.api_board.restapiboard.domain.post;

import com.api_board.restapiboard.domain.category.Category;
import com.api_board.restapiboard.domain.common.EntityDate;
import com.api_board.restapiboard.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)//게시글이 처음 저장될 때 등록했던 이미지도 함께 저장하게 하기 위함
    private List<Image> images;

    public Post(String title, String content, Long price, Member member, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }

    private void addImages(List<Image> added) { //cascade 옵션을 PERSIST로 설정해두었기 때문에, Post가 저장되면서 Image도 함께 저장
        added.stream().forEach(i -> {
            images.add(i);
            i.initPost(this);
        });
    }

    private void deleteImages(List<Image> deleted) {
        deleted.stream().forEach(di -> this.images.remove(di));
    }

    private ImageUpdatedResult findImageUpdateResult(List<MultipartFile> addedImageFile, List<Long> deleteImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFile);
        List<Image> deletedImages = convertImageIdsToImages(deleteImageIds);
        return new ImageUpdatedResult(addedImageFile, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(id -> convertImageIdsToImages(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(Collectors.toList());
    }

    private Optional<Image> convertImageIdToImage(Long id) {
        return this.images.stream().filter(i -> i.getId().equals(id).findAny());
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imagesFiles) {
        return imagesFiles.stream().map(imageFile -> new Image(imageFile.getOriginalFilename())).collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult { // 4
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}
