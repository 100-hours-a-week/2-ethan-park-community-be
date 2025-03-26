package hw6.integration.image.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImageValidator {

    public void validatorImageSize(int size) {

        if (size > 10) {
            throw new BusinessException(ErrorCode.IMAGE_LIMIT_EXCEEDED);
        }
    }
}
