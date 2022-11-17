package com.example.socialnetwork;

import com.example.socialnetwork.dto.ChatRoomDTO;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import com.example.socialnetwork.service.ChatRoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRoomServiceTests extends AbstractTests {
    @Autowired
    private ChatRoomService chatRoomService;

    @Test
    public void testCreateChatRoom_Successful() {
        String name = "create test";
        ChatRoomDTO created = Assertions.assertDoesNotThrow(() -> chatRoomService.create(name));
        Assertions.assertEquals(name, created.getName());
    }

    @Test
    public void testCreateChatRoom_ThrowsRequiredFieldsExceptionOnEmpty() {
        Assertions.assertThrows(RequiredFieldsException.class, () -> chatRoomService.create(""));
    }

    @Test
    public void testCreateChatRoom_ThrowsRequiredFieldsExceptionOnNull() {
        Assertions.assertThrows(RequiredFieldsException.class, () -> chatRoomService.create(null));
    }
}
