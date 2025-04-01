package sourse.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sourse.entity.Room;

import java.util.List;
@Converter(autoApply = true)
public class JsonConverter implements AttributeConverter<List<Room.ObjectData>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Room.ObjectData> objectDataList) {
        try {
            return objectDataList == null ? null : objectMapper.writeValueAsString(objectDataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi chuyển đổi object thành JSON", e);
        }
    }

    @Override
    public List<Room.ObjectData> convertToEntityAttribute(String json) {
        try {
            return json == null ? null : objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class,Room.ObjectData.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi chuyển đổi JSON thành object", e);
        }
    }
}
