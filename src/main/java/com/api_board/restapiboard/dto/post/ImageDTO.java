package com.api_board.restapiboard.dto.post;

import com.api_board.restapiboard.domain.post.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDTO {
    private Long id;
    private String originName;
    private String uniqueName;
    public static ImageDTO imageDTO(Image image) {
        return new ImageDTO(image.getId(), image.getOriginName(), image.getUniqueName());
    }
}