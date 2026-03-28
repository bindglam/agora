package com.bindglam.agora.registry;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.board.BoardProperties;
import com.bindglam.agora.user.UserPermission;
import org.springframework.data.util.Pair;

import java.util.List;

public final class BuiltInRegistries {
    public static final Registry<Board> BOARDS = mapped(List.of(
            Pair.of("free", Board.builder()
                    .id("free")
                    .properties(BoardProperties.builder()
                            .displayName("자유방")
                            .description("어떤 주제든 자유롭게 소통해보세요!")
                            .build())
                    .build()),
            Pair.of("announcements", Board.builder()
                    .id("announcements")
                    .properties(BoardProperties.builder()
                            .displayName("공지")
                            .description("웹사이트 관련 중요 소식에 관해 알려드립니다!")
                            .tryCensor(false)
                            .permissions(BoardProperties.Permissions.builder()
                                    .posting(UserPermission.ADMIN)
                                    .build())
                            .build())
                    .build())
    ));

    private BuiltInRegistries() {
    }

    private static <T> Registry<T> mapped(List<Pair<String, T>> map) {
        return new MappedRegistry<>(map);
    }
}
