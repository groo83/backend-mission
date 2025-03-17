package com.murple.chat

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ChatServerApplicationTests {

    @Test
    fun `extractMentionedUsers returns name`() {
        val mentionPattern = "@([\\p{Alnum}가-힣]{1,1024})".toRegex()
        val list = mentionPattern.findAll("@홍길동 안녕 @김영희 _안녕 @seeya@heya# hello").map { it.groupValues[1] }.toList()
        for (s in list) {
            println(s)
        }
    }
}
