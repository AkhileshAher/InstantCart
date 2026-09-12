package in.akhilesh.instantcart.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageServiceImpl implements CloudinaryImageService {

    private final Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            // save url in user avatar
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Image Uploading Failed",e);
        }
    }
}
