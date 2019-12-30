package best.server.common;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 * Utilities for Rest docs fields
 */
public class RestDocsUtil {

    public static List<FieldDescriptor> getMemberFieldDescriptors(FieldDescriptor... fieldDescriptors) {
        final List<FieldDescriptor> descriptors = toList(fieldDescriptors);

        return addAll(
                descriptors,
                fieldWithPath("id").description("member's id").type(JsonFieldType.NUMBER),
                fieldWithPath("email").description("member's email").type(JsonFieldType.STRING),
                fieldWithPath("firstName").description("member's first name").type(JsonFieldType.STRING),
                fieldWithPath("lastName").description("member's last name").type(JsonFieldType.STRING),
                fieldWithPath("roles").description("member's ROLES").type(JsonFieldType.ARRAY),
                fieldWithPath("address").description("member's address").type(JsonFieldType.OBJECT),
                fieldWithPath("address.city").description("address's city").type(JsonFieldType.STRING),
                fieldWithPath("address.street").description("address's street").type(JsonFieldType.STRING),
                fieldWithPath("address.zipCode").description("address's zip code").type(JsonFieldType.STRING)
        );
    }

    private static List<FieldDescriptor> toList(FieldDescriptor... fieldDescriptors) {
        return (fieldDescriptors == null || fieldDescriptors.length == 0)
               ? new ArrayList<>()
               : new ArrayList<>(Arrays.asList(fieldDescriptors));
    }

    private static List<FieldDescriptor> addAll(List<FieldDescriptor> prev,
                                                FieldDescriptor... fieldDescriptors) {

        prev.addAll(Arrays.asList(fieldDescriptors));

        return prev;
    }

    private RestDocsUtil() {
    }
}
