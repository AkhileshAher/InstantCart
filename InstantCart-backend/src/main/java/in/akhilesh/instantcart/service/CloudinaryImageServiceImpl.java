package in.akhilesh.instantcart.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageServiceImpl implements CloudinaryImageService {

    private final Cloudinary cloudinary;

    @Override
    @PreAuthorize("isAuthenticated()")
    public Map<?, ?> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("No file provided for upload");
        }
        try {
            return cloudinary.uploader().upload(file.getBytes(), Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Image Uploading Failed", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Image Uploading Failed", e);
        }
    }


}
