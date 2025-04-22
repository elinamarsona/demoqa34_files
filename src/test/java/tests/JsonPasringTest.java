package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonPasringTest {

    @Test
    void jsonTest() throws Exception {
        String fileName = "test.json";
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> person = mapper.readValue(new File(fileName), Map.class);
        System.out.println(person.get("name")); // => Ivan
        System.out.println(person.get("tel")); // => 25-12-86
    }
}
