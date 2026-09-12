package in.akhilesh.instantcart.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoDbInfo implements CommandLineRunner {

    private final MongoDatabaseFactory mongoDatabaseFactory;

    @Override
    public void run(String... args) {

        var database = mongoDatabaseFactory.getMongoDatabase();

        System.out.println("=================================");
        System.out.println("MongoDB Database : " + database.getName());
        System.out.println("MongoDB Address  : " +
                database.getCodecRegistry());
        System.out.println("=================================");
    }
}